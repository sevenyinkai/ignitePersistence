package com.gandem.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 产品类
 * 
 * @author gandam
 * @date 2016年3月8日 下午5:01:23
 */
public class Product implements Serializable {

	/**
	 * 初始化个数
	 */
	private static int count = 1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ProductID;
	private String ProductName;
	private int Price;

	public String getProductID() {
		return ProductID;
	}

	public void setProductID(String productID) {
		ProductID = productID;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public int getPrice() {
		return Price;
	}

	public void setPrice(int price) {
		Price = price;
	}

	/**
	 * 插入数据
	 * 
	 * @param count
	 * @param conn
	 * @throws SQLException
	 */
	public static void initData(int count, Connection conn) throws SQLException {
		Product.count = count;

		Statement st = conn.createStatement();

		st.execute("delete from Product");

		for (int i = 1; i <= count; i++) {
			st.execute("insert into Product(ProductID,ProductName,price)values" + "('" + getProductID(i) + "','"
					+ getProductName(i) + "'," + i + ")");
		}
		st.close();

	}

	public static String getProductID(int i) {
		return "pd" + i;
	}

	public static String getProductName(int i) {
		return "产品" + i;
	}

	public static int getCount() {
		return count;
	}

}
