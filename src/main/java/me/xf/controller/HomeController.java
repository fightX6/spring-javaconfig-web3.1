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
	
	@RequestMapping(value="index")
	public String index(){
		return "index";
	}
}
