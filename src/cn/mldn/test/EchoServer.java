package cn.mldn.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
	public static final int PORT=9999;//设置绑定端口
	public static void main(String[]args) throws IOException{
		ExecutorService executorService=Executors.newFixedThreadPool(5);//允许5个用户访问
		//打开一个服务端的Socket的连接通道
		ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);//设置为非阻塞模式
		//设置一个服务的绑定端口
		serverSocketChannel.bind(new InetSocketAddress(PORT));
		//打开一个选择器
		Selector selector=Selector.open();
		//将当前的ServerSocketChannel注册到Selector之中，接收统一的管理
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("服务端启动程序，该程序在"+PORT+"端口上监听，等待客户端连接.....");
		//接收连接的状态
		int keySelect=0;
		while((keySelect=selector.select())>0){
			//获取全部的连接信息
			Set<SelectionKey> selectedKeys=selector.selectedKeys();
			Iterator<SelectionKey> selectionIter=selectedKeys.iterator();
			while(selectionIter.hasNext()){
				SelectionKey selectionKey=selectionIter.next();//获取每一个通道
				if(selectionKey.isAcceptable()){
					//接收连接模式
					SocketChannel clientChannel=serverSocketChannel.accept();//等待接收
					if(clientChannel!=null){
						//已经有了连接
						executorService.submit(new SocketClientChannelThread(clientChannel));
					}
				}
				//移除掉此通道信息
				selectionIter.remove();
			}
		}
		executorService.shutdown();
		serverSocketChannel.close();
	}

}
