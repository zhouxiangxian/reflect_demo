package cn.mldn.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class EchoClient {
	public static final String HOST="localhost";//连接主机
	public static final int PORT=9999;//绑定端口
	public static void main(String[]args) throws IOException{
		//获取客户端的SocketChannel对象
		SocketChannel clientChannel=SocketChannel.open();
		//连接服务端
		clientChannel.connect(new InetSocketAddress(HOST, PORT));
		//进行数据的接收
		ByteBuffer buffer=ByteBuffer.allocate(50);
		boolean flag=true;
		while(flag){//一直进行数据的输入
			buffer.clear();//清空缓存区
			String msg=InputUtil.getString("请输入要发送的数据:");
			buffer.put(msg.getBytes());//将数据保存到缓存区中
			buffer.flip();//重设缓存区
			clientChannel.write(buffer);
			//在数据读取之前进行缓存区的清空处理
			buffer.clear();
			int readCount=clientChannel.read(buffer);
			//读取完进行缓存区的重设
			buffer.flip();
			System.err.println(new String(buffer.array(),0,readCount));
			if("exit".equals(msg)){
				flag=false;
			}
		}
		clientChannel.close();
	}

}
