package com.driversfiles.www.captcha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Self-contained captcha service. Renders a random dictionary word as a
 * distorted image and stores expected answers in memory. Replaces the
 * vendored jcaptcha stack whose core classes cannot load on modern JDKs.
 */
@Service("captchaService")
public class JCaptchaServiceImpl implements CaptchaService {

	private static final Logger log = LoggerFactory.getLogger(JCaptchaServiceImpl.class);
	private static final String CAPTCHA_WORD_SOURCE = "/com/driversfiles/www/captcha.properties";
	private static final int MAX_WORD_LENGTH = 15;
	private static final int MIN_WORD_LENGTH = 3;
	private static final int IMAGE_WIDTH = 300;
	private static final int IMAGE_HEIGHT = 100;
	private static final int FONT_SIZE = 40;
	private static final Color ORANGE = new Color(241, 135, 0);
	private static final long EXPIRY_MILLIS = 10 * 60 * 1000L;

	static class Entry {
		final String word;
		final long created;
		Entry(String word, long created) { this.word = word; this.created = created; }
	}

	private final Map<String, Entry> store = new ConcurrentHashMap<>();
	private final Random random = new SecureRandom();
	private final Font font;
	private final String[] words;

	public JCaptchaServiceImpl() {
		this.font = loadFont();
		this.words = loadWords();
	}

	Map<String, Entry> entries() {
		return store;
	}

	BufferedImage writeImageToBuffer(String captchaId) {
		return render(issueChallenge(captchaId));
	}

	String currentWord(String captchaId) {
		Entry e = store.get(captchaId);
		return e != null ? e.word : null;
	}

	private static Font loadFont() {
		try (InputStream in = JCaptchaServiceImpl.class.getResourceAsStream("/com/driversfiles/www/arial.ttf")) {
			if (in == null) {
				log.warn("Arial font resource not found; falling back to system sans-serif");
				return new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE);
			}
			return Font.createFont(Font.TRUETYPE_FONT, in).deriveFont((float) FONT_SIZE);
		} catch (Exception x) {
			throw new IllegalStateException("Failed to load captcha font: " + x.getMessage(), x);
		}
	}

	private static String[] loadWords() {
		Properties props = new Properties();
		try (InputStream in = JCaptchaServiceImpl.class.getResourceAsStream(CAPTCHA_WORD_SOURCE)) {
			if (in != null) {
				props.load(in);
			}
		} catch (IOException x) {
			log.warn("Unable to read captcha word list: " + x.getMessage());
		}
		if (props.isEmpty()) {
			return new String[]{"drivers", "trucking", "freight", "cargo", "highway"};
		}
		return props.values().toArray(new String[0]);
	}

	private synchronized String nextWord() {
		for (int attempts = 0; attempts < 25; attempts++) {
			String candidate = words[random.nextInt(words.length)];
			if (candidate.length() >= MIN_WORD_LENGTH && candidate.length() <= MAX_WORD_LENGTH
					&& candidate.chars().allMatch(Character::isLetterOrDigit)) {
				return candidate.toLowerCase(Locale.US);
			}
		}
		return String.valueOf(10000 + random.nextInt(90000));
	}

	private String issueChallenge(String captchaId) {
		evictExpired();
		String word = nextWord();
		store.put(captchaId, new Entry(word, System.currentTimeMillis()));
		return word;
	}

	private void evictExpired() {
		long cutoff = System.currentTimeMillis() - EXPIRY_MILLIS;
		store.values().removeIf(e -> e.created < cutoff);
	}

	private BufferedImage render(String word) {
		BufferedImage img = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		try {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

			g.setStroke(new BasicStroke(2f));
			for (int i = 0; i < 6; i++) {
				g.setColor(new Color(200 + random.nextInt(55), 180 + random.nextInt(60), 120 + random.nextInt(90)));
				g.drawLine(random.nextInt(IMAGE_WIDTH / 2), random.nextInt(IMAGE_HEIGHT),
						IMAGE_WIDTH / 2 + random.nextInt(IMAGE_WIDTH / 2), random.nextInt(IMAGE_HEIGHT));
			}

			g.setFont(font.deriveFont(Font.BOLD, (float) (FONT_SIZE - random.nextInt(8))));
			int x = Math.max(10, (IMAGE_WIDTH - g.getFontMetrics().stringWidth(word)) / 2 - 10);
			for (char c : word.toCharArray()) {
				double rot = (random.nextDouble() - 0.5) * 0.5;
				int y = IMAGE_HEIGHT / 2 + random.nextInt(14) - 7 + FONT_SIZE / 3;
				g.setColor(ORANGE.darker());
				g.rotate(rot, x, y);
				g.drawString(String.valueOf(c), x, y);
				x += g.getFontMetrics().charWidth(c) + 2;
				g.rotate(-rot, x, y);
			}
		} finally {
			g.dispose();
		}
		return img;
	}

	@Override
	public void writeImage(OutputStream out, String captchaId, Locale locale) throws IOException {
		try {
			BufferedImage image = render(issueChallenge(captchaId));
			ImageIO.write(image, "png", out);
		} catch (Throwable t) {
			log.error("Error writing captcha image: " + t.getMessage(), t);
		} finally {
			out.flush();
			out.close();
		}
	}

	@Override
	public boolean validateImageResponse(String captchaId, String response) {
		if (captchaId == null || response == null || response.isBlank()) {
			return false;
		}
		Entry e = store.remove(captchaId);
		boolean ok = e != null && e.word.equalsIgnoreCase(response.trim());
		if (!ok) {
			log.warn("Error validating captcha: incorrect answer or expired challenge");
		}
		return ok;
	}
}
