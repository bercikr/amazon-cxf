package com.googlecode.amazoncxf.util;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.security.WSPasswordCallback;

public class PasswordCallback implements CallbackHandler {

	protected final Log logger = LogFactory.getLog(getClass());

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		for (int i = 0; i < callbacks.length; i++) {
			WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

			int usage = pc.getUsage();
			logger.debug("identifier: " + pc.getIdentifier());
			logger.debug("usage: " + pc.getUsage());

			ResourceBundle props = ResourceBundle.getBundle("client_sign");

			if (usage == WSPasswordCallback.USERNAME_TOKEN) {
				// username token pwd...
				pc
						.setPassword(props
								.getString("org.apache.ws.security.crypto.merlin.keystore.password"));
			} else if (usage == WSPasswordCallback.SIGNATURE) {
				// set the password for client's keystore.keyPassword
				pc
						.setPassword(props
								.getString("org.apache.ws.security.crypto.merlin.keystore.password"));
			}
		}
	}
}