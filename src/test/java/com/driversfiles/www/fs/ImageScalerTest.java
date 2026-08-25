package com.driversfiles.www.fs;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class ImageScalerTest {

	private BufferedImage createImage(int w, int h) {
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		try {
			g.setColor(Color.ORANGE);
			g.fillRect(0, 0, w, h);
		} finally {
			g.dispose();
		}
		return img;
	}

	@Test
	void downscalesToFitBoundingBoxPreservingAspectRatio() {
		BufferedImage wide = createImage(1000, 500);
		BufferedImage out = ImageScaler.resize(wide, 250, 250);
		assertEquals(250, out.getWidth());
		assertEquals(125, out.getHeight());
	}

	@Test
	void tallImagesFitHeightConstraint() {
		BufferedImage tall = createImage(400, 1200);
		BufferedImage out = ImageScaler.resize(tall, 250, 250);
		assertEquals(83, out.getWidth());
		assertEquals(250, out.getHeight());
	}

	@Test
	void smallImagesAreNotUpscaled() {
		BufferedImage small = createImage(100, 80);
		BufferedImage out = ImageScaler.resize(small, 250, 250);
		assertSame(small, out);
	}

	@Test
	void exactSizeImagesAreReturnedUnchanged() {
		BufferedImage exact = createImage(250, 250);
		assertSame(exact, ImageScaler.resize(exact, 250, 250));
	}

	@Test
 void transparentPixelsAreFlattenedOntoWhite() {
		BufferedImage transparent = new BufferedImage(600, 300, BufferedImage.TYPE_INT_ARGB);
		BufferedImage out = ImageScaler.resize(transparent, 250, 250);
		assertEquals(BufferedImage.TYPE_INT_RGB, out.getType());
		int rgb = out.getRGB(out.getWidth() - 1, out.getHeight() - 1);
		assertEquals(Color.WHITE.getRGB() & 0x00FFFFFF, rgb & 0x00FFFFFF);
	}
}
