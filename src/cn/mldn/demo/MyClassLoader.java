package cn.mldn.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MyClassLoader extends ClassLoader {
	public Class<?> getMyClass(String className) throws ClassNotFoundException {
		byte[] d = this.getFileDataByServer(className);
//		return super.loadClass(className);
		return super.defineClass(className, d, 0, d.length);
	}

	private byte[] getFileData(String className) {
		byte ret[] = null;
		try {
			String temp="D:" + File.separator+ className.substring(className.lastIndexOf(".") + 1)+ ".class";
			File file = new File(temp);
			InputStream input = new FileInputStream(file);
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			byte data[]=new byte[1024];
			int len=0;
			while((len=input.read(data))!=-1){
				bos.write(data,0,len);
			}
			ret=bos.toByteArray();
			bos.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	private byte[] getFileDataByServer(String className){
		byte ret[]=null;
		try {
			URL url=new URL("http://localhost:8080/p_reflect_demo/cn/mldn/demo/Salgrade.class");
			InputStream input = url.openStream();
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			byte data[]=new byte[1024];
			int len=0;
			while((len=input.read(data))!=-1){
				bos.write(data, 0, len);
			}
			ret=bos.toByteArray();
			bos.close();
			input.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}

}
