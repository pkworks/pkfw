package org.pkframework.web.rest;

import java.util.HashMap;
import java.util.Map;

public class RestResult {

	public static Map set(String key, Object value) {
		Map result = new HashMap();
		result.put(key, value);

		return result;
	}

}
