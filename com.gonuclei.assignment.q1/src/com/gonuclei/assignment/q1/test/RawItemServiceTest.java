package com.gonuclei.assignment.q1.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.gonuclei.assignment.q1.model.Item;
import com.gonuclei.assignment.q1.serviceImpl.RawItemService;

public class RawItemServiceTest {

	@Test
	public void testCalculateTax() {
		RawItemService rawItemService = new RawItemService();
		Item item = new Item();
		item.setName("Pencil");
		item.setPrice(100);
		item.setQuantity(3);
		item.setType("raw");
		Item assertItem = new Item();
		assertItem = item;
		assertItem.setTax(12);
		assertEquals(assertItem, rawItemService.calculateTax(item));
	}

}
