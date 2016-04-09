package org.pkframework.web.auth;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.pkframework.web.security.PasswordEncoder;
import org.pkframework.web.session.SessionHolder;

public class UserManager {

	private static final String ZERO = "0";

	public static String createUid(String secureKey) {
		int random = (int) (System.currentTimeMillis() % 1000);

		return PasswordEncoder.encode(
				StringUtils.leftPad(String.valueOf(random) + secureKey, 12, ZERO));
	}

	public static void setUserInfo(Map<String, String> userInfo) {
		SessionHolder.setSession(SessionHolder.KEY_USER_INFO, userInfo);
	}

	public static Map<String, String> getUserInfo() {
		return (Map<String, String>) SessionHolder.getObject(SessionHolder.KEY_USER_INFO);
	}

	public static Map<String, String> getUserInfo(HttpServletRequest request) {
		return (Map<String, String>) request.getSession().getAttribute(SessionHolder.KEY_USER_INFO);
	}

	public static String getUid() {
		Map<String, String> userInfo = getUserInfo();

		if (userInfo == null) {
			return null;
		}

		return userInfo.get("uid");
	}

	public static void setUserRoles(List<String> userRoles) {
		SessionHolder.setUserRoles(userRoles);
	}

	public static List<String> getUserRoles() {
		return SessionHolder.getUserRoles();
	}

	public static boolean isAuthorized(String[] authorities) {
		List<String> userRoles = getUserRoles();

		if (userRoles == null) {
			return false;
		}

		for (String authority : authorities) {
			if (userRoles.contains(authority)) {
				return true;
			}
		}

		return false;
	}

}
