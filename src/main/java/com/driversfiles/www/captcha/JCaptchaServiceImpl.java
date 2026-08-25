package com.driversfiles.www.captcha;

import com.octo.captcha.Captcha;
import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.FileDictionary;
import com.octo.captcha.component.word.wordgenerator.DictionaryWordGenerator;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.image.gimpy.Gimpy;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.MapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Random;

/**
 * {@inheritDoc}
 */
@Service("captchaService")
public class JCaptchaServiceImpl extends DefaultManageableImageCaptchaService implements CaptchaService {

	private static final Logger log = LoggerFactory.getLogger(JCaptchaServiceImpl.class);
	private static final String ARIAL_FONT_PATH = "/com/driversfiles/www/arial.ttf";
	private static final String CAPTCHA_WORD_SOURCE = "com.driversfiles.www.captcha";
	private static final int MAX_WORD_LENGTH = 15;
	private static final int MIN_WORD_LENGTH = 3;
	private static final int IMAGE_WIDTH = 300;
	private static final int IMAGE_HEIGHT = 100;
	private static final int FONT_SIZE = 25;
	private static final int MIN_STORAGE_DELAY = 600; // 10 Minutes
	private static final int MAX_STORE_SIZE = 100000;
	private static final int STORE_LOAD_BEFORE_GARBAGE_COLLECTION = 7500;
	private static final Color ORANGE = new Color(241, 135, 0);

	public JCaptchaServiceImpl() {
		super(new MapCaptchaStore(), buildEngine(),
				MIN_STORAGE_DELAY, MAX_STORE_SIZE, STORE_LOAD_BEFORE_GARBAGE_COLLECTION);
	}

	private static CaptchaEngine buildEngine() {
		DictionaryWordGenerator wordgen = new DictionaryWordGenerator(new FileDictionary(CAPTCHA_WORD_SOURCE));
		ComposedWordToImage word2image = new ComposedWordToImage(
				new RandomFontGenerator(FONT_SIZE, FONT_SIZE, new Font[]{loadFont()}),
				new TransparentBackgroundGenerator(),
				new SimpleTextPaster(MIN_WORD_LENGTH, MAX_WORD_LENGTH, ORANGE));
		CaptchaFactory factory = new DriversFilesCaptchaFactory(wordgen, word2image);
		return new GenericCaptchaEngine(new CaptchaFactory[]{factory});
	}

	private static class DriversFilesCaptchaFactory implements CaptchaFactory {

		private final DictionaryWordGenerator wordGen;
		private final WordToImage wordToImage;
		private final Random random = new Random();

		DriversFilesCaptchaFactory(DictionaryWordGenerator wordGen, WordToImage wordToImage) {
			this.wordGen = wordGen;
			this.wordToImage = wordToImage;
		}

		public Captcha getCaptcha() {
			return getCaptcha(Locale.getDefault());
		}

		public Captcha getCaptcha(Locale locale) {
			int min = wordToImage.getMinAcceptedWordLength();
			int max = wordToImage.getMaxAcceptedWordLength();
			String word = wordGen.getWord(min + random.nextInt(max - min + 1), locale);
			BufferedImage image = wordToImage.getImage(word);
			return newGimpy("Type the word shown", image, word);
		}

		private Gimpy newGimpy(String question, BufferedImage image, String word) {
			try {
				java.lang.reflect.Constructor<Gimpy> c = Gimpy.class
						.getDeclaredConstructor(String.class, BufferedImage.class, String.class);
				c.setAccessible(true);
				return c.newInstance(question, image, word);
			} catch (ReflectiveOperationException x) {
				throw new IllegalStateException("Unable to create captcha: " + x.getMessage(), x);
			}
		}
	}

	private static Font loadFont() {
		try {
			return Font.createFont(Font.PLAIN, JCaptchaServiceImpl.class.getResourceAsStream(ARIAL_FONT_PATH));
		} catch (IOException x) {
			throw new IllegalStateException("Failed to load font [" + ARIAL_FONT_PATH + "]: " + x.getMessage(), x);
		} catch (FontFormatException x) {
			throw new IllegalStateException("Failed to load font [" + ARIAL_FONT_PATH + "]: " + x.getMessage(), x);
		}
	}

	private static class TransparentBackgroundGenerator implements BackgroundGenerator {

		@Override
		public int getImageHeight() {
			return IMAGE_HEIGHT;
		}

		@Override
		public int getImageWidth() {
			return IMAGE_WIDTH;
		}

		@Override
		public BufferedImage getBackground() {
			return new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeImage(OutputStream out, String captchaId, Locale locale) throws IOException {
		try {
			BufferedImage image = getImageChallengeForID(captchaId, locale);
			ImageIO.write(image, "png", out);
		} catch (Throwable t) {
			log.error("Error writing captcha image: " + t.getMessage(), t);
		} finally {
			out.flush();
			out.close();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validateImageResponse(String captchaId, String response) {
		try {
			return validateResponseForID(captchaId, response);
		} catch (CaptchaServiceException x) {
			log.warn("Error validating captcha: " + x.getMessage(), x);
		}
		return false;
	}
}
