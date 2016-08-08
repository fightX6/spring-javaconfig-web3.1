/**
 * 
 */
package me.xf.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import me.xf.utils.LogHelper;

/**
 * ChatController
 * @author xf
 * 2016年8月7日下午2:25:47
 */

@Controller
public class ChatController  extends LogHelper {
		
	/**
	 *  广播推送，所有用户都收得到
     * @param msg
     * @param principal
	 * @Description: 
	 * @author xf
	 * @date 2016年8月8日下午3:51:41 
	 * @param message
	 * @param principal
	 * @return
	 * @throws Exception
	 */
	//@MessageMapping接收客户端消息
	@MessageMapping("client")
	//@SendTo广播消息出去  上面msg会被广播到”/broker/keng”这个订阅路径中，只要客户端订阅了这条路径，不管是哪个用户，都会接收到消息
	@SendTo("broker/keng") 
    public String chat(String message,Principal principal) throws Exception {
		//TODO  websocket 发送消息返回指定用户的路径
        long time = System.currentTimeMillis();
        logInfo(time+":"+message);
        return "{\"time\":\""+time+"\",\"msg\":\"/app/client 用户消息发送地址 转发到  用户关注地址  /broker/keng\" }";
	}
	
	@SubscribeMapping("subscr")
	public String handlerSubscription(){
        long time = System.currentTimeMillis();
        logInfo(time+":");
        return "{\"time\":\""+time+"\",\"msg\":\"/app/subscr    用户关注地址\" }";
	}
}
