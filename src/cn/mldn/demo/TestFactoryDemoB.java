package cn.mldn.demo;

import java.lang.annotation.Annotation;

import cn.mldn.annotation.UseFactory;

interface Fruit {
	public void eat();
}

class Apple implements Fruit {

	public void eat() {
		System.out.println("吃苹果！！！！");
	}

}

class Orange implements Fruit {
	public void eat() {
		System.out.println("吃橘子");
	}
}
@UseFactory(className = "cn.mldn.demo.Apple")
public class TestFactoryDemoB {
	public static void main(String[] args) {
		Fruit fruit=getFruit();
        fruit.eat();
	}
	public static Fruit getFruit(){
		Fruit instance=null;
		Class<?> cls=TestFactoryDemoB.class;
		Annotation an=cls.getAnnotation(UseFactory.class);
		UseFactory uf=(UseFactory)an;
		try {
			instance=(Fruit) Class.forName(uf.className()).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return instance;
	}
}
