package com.driversfiles.www.fs;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Minimal high-quality image scaler used for fitting uploaded images
 * (e.g. company logos) within a maximum bounding box. Replaces the
 * vendored imgscalr library with pure-JDK code.
 */
public final class ImageScaler {

	private ImageScaler() {}

	/**
	 * Returns a copy of the source image scaled down to fit within
	 * maxWidth x maxHeight while preserving the aspect ratio. Images already
	 * within the bounds are returned unchanged. Transparency is flattened
	 * onto white so JPEG output stays correct.
	 */
	public static BufferedImage resize(BufferedImage src, int maxWidth, int maxHeight) {
		int w = src.getWidth();
		int h = src.getHeight();
		if (w <= maxWidth && h <= maxHeight) {
			return src;
		}
		double scale = Math.min((double) maxWidth / w, (double) maxHeight / h);
		int targetW = Math.max(1, (int) Math.round(w * scale));
		int targetH = Math.max(1, (int) Math.round(h * scale));

		BufferedImage current = src;
		int cw = w;
		int ch = h;
		while (cw / 2 >= targetW && ch / 2 >= targetH && cw > 2 && ch > 2) {
			int nw = Math.max(targetW, cw / 2);
			int nh = Math.max(targetH, ch / 2);
			current = scaleStep(current, nw, nh, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			cw = nw;
			ch = nh;
		}
		return scaleStep(current, targetW, targetH, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	}

	private static BufferedImage scaleStep(BufferedImage src, int w, int h, Object interpolation) {
		BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = out.createGraphics();
		try {
			g.setColor(java.awt.Color.WHITE);
			g.fillRect(0, 0, w, h);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolation);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(src, 0, 0, w, h, null);
		} finally {
			g.dispose();
		}
		return out;
	}

	private static BufferedImage createCompatibleBuffer(int w, int h) {
		return new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	}
}
