/**
 * 
 */
package me.xf.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import me.xf.commons.bean.ResultData;
import me.xf.utils.LogHelper;

/**
 * ChatController
 * @author xf
 * 2016年8月7日下午2:25:47
 */

@Controller
@RequestMapping(value="/")
public class ChatController  extends LogHelper {
	@MessageMapping("/hello")
    @SendTo("/broker/keng")
    public ResultData chat(String message) throws Exception {
        long time = System.currentTimeMillis();
        logInfo(time+":"+message);
        return new ResultData(time,message);
    }
}
