package com.gandem.example1;

import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.gandem.bean.Customer;
import com.gandem.bean.Order_D;
import com.gandem.bean.Order_H;
import com.gandem.bean.Product;
import com.gandem.common.ConfigurationTool;

public class Example1_SQL {

	public static void main(String[] args) throws InterruptedException {

		IgniteConfiguration ic = ConfigurationTool.getTool().getIgniteConfiguration(false);
		try (Ignite ignite = Ignition.start(ic)) {

			IgniteCache<Object, Object> cache = ignite.getOrCreateCache(Example1_Server.PARTITIONED_cache);
			
			SqlFieldsQuery sfq = new SqlFieldsQuery("select top 1 * from Order_H "
					+ "join Order_D on Order_H.orderid=Order_D.orderid  order by Order_H.UpdateDate ");

			while (true) {
				QueryCursor<List<?>> cursor = cache.query(sfq);

				for (List<?> row : cursor) {
					StringBuffer sb = new StringBuffer();
					for (Object p : row) {
						sb.append(p);
						sb.append(",");
					}

					System.out.println(sb.toString());
				}
				Thread.sleep(10 * 1000);
			}

		}
	}
	


}
