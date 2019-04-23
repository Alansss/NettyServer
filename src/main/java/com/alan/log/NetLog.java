package com.alan.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetLog {
	private static final Logger LOGGER = LoggerFactory.getLogger(NetLog.class);

	public static final boolean useNetMsgLog = true;

	public static void Log(String strLog) {
		LOGGER.info(strLog);
	}

	public static void LogNetMsg(String strLog) {
		LOGGER.info(strLog);
	}

}