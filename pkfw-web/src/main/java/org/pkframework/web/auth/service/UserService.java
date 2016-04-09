package org.pkframework.web.auth.service;

import java.util.List;
import java.util.Map;

import org.pkframework.web.auth.dao.SignUpDAO;
import org.pkframework.web.auth.dao.UserDAO;
import org.pkframework.web.exception.CommonErrorCode;
import org.pkframework.web.exception.WebException;
import org.pkframework.web.security.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private SignUpDAO signUpDAO;
	@Autowired
	private UserDAO userDAO;

	public Map<String, String> signIn(String userId, String userPassword) {
		if (signUpDAO.countUserId(userId) == 0) {
			throw new WebException(CommonErrorCode.NOT_SIGNUP);
		}

		Map<String, String> userCredential = userDAO.selectUserCredential(userId);

		String uid = userCredential.get("uid");
		String encodedUserPassword = userCredential.get("userPassword");

		if (!PasswordEncoder.match(userPassword, encodedUserPassword)) {
			throw new WebException(CommonErrorCode.INVALID_SIGNIN);
		}

		userDAO.updateSigninDt(uid);

		return userDAO.selectUserInfo(uid);
	}

	public List<String> getUserRoles(String uid) {
		return userDAO.selectUserRoles(uid);
	}

}
