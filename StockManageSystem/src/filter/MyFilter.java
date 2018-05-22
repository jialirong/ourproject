package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class MyFilter implements Filter{

	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("destory.......................");
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest)arg0;  
		HttpSession session = req.getSession();
		String name = (String) session.getAttribute("currentUser");
		if(name!=null){
			arg2.doFilter(arg0, arg1);
		}
		else{
			System.out.println(name);
			req.getRequestDispatcher("/pages/admin/index_1.jsp").forward(arg0, arg1);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("init..................");
	}

}
