package myinterceptor;


import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

//public class MyInterceptor extends AbstractInterceptor  {
//
////	/*@Override
////	public String intercept(ActionInvocation arg0) throws Exception {
////		// TODO Auto-generated method stub
////		  Object loginUserName = ActionContext.getContext().getSession().get("currentUser"); 
////      	System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
////
////	        if(null == loginUserName){  
////	        	System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
////	            return Action.ERROR;  // ���ﷵ���û���¼ҳ����ͼ  
////	        }  
////		return arg0.invoke();
////	}*/
//	
//}