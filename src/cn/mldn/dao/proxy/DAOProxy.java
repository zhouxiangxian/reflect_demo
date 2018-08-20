package cn.mldn.dao.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DAOProxy implements InvocationHandler {
	//真是对象主题
	private Object obj;
	/**
	 * 将真实主题绑定到代理之中
	 * @param obj
	 * @return
	 */
    public Object bind(Object obj){
    	this.obj=obj;
    	return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),this);
    }
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object retObject=null;
		if(method.getName().contains("do")){
			this.prepare();
			try{
			retObject=method.invoke(this.obj, args);
			this.commit();
			}catch(Exception e){
				this.rollback();
			}
		}else{
			retObject=method.invoke(this.obj,args);
		}
		return retObject;
	}
	public void prepare(){
		System.out.println("取消掉jdbc的自动提交");
	}
	public void commit(){
		System.out.println("手工提交jdbc事务");
	}
	public void rollback(){
		System.out.println("出现错误，手工回滚jdbc事务");
	}

}
