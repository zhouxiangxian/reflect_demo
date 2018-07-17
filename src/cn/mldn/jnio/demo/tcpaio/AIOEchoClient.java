package cn.mldn.jnio.demo.tcpaio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

import cn.mldn.test.InputUtil;

class ClientReadHandler implements CompletionHandler<Integer,ByteBuffer>{
    private CountDownLatch latch;
    private AsynchronousSocketChannel clientChannel=null;
    public ClientReadHandler(AsynchronousSocketChannel clientChannel,CountDownLatch latch){
    	this.clientChannel=clientChannel;
    	this.latch=latch;
    }
	public void completed(Integer result, ByteBuffer buffer) {
		buffer.flip();//重设缓存区
		//读取返回的内容
		String receiveMessage=new String(buffer.array(),0,buffer.remaining());
		//数据回应的处理数据
		System.err.println(receiveMessage);
	}

	public void failed(Throwable exc, ByteBuffer buffer) {
		System.out.println("对不起，发送出现了问题，该客户端被关闭......");
		try {
			this.clientChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.latch.countDown();
	}
	
}

class ClientWriteHandler implements CompletionHandler<Integer,ByteBuffer>{
	private CountDownLatch latch;
	private AsynchronousSocketChannel clientChannel;//客户端的连接对象
	public ClientWriteHandler(AsynchronousSocketChannel clientChannel,CountDownLatch latch){
		this.latch=latch;
		this.clientChannel=clientChannel;
	}

	public void completed(Integer result, ByteBuffer buffer) {
		if(buffer.hasRemaining()){
			//有数据要进行发送
			this.clientChannel.write(buffer,buffer, this);
		}else{
			//考虑到数据的读取
			ByteBuffer readBuffer=ByteBuffer.allocate(100);
			this.clientChannel.read(readBuffer, readBuffer, new ClientReadHandler(this.clientChannel,this.latch));
		}
		
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		System.out.println("对不起，发送出现了问题，该客户端被关闭......");
		try {
			this.clientChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.latch.countDown();
	}
	
}

//异步客户端线程类
class AIOClientThread implements Runnable{
    public static final String HOST="192.168.1.109";//连接主机
    public static final int PORT=9999;//绑定的端口
    private CountDownLatch latch;
    //客户端的连接对象
    private AsynchronousSocketChannel clientChannel;
    public AIOClientThread() throws IOException{
    	//在构造方法中进行服务主机的连接
    	this.clientChannel=AsynchronousSocketChannel.open();
    	this.clientChannel.connect(new InetSocketAddress(HOST,PORT));
    	this.latch=new CountDownLatch(1);//做一个阻塞处理操作
    }
	public void run() {
		try {
			this.latch.await();//等待处理
			this.clientChannel.close();//关闭客户端的连接处理
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public boolean sendMessage(String msg){
		//实现消息的发送
		ByteBuffer buffer=ByteBuffer.allocate(100);//设置一个定长的操作数据
		buffer.put(msg.getBytes());//保存要发送的内容
		buffer.flip();//重设缓存区进行发送处理
		this.clientChannel.write(buffer,buffer, new ClientWriteHandler(this.clientChannel,this.latch));
		if("exits".equalsIgnoreCase(msg)){
			return false;
		}
		return true;
	}
	
}
public class AIOEchoClient {
	public static void main(String[] args) throws IOException {
		AIOClientThread client=new AIOClientThread();
		new Thread(client).start();
		while(client.sendMessage(InputUtil.getString("请输入要发送的信息:"))){
			//System.out.println("发送数据");
		}
	}

}
