package jb;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class JunitConversionLogicTest {

	@Test
	void importWildcardStaticAssertions() {
		String actual = JunitConversionLogic.convert("import static org.junit.Assert.*;");
		assertEquals("import static org.junit.jupiter.api.Assertions.*;", actual);
	}

	@Test
	void specificStaticAssertions() {
		String actual = JunitConversionLogic.convert("org.junit.Assert.assertTrue;");
		assertEquals("org.junit.jupiter.api.Assertions.assertTrue;", actual);
	}

	@Test
	void importWildcardStaticAssumptions() {
		String actual = JunitConversionLogic.convert("import static org.junit.Assume.*;");
		assertEquals("import static org.junit.jupiter.api.Assumptions.*;", actual);
	}

	@Test
	void specificStaticAssumptions() {
		String actual = JunitConversionLogic.convert("org.junit.Assume.assumeFalse;");
		assertEquals("org.junit.jupiter.api.Assumptions.assumeFalse;", actual);
	}

	@Test
	void importWildcardMainPackage() {
		String actual = JunitConversionLogic.convert("import org.junit.*;");
		assertEquals("import org.junit.jupiter.api.*;", actual);
	}

	@Test
	void specificMainPackage() {
		String actual = JunitConversionLogic.convert("org.junit.Test");
		assertEquals("org.junit.jupiter.api.Test", actual);
	}

	@Test
	void assertThat() {
		String actual = JunitConversionLogic.convert("org.junit.Assert.assertThat;");
		assertEquals("org.hamcrest.MatcherAssert.assertThat;", actual);
	}

	@Test
	void newImportForAssertThat() {
		String actual = JunitConversionLogic.convert("import static org.junit.Assert.*;\n"
				+ "assertThat(xxx);");
		assertEquals("import static org.hamcrest.MatcherAssert.assertThat;\n"
				+ "import static org.junit.jupiter.api.Assertions.*;\n"
				+ "assertThat(xxx);", actual);
	}
	
	@Test
	void importAlreadyPresentForAssertThat() {
		String actual = JunitConversionLogic.convert("import static org.hamcrest.MatcherAssert.assertThat;\n"
				+ "assertThat(xxx);");
		assertEquals("import static org.hamcrest.MatcherAssert.assertThat;\n"
				+ "assertThat(xxx);", actual);
	}
	
	@Test
	void doNotUpdateIfAlreadyJupiter() {
		String actual = JunitConversionLogic.convert("import static org.junit.jupiter.api.Assertions.*;");
		assertEquals("import static org.junit.jupiter.api.Assertions.*;", actual);
	}

	// -------------------------------------------------------

	@ParameterizedTest
	@MethodSource("annotationsProvider")
	void newAnnotationNames(String oldAnnotationName, String newAnnotationName) {
		String fullyQualified = JunitConversionLogic.convert("org.junit." + oldAnnotationName);
		assertEquals("org.junit.jupiter.api." + newAnnotationName, fullyQualified);

		String annotationActual = JunitConversionLogic.convert("@" + oldAnnotationName);
		assertEquals("@" + newAnnotationName, annotationActual);
	}

	static Stream<Arguments> annotationsProvider() {
		return Stream.of(Arguments.of("Before", "BeforeEach"),
				Arguments.of("BeforeClass", "BeforeAll"),
				Arguments.of("After", "AfterEach"),
				Arguments.of("AfterClass", "AfterAll"),
				Arguments.of("Ignore", "Disabled"));
	}

}