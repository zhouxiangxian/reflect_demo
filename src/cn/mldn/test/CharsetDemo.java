package cn.mldn.test;

import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

public class CharsetDemo {
	public static void main(String[] args) {
		SortedMap<String,Charset> map=Charset.availableCharsets();
        Set<Entry<String,Charset>> set=map.entrySet();
        for(Entry<String,Charset> temp:set){
        	System.out.println("key="+temp.getKey()+"\tvalue="+temp.getValue());
        }
	}

}
