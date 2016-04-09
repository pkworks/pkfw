package org.pkframework.web.auth.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.pkframework.data.mybatis.AbstractMybatisDAO;
import org.springframework.stereotype.Repository;

@Repository
public class SignUpDAO extends AbstractMybatisDAO {

	public int countUserId(@Param("userId") String userId) {
		return selectOne("common.auth.signup.countUserId", userId);
	}

	public void insertUser(@Param("userId") String userId,
			@Param("userPassword") String userPassword) {
		insert("common.auth.signup.insertUser", userId, userPassword);
	}

	public void updateUid(@Param("uid") String uid,
			@Param("userId") String userId) {
		update("common.auth.signup.updateUid", uid, userId);
	}

	public void insertUserDetail(Map<String, String> signUpParams) {
		insert("common.auth.signup.insertUserDetail", signUpParams);
	}

	public void insertUserSecure(@Param("uid") String uid,
			@Param("secureKey") String secureKey) {
		insert("common.auth.signup.insertUserSecure", uid, secureKey);
	}

}
