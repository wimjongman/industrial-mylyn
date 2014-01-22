package com.industrialtsi.mylyn.core.dto;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndustrialQueryOptionsTest {

	private static final String TEXT = "text";
	private static final String[] STRINGS = new String[] { "a", "b" };
	private IndustrialQueryOptions dummy;

	@Test
	public final void getComments() throws Exception {

	}

	@Test
	public final void getCommentsOrEmpty() throws Exception {

	}

	@Test
	public final void getCompleted() throws Exception {

	}

	@Test
	public final void getIssueStatus() throws Exception {

	}

	@Test
	public final void getIssueStatusOrEmpty() throws Exception {

	}

	@Test
	public final void getNotes() throws Exception {

	}

	@Test
	public final void getNotesOrEmpty() throws Exception {

	}

	@Test
	public final void getOwner() throws Exception {

		Assert.assertArrayEquals(STRINGS, dummy.getOwner());
	}

	@Test
	public final void getOwnerOrEmpty() throws Exception {

	}

	@Test
	public final void getPriority() throws Exception {
		Assert.assertArrayEquals(STRINGS, dummy.getPriority());

	}

	@Test
	public final void getPriorityOrEmpty() throws Exception {
		Assert.assertArrayEquals(STRINGS, dummy.getPriorityOrEmpty());
		dummy.setPriority(null);
		Assert.assertArrayEquals(new String[0], dummy.getPriorityOrEmpty());

	}

	@Test
	public final void getProduct() throws Exception {
		Assert.assertArrayEquals(STRINGS, dummy.getProduct());
	}

	@Test
	public final void getProductOrEmpty() throws Exception {
		Assert.assertArrayEquals(STRINGS, dummy.getProductOrEmpty());
		dummy.setProduct(null);
		Assert.assertArrayEquals(new String[0], dummy.getProductOrEmpty());

	}

	@Test
	public final void getSummary() throws Exception {
		Assert.assertEquals(TEXT, dummy.getSummary());

	}

	@Test
	public final void getSummaryOrEmpty() throws Exception {
		Assert.assertEquals(TEXT, dummy.getSummaryOrEmpty());
		dummy.setSummary(null);
		Assert.assertEquals("", dummy.getSummaryOrEmpty());
	}

	@Test
	public final void IndustrialQueryOptions() throws Exception {

	}

	@Test
	public final void setComments() throws Exception {

	}

	@Test
	public final void setCompleted() throws Exception {

	}

	@Test
	public final void setCompletedSuffix() throws Exception {

	}

	@Test
	public final void setIssueStatus() throws Exception {

	}

	@Test
	public final void setNotes() throws Exception {

	}

	@Test
	public final void setOwner() throws Exception {

	}

	@Test
	public final void setPriority() throws Exception {

	}

	@Test
	public final void setProduct() throws Exception {

	}

	@Test
	public final void setSummary() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		dummy = new IndustrialQueryOptions();
		dummy.setOwner(STRINGS);
		dummy.setProduct(STRINGS);
		dummy.setSummary(TEXT);
		dummy.setPriority(STRINGS);
	}

	@After
	public void tearDown() throws Exception {
	}

}
