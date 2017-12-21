package com.gandem.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.ignite.Ignite;

import com.gandem.bean.Customer;
import com.gandem.bean.Order_H;
import com.gandem.bean.Product;

/**
 * jdbc工具类
 * 
 * @author gandam
 * @date 2016年3月8日 下午4:49:06
 */
public class JdbcSource {

	public static final String jdbcUrl = "jdbc:mysql://192.168.2.155:3306/znyuantest?user=znyuan&password=victop";

	public static boolean isMake = false;

	public static int count = 10;

	public static int sec = 5;

	/**
	 * 初始化数据库
	 */
	public void initData() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(jdbcUrl);

			Customer.initData(count, con);
			Product.initData(count, con);
			Order_H.initData(count, con);

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 每一秒插入数据
	 */
	public void makeData() {
		if (!isMake) {
			isMake = true;
			new Thread(new Runnable() {
				public void run() {
					long start = System.currentTimeMillis();
					System.out.println("生成开始...");
					while (true) {
						try {
							Class.forName("com.mysql.jdbc.Driver");
							Connection con = DriverManager.getConnection(jdbcUrl);
							Order_H.AddData(con);
							con.close();
							//System.out.println("生成一张订单！=。=");
						} catch (Exception e) {
							e.printStackTrace();
						}
						long end = System.currentTimeMillis();
						if (end - start > 4 * 60 * 1000l) {
							System.out.println("太多单据了～～～！T_T");
							return;
						}
						try {
							Thread.sleep(sec * 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}).start();

		}
	}

	public static void createData() {

		JdbcSource js = new JdbcSource();
		js.initData();
		js.makeData();

	}

	public static void main(String[] arg) {
		createData();
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(jdbcUrl);
	}

}
