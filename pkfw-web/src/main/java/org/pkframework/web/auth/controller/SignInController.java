package org.pkframework.web.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.pkframework.core.utils.PropertiesUtils;
import org.pkframework.web.auth.UserManager;
import org.pkframework.web.auth.service.UserService;
import org.pkframework.web.exception.CommonErrorCode;
import org.pkframework.web.exception.WebException;
import org.pkframework.web.rest.RestResult;
import org.pkframework.web.session.SessionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SignInController {

	@Autowired
	private UserService userService;

	@RequestMapping(value="/api/auth/signin", method={RequestMethod.POST})
	@ResponseBody
	public Map signInApi(@RequestParam("userId") String userId,
			@RequestParam("userPassword") String userPassword,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			signIn(userId, userPassword);
		} catch (WebException e) {
			if (CommonErrorCode.INVALID_ACCESS.equals(e.getErrorCode())) {
				response.sendError(404);

				return null;
			}

			throw e;
		}

		return RestResult.set("result", "ok");
	}

	@RequestMapping(value="/auth/signin", method={RequestMethod.POST})
	public String signIn(@RequestParam("userId") String userId,
			@RequestParam("userPassword") String userPassword,
			@RequestParam("requestUrl") String requestUrl,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			signIn(userId, userPassword);
		} catch (WebException e) {
			if (CommonErrorCode.INVALID_ACCESS.equals(e.getErrorCode())) {
				response.sendError(404);

				return null;
			}

			throw e;
		}

		response.sendRedirect(requestUrl);

		return null;
	}

	private void signIn(String userId, String userPassword) {
		if (!PropertiesUtils.getBoolean("signin.enable")) {
			throw new WebException(CommonErrorCode.INVALID_ACCESS);
		}

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(userPassword)) {
			throw new WebException(CommonErrorCode.INVALID_SIGNIN);
		}

		Map<String, String> userInfo = userService.signIn(userId, userPassword);

		UserManager.setUserInfo(userInfo);
		UserManager.setUserRoles(userService.getUserRoles(UserManager.getUid()));

		SessionHolder.signIn();
	}

	@RequestMapping("/api/auth/signout")
	@ResponseBody
	public Map signOutApi() {
		SessionHolder.signOut();

		return RestResult.set("result", "ok");
	}

	@RequestMapping("/auth/signout")
	@ResponseBody
	public String signOut(HttpServletResponse response) throws Exception {
		SessionHolder.signOut();

		response.sendRedirect(PropertiesUtils.getString("signout.url"));

		return null;
	}

}
