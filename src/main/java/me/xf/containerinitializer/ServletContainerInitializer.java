package me.xf.containerinitializer;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 
 * 在Spring整体框架的核心概念中，容器是核心思想，就是用来管理Bean的整个生命周期的，
 * 而在一个项目中，容器不一定只有一个，Spring中可以包括多个容器，而且容器有上下层关系，
 * 目前最常见的一种场景就是在一个项目中引入Spring和SpringMVC这两个框架，其实就是2个容器，
 * Spring是根容器，SpringMVC是其子容器，并且在Spring根容器中对于SpringMVC容器中的Bean是不可见的，
 * 而在SpringMVC容器中对于Spring根容器中的Bean是可见的，也就是子容器可以看见父容器中的注册的Bean，反之就不行。
 * spring 和 spring mvc 的区别  
 * 	1、 spring是 父容器 根容器   springmvc 是子容器    
 *  2、子容器 可见  父容器的bean   ，父容器 不可见 子容器的bean 
 * ServletContainerInitializer
 * @author xf
 * 2016年8月6日下午7:48:55
 */
public class ServletContainerInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) throws ServletException {
		// 初始化 日志系统
		initializeLog4jConfig(container);
		// 初始化spring 根容器
		initializeSpringConfig(container);
		// 初始化 springmvc 子容器
		initializeSpringMVCConfig(container);
		// 注册 监听器 
		registerListener(container);
		// 注册 servlet 
		registerServlet(container);
		// 注册 过滤器 
		registerFilter(container);
	}
	/**
	 * 初始化日志系统
	 * @author xf
	 * 2016年8月6日下午10:07:42
	 * @param container
	 */
	private void initializeLog4jConfig(ServletContext container) {
		// Log4jConfigListener
		container.setInitParameter("log4jConfigLocation", "classpath:log4j2.xml");
		container.setInitParameter("isLog4jAutoInitializationDisabled", "false");
	}
	/**
	 * 初始化spring根容器
	 * @author xf
	 * 2016年8月6日下午8:53:14
	 * @param container
	 */
	private void initializeSpringConfig(ServletContext container) {
		// 创建 spring容器 上下文 环境
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(AppConfiguration.class);
		// 管理 spring上下文环境的 生命周期   添加上下文加载监听器
		container.addListener(new ContextLoaderListener(rootContext));
	}

	/**
	 * 初始化 springmvc 子容器
	 * @author xf
	 * 2016年8月6日下午8:55:26
	 * @param container
	 */
	private void initializeSpringMVCConfig(ServletContext container) {
		// 创建 spring mvc 容器 上下文 环境   Create the spring rest servlet's Spring application context
		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
		dispatcherContext.register(MVCConfigureation.class);
		// 注册和映射 springmvc的servlet    Register and map the spring rest servlet
		ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher",new DispatcherServlet(dispatcherContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.setAsyncSupported(true);
		dispatcher.addMapping("/");
	}
	/**
	 * 注册  servlet 
	 * @author xf
	 * 2016年8月6日下午9:02:30
	 * @param container
	 */
	private void registerServlet(ServletContext container) {
		//initializeStaggingServlet(container);
	}
	/**
	 * 注册  过滤器 
	 * @author xf
	 * 2016年8月6日下午9:02:45
	 * @param container
	 */
	private void registerFilter(ServletContext container) {
		if(true){
			FilterRegistration.Dynamic filterRegistration = container.addFilter("HiddenHttpMethodFilter", HiddenHttpMethodFilter.class);
			filterRegistration.addMappingForUrlPatterns(null, false, "/*");
			filterRegistration.setAsyncSupported(true);
		}
		if(true){
			FilterRegistration.Dynamic filterRegistration = container.addFilter("CharacterEncodingFilter", CharacterEncodingFilter.class);
			filterRegistration.addMappingForUrlPatterns(null, false, "/*");
			filterRegistration.setAsyncSupported(true);
		}
		
	}
	/**
	 * 注册  监听器 
	 * @author xf
	 * 2016年8月6日下午9:03:02
	 * @param container
	 */
	private void registerListener(ServletContext container) {
		//container.addListener(RequestContextListener.class);
	}
}
