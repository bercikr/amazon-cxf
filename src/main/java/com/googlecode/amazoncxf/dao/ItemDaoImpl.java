package com.googlecode.amazoncxf.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import com.amazon.webservices.awsecommerceservice._2009_02_01.AWSECommerceServicePortType;
import com.amazon.webservices.awsecommerceservice._2009_02_01.Item;
import com.amazon.webservices.awsecommerceservice._2009_02_01.ItemLookup;
import com.amazon.webservices.awsecommerceservice._2009_02_01.ItemLookupRequest;
import com.amazon.webservices.awsecommerceservice._2009_02_01.ItemLookupResponse;
import com.amazon.webservices.awsecommerceservice._2009_02_01.ItemSearch;
import com.amazon.webservices.awsecommerceservice._2009_02_01.ItemSearchRequest;
import com.amazon.webservices.awsecommerceservice._2009_02_01.ItemSearchResponse;
import com.amazon.webservices.awsecommerceservice._2009_02_01.Items;
import com.amazon.webservices.awsecommerceservice._2009_02_01.Errors.Error;
import com.googlecode.amazoncxf.domain.AmazonAssociatesWebServiceAccount;

public class ItemDaoImpl implements ItemDao {
	private static Log log = LogFactory.getLog(ItemDaoImpl.class);

	private AmazonAssociatesWebServiceAccount amazonAssociatesWebServiceAccount;

	private AWSECommerceServicePortType awseCommerceServicePort;

	public AmazonAssociatesWebServiceAccount getAmazonAssociatesWebServiceAccount() {
		return amazonAssociatesWebServiceAccount;
	}

	@Required
	public void setAmazonAssociatesWebServiceAccount(
			AmazonAssociatesWebServiceAccount amazonAssociatesWebServiceAccount) {
		this.amazonAssociatesWebServiceAccount = amazonAssociatesWebServiceAccount;
	}

	public AWSECommerceServicePortType getAwseCommerceServicePort() {
		return awseCommerceServicePort;
	}

	@Required
	public void setAwseCommerceServicePort(
			AWSECommerceServicePortType awseCommerceServicePort) {
		this.awseCommerceServicePort = awseCommerceServicePort;
	}

	public Item lookup(String asin) {
		if (StringUtils.isBlank(asin)) {
			throw new IllegalArgumentException(
					"Parameter asins can't be null or blank.");
		}
		Item item = null;
		AWSECommerceServicePortType client = getAwseCommerceServicePort();

		ItemLookup itemLookup = new ItemLookup();
		itemLookup.setAWSAccessKeyId(amazonAssociatesWebServiceAccount
				.getAwsAccessKeyId());
		itemLookup.setAssociateTag(amazonAssociatesWebServiceAccount
				.getAssociateTag());
		ItemLookupRequest itemLookupRequest = new ItemLookupRequest();
		itemLookupRequest.setIdType("ASIN");
		itemLookupRequest.setMerchantId("Amazon"); // Amazon USA
		itemLookupRequest.getItemId().add(asin);
		itemLookupRequest.getResponseGroup().add("ItemAttributes");
		itemLookupRequest.getResponseGroup().add("Offers");
		itemLookupRequest.getResponseGroup().add("OfferSummary");
		itemLookupRequest.getResponseGroup().add("Images");
		itemLookup.setShared(itemLookupRequest);

		ItemLookupResponse itemLookupResponse = client.itemLookup(itemLookup);

		for (Items items : itemLookupResponse.getItems()) {
			for (Item itemInLoop : items.getItem()) {
				item = itemInLoop;
			}
		}

		return item;
	}

