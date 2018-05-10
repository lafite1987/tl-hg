package cn.lfy.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.google.common.collect.Lists;  
  
public class JdbcUtil {  
  
    // 表示定义数据库的用户名  
    private static String USERNAME ;  
  
    // 定义数据库的密码  
    private static String PASSWORD;  
  
    // 定义访问数据库的地址  
    private static String URL;  
  
    // 定义数据库的链接  
    private Connection connection;  
  
    // 定义sql语句的执行对象  
    private PreparedStatement pstmt;  
  
    // 定义查询返回的结果集合  
    private ResultSet resultSet;  
      
    public JdbcUtil(String url, String username, String password) {  
    	URL = url;
    	USERNAME = username;
    	PASSWORD = password;
    }  
  
    /** 
     * 获取数据库连接 
     *  
     * @return 数据库连接 
     */  
    public Connection getConnection() {  
        try {  
            Class.forName("com.mysql.jdbc.Driver"); // 注册驱动  
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // 获取连接  
        } catch (Exception e) {  
            throw new RuntimeException("get connection error!", e);  
        }  
        return connection;  
    }  
  
    /** 
     * 执行更新操作 
     *  
     * @param sql 
     *            sql语句 
     * @param params 
     *            执行参数 
     * @return 执行结果 
     * @throws SQLException 
     */  
    public boolean updateByPreparedStatement(String sql, List<?> params)  
            throws SQLException {  
        boolean flag = false;  
        int result = -1;// 表示当用户执行添加删除和修改的时候所影响数据库的行数  
        pstmt = connection.prepareStatement(sql);  
        int index = 1;  
        // 填充sql语句中的占位符  
        if (params != null && !params.isEmpty()) {  
            for (int i = 0; i < params.size(); i++) {  
                pstmt.setObject(index++, params.get(i));  
            }  
        }  
        result = pstmt.executeUpdate();  
        flag = result > 0 ? true : false;  
        return flag;  
    }  
  
    /** 
     * 执行查询操作 
     *  
     * @param sql 
     *            sql语句 
     * @param params 
     *            执行参数 
     * @return 
     * @throws SQLException 
     */  
    public List<Map<String, Object>> findResult(String sql, List<?> params)  
            throws SQLException {  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
        int index = 1;  
        pstmt = connection.prepareStatement(sql);  
        if (params != null && !params.isEmpty()) {  
            for (int i = 0; i < params.size(); i++) {  
                pstmt.setObject(index++, params.get(i));  
            }  
        }  
        resultSet = pstmt.executeQuery();  
        ResultSetMetaData metaData = resultSet.getMetaData();  
        int cols_len = metaData.getColumnCount();  
        while (resultSet.next()) {  
            Map<String, Object> map = new HashMap<String, Object>();  
            for (int i = 0; i < cols_len; i++) {  
                String cols_name = metaData.getColumnName(i + 1);  
                Object cols_value = resultSet.getObject(cols_name);  
                if (cols_value == null) {  
                    cols_value = "";  
                }  
                map.put(cols_name, cols_value);  
            }  
            list.add(map);  
        }  
        return list;  
    }  
  
    /** 
     * 释放资源 
     */  
    public void releaseConn() {  
        if (resultSet != null) {  
            try {  
                resultSet.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
        if (pstmt != null) {  
            try {  
                pstmt.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
        if (connection != null) {  
            try {  
                connection.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    public static void main(String[] args) { 
    	String url = "jdbc:mysql://120.77.241.130:3306/xyct?useUnicode=true&characterEncoding=utf8&useSSL=false";
    	String username = "root";
    	String password = "chen123!@#$";
//        JdbcUtil jdbcUtil = new JdbcUtil(url, username, password);  
//        jdbcUtil.getConnection();  
//        try {  
//        	String sql = "insert into test(`nickname`) values(?)";
//        	List<String> params = Lists.newArrayList("拉菲😊");
//            boolean result = jdbcUtil.updateByPreparedStatement(sql, params);
//            System.out.println(result);
//        } catch (Throwable e) {  
//            e.printStackTrace();  
//        } finally {  
//            jdbcUtil.releaseConn();  
//        }  
        
//        com.alibaba.druid.pool.DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        dataSource.setConnectionInitSqls(Lists.newArrayList("set names utf8mb4;"));
//        try {
//        	String sql = "insert into test(`nickname`) values(?)";
//        	String nickname = "拉菲😊";
//			DruidPooledConnection connection2 = dataSource.getConnection();
//			PreparedStatement prepareStatement = connection2.prepareStatement(sql);
//			prepareStatement.setString(1, nickname);
//			int result = prepareStatement.executeUpdate();  
//			System.out.println(result);
//			connection2.close();
//			dataSource.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
    	
    	com.alibaba.druid.pool.DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setConnectionInitSqls(Lists.newArrayList("set names utf8mb4;"));
        try {
        	String sql = "select * from test where id = 3";
			DruidPooledConnection connection2 = dataSource.getConnection();
			PreparedStatement prepareStatement = connection2.prepareStatement(sql);
			ResultSet executeQuery = prepareStatement.executeQuery();
			while(executeQuery.next()) {
				String nickname = executeQuery.getString("nickname");
				System.out.println(nickname);
			}
			connection2.close();
			dataSource.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }  
}  
