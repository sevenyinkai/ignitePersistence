package com.gandem.example1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.gandem.bean.Customer;
import com.gandem.bean.Order_D;
import com.gandem.bean.Order_H;
import com.gandem.bean.Product;
import com.gandem.common.ConfigurationTool;
import com.gandem.common.JdbcSource;

public class Example1_Server {

	public static String REPLICATED_cache = "REPLICATED";
	public static String PARTITIONED_cache = "PARTITIONED";
	public static String nowDate = "1999-09-09"; // 检测时间
	public static SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static Ignite ignite;

	public static void main(String[] args) throws SQLException {

		IgniteConfiguration ic = ConfigurationTool.getTool().getIgniteConfiguration(false);
		createCache(ic);
		ignite = Ignition.start(ic);
		System.out.println();
		System.out.println(">>> Cache star  example1_Server started.");
		JdbcSource.createData();
		loadCache();
		checkData();

	}

	/**
	 * 初始化缓存
	 * 
	 * @param ic
	 */
	public static void createCache(IgniteConfiguration ic) {
		/**
		 * 重复类型
		 */
		CacheConfiguration<String, Object> repCacheCfg = new CacheConfiguration<>();
		repCacheCfg.setName(REPLICATED_cache);
		repCacheCfg.setCacheMode(CacheMode.REPLICATED);
		repCacheCfg.setIndexedTypes(String.class, Customer.class, String.class, Product.class);

		/**
		 * 分区类型
		 */
		CacheConfiguration<Object, Object> factCacheCfg = new CacheConfiguration<>();
		factCacheCfg.setName(PARTITIONED_cache);
		factCacheCfg.setCacheMode(CacheMode.PARTITIONED);
		factCacheCfg.setIndexedTypes(Object.class, Order_H.class, Object.class, Order_D.class);
		
		
		ic.setCacheConfiguration(repCacheCfg,factCacheCfg);

	}

	/**
	 * 手动加载缓存
	 * 
	 * @param ignite
	 * @throws SQLException
	 */
	private static void loadCache() throws SQLException {
		Connection conn = JdbcSource.getConnection();
		IgniteCache<String, Object> REPcache = ignite.getOrCreateCache(REPLICATED_cache);

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery("select * from Customer");
		while (rs.next()) {
			Customer cr = new Customer();
			cr.setCustomerID(rs.getString("CustomerID"));
			cr.setCustomerName(rs.getString("customerName"));
			REPcache.put(rs.getString("CustomerID"), cr);
		}
		rs.close();
		rs = st.executeQuery("select * from Product");
		while (rs.next()) {
			Product cr = new Product();
			cr.setProductID(rs.getString("ProductID"));
			cr.setProductName(rs.getString("ProductName"));
			cr.setPrice(rs.getInt("Price"));
			REPcache.put(rs.getString("ProductID"), cr);
		}
		rs.close();

		IgniteCache<Object, Object> PARTcache = ignite.getOrCreateCache(PARTITIONED_cache);

		rs = st.executeQuery("select * from Order_H order by updateDate");
		while (rs.next()) {
			String orderID = rs.getString("orderID");
			Order_H cr = new Order_H();
			cr.setCustomerID(rs.getString("customerID"));
			cr.setOrderID(orderID);
			cr.setOrderState(rs.getInt("orderState"));
			cr.setSumMoney(rs.getInt("sumMoney"));
			cr.setOrderDate(rs.getString("orderDate"));
			cr.setUpdateDate(rs.getString("updateDate"));

			nowDate = rs.getString("updateDate");

			PARTcache.put(orderID, cr);
			Statement std = conn.createStatement();
			ResultSet rsd = std.executeQuery("select * from Order_D where orderid='" + orderID + "'");
			while (rsd.next()) {
				Object personKey1 = new AffinityKey(rsd.getString("guid"), orderID);
				Order_D od = new Order_D();
				od.setGuid(rsd.getString("guid"));
				od.setOrderID(rsd.getString("orderid"));
				od.setPcount(rsd.getInt("pcount"));
				od.setPMoney(rsd.getInt("pMoney"));
				od.setPrice(rsd.getInt("price"));
				od.setProductID(rsd.getString("productID"));
				PARTcache.put(personKey1, od);
			}
			rsd.close();
		}
		rs.close();
		st.close();
	}

	/**
	 * 手工检测数据
	 */
	public static void checkData() {
		new Thread(new Runnable() {
			public void run() {
				System.out.println("检查开始...");
				while (true) {
					try {
						Connection conn = JdbcSource.getConnection();
						int count = 0;
						Statement st = conn.createStatement();
						ResultSet rs = st.executeQuery(
								"select * from Order_H where updateDate>'" + nowDate + "' order by updateDate");
						IgniteCache<Object, Object> PARTcache = ignite.getOrCreateCache(PARTITIONED_cache);
						while (rs.next()) {
							count++;
							Order_H cr = new Order_H();
							cr.setCustomerID(rs.getString("customerID"));
							cr.setOrderID(rs.getString("orderID"));
							cr.setOrderState(rs.getInt("orderState"));
							cr.setSumMoney(rs.getInt("sumMoney"));
							cr.setOrderDate(rs.getString("orderDate"));
							cr.setUpdateDate(rs.getString("updateDate"));

							nowDate = rs.getString("updateDate");

							PARTcache.put(rs.getString("orderID"), cr);
							Statement std = conn.createStatement();
							ResultSet rsd = std.executeQuery(
									"select * from Order_D where orderid='" + rs.getString("orderID") + "'");
							while (rsd.next()) {
								Object personKey1 = new AffinityKey(rsd.getString("guid"), rs.getString("orderID"));
								Order_D od = new Order_D();
								od.setGuid(rsd.getString("guid"));
								od.setOrderID(rsd.getString("orderid"));
								od.setPcount(rsd.getInt("pcount"));
								od.setPMoney(rsd.getInt("pMoney"));
								od.setPrice(rsd.getInt("price"));
								od.setProductID(rsd.getString("productID"));
								PARTcache.put(personKey1, od);
							}
							rsd.close();
						}
						rs.close();
						st.close();
						conn.close();
						//System.out.println("检查" + count + "张订单大于时间," + nowDate + "！>_<");
						count = 0;
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(10 * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

}
