package com.industrialtsi.mylyn.core.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class IndustrialQueryParamsTest {

	IndustrialQueryParams params;
	private final String[] product;

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays
				.asList(new Object[][] { { new String[] { "a" } },
						{ new String[] { "a", "a&b" } },
						{ new String[] { "a", "a|b" } },
						{ new String[] { "a", "b" } } });
	}

	public IndustrialQueryParamsTest(String[] product) {
		this.product = product;
		params = new IndustrialQueryParams(IndustrialQueryParams.QUERY_URL);
		params.setProduct(product);
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testAsUrl() {
		assertNotNull("url is null", params.asUrl());
		assertTrue("should be String", params.asUrl() instanceof String);
	}

	@Test
	public void testFromUrl() {
		IndustrialQueryParams test = new IndustrialQueryParams(
				IndustrialQueryParams.QUERY_URL);
		test.fromUrl(params.asUrl());
		assertEquals(params.asUrl(), test.asUrl());

	}

	@Test
	public void testGetProduct() {
		assertNotNull(params.getProduct());
		assertTrue("should be array", params.getProduct() instanceof String[]);
	}

	@Test
	public void testSetProduct() {
		IndustrialQueryParams test = new IndustrialQueryParams(
				IndustrialQueryParams.QUERY_URL);
		test.setProduct(product);

		assertEquals(test.getProduct().length, product.length);
	}

}
