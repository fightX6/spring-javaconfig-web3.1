/**
 * 
 */
package me.xf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * HomeController
 * @author xf
 * 2016年8月6日下午9:27:30
 */
@Controller
@RequestMapping(value="/")
public class HomeController {
	/**
	 * web默认访问页面
	 * @Description: 
	 * @author xf
	 * @date 2016年8月8日下午3:11:06 
	 * @return
	 */
	@RequestMapping(value="")
	public String welcome(){
		return "forward:/index";
	}
	
	@RequestMapping(value="index")
	public String index(){
		return "index";
	}
}
