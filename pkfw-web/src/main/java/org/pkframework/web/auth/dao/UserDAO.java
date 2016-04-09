package org.pkframework.web.auth.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.pkframework.data.mybatis.AbstractMybatisDAO;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO extends AbstractMybatisDAO {

	public Map<String, String> selectUserCredential(@Param("userId") String userId) {
		return selectOne("common.auth.user.selectUserCredential", userId);
	}

	public void updateSigninDt(@Param("userid") String uid) {
		update("common.auth.user.updateSigninDt", uid);
	}

	public Map<String, String> selectUserInfo(@Param("uid") String uid) {
		return selectOne("common.auth.user.selectUserInfo", uid);
	}

	public List<String> selectUserRoles(@Param("uid") String uid) {
		return selectList("common.auth.user.selectUserRoles", uid);
	}

}
