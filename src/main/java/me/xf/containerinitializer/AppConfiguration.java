/**
 * 
 */
package me.xf.containerinitializer;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.XmlEscape;

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
@ComponentScan(basePackages = { "me.xf" }, excludeFilters = { @Filter(type = FilterType.ANNOTATION,classes = { Controller.class }),
															  @Filter(type = FilterType.REGEX,pattern={"me.xf.containerinitializer.MVCConfigureation"})			
															} )
public class AppConfiguration {
	private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(AppConfiguration.class);
	/**
	 * 
	 */
	public AppConfiguration() {
		logInfo((ServletContainerInitializer.count++)+"、[Initialize application]");
		Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
	}

	@Bean
	public SpringContextUtil getSpringContextUtil(){
		logInfo((ServletContainerInitializer.count++)+"、[Initialize SpringContextUtil]");
		SpringContextUtil contextUtil = new SpringContextUtil();
		return contextUtil;
	}
	/**
	 * freemarker xml特殊字符过滤配置
	 * 
	 * @Description:
	 * @author xf
	 * @date 2016年7月23日上午10:47:29
	 * @return
	 */
	@Bean
	public XmlEscape fmXmlEscape() {
		XmlEscape escape = new XmlEscape();
		return escape;
	}

	/**
	 * freemarker 配置
	 * 
	 * @Description:
	 * @author xf
	 * @date 2016年7月23日上午11:09:21
	 * @return
	 * @throws TemplateModelException 
	 */
	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() throws TemplateModelException {
		logInfo((ServletContainerInitializer.count++)+"、[Initialize FreeMarkerConfigurer]");
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		configuration.setBooleanFormat("true,false");
		configuration.setNumberFormat("0.##");
		configuration.setTimeFormat("HH:mm:ss");
		configuration.setDateFormat("yyyy-MM-dd");
		configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
		configuration.setURLEscapingCharset("UTF-8");
		configuration.setDefaultEncoding("UTF-8");
		configuration.setLocale(new Locale("zh_CN"));
		configuration.setClassicCompatible(false);
		configuration.setWhitespaceStripping(true);
		//模板更新时间
		configuration.setTemplateUpdateDelayMilliseconds(0);
		// 设置标签类型([]、<>),[]这种标记解析要快些
		configuration.setTagSyntax(freemarker.template.Configuration.AUTO_DETECT_TAG_SYNTAX);
		//拦截的异常只是  template_exception
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);//ignore  rethrow   debug  html_debug
		configuration.setSharedVariable("xml_escape", fmXmlEscape());
		try {
			//此目录为web/WEB-INF/classes/   相对就是src目录下
			String root = Thread.currentThread().getContextClassLoader().getResource("").getPath().replaceAll("classes/", "") ;
			//设置 freemarker模板的物理根路径
			configuration.setDirectoryForTemplateLoading(new File(root));
		} catch (IOException e) {
			logErr("找不到模板路径！",e);
		}
		//设置  所有   自定义标签  类 到 freemarker 中 
		ApplicationContext context =  SpringContextUtil.getApplicationContext();
		// 获取实现TemplateDirectiveModel的bean
		Map<String, TemplateDirectiveModel> beans = context.getBeansOfType(TemplateDirectiveModel.class);
		
		for (String key : beans.keySet()) {
			Object obj = beans.get(key);
			if (obj != null && obj instanceof TemplateDirectiveModel) {
				configuration.setSharedVariable(key, obj);
			}
		}
		freeMarkerConfigurer.setConfiguration(configuration);
		return freeMarkerConfigurer;
	}
	/**
	 * freemarker视图解释器 -
	 * 
	 * @Description:
	 * @author xf
	 * @date 2016年7月23日上午11:09:12
	 * @return
	 */
	@Bean
	public FreeMarkerViewResolver freeMarkerViewResolver() {
		logInfo((ServletContainerInitializer.count++)+"、[Initialize FreeMarkerViewResolver]");
		FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
		viewResolver.setViewClass(FreeMarkerView.class);
		viewResolver.setSuffix(".html");
		viewResolver.setPrefix("");
		viewResolver.setOrder(0);
		viewResolver.setCache(false);
		viewResolver.setContentType("text/html;charset=UTF-8");
		viewResolver.setExposeRequestAttributes(true);
		viewResolver.setExposeSessionAttributes(true);
		viewResolver.setExposeSpringMacroHelpers(true);
		viewResolver.setExposePathVariables(true);
		return viewResolver;
	}
	
	private void logInfo(String msg,Object... params){
		if(log.isInfoEnabled()){
			log.info(msg,params);
		}
	}
	private void logErr(String msg,Exception e){
		if(log.isErrorEnabled()){
			log.error(msg,e);
		}
	}
}
