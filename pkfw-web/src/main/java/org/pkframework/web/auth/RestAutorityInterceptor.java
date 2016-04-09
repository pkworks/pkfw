package org.pkframework.web.auth;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.pkframework.web.annotation.SignInRequired;
import org.pkframework.web.exception.CommonErrorCode;
import org.pkframework.web.session.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class RestAutorityInterceptor implements MethodInterceptor {

	private static Logger LOGGER = LoggerFactory.getLogger(RestAutorityInterceptor.class);

	@Autowired
	private MessageSource messageSource;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (!SessionHolder.isSignedIn()) {
			return autorityException(invocation, CommonErrorCode.NOT_SIGNIN);
		} else {
			String[] authorities = getAuthorities(invocation);

			if (ArrayUtils.isNotEmpty(authorities)
					&& !UserManager.isAuthorized(authorities)) {
				return autorityException(invocation, CommonErrorCode.NOT_AUTHORIZED);
			}
		}

		return invocation.proceed();
	}

	private String[] getAuthorities(MethodInvocation invocation) {
		return invocation.getMethod().getAnnotation(SignInRequired.class).value();
	}

	private String getAuthority(MethodInvocation invocation) {
		String[] values = invocation.getMethod().getAnnotation(SignInRequired.class).value();

		if (ArrayUtils.isEmpty(values)) {
			return StringUtils.EMPTY;
		}

		return values[0];
	}

	private Object autorityException(MethodInvocation invocation, String errorCode) throws Exception {
		Class returnType = invocation.getMethod().getReturnType();

		if (String.class.equals(returnType)) {
			if (CommonErrorCode.NOT_AUTHORIZED.equals(errorCode)) {
				SessionHolder.getHttpRespnse().sendError(403);
			}

			HttpServletRequest request = SessionHolder.getHttpRequest();

			String requestUrl = request.getRequestURI();
			if (StringUtils.isNotBlank(request.getQueryString())) {
				requestUrl += "?" + request.getQueryString();
			}

			request.setAttribute("requestUrl", requestUrl);

			return "common/signin";
		}

		SessionHolder.getHttpRespnse().sendError(401);

		return null;
	}

}
