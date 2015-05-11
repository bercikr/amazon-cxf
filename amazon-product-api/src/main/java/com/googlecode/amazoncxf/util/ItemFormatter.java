package com.googlecode.amazoncxf.util;

import com.amazon.webservices.awsecommerceservice.ImageSet;
import com.amazon.webservices.awsecommerceservice.Item;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileOutputStream;

public class ItemFormatter {
	public static String formatItem(Item item) {
		StringBuilder sb = new StringBuilder();

		sb.append("ASIN: " + item.getASIN());
		sb.append(", Name: " + item.getItemAttributes().getTitle());
		if (item.getOffers() != null && item.getOffers().getTotalOffers().intValue() > 0) {
			sb.append(", Price: " + item.getOffers().getOffer().get(0).getOfferListing().get(0).getPrice().getFormattedPrice());
			sb.append(", Qty: " + item.getOfferSummary().getTotalNew());
		}

		sb.append(", Manufacturer: " + ((item.getItemAttributes() != null && item.getItemAttributes().getManufacturer() != null) ? item.getItemAttributes().getManufacturer() : null));
		sb.append(", Lowest new price: " + ((item.getOfferSummary() != null && item.getOfferSummary().getLowestNewPrice() != null)?item.getOfferSummary().getLowestNewPrice().getFormattedPrice():null));
		sb.append(", Lowest used price: " + ((item.getOfferSummary() != null && item.getOfferSummary().getLowestUsedPrice() != null) ? item.getOfferSummary().getLowestUsedPrice().getFormattedPrice() : null));

        ImageSet imageSet = item.getImageSets().get(0).getImageSet().get(0);
        if(imageSet.getHiResImage()!=null) {
            sb.append("\n"+imageSet.getHiResImage().getURL()+"\n");
        }


		try {
			JAXBContext ctx = JAXBContext.newInstance(Item.class);
			Marshaller m = ctx.createMarshaller();
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
			m.marshal(item, new FileOutputStream(new File(item.getASIN()+".xml")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
}