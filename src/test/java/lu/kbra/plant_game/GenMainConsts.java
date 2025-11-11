package lu.kbra.plant_game;

import java.io.File;

import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;

public class GenMainConsts extends Consts {

	public static final File PROJECT_DIR = new File(".");

	public static final File SRC_DIR = new File(PROJECT_DIR, "/src");

	public static final File SRC_MAIN_DIR = new File(SRC_DIR, "/main");
	public static final File SRC_MAIN_JAVA_DIR = new File(SRC_MAIN_DIR, "/java");
	public static final File SRC_MAIN_RESOURCES_DIR = new File(SRC_MAIN_DIR, "/resources");

	public static final File SRC_TEST_DIR = new File(SRC_DIR, "/test");
	public static final File SRC_TEST_JAVA_DIR = new File(SRC_TEST_DIR, "/java");
	public static final File SRC_TEST_RESOURCES_DIR = new File(SRC_TEST_DIR, "/resources");

	public static final String MAIN_PACKAGE = PGMain.class.getPackageName();
	public static final String GEN_PACKAGE = MAIN_PACKAGE + ".generated";
	public static final File SRC_MAIN_JAVA_GEN_DIR = new File(SRC_MAIN_JAVA_DIR, GEN_PACKAGE.replace(".", "/"));


	public static final File SRC_MAIN_RESOURCES_FONTS_DIR = new File(SRC_MAIN_RESOURCES_DIR, "/fonts");
	public static final File SRC_MAIN_RESOURCES_BAKES_DIR = new File(SRC_MAIN_RESOURCES_DIR, "/bakes");
	public static final File SRC_MAIN_RESOURCES_BAKES_FONTS_DIR = new File(SRC_MAIN_RESOURCES_BAKES_DIR, "/fonts");

	static {
		SRC_MAIN_JAVA_GEN_DIR.mkdirs();
		SRC_MAIN_RESOURCES_BAKES_FONTS_DIR.mkdirs();
	}

}
