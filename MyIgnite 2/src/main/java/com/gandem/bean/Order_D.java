package com.gandem.bean;

import java.io.Serializable;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * 订单明细
 * @author gandam
 * @date 2016年3月8日 下午5:18:37
 */
public class Order_D implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 @QuerySqlField
	private String Guid;
	 @QuerySqlField
	private String OrderID;
	 @QuerySqlField
	private String ProductID;
	private int Pcount;
	private int Price;
	private int PMoney;

	public String getGuid() {
		return Guid;
	}

	public void setGuid(String guid) {
		Guid = guid;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getProductID() {
		return ProductID;
	}

	public void setProductID(String productID) {
		ProductID = productID;
	}

	public int getPcount() {
		return Pcount;
	}

	public void setPcount(int pcount) {
		Pcount = pcount;
	}

	public int getPrice() {
		return Price;
	}

	public void setPrice(int price) {
		Price = price;
	}

	public int getPMoney() {
		return PMoney;
	}

	public void setPMoney(int pMoney) {
		PMoney = pMoney;
	}

}
