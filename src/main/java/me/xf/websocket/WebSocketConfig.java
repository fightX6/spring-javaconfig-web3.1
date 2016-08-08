/**
 * 
 */
package me.xf.websocket;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * WebSocketConfig
 * 
 * Javascript连接到 websocket 端点 "/quakesep" ，然后订阅"/topic/quakes.all" 端点，
 * 内部将注册RabbitMQ 一个临时队列用于websocket 会话，映射AMQP 路由key"quakes.all" 到这个临时队列, 
 * 必要时发送所有地震信息到这个会话的临时队列。
 * @author xf
 * 2016年8月7日下午1:52:30
 */
@Configuration
@EnableWebMvc
@EnableWebSocketMessageBroker
@ComponentScan(basePackages="me.xf")//websocket扫描 messageMapping等注解路径
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{

	/**
	 * 服务器要监听的端口，message会从这里进来，要对这里加一个Handler
     * 这样在网页中就可以通过websocket连接上服务了
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//正常的websocket  websocket端点
		registry.addEndpoint("/hello")
        .setHandshakeHandler(new HandshakeHandler())
        .setAllowedOrigins("*").addInterceptors(new HandshakeInterceptor());
		//使用 sockjs  websocket端点
		registry.addEndpoint("/hello/sockjs")
        .setHandshakeHandler(new HandshakeHandler())
        .setAllowedOrigins("*").addInterceptors(new HandshakeInterceptor()).withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//设置服务器广播消息的基础路径 设置可以订阅的地址，也就是服务器可以发送的地址
		registry.enableSimpleBroker("/broker");   
		
		// 是浏览器请求编码的前缀，T
		registry.setApplicationDestinationPrefixes("/app");  //设置客户端订阅消息的基础路径
        registry.setPathMatcher(new AntPathMatcher("."));    //可以已“.”来分割路径，看看类级别的@messageMapping和方法级别的@messageMapping
	} 

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
		registry.setMessageSizeLimit(64*1024); //设置消息字节数大小
		registry.setSendBufferSizeLimit(512*1024);//设置消息缓存大小
		registry.setSendTimeLimit(10*1000); //设置消息发送时间限制毫秒
	}

	/**
	 * MessageConverter的作用主要有两方面，
	 * 一方面它可以把我们的非标准化Message对象转换成我们的目标Message对象， 这主要是用在发送消息的时候；
	 * 另一方面它又可以把我们的Message对象转换成对应的目标对象，这主要是用在接收消息的时候。
	 */
	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		return true;
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		super.configureClientInboundChannel(registration);
	}
	
	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		super.configureClientOutboundChannel(registration);
	}
	
}
