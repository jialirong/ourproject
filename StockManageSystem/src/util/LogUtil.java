package util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import action.LoginAction;

public class LogUtil extends ActionSupport {
	private static Logger logger = LoggerFactory.getLogger(LogUtil.class);//系统自带的

	public static String log(String mes){
		Date day=new Date();   
		ActionContext ac = ActionContext.getContext();
		String user = (String) ac.getSession().get("username");
		mes = user+"   用户于"+DateUtil.formatDate(day, "yyyy-MM-dd HH:mm:ss")+mes;
		FileUtil.writeToLog(mes);
		logger.info(mes);
		return mes;
	}
}
