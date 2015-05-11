package com.googlecode.amazoncxf.dao;

import com.amazon.webservices.awsecommerceservice.*;
import com.amazon.webservices.awsecommerceservice.Errors.Error;
import com.googlecode.amazoncxf.domain.AmazonAssociatesWebServiceAccount;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDaoImpl {
	private static Log log = LogFactory.getLog(ItemDaoImpl.class);

	private AmazonAssociatesWebServiceAccount amazonAssociatesWebServiceAccount;

	private AWSECommerceServicePortType awseCommerceServicePort;

	public AmazonAssociatesWebServiceAccount getAmazonAssociatesWebServiceAccount() {
		return amazonAssociatesWebServiceAccount;
	}

	@Required
	public void setAmazonAssociatesWebServiceAccount(AmazonAssociatesWebServiceAccount amazonAssociatesWebServiceAccount) {
		this.amazonAssociatesWebServiceAccount = amazonAssociatesWebServiceAccount;
	}

	public AWSECommerceServicePortType getAwseCommerceServicePort() {
		return awseCommerceServicePort;
	}

	@Required
	public void setAwseCommerceServicePort(AWSECommerceServicePortType awseCommerceServicePort) {
		this.awseCommerceServicePort = awseCommerceServicePort;
	}

	public List<String> getResponseGroups(){
		List<String> responseGroups = new ArrayList<String>();
		//responseGroups.add("ItemAttributes");
//		responseGroups.add("Offers");
//		responseGroups.add("OfferSummary");
//		responseGroups.add("Images");
//		responseGroups.add("Reviews");
		responseGroups.add("Large");
		return responseGroups;
	}

	public Item lookup(String asin) {
		return lookup(asin, getResponseGroups());
	}

	public Item lookup(String asin, List<String> responseGroups) {
		return lookup(asin, responseGroups, true);
	}

	public Item lookup(String asin, List<String> responseGroups, Boolean amazonOnly) {
		if (asin==null||asin.trim().equals("")) {
			throw new IllegalArgumentException("Parameter asins can't be null or blank.");
		}
		Item item = null;
		AWSECommerceServicePortType client = getAwseCommerceServicePort();

		ItemLookup itemLookup = new ItemLookup();
		itemLookup.setAWSAccessKeyId(amazonAssociatesWebServiceAccount.getAwsAccessKeyId());
		itemLookup.setAssociateTag(amazonAssociatesWebServiceAccount.getAssociateTag());
		ItemLookupRequest itemLookupRequest = new ItemLookupRequest();
		itemLookupRequest.setIdType("ASIN");
		itemLookupRequest.setMerchantId(amazonOnly ? "Amazon" : "All"); // Amazon USA
		itemLookupRequest.getItemId().add(asin);
		itemLookupRequest.getResponseGroup().addAll(responseGroups);
		itemLookupRequest.setCondition("All");
		itemLookup.setShared(itemLookupRequest);

		ItemLookupResponse itemLookupResponse = client.itemLookup(itemLookup);

		if (itemLookupResponse.getItems() != null) {
			for (Items items : itemLookupResponse.getItems()) {
				if (items.getItem() != null) {
					if (items.getRequest().getErrors() != null) {
						for (Error error : items.getRequest().getErrors().getError()) {
							log.error(error.getCode() + ": " + error.getMessage());
							if (error.getCode().equals("AWS.InvalidParameterValue")) {
								log.error("Couldn't find item, please check previous error.");
							} else { // We're dealing with another error
								throw new IllegalArgumentException(error.getCode() + ": " + error.getMessage());
							}
						}
					}
				}
			}
		}

		for (Items items : itemLookupResponse.getItems()) {
			for (Item itemInLoop : items.getItem()) {
				item = itemInLoop;
			}
		}

		return item;
	}

	public List<BrowseNodes> browseNode(String nodeId){
		AWSECommerceServicePortType client = getAwseCommerceServicePort();

		BrowseNodeLookup body = new BrowseNodeLookup();
		body.setAWSAccessKeyId(amazonAssociatesWebServiceAccount.getAwsAccessKeyId());
		body.setAssociateTag(amazonAssociatesWebServiceAccount.associateTag);
		BrowseNodeLookupRequest req = new BrowseNodeLookupRequest();
		req.getBrowseNodeId().add(nodeId);
		body.setShared(req);
		BrowseNodeLookupResponse resp = client.browseNodeLookup(body);

		return resp.getBrowseNodes();

	}

	public List<Item> getItems(List<String> asins) {
		return getItems(asins, getResponseGroups());
	}

	public List<Item> getItems(List<String> asins, List<String> responseGroups) {
		if (asins == null) {
			throw new IllegalArgumentException("Parameter asins can't be null.");
		}
		List<Item> itemColl = new ArrayList<Item>();
		AWSECommerceServicePortType client = getAwseCommerceServicePort();

		ItemLookup itemLookup = new ItemLookup();
		itemLookup.setAWSAccessKeyId(amazonAssociatesWebServiceAccount.getAwsAccessKeyId());
		itemLookup.setAssociateTag(amazonAssociatesWebServiceAccount.getAssociateTag());
		ItemLookupRequest itemLookupRequest = new ItemLookupRequest();
		itemLookupRequest.setIdType("ASIN");
		itemLookupRequest.setMerchantId("Amazon"); // Amazon USA
		itemLookupRequest.getItemId().addAll(asins);
		itemLookupRequest.getResponseGroup().addAll(responseGroups);
		itemLookup.setShared(itemLookupRequest);

		ItemLookupResponse itemLookupResponse = client.itemLookup(itemLookup);

		// Were there any errors?
		if (itemLookupResponse.getOperationRequest() != null && itemLookupResponse.getOperationRequest().getErrors() != null && itemLookupResponse.getOperationRequest().getErrors() != null) {
			for (Error error : itemLookupResponse.getOperationRequest().getErrors().getError()) {
				log.error(error.getCode() + ": " + error.getCode());
			}
		}

		if (itemLookupResponse.getItems() != null) {
			for (Items items : itemLookupResponse.getItems()) {
				if (items.getItem() != null) {
					if (items.getRequest().getErrors() != null) {
						for (Error error : items.getRequest().getErrors().getError()) {
							log.error(error.getCode() + ": " + error.getMessage());
							if (error.getCode().equals("AWS.InvalidParameterValue")) {
								log.error("Couldn't find item, please check previous error.");
							} else { // We're dealing with another error
								throw new IllegalArgumentException(error.getCode() + ": " + error.getMessage());
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

	public List<Item> searchItems(String keywords) {

		return searchItems(keywords, getResponseGroups(), "All");
	}

	public List<Item> searchItems(String keywords, List<String> responseGroups) {
		return searchItems(keywords, responseGroups, "All");
	}

	public List<Item> searchItems(String keywords, String searchIndex) {
		return searchItems(keywords, getResponseGroups(), searchIndex);
	}

	public List<Item> searchItems(String keywords, List<String> responseGroups, String searchIndex) {
		return (List<Item>) searchItems(keywords, responseGroups, searchIndex, null, 1).get("items");
	}

	public Map<String, Object> searchItems(String keywords, List<String> responseGroups, String searchIndex, String browseNode, Integer pageNumber) {
		if (keywords==null||keywords.trim().equals("")) {
			throw new IllegalArgumentException("Parameter keyword can't be null or blank.");
		}
		List<Item> itemColl = new ArrayList<Item>();
		AWSECommerceServicePortType client = getAwseCommerceServicePort();

		ItemSearch itemSearch = new ItemSearch();
		itemSearch.setAWSAccessKeyId(amazonAssociatesWebServiceAccount.getAwsAccessKeyId());
		itemSearch.setAssociateTag(amazonAssociatesWebServiceAccount.getAssociateTag());
		ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
		itemSearchRequest.setSearchIndex(searchIndex);
        itemSearchRequest.setSort("salesrank");
        if(browseNode!=null)
            itemSearchRequest.setBrowseNode(browseNode);
		itemSearchRequest.getResponseGroup().addAll(responseGroups);
		itemSearchRequest.setKeywords(keywords);
		itemSearchRequest.setMerchantId("All");
		itemSearchRequest.setItemPage(BigInteger.valueOf(pageNumber));

		itemSearch.setShared(itemSearchRequest);

		ItemSearchResponse itemSearchResponse = client.itemSearch(itemSearch);

		// Were there any errors?
		if (itemSearchResponse.getOperationRequest() != null && itemSearchResponse.getOperationRequest().getErrors() != null && itemSearchResponse.getOperationRequest().getErrors() != null) {
			for (Error error : itemSearchResponse.getOperationRequest().getErrors().getError()) {
				log.error(error.getCode() + ": " + error.getCode());
			}
		}

		if (itemSearchResponse.getItems() != null) {
			for (Items items : itemSearchResponse.getItems()) {
				if (items.getItem() != null) {
					if (items.getRequest().getErrors() != null) {
						for (Error error : items.getRequest().getErrors().getError()) {
							log.error(error.getCode() + ": " + error.getMessage());
							throw new IllegalArgumentException(error.getCode() + ": " + error.getMessage());
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

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("items", itemColl);
		map.put("totalPages", itemSearchResponse.getItems().get(0).getTotalPages());
		map.put("totalResults", itemSearchResponse.getItems().get(0).getTotalResults());

		return map;
	}
}