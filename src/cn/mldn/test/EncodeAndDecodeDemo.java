package cn.mldn.test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;


public class EncodeAndDecodeDemo {
	public static void main(String[] args) throws CharacterCodingException {
		Charset charset=Charset.forName("UTF-8");
		//获取编码类对象
		CharsetEncoder encoder=charset.newEncoder();
		//获取解码类对象
		CharsetDecoder decoder=charset.newDecoder();
		String str="魔乐科技:www.mldn.cn";//字符数据
		CharBuffer buffer=CharBuffer.allocate(50);
		buffer.put(str);//向字符缓冲区中保存数据
		buffer.flip();//进行缓冲区的重置
		//进行编码处理
		ByteBuffer buf=encoder.encode(buffer);
		//对字节缓存区中的数据进行解码
		System.out.println(decoder.decode(buf));
		
		
	}

}
