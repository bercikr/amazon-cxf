package com.googlecode.amazoncxf.util;

import com.amazon.webservices.awsecommerceservice._2009_02_01.Item;

public class ItemFormatter {
	public static String formatItem(Item item) {
		StringBuilder sb = new StringBuilder();

		sb.append("ASIN: " + item.getASIN());
		sb.append(", Name: " + item.getItemAttributes().getTitle());
		if (item.getOffers() != null
				&& item.getOffers().getTotalOffers().intValue() > 0) {
			sb.append(", Price: "
					+ item.getOffers().getOffer().get(0).getOfferListing().get(
							0).getPrice().getFormattedPrice());
			sb.append(", Qty: "
					+ item.getOffers().getOffer().get(0).getOfferListing().get(
							0).getQuantity());
		}
		sb.append(", Manufacturer: "
				+ item.getItemAttributes().getManufacturer());

		return sb.toString();
	}
}