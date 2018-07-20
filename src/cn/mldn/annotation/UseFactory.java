package cn.mldn.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UseFactory {
	/**
	 * 取得工厂类可以实例化的类对象
	 * @return
	 */
	public String className();

}
