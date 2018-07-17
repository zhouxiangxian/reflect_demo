package cn.mldn.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputUtil {
	private static final BufferedReader KEYBOARD_INPUT=new BufferedReader(new InputStreamReader(System.in));
	private InputUtil(){}
	public static String getString(String prompt) throws IOException{//接收数据信息
		boolean flag=true;
		String str=null;
		while(flag){
			System.out.print(prompt);
			str=KEYBOARD_INPUT.readLine();//读取数据
			if(str==null||"".equals(str)){
				System.out.println("数据输入错误,请重新输入!");
			}else{
				flag=false;
			}
		}
		return str;//返回数据
	}

}
