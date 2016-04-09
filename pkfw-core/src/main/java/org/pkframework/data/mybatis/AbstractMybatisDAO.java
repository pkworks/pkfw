package org.pkframework.data.mybatis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMybatisDAO {

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public enum QueryType {
		INSERT, UPDATE, DELETE
	}

	private SqlSessionFactory sqlSessionFactory;

	private SqlSession sqlSession;

	@Resource(name="sqlSessionFactory")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	@Resource(name="sqlSession")
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	protected <T> T selectOne(String queryId) {
		return getSqlSession().selectOne(queryId);
	}

	protected <T> T selectOne(String queryId, Object parameter) {
		return getSqlSession().selectOne(queryId, parameter);
	}

	protected <T> T selectOne(String queryId, Object... parameters) {
		return getSqlSession().selectOne(queryId, getParameterMap(parameters));
	}

	protected <E> List<E> selectList(String queryId) {
		return getSqlSession().selectList(queryId);
	}

	protected <E> List<E> selectList(String queryId, Object parameter) {
		return getSqlSession().selectList(queryId, parameter);
	}

	protected <E> List<E> selectList(String queryId, Object... parameters) {
		return getSqlSession().selectList(queryId, getParameterMap(parameters));
	}

	protected <E> List<E> selectList(String queryId, Object parameter,
			int offset, int limit) {
		return getSqlSession().selectList(queryId, parameter, new RowBounds(offset,
				limit));
	}

	protected <E> List<E> selectListPaging(String queryId, Object parameter,
			int page, int rowPerPage) {
		int offset = (page - 1) * rowPerPage;
		return selectList(queryId, parameter, offset, rowPerPage);
	}

	protected <K, V> Map<K, V> selectMap(String queryId, String mapKey) {
		return getSqlSession().selectMap(queryId, mapKey);
	}

	protected <K, V> Map<K, V> selectMap(String queryId, Object parameter,
			String mapKey) {
		return getSqlSession().selectMap(queryId, parameter, mapKey);
	}

	protected <K, V> Map<K, V> selectMap(String queryId, Object parameter,
			String mapKey, int offset, int limit) {
		return getSqlSession().selectMap(queryId, parameter, mapKey, new RowBounds(
				offset, limit));
	}

	protected <K, V> Map<K, V> selectMapPaging(String queryId,
			Object parameter, String mapKey, int page, int rowPerPage) {
		int offset = (page - 1) * rowPerPage;
		return selectMap(queryId, parameter, mapKey, offset, rowPerPage);
	}

	protected int insert(String queryId, Object parameter) {
		return insert(getSqlSession(), queryId, parameter);
	}

	protected int insert(String queryId, Object... parameters) {
		return insert(getSqlSession(), queryId, getParameterMap(parameters));
	}

	protected int insert(SqlSession sqlSession, String queryId, Object parameter) {
		return sqlSession.insert(queryId, parameter);
	}

	protected int insert(SqlSession sqlSession, String queryId,
			Object... parameters) {
		return sqlSession.insert(queryId, getParameterMap(parameters));
	}

	protected int update(String queryId, Object parameter) {
		return update(getSqlSession(), queryId, parameter);
	}

	protected int update(String queryId, Object... parameters) {
		return update(getSqlSession(), queryId, getParameterMap(parameters));
	}

	protected int update(SqlSession sqlSession, String queryId, Object parameter) {
		return sqlSession.update(queryId, parameter);
	}

	protected int update(SqlSession sqlSession, String queryId,
			Object... parameters) {
		return sqlSession.update(queryId, getParameterMap(parameters));
	}

	protected int delete(String queryId, Object parameter) {
		return delete(getSqlSession(), queryId, parameter);
	}

	protected int delete(String queryId, Object... parameters) {
		return delete(getSqlSession(), queryId, getParameterMap(parameters));
	}

	protected int delete(SqlSession sqlSession, String queryId, Object parameter) {
		return sqlSession.delete(queryId, parameter);
	}

	protected int delete(SqlSession sqlSession, String queryId,
			Object... parameters) {
		return sqlSession.delete(queryId, getParameterMap(parameters));
	}

	protected <T> int batch(QueryType queryType, String queryId,
			List<T> parameters) {
		List<QueryType> queryTypes = new ArrayList<QueryType>();
		queryTypes.add(queryType);
		List<String> queryIds = new ArrayList<String>();
		queryIds.add(queryId);
		return batch(queryTypes, queryIds, parameters);
	}

	protected <T> int batch(QueryType queryType, List<String> queryIds,
			List<T> parameters) {
		List<QueryType> queryTypes = new ArrayList<QueryType>();
		queryTypes.add(queryType);
		return batch(queryTypes, queryIds, parameters);
	}

	protected <T> int batch(QueryType queryType, List<String> queryIds,
			T parameter) {
		List<QueryType> queryTypes = new ArrayList<QueryType>();
		queryTypes.add(queryType);
		List<T> parameters = new ArrayList<T>();
		parameters.add(parameter);
		return batch(queryTypes, queryIds, parameters);
	}

	protected <T> int batch(List<QueryType> queryTypes, List<String> queryIds,
			T parameter) {
		List<T> parameters = new ArrayList<T>();
		parameters.add(parameter);
		return batch(queryTypes, queryIds, parameters);
	}

	protected <T> int batch(List<QueryType> queryTypes, List<String> queryIds,
			List<T> parameters) {
		if (queryTypes == null || queryTypes.isEmpty()) {
			return 0;
		}

		if (queryIds == null || queryIds.isEmpty()) {
			return 0;
		}

		if (parameters == null || parameters.isEmpty()) {
			return 0;
		}

		SqlSession sqlSession = getSqlSessionFactory().openSession(ExecutorType.BATCH);

		int resultCount = 0;
		int maxSize = parameters.size() >= queryIds.size() ? parameters.size()
				: queryIds.size();
		try {
			for (int index = 0; index < maxSize; index++) {
				QueryType queryType = queryTypes.size() == 1 ? queryTypes
						.get(0) : queryTypes.get(index);
				String queryId = queryIds.size() == 1 ? queryIds.get(0)
						: queryIds.get(index);
				Object parameter = parameters.size() == 1 ? parameters.get(0)
						: parameters.get(index);
				switch (queryType) {
				case INSERT:
					resultCount += insert(sqlSession, queryId, parameter);
					break;
				case UPDATE:
					resultCount += update(sqlSession, queryId, parameter);
					break;
				case DELETE:
					resultCount += delete(sqlSession, queryId, parameter);
					break;
				}
			}
			sqlSession.commit();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			sqlSession.rollback();
			resultCount = 0;
		} finally {
			sqlSession.close();
		}
		return resultCount;
	}

	/**
	 * @param parameters
	 * @return
	 */
	private Map<String, Object> getParameterMap(Object... parameters) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		Method method = getMethod(parameters);
		if (method != null) {
			Annotation[][] annotationsList = method.getParameterAnnotations();
			for (int index = 0; index < annotationsList.length; index++) {
				for (Annotation annotation : annotationsList[index]) {
					if (!(annotation instanceof Param)) {
						continue;
					}
					String key = ((Param) annotation).value();
					Object value = parameters[index];
					parameterMap.put(key, value);
				}
			}
		}

		return parameterMap;
	}

	/**
	 * @param parameters
	 * @return
	 * @throws NoSuchMethodException
	 */
	private Method getMethod(Object... parameters) {
		Method method = null;
		Class<?>[] classes = getParameterClasses(parameters);
		String methodName = Thread.currentThread().getStackTrace()[4]
				.getMethodName();
		try {
			method = getClass().getMethod(methodName, classes);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return method;
	}

	/**
	 * @param parameters
	 * @return
	 */
	private Class<?>[] getParameterClasses(Object... parameters) {
		Class<?>[] classes = new Class[parameters.length];
		for (int index = 0; index < parameters.length; index++) {
			classes[index] = parameters[index].getClass();
		}
		return classes;
	}

}
