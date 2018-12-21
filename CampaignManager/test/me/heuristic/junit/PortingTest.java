package me.heuristic.junit;

import static org.junit.Assert.*;
import me.heuristic.references.Porting;

import org.junit.Test;

public class PortingTest {

	@Test
	public void testGetDelimiterRegEx() {
		assertEquals("Result", "\\t", Porting.getDelimiterRegEx(Porting.DEL_TSV));
	}

	@Test
	public void testHasHeader() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseLine() {
		fail("Not yet implemented");
	}

}