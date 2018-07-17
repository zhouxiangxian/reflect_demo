package cn.mldn.test;

import cn.mldn.util.BeanOperate;
import cn.mldn.vo.Emp;

public class TestDemo {
	private Emp emp=new Emp();
	public Emp getEmp() {
		return emp;
	}
	public void setEmp(Emp emp) {
		this.emp = emp;
	}
	public static void main(String[]args){
		String   attribute="emp.dept.company.number";//属性
		String value1="23.01";
		String value[]={"梦网科技","hhhhh","aaaaa"};//内容
		TestDemo td=new TestDemo();
		BeanOperate bo=new BeanOperate(td,attribute,value);
		String temp[]=td.getEmp().getDept().getCompany().getNumber();
		for(String t:temp){
			System.out.println(t);
		}
		int temp1=1<<4;
		System.out.println("temp1="+temp1);
	}

}
