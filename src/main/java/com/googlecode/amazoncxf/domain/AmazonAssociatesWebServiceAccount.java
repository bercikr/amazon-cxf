package com.googlecode.amazoncxf.domain;

import org.springframework.beans.factory.annotation.Required;

public class AmazonAssociatesWebServiceAccount {
	public String awsAccessKeyId;

	public String associateTag;

	public String getAwsAccessKeyId() {
		return awsAccessKeyId;
	}

	@Required
	public void setAwsAccessKeyId(String awsAccessKeyId) {
		this.awsAccessKeyId = awsAccessKeyId;
	}

	public String getAssociateTag() {
		return associateTag;
	}

	@Required
	public void setAssociateTag(String associateTag) {
		this.associateTag = associateTag;
	}

}
