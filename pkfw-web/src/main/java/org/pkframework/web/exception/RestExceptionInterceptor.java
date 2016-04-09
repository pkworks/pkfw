package org.pkframework.web.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.pkframework.web.session.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestExceptionInterceptor implements MethodInterceptor {

	private static Logger LOGGER = LoggerFactory.getLogger(RestExceptionInterceptor.class);

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		Object result = null;

		try {
			result = invocation.proceed();
		} catch (Throwable t) {
			LOGGER.error(ExceptionUtils.getStackTrace(t));

			String errorCode = CommonErrorCode.UNEXPECTED;
			String errorMsg = CommonErrorCode.MSG_UNEXPECTED;

			if (t instanceof WebException) {
				errorCode = ((WebException) t).getErrorCode();
				errorMsg = ((WebException) t).getErrorMsg();
			}

			Class returnType = invocation.getMethod().getReturnType();

			if (String.class.equals(returnType)) {
				if (CommonErrorCode.INVALID_SIGNIN.equals(errorCode)
						|| CommonErrorCode.NOT_SIGNUP.equals(errorCode)) {
					HttpServletRequest request = SessionHolder.getHttpRequest();
					HttpServletResponse response = SessionHolder.getHttpRespnse();

					SessionHolder.setSession("errorMsg", "Login unsuccessful. Please try again.");

					response.sendRedirect(request.getParameter("requestUrl"));

					return null;
				} else if (CommonErrorCode.NOT_AUTHORIZED.equals(errorCode)) {
					SessionHolder.getHttpRespnse().sendError(403);
				}
			}

			SessionHolder.getHttpRespnse().sendError(500, errorMsg);
		}

		return result;
	}

}
