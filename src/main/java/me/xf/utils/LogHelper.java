package me.xf.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class LogHelper {

	private  Logger log = LogManager.getLogger(this.getClass());
	
	public void logInfo(String msg,Object... params){
		if(log.isInfoEnabled()){
			log.info(msg,params);
		}
	}
	public void logInfo(String msg,Exception e){
		if(log.isInfoEnabled()){
			log.info(msg,e);
		}
	}
	public void logErr(String msg,Exception e){
		if(log.isErrorEnabled()){
			log.error(msg,e);
		}
	}
	public void logErr(String msg,Object... params){
		if(log.isErrorEnabled()){
			log.error(msg,params);
		}
	}
	public void logDebug(String msg,Object... params){
		if(log.isDebugEnabled()){
			log.debug(msg,params);
		}
	}
	public void logDebug(String msg,Exception e){
		if(log.isDebugEnabled()){
			log.debug(msg,e);
		}
	}
}
