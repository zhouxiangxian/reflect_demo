package cn.mldn.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestCreatePhones {
	public static void main(String[] args) throws IOException {
	      File file = new File("d:"+File.separator+"phones9.txt"); // 这里设定需要的文件地址，windows系统中的"\"必须要两个"\\"表示
	        BufferedWriter out = new BufferedWriter(new FileWriter(file));
	        for (int i = 0; i < 5000; i++)
	        { 
	        	    StringBuffer sb=new StringBuffer();
	        	    sb.append((13900000000l+i));
	                out.write(sb.toString());
	                out.newLine();
	                out.flush();
	                System.out.println(sb.toString());
	        }
	        out.close();
	    }
	
	}


