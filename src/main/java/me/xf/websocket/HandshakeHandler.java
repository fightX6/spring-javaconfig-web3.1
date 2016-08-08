/**
 * 
 */
package me.xf.websocket;

import org.apache.logging.log4j.LogManager;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * HandshakeHandler
 * 
 * @author xf 2016年8月7日下午4:04:49
 */
public class HandshakeHandler extends DefaultHandshakeHandler {
	private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(HandshakeHandler.class);

	
	public void logInfo(String msg, Object... params) {
		if (log.isInfoEnabled()) {
			log.info(msg, params);
		}
	}

	public void logInfo(String msg, Exception e) {
		if (log.isInfoEnabled()) {
			log.info(msg, e);
		}
	}

	public void logErr(String msg, Exception e) {
		if (log.isErrorEnabled()) {
			log.error(msg, e);
		}
	}

	public void logErr(String msg, Object... params) {
		if (log.isErrorEnabled()) {
			log.error(msg, params);
		}
	}

	public void logDebug(String msg, Object... params) {
		if (log.isDebugEnabled()) {
			log.debug(msg, params);
		}
	}

	public void logDebug(String msg, Exception e) {
		if (log.isDebugEnabled()) {
			log.debug(msg, e);
		}
	}
}
