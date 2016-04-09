package org.pkframework.web.exception;



public class WebException extends RuntimeException {

	private static final long serialVersionUID = 2992143330207076112L;

	private String errorCode = CommonErrorCode.UNEXPECTED;
	private String errorMsg = CommonErrorCode.MSG_UNEXPECTED;

	public WebException(String errorCode) {
		this(errorCode, errorCode, null);
	}

	public WebException(String errorCode, String errorMsg) {
		this(errorCode, errorMsg, null);
	}

	public WebException(String errorCode, Throwable cause) {
		this(errorCode, errorCode, cause);
	}

	public WebException(String errorCode, String errorMsg, Throwable cause) {
		super(errorCode, cause);

		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

    public WebException(Throwable cause) {
        this(CommonErrorCode.UNEXPECTED, cause);
    }

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
