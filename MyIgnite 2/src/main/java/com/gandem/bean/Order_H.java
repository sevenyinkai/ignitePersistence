package com.gandem.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * 订单
 * 
 * @author gandam
 * @date 2016年3月8日 下午5:18:51
 */
public class Order_H implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 @QuerySqlField
	private String OrderID;
	 @QuerySqlField
	private String CustomerID;
	private String OrderDate;
	private int OrderState;
	@QuerySqlField
	private int SumMoney;
	@QuerySqlField
	private String UpdateDate;

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}

	public String getOrderDate() {
		return OrderDate;
	}

	public void setOrderDate(String orderDate) {
		OrderDate = orderDate;
	}

	public int getOrderState() {
		return OrderState;
	}

	public void setOrderState(int orderState) {
		OrderState = orderState;
	}

	public int getSumMoney() {
		return SumMoney;
	}

	public void setSumMoney(int sumMoney) {
		SumMoney = sumMoney;
	}

	public String getUpdateDate() {
		return UpdateDate;
	}

	public void setUpdateDate(String updateDate) {
		UpdateDate = updateDate;
	}

	/**
	 * 
	 * @param conn
	 */
	public static void initData(int count, Connection conn) throws SQLException {
		Statement st = conn.createStatement();

		st.execute("delete from Order_D");
		st.execute("delete from Order_H");
		st.close();
		for (int i = 1; i <= count; i++) {
			AddData(conn);
		}

	}

	/*
	 * 
	 * 
	 * 销售单表头
	 * Order_H【OrderID(订单号),CustomerID(客户编号),OrderDate(订单日期),OrderState(订单状态),
	 * SumMoney(总金额),UpdateDate(修改时间)】
	 * 
	 * 销售单表体
	 * Order_D【Guid,OrderID(订单号),ProductID(编号),Pcount(数量)，Price(价格),PMoney(金额)】
	 */
	public static void AddData(Connection conn) throws SQLException {
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String OrderID = "Order" + time.format(nowTime);

		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = datef.format(nowTime);

		int CustomerID = (int) (Math.random() * Customer.getCount());
		int sumMoney = 0;
		int detailCount = (int) (Math.random() * 10);

		Statement st = conn.createStatement();

		for (int i = 0; i < detailCount; i++) {
			UUID uuid = UUID.randomUUID();
			String rowid = uuid.toString();
			int ProductID = (int) (Math.random() * (Product.getCount() - 1)) + 1;
			int Pcount = (int) (Math.random() * 20) + 1;
			int PMoney = Pcount * ProductID;

			st.execute("insert into Order_D(Guid,OrderID,ProductID,Pcount,Price,PMoney)values" + "('" + rowid + "','"
					+ OrderID + "','" + Product.getProductID(ProductID) + "'," + Pcount + "," + ProductID + "," + PMoney
					+ ")");
			sumMoney = sumMoney + PMoney;
		}

		st.execute("insert into Order_H(OrderID,CustomerID,OrderDate,OrderState,SumMoney,UpdateDate)values" + "('"
				+ OrderID + "','" + Customer.getCustomerID(CustomerID) + "','" + dateTime + "',0," + sumMoney + ",'"
				+ dateTime + "')");
		st.close();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toString() {
		return "OrderID:" + this.OrderID + ",CustomerID:" + this.CustomerID + ",OrderDate:" + this.OrderDate
				+ ",OrderState:" + this.OrderState + ",SumMoney:" + this.SumMoney + ",UpdateDate:" + this.UpdateDate;
	}

}
