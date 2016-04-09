package org.pkframework.web.security;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

	private static final int STRENGTH = 4;

	private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(STRENGTH);

	public static String encode(String rawPassword) {
		return encode(rawPassword, true);
	}

	public static String encode(String rawPassword, boolean base64) {
		String encodedPassword = encoder.encode(rawPassword);

		if (base64) {
			return encodeBase64(encodedPassword);
		}

		return encodedPassword;
	}

	public static boolean match(String rawPassword, String encodedPassword) {
		return match(rawPassword, encodedPassword, true);
	}

	public static boolean match(String rawPassword, String encodedPassword, boolean base64) {
		if (base64) {
			return encoder.matches(rawPassword, decodeBase64(encodedPassword));
		}

		return encoder.matches(rawPassword, encodedPassword);
	}

	private static String encodeBase64(String encodedPassword) {
		return Base64.encodeBase64String(encodedPassword.getBytes());
	}

	private static String decodeBase64(String base64Password) {
		return new String(Base64.decodeBase64(base64Password));
	}

}
