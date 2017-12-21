package com.gandem.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 客户类
 * 
 * @author gandam
 * @date 2016年3月8日 下午4:58:33
 */
public class Customer implements Serializable {

	/**
	 * 初始化个数
	 */
	private static int count = 1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String CustomerID;

	private String CustomerName;

	public String getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	/**
	 * 插入数据
	 * 
	 * @param count
	 * @param conn
	 * @throws SQLException
	 */
	public static void initData(int count, Connection conn) throws SQLException {
		Customer.count = count;

		Statement st = conn.createStatement();

		st.execute("delete from Customer");

		for (int i = 1; i <= count; i++) {
			st.execute("insert into Customer(CustomerID,CustomerName)values" + "('" + getCustomerID(i) + "','"
					+ getCustomerName(i) + "')");
		}
		st.close();

	}

	public static String getCustomerID(int i) {
		return "ct" + i;
	}

	public static String getCustomerName(int i) {
		return "客户" + i;
	}

	public static int getCount() {
		return count;
	}

}
