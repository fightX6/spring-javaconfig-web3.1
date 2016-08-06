/**
 * 
 */
package me.xf.containerinitializer;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * AppConfiguration
 * 
 * @author xf
 *         2016年8月6日下午8:38:05
 */
@Configuration
@EnableTransactionManagement
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
@Import({ MVCConfigureation.class })
@ComponentScan(basePackages = { "me.xf" }, excludeFilters = { @Filter(classes = { Controller.class }) })
public class AppConfiguration {
	private Logger log = LoggerFactory.getLogger(AppConfiguration.class);

	/**
	 * 
	 */
	public AppConfiguration() {
		log.info("[Initialize application]");
		Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
	}
}
