package org.pkframework.web.auth.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.pkframework.core.utils.PropertiesUtils;
import org.pkframework.web.auth.service.SignUpService;
import org.pkframework.web.exception.CommonErrorCode;
import org.pkframework.web.exception.WebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SignUpController {

	@Autowired
	private SignUpService signUpService;

	//@RequestMapping(value="/auth/signup", method=RequestMethod.POST)
	@RequestMapping("/auth/signup")
	@ResponseBody
	public Map signUp(@RequestParam("userId") String userId,
			@RequestParam("userPassword") String userPassword,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (!PropertiesUtils.getBoolean("signup.enable")) {
			response.sendError(404);

			return null;
		}

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(userPassword)) {
			throw new WebException(CommonErrorCode.INVALID_SIGNUP);
		}

		Map<String, String> signUpParams = new HashMap<String, String>();
		String[] signUpParamNames = PropertiesUtils.getStringArray("signup.params");

		if (signUpParamNames == null) {
			throw new WebException(CommonErrorCode.SERVER_EXCEPTION);
		}

		for (int i=0;i<signUpParamNames.length;i++) {
			String signUpParamName = StringUtils.trim(signUpParamNames[i]);

			signUpParams.put(signUpParamName, StringUtils.defaultString(request.getParameter(signUpParamName)));
		}

		Map result = signUpService.signUp(userId, userPassword, signUpParams);

		return result;
	}

}