	public List<Item> getItems(List<String> asins) {
		if (asins == null) {
			throw new IllegalArgumentException("Parameter asins can't be null.");
		}
		List<Item> itemColl = new ArrayList<Item>();
		AWSECommerceServicePortType client = getAwseCommerceServicePort();

		ItemLookup itemLookup = new ItemLookup();
		itemLookup.setAWSAccessKeyId(amazonAssociatesWebServiceAccount
				.getAwsAccessKeyId());
		itemLookup.setAssociateTag(amazonAssociatesWebServiceAccount
				.getAssociateTag());
		ItemLookupRequest itemLookupRequest = new ItemLookupRequest();
		itemLookupRequest.setIdType("ASIN");
		itemLookupRequest.setMerchantId("Amazon"); // Amazon USA
		for (String asin : asins) {
			itemLookupRequest.getItemId().add(asin);
		}
		itemLookupRequest.getResponseGroup().add("Small");
		itemLookupRequest.getResponseGroup().add("Offers");
		itemLookupRequest.getResponseGroup().add("Images");
		itemLookup.setShared(itemLookupRequest);

		ItemLookupResponse itemLookupResponse = client.itemLookup(itemLookup);

		// Were there any errors?
		if (itemLookupResponse.getOperationRequest() != null
				&& itemLookupResponse.getOperationRequest().getErrors() != null
				&& itemLookupResponse.getOperationRequest().getErrors() != null) {
			for (Error error : itemLookupResponse.getOperationRequest()
					.getErrors().getError()) {
				log.error(error.getCode() + ": " + error.getCode());
			}
		}

		if (itemLookupResponse.getItems() != null) {
			for (Items items : itemLookupResponse.getItems()) {
				if (items.getItem() != null) {
					if (items.getRequest().getErrors() != null) {
						for (Error error : items.getRequest().getErrors()
								.getError()) {
							log.error(error.getCode() + ": "
									+ error.getMessage());
							if (error.getCode().equals(
									"AWS.InvalidParameterValue")) {
								log
										.error("Couldn't find item, please check previous error.");
							} else { // We're dealing with another error
								throw new IllegalArgumentException(error
										.getCode()
										+ ": " + error.getMessage());
							}
						}
					}
				}
			}
		}

		// Populate the items that are going to be returned.
		for (Items items : itemLookupResponse.getItems()) {
			for (Item itemInLoop : items.getItem()) {
				itemColl.add(itemInLoop);
			}
		}

		return itemColl;
	}

	public List<Item> searchItems(String keyword) {
		if (StringUtils.isBlank(keyword)) {
			throw new IllegalArgumentException(
					"Parameter keyword can't be null or blank.");
		}
		List<Item> itemColl = new ArrayList<Item>();
		AWSECommerceServicePortType client = getAwseCommerceServicePort();

		ItemSearch itemSearch = new ItemSearch();
		itemSearch.setAWSAccessKeyId(amazonAssociatesWebServiceAccount
				.getAwsAccessKeyId());
		itemSearch.setAssociateTag(amazonAssociatesWebServiceAccount
				.getAssociateTag());
		ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
		itemSearchRequest.setSearchIndex("All");
		itemSearchRequest.setKeywords(keyword);
		itemSearchRequest.setMerchantId("Amazon");
		itemSearchRequest.getResponseGroup().add("Small");
		itemSearchRequest.getResponseGroup().add("Offers");
		itemSearchRequest.getResponseGroup().add("Images");
		itemSearch.setShared(itemSearchRequest);

		ItemSearchResponse itemSearchResponse = client.itemSearch(itemSearch);

		// Were there any errors?
		if (itemSearchResponse.getOperationRequest() != null
				&& itemSearchResponse.getOperationRequest().getErrors() != null
				&& itemSearchResponse.getOperationRequest().getErrors() != null) {
			for (Error error : itemSearchResponse.getOperationRequest()
					.getErrors().getError()) {
				log.error(error.getCode() + ": " + error.getCode());
			}
		}

		if (itemSearchResponse.getItems() != null) {
			for (Items items : itemSearchResponse.getItems()) {
				if (items.getItem() != null) {
					if (items.getRequest().getErrors() != null) {
						for (Error error : items.getRequest().getErrors()
								.getError()) {
							log.error(error.getCode() + ": "
									+ error.getMessage());
							throw new IllegalArgumentException(error.getCode()
									+ ": " + error.getMessage());
						}
					}
				}
			}
		}

		// Populate the items that are going to be returned.
		for (Items items : itemSearchResponse.getItems()) {
			for (Item itemInLoop : items.getItem()) {
				itemColl.add(itemInLoop);
			}
		}

		return itemColl;
	}
}