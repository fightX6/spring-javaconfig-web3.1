/**
 * 
 */
package me.xf.containerinitializer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

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
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		logInfo((ServletContainerInitializer.count++)+"、[registry addInterceptors...]");
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
		logInfo((ServletContainerInitializer.count++)+"、[Initialize RequestMappingHandlerAdapter]");
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
		logInfo((ServletContainerInitializer.count++)+"、[Initialize CommonsMultipartResolver]");
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("utf-8");
		multipartResolver.setMaxUploadSize(10485760000l);
		multipartResolver.setMaxInMemorySize(40960);
		return multipartResolver;
	}

	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		RequestMappingHandlerAdapter handlerAdapter = super.requestMappingHandlerAdapter();
		logInfo((ServletContainerInitializer.count++)+"、[Initialize RequestMappingHandlerAdapter]");
		return handlerAdapter;
	}
	
	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping handlerMapping = super.requestMappingHandlerMapping();
		logInfo((ServletContainerInitializer.count++)+"、[Initialize RequestMappingHandlerMapping]");
		return handlerMapping;
	}
	
	@Bean
	public HandlerMapping resourceHandlerMapping() {
		HandlerMapping handlerMapping = super.resourceHandlerMapping();
		logInfo((ServletContainerInitializer.count++)+"、[Initialize HandlerMapping]");
		return handlerMapping;
	}
	
	private void logInfo(String msg,Object... params){
		if(log.isInfoEnabled()){
			log.info(msg,params);
		}
	}
}
