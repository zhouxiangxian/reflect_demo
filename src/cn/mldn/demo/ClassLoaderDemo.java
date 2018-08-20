package cn.mldn.demo;

public class ClassLoaderDemo {
	public static void main(String[] args) throws ClassNotFoundException {
		MyClassLoader classLoader=new MyClassLoader();
		Class<?> cls=classLoader.getMyClass("cn.mldn.demo.Salgrade");
		System.out.println(cls);
	}

}
