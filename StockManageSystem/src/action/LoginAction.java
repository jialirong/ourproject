package action;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.User;

import org.apache.struts2.interceptor.ServletRequestAware;

import util.DbUtil;
import util.FileUtil;
import util.LogUtil;
import util.StringUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import dao.UserDao;

public class LoginAction extends ActionSupport implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private String error;
	private String imageCode;
	private Logger logger = LoggerFactory.getLogger(LoginAction.class);
	DbUtil dbUtil = new DbUtil();
	UserDao userDao = new UserDao();
	HttpServletRequest request ;
	public String name;
	public String getImageCode() {
		return imageCode;
	}
	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	

	public String login() throws Exception{
		if(request!=null){
			
		if(user.getUserName().contains("' or 1='1")||user.getUserName().contains("' or 1=1#")||user.getUserName().contains("abc ' or 1='1")){
				error = "用户不存在!";
				request.setAttribute("error", error);
				return ERROR;
			}
		if(StringUtil.isEmpty(user.getUserName()) || StringUtil.isEmpty(user.getPassword())) {
			error = "用户名或密码为空!";
			request.setAttribute("error", error);
			logger.info("用户名或密码为空!");
			return ERROR;
		}
		
		if(StringUtil.isEmpty(imageCode)) {
			error = "验证码为空!";
			request.setAttribute("error", error);

			return ERROR;
		}
		HttpSession session=request.getSession();
		
		if(!imageCode.equals(session.getAttribute("sRand"))){
			error = "验证码错误！";
			request.setAttribute("error", error);

			return ERROR;
		}
		Connection con = null;
		
		try {
			con=dbUtil.getCon();
			User currentUser = userDao.login(con, user);
			if(currentUser!=null){
//				session.setAttribute("currentUser", user);
//				session.setAttribute("role", currentUser.getId());
//				session.setAttribute("username", user.getUserName());
				Map<String, Object> sessionmap = ActionContext.getContext().getSession();
				
				sessionmap.put("username", user.getUserName());
				sessionmap.put("role", currentUser.getId());
				
				Date day=new Date();    
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				String mes = session.getAttribute("username")+"   用户于"+df.format(day)+"登陆成功";
				FileUtil.writeToLog(mes);
				name = (String) session.getAttribute("username");
				logger.info(mes);
				return "main";
			}else{
				request.setAttribute("error", "用户名或密码错误！");
				return "login";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		else{
			return ERROR;
		}
		return super.execute();
	}
	
	public String logOut() throws Exception {
		HttpSession session=request.getSession();
		Date day=new Date();    

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String mes = session.getAttribute("username")+"   用户于"+df.format(day)+"退出系统成功";
		FileUtil.writeToLog(mes);
		logger.info(mes);
		session.removeAttribute("currentUser");
		session.invalidate();
		ActionContext ac = ActionContext.getContext();
		Map<String, Object> sessionmap = new HashMap<String, Object>();
		sessionmap.put("username", null);
		ac.setSession(sessionmap);
		return "logout";
	}


	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}

}
