package cn.mldn.demo;

import java.lang.annotation.Annotation;

import cn.mldn.annotation.MyFlag;

public class ClassDemo {
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		Class<?> cls=Class.forName("cn.mldn.vo.LfEmp");
		Annotation an[]=cls.getAnnotations();
		for(int x=0;x<an.length;x++){
			MyFlag mf=(MyFlag)an[x];
			System.out.println(mf.name()+"="+mf.value());
		}
	}

}
