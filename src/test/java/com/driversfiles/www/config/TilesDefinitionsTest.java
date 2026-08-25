package com.driversfiles.www.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TilesDefinitionsTest {

	private static final String TILES_XML = "src/main/webapp/WEB-INF/tiles.xml";
	private static final TilesDefinitions defs = new TilesDefinitions();

	@BeforeAll
	static void load() throws Exception {
		assertTrue(Files.exists(Paths.get(TILES_XML)), "tiles.xml must exist relative to project root");
		try (FileInputStream in = new FileInputStream(TILES_XML)) {
			defs.load(in, TILES_XML);
		}
	}

	@Test
	void loadsAllDefinitions() {
		assertTrue(defs.contains("base.template"));
		assertTrue(defs.contains("main.template"));
		assertTrue(defs.contains("login.page"));
		assertTrue(defs.contains("home.page"));
		assertTrue(defs.contains("driver_dashboard.page"));
		assertTrue(defs.contains("company_drivers.page"));
		assertTrue(defs.contains("admin_import.page"));
		assertFalse(defs.contains("does.not.exist"));
	}

	@Test
	void inheritanceFlattensTemplateThroughWholeChain() {
		var loginPage = defs.get("login.page");
		assertNotNull(loginPage);
		assertEquals("/WEB-INF/jsp/base.jsp", loginPage.get(TilesDefinitions.TEMPLATE_KEY));
		assertEquals("/WEB-INF/jsp/main.jsp", loginPage.get("body"));
		assertEquals("/WEB-INF/jsp/public_menu.jsp", loginPage.get("menu"));
		assertEquals("/WEB-INF/jsp/login.jsp", loginPage.get("content"));
		assertEquals("login.title", loginPage.get("title"));
		assertEquals("login", loginPage.get("active_menu"));
	}

	@Test
	void childOverridesParentAttribute() {
		var printTemplate = defs.get("print.template");
		assertEquals("/WEB-INF/jsp/main_print.jsp", printTemplate.get("body"),
				"print template overrides body inherited from main.template");
	}

	@Test
	void twoColumnChainProvidesLeftAndRightSlots() {
		var personalInfo = defs.get("personal_information.page");
		assertNotNull(personalInfo);
		assertEquals("/WEB-INF/jsp/two_column.jsp", personalInfo.get("content"));
		assertEquals("/WEB-INF/jsp/secure/driver/mydata/mydata_menu.jsp", personalInfo.get("left"));
		assertEquals("/WEB-INF/jsp/secure/driver/mydata/personal_information.jsp", personalInfo.get("right"));
	}

	@Test
	void optionalAttributesAreAbsentWhenUndefined() {
		var loginPage = defs.get("login.page");
		assertNull(loginPage.get("left"), "login page should not define a two-column left slot");
	}
}
