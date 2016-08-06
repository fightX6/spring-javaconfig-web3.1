/**
 * 
 */
package me.xf.containerinitializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.XmlEscape;

/**
 * MvcConfigureation
 * @author xf
 * 2016年8月6日下午8:39:26
 */
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = { "me.xf" },useDefaultFilters=false,includeFilters={@Filter(type = FilterType.ANNOTATION, classes={Controller.class})})
public class MVCConfigureation  extends WebMvcConfigurationSupport {
	private static final Logger log = LoggerFactory.getLogger(MVCConfigureation.class);

	@Bean
	public SpringContextUtil getSpringContextUtil(){
		log.info("[Initialize SpringContextUtil]");
		SpringContextUtil contextUtil = new SpringContextUtil();
		return contextUtil;
	}
	
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		log.info("addInterceptors");
	}
	
	/**
	 * 注解请求映射 处理字符串..
	 * 
	 * @Description:
	 * @author xf
	 * @date 2016年7月23日上午9:54:19
	 * @return
	 */
	@Bean
	public StringHttpMessageConverter string_hmc() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(MediaType.TEXT_HTML);
		supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		return converter;
	}
	

	/**
	 * 注解请求映射 使用fastjson 处理json
	 * 
	 * @Description:
	 * @author xf
	 * @date 2016年7月23日上午9:54:19
	 * @return
	 */
	@Bean
	public FastJsonHttpMessageConverter fastjson_hmc() {
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		FastJsonConfig config = new FastJsonConfig();
		config.setSerializerFeatures(SerializerFeature.WriteNonStringKeyAsString, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteNullBooleanAsFalse,
				SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.SkipTransientField,
				SerializerFeature.QuoteFieldNames);
		config.setDateFormat("yyyy-MM-dd HH:mm:ss");
		config.setSerializeFilters(new ValueFilter() {
			@Override
			public Object process(Object obj, String key, Object val) {
				if (val == null && val instanceof String) {
					return "";
				} else {
					return val;
				}
			}
		});
		converter.setFastJsonConfig(config);

		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(MediaType.TEXT_HTML);
		supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		converter.setSupportedMediaTypes(supportedMediaTypes);
		return converter;
	}
	/**
	 * 启动Spring MVC的注解功能，完成请求和注解POJO的映射 描述 : <注册servlet适配器>. <br>
	 * <p>
	 * <只需要在自定义的servlet上用@Controller("映射路径")标注即可>
	 * </p>
	 * 
	 * @return
	 */
	@Bean
	public HandlerAdapter servletHandlerAdapter() {
		logInfo("HandlerAdapter");
		RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		messageConverters.add(string_hmc());
		messageConverters.add(fastjson_hmc());
		adapter.setMessageConverters(messageConverters);
		return adapter;
	}
	
	/**
	 * 描述 : <文件上传处理器>. <br>
	 * <p>
	 * <使用方法说明>
	 * </p>
	 * 
	 * @return
	 */
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver commonsMultipartResolver() {
		logInfo("CommonsMultipartResolver");
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("utf-8");
		multipartResolver.setMaxUploadSize(10485760000l);
		multipartResolver.setMaxInMemorySize(40960);
		return multipartResolver;
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
	
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		RequestMappingHandlerAdapter handlerAdapter = super.requestMappingHandlerAdapter();
		return handlerAdapter;
	}
	
	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping handlerMapping = super.requestMappingHandlerMapping();
		logInfo("RequestMappingHandlerMapping");
		return handlerMapping;
	}
	
	@Bean
	public HandlerMapping resourceHandlerMapping() {
		HandlerMapping handlerMapping = super.resourceHandlerMapping();
		logInfo("HandlerMapping");
		return handlerMapping;
	}
	
	private void logInfo(String msg,Object... params){
		if(log.isInfoEnabled()){
			log.info(msg,params);
		}
	}
	private void logDebug(String msg,Object... params){
		if(log.isDebugEnabled()){
			log.debug(msg,params);
		}
	}
	private void logErr(String msg,Exception e){
		if(log.isErrorEnabled()){
			log.error(msg,e);
		}
	}
	
}
