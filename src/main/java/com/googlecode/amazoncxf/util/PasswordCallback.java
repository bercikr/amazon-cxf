package com.googlecode.amazoncxf.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wss4j.common.ext.WSPasswordCallback;

public class PasswordCallback implements CallbackHandler {
	protected static final Log logger = LogFactory.getLog(PasswordCallback.class);
	private static Properties props;
	private static Map<String, String> passwords;

	static {
		props = new Properties();
		passwords = new HashMap<String, String>();
		try {
			props.load(ClassLoader.getSystemResourceAsStream("client_sign.properties"));
			passwords.put(props.getProperty("org.apache.ws.security.crypto.merlin.keystore.alias"), props.getProperty("org.apache.ws.security.crypto.merlin.keystore.password"));
		} catch (Throwable t) {
			logger.error(t);
		}
		logger.debug(passwords);
	}

	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];

			String pass = passwords.get(pc.getIdentifier());
			if (pass != null) {
				pc.setPassword(pass);
				return;
			}
		}
	}
}