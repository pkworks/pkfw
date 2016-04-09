package org.pkframework.web.security;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

public class CryptManager {

	public static String generateKey() {
		return KeyGenerators.string().generateKey();
	}

	public static String encrypt(String password, String salt, String text) {
		return textEncryptor(password, salt).encrypt(text);
	}

	public static String decrypt(String password, String salt, String encryptedText) {
		return textEncryptor(password, salt).decrypt(encryptedText);
	}

	private static TextEncryptor textEncryptor(String password, String salt) {
		return Encryptors.queryableText(password, salt);
	}

}
