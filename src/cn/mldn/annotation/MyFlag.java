package cn.mldn.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MyFlag {
	public String name() default "无名氏";//定义了一个name属性
	public String value();//定义了一个value属性
	

}
