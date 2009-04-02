package com.supervaca.pricewatch.dao;

import java.util.List;

import com.amazon.webservices.awsecommerceservice._2009_02_01.Item;

/**
 * Operations that can be performed on an Item, it uses the webservices to
 * populate the data.
 * 
 * @author eliseo.soto
 * 
 */
public interface ItemDao {
	/**
	 * Returns the Item data for the given ASIN
	 * 
	 * @param asin
	 *            A valid ASIN
	 * @return
	 */
	Item lookup(String asin);

	/**
	 * Gets the Item data for several ASINs at once
	 * 
	 * @param asins
	 *            Collection of valid ASINs
	 * @return
	 */
	List<Item> getItems(List<String> asins);

	/**
	 * Searches items by keyword in all categories.
	 * 
	 * @param keyword
	 * @return
	 */
	List<Item> searchItems(String keyword);
}