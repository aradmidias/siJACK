package net.noratargo.siJACK.util;

import static org.junit.Assert.*;

import net.noratargo.siJACK.util.DoubleHashMap;

import org.junit.Test;

public class TestDoubleHashMap {

	@Test
	public final void testPut() {
		DoubleHashMap<String, String, String> dhm = new DoubleHashMap<String, String, String>();
		
		assertFalse(dhm.contains("a", "1"));

		/* test insertion and retrival: */
		dhm.put("a", "1", "a.1");
		assertTrue(dhm.contains("a", "1"));
		assertEquals("a.1", dhm.get("a", "1"));
		
		/* inserting an element to an already existing group must not remove the already stored value: */
		dhm.put("a", "2", "a.2");
		assertTrue(dhm.contains("a", "1"));
		assertEquals("a.1", dhm.get("a", "1"));
		assertTrue(dhm.contains("a", "2"));
		assertEquals("a.2", dhm.get("a", "2"));
		
		/* Adding an element to another group must nor remove the already existing group: */
		dhm.put("b", "1", "b.1");
		assertTrue(dhm.contains("a", "1"));
		assertEquals("a.1", dhm.get("a", "1"));
		assertTrue(dhm.contains("a", "2"));
		assertEquals("a.2", dhm.get("a", "2"));
		assertTrue(dhm.contains("b", "1"));
		assertEquals("b.1", dhm.get("b", "1"));
		
		
	}

}
