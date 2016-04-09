package org.pkframework.web.session;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionHolder {

	private static Logger LOGGER = LoggerFactory.getLogger(SessionHolder.class);

	private static final ThreadLocal<HttpServletRequest> THREAD_LOCAL_REQUEST = new ThreadLocal<HttpServletRequest>();
	private static final ThreadLocal<HttpServletResponse> THREAD_LOCAL_RESPONSE = new ThreadLocal<HttpServletResponse>();

	public static final String KEY_USER_IP = "_userIp";
	public static final String KEY_SIGNED_IN = "_signedIn";
	public static final String KEY_USER_ROLES = "_userRoles";
	public static final String KEY_USER_INFO = "_userInfo";

	public static HttpServletRequest getHttpRequest() {
		return THREAD_LOCAL_REQUEST.get();
	}

	public static HttpServletResponse getHttpRespnse() {
		return THREAD_LOCAL_RESPONSE.get();
	}

	private static HttpSession getSession() {
		return getHttpRequest().getSession();
	}

	public static void _init(HttpServletRequest request,
			HttpServletResponse response) {
		THREAD_LOCAL_REQUEST.set(request);
		THREAD_LOCAL_RESPONSE.set(response);

		setUserIp();
	}

	public static void _remove() {
		THREAD_LOCAL_REQUEST.remove();
		THREAD_LOCAL_RESPONSE.remove();
	}

	public static String getSessionId() {
		return getSession().getId();
	}

	public static String getUserIp() {
		return getString(KEY_USER_IP);
	}

	private static void setUserIp() {
		HttpServletRequest request = getHttpRequest();

		String remoteAddr = request.getHeader("x-forwarded-for");
		if (StringUtils.isEmpty(remoteAddr)) {
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
			if (StringUtils.isEmpty(remoteAddr)) {
				remoteAddr = request.getHeader("Proxy-Client-IP");
				if (StringUtils.isEmpty(remoteAddr)) {
					remoteAddr = request.getRemoteAddr();
				}
			}
		}

		if (remoteAddr.indexOf(",") >= 0) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("clientIP[Array]=" + remoteAddr);
			}
			String[] clientIPs = remoteAddr.split(",");
			remoteAddr = clientIPs[0];
		}

		setSession(KEY_USER_IP, remoteAddr);
	}

	public static Object getObject(String key) {
		return getSession().getAttribute(key);
	}

	public static String getString(String key) {
		return (String) getSession().getAttribute(key);
	}

	public static void setSession(String key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static void signIn() {
		setSession(KEY_SIGNED_IN, true);
	}

	public static void signOut() {
		getSession().invalidate();
	}

	public static boolean isSignedIn() {
		if (getObject(KEY_SIGNED_IN) == null)
			return false;

		return (Boolean) getObject(KEY_SIGNED_IN);
	}

	public static boolean isSignedIn(HttpServletRequest request) {
		if (request.getSession().getAttribute(KEY_SIGNED_IN) == null)
			return false;

		return (Boolean) request.getSession().getAttribute(KEY_SIGNED_IN);
	}

	public static void setUserRoles(List<String> userRoles) {
		setSession(KEY_USER_ROLES, userRoles);
	}

	public static List<String> getUserRoles() {
		return (List<String>) getObject(KEY_USER_ROLES);
	}

}
