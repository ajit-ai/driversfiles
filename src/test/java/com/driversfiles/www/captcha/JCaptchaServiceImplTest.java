package com.driversfiles.www.captcha;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class JCaptchaServiceImplTest {

	@Test
	void serviceInstantiates() {
		assertDoesNotThrow(JCaptchaServiceImpl::new);
	}

	@Test
	void generatesChallengeImageAtConfiguredSize() {
		JCaptchaServiceImpl svc = new JCaptchaServiceImpl();
		BufferedImage img = svc.writeImageToBuffer("test-captcha-id");
		assertNotNull(img);
		assertEquals(300, img.getWidth());
		assertEquals(100, img.getHeight());
	}

	@Test
	void issuedChallengeStoresExpectedWord() {
		JCaptchaServiceImpl svc = new JCaptchaServiceImpl();
		svc.writeImageToBuffer("id-1");
		String word = svc.currentWord("id-1");
		assertNotNull(word);
		assertTrue(word.matches("[a-z0-9]+"));
		assertFalse(word.isBlank());
	}

	@Test
	void correctAnswerValidatesAndConsumesChallenge() {
		JCaptchaServiceImpl svc = new JCaptchaServiceImpl();
		svc.writeImageToBuffer("id-2");
		String word = svc.currentWord("id-2");
		assertTrue(svc.validateImageResponse("id-2", word.toUpperCase(Locale.US)));
		assertNull(svc.currentWord("id-2"), "challenge must be consumed after validation");
	}

	@Test
	void wrongAnswerFailsValidation() {
		JCaptchaServiceImpl svc = new JCaptchaServiceImpl();
		svc.writeImageToBuffer("id-3");
		String word = svc.currentWord("id-3");
		if (word.equalsIgnoreCase("zzz")) word = "yyy";
		assertFalse(svc.validateImageResponse("id-3", word + "x"));
	}

	@Test
	void unknownOrBlankIdsAlwaysFail() {
		JCaptchaServiceImpl svc = new JCaptchaServiceImpl();
		assertFalse(svc.validateImageResponse("never-issued", "anything"));
		assertFalse(svc.validateImageResponse(null, "x"));
		assertFalse(svc.validateImageResponse("some-id", null));
		assertFalse(svc.validateImageResponse("some-id", "  "));
	}
}
