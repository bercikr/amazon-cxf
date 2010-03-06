package com.googlecode.amazoncxf.util;

import com.amazon.webservices.awsecommerceservice.Item;

public class ItemFormatter {
	public static String formatItem(Item item) {
		StringBuilder sb = new StringBuilder();

		sb.append("ASIN: " + item.getASIN());
		sb.append(", Name: " + item.getItemAttributes().getTitle());
		if (item.getOffers() != null && item.getOffers().getTotalOffers().intValue() > 0) {
			sb.append(", Price: " + item.getOffers().getOffer().get(0).getOfferListing().get(0).getPrice().getFormattedPrice());
			sb.append(", Qty: " + item.getOffers().getOffer().get(0).getOfferListing().get(0).getQuantity());
		}
		sb.append(", Manufacturer: " + ((item.getItemAttributes() != null && item.getItemAttributes().getManufacturer() != null) ? item.getItemAttributes().getManufacturer() : null));
		sb.append(", Lowest new price: " + ((item.getOfferSummary() != null && item.getOfferSummary().getLowestNewPrice() != null)?item.getOfferSummary().getLowestNewPrice().getFormattedPrice():null));
		sb.append(", Lowest used price: " + ((item.getOfferSummary() != null && item.getOfferSummary().getLowestUsedPrice() != null)?item.getOfferSummary().getLowestUsedPrice().getFormattedPrice():null));

		return sb.toString();
	}
}