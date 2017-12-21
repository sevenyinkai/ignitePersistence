package com.gandem.common;

import java.util.Arrays;

import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

/**
 * 配置类
 * 
 * @author gandam
 * @date 2016年3月8日 上午11:48:07
 */
public class ConfigurationTool {

	private static ConfigurationTool ct = new ConfigurationTool();

	public static ConfigurationTool getTool() {
		return ct;
	}

	/**
	 * 通过类配置Ignite节点
	 * 
	 * @param gridName
	 * @param isClient
	 * @return
	 */
	public IgniteConfiguration getIgniteConfiguration(Boolean isClient) {
		return getIgniteConfiguration("client", isClient);
	}

	/**
	 * 通过类配置Ignite节点
	 * 
	 * @param gridName
	 * @param isClient
	 * @return
	 */
	public IgniteConfiguration getIgniteConfiguration(String gridName, Boolean isClient) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		// ip
		TcpDiscoverySpi discoSpi = new TcpDiscoverySpi();
		TcpDiscoveryVmIpFinder IP_FINDER = new TcpDiscoveryVmIpFinder(true);
		IP_FINDER.setAddresses(Arrays.asList("127.0.0.1:47500..47509"));
		discoSpi.setIpFinder(IP_FINDER);	
		cfg.setDiscoverySpi(discoSpi);
		// 是否客户端模式
		cfg.setClientMode(isClient);
		// 网格名称
		cfg.setGridName(gridName);
		// 自动传递闭包类
		cfg.setPeerClassLoadingEnabled(true);

		return cfg;
	}

	/**
	 * 通过类配置cache
	 * 
	 * @return
	 */
	private CacheConfiguration getcacheConfiguration() {
		CacheConfiguration<Object, Object> ct = new CacheConfiguration();

		return ct;
	}

}
