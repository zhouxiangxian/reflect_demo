package cn.mldn.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClientChannelThread implements Runnable {
	private SocketChannel clientChannel;// 客户端通道
	private boolean flag = true;// 循环标记

	public SocketClientChannelThread(SocketChannel clientChannel)
			throws IOException {
		this.clientChannel = clientChannel;
		System.out.println("【客户端连接成功】，该客户端的地址为:"
				+ clientChannel.getRemoteAddress());
	}

	public void run() {
		ByteBuffer buffer = ByteBuffer.allocate(50);// 开辟缓存区
		try {
			while (this.flag) {// 一直与客户端进行交互
				buffer.clear();// 做清空处理
				// 接收客户端发送过来的信息
				int readCount = this.clientChannel.read(buffer);
                //将接收过来的数据变为字符串
				String readMessage=new String(buffer.array(),0,readCount).trim();
				System.out.println("【服务器接收到信息】"+readMessage);
				//回信息
				String writeMessage="【Echo】"+readMessage+"\n";
				//如果接收的是结束指令
				if("exit".equals(readMessage)){
					writeMessage="【EXIT】拜拜，下次再见!";
					this.flag=false;//结束本次的Echo操作
				}
				//清空缓存区
				buffer.clear();
				buffer.put(writeMessage.getBytes());//将数据添加到缓存区中
				buffer.flip();//重设缓存区
				this.clientChannel.write(buffer);
			}
			this.clientChannel.close();//关闭客户端的通道
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
