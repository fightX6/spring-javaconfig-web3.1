/**
 * 
 */
package me.xf.websocket;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * 创建握手（handshake）接口
 * HandshakeInterceptor
 * @author xf
 * 2016年8月7日下午4:04:00
 */
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor  {
	private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(HandshakeInterceptor.class);
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map attributes) throws Exception {
    	log.info("============Before Handshake===========");
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception ex) {
    	log.info("============After Handshake==============");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
