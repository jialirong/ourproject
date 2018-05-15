package action;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.User;

import org.apache.struts2.interceptor.ServletRequestAware;

import util.DbUtil;
import util.StringUtil;

import com.opensymphony.xwork2.ActionSupport;

import dao.UserDao;

public class LoginAction extends ActionSupport implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private String error;
	private String imageCode;
	
	
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
	
	DbUtil dbUtil = new DbUtil();
	UserDao userDao = new UserDao();
	HttpServletRequest request;
	
	public String isLogin()throws Exception{
		HttpSession session=request.getSession();
		Object object=session.getAttribute("currentUser");
		if(object==null || !(object instanceof User)){
			return "login";
		}else{
			return "main";
		}
	}
	
	
	public String login() throws Exception{
		HttpSession session=request.getSession();
		System.out.println("#####################3");
		if(StringUtil.isEmpty(user.getUserName()) || StringUtil.isEmpty(user.getPassword())) {
			error = "用户名或密码为空!";
			request.setAttribute("error", error);

			return ERROR;
		}
		if(user.getUserName().contains("' and 1='1")||user.getUserName().contains("=")||user.getUserName().contains("' or 1='1")){
			error = "用户不存在!";
			request.setAttribute("error", error);

			return ERROR;
		}
		if(StringUtil.isEmpty(imageCode)) {
			error = "验证码为空!";
			request.setAttribute("error", error);

			return ERROR;
		}
		if(!imageCode.equals(session.getAttribute("sRand"))){
			error = "验证码错误！";
			request.setAttribute("error", error);

			return ERROR;
		}
		Connection con = null;
		try {
			System.out.println(user.getUserName());
			con=dbUtil.getCon();
			User currentUser = userDao.login(con, user);
			if(currentUser!=null){
				session.setAttribute("currentUser", user);
				session.setAttribute("role", currentUser.getId());
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
		return super.execute();
	}
	
	public String logOut() throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("currentUser");
		session.invalidate();
		return "logout";
	}


	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}

}
