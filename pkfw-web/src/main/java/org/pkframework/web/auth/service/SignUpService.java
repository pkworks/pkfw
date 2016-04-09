package org.pkframework.web.auth.service;

import java.util.HashMap;
import java.util.Map;

import org.pkframework.web.auth.UserManager;
import org.pkframework.web.auth.dao.SignUpDAO;
import org.pkframework.web.exception.CommonErrorCode;
import org.pkframework.web.exception.WebException;
import org.pkframework.web.security.CryptManager;
import org.pkframework.web.security.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignUpService {

	@Autowired
	private SignUpDAO signUpDAO;

	@Transactional
	public Map signUp(String userId, String userPassword, Map<String, String> signUpParams) {
		if (signUpDAO.countUserId(userId) > 0) {
			throw new WebException(CommonErrorCode.ALREADY_SIGNUP);
		}

		String encodedUserPassword = PasswordEncoder.encode(userPassword);

		signUpDAO.insertUser(userId, encodedUserPassword);

		String secureKey = CryptManager.generateKey();
		String uid = UserManager.createUid(secureKey);

		signUpDAO.updateUid(uid, userId);

		signUpParams.put("uid", uid);

		signUpDAO.insertUserDetail(signUpParams);

		signUpDAO.insertUserSecure(uid, secureKey);

		Map<String, String> result = new HashMap<String, String>();

		result.put("uid", uid);

		return result;
	}

}
