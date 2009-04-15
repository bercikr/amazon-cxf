package com.googlecode.amazoncxf.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
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

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
