package cn.mldn.jnio.demo.tcpaio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

class EchoHandler implements CompletionHandler<Integer,ByteBuffer>{
	private AsynchronousSocketChannel clientChannel;//客户端对象
	private boolean exit=false;//回应是否结束，如果exit=true表示不再接收
	public EchoHandler(AsynchronousSocketChannel clientChannel){
		this.clientChannel=clientChannel;
	}

	public void completed(Integer result, ByteBuffer attachment) {
		//如果需要读取数据，则应该重置Buffer缓存区
		attachment.flip();
		//接收读取的数据
		String readMessage=new String(attachment.array(),0,attachment.remaining()).trim();
		System.err.println("【服务器读取到数据】"+readMessage);
		//保存数据的回应处理信息
		String resultMessage="【ECHO】"+readMessage+"\n";
		if("exit".equalsIgnoreCase(readMessage)){
			resultMessage="【ECHO】拜拜，下次再见!";
			this.exit=true;
		}
		this.echoWrite(resultMessage);//回应处理
		
		
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		this.closeClient();
		
	}
	//实现数据的回应处理
	private void echoWrite(String result){
		ByteBuffer buffer=ByteBuffer.allocate(100);//设置回应缓存区
		buffer.put(result.getBytes());//信息回应处理
		buffer.flip();//准备信息内容的输出
		this.clientChannel.write(buffer, buffer, new CompletionHandler<Integer,ByteBuffer>(){

			public void completed(Integer result, ByteBuffer attachment) {
				if(attachment.hasRemaining()){//有数据才进行写入
					EchoHandler.this.clientChannel.write(attachment, attachment, this);//进行数据的输出操作
				}else{
					if(EchoHandler.this.exit==false){
						//还可以继续读取
						ByteBuffer readBuffer=ByteBuffer.allocate(100);
						EchoHandler.this.clientChannel.read(readBuffer,readBuffer,new EchoHandler(EchoHandler.this.clientChannel));
					}
				}
			}
			public void failed(Throwable exc, ByteBuffer attachment) {
				EchoHandler.this.closeClient();
			}}
			);
		}

	private void closeClient() {
		System.out.println("客户端连接有错误，中断与此客户端的处理!");
		try {
			this.clientChannel.close();//关闭客户端的信息处理操作
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	}
	


class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel,AIOServerThread>{

	public void completed(AsynchronousSocketChannel channel,
			AIOServerThread aioThread) {
		try {
			System.out.println("连接客户端的地址:"+channel.getRemoteAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
		aioThread.getServerChannel().accept(aioThread, this);//接收连接
		//开辟一个接收的缓存区
		ByteBuffer buffer=ByteBuffer.allocate(100);
		//创建一个异步处理操作实现回应处理
		channel.read(buffer, buffer, new EchoHandler(channel));
	}

	public void failed(Throwable exc, AIOServerThread aioThread) {
		System.out.println("服务器的连接处理失败......");
		aioThread.getLatch().countDown();//减一，减除阻塞状态
	}
	
}
//定义一个AIO的服务处理线程
class AIOServerThread implements Runnable{
    private static final int PORT=9999;//监听端口
    //保证服务器端线程执行完毕后关闭
    private CountDownLatch latch=null;
    //异步服务的处理Channel
    private AsynchronousServerSocketChannel serverChannel=null;
    public AIOServerThread() throws IOException{
    	//在构造方法中为相应的类实例化
    	this.latch= new CountDownLatch(1);//服务器端线程只有一个
    	this.serverChannel=AsynchronousServerSocketChannel.open();//打开异步通道
    	//进行服务器端口的绑定
    	this.serverChannel.bind(new InetSocketAddress(PORT));
    	System.out.println("服务器端启动成功，在"+PORT+"端口上监听，等待客户端的连接........");
    }
    
    public AsynchronousServerSocketChannel getServerChannel(){
    	return this.serverChannel;
    }
    public CountDownLatch getLatch(){
    	return this.latch;
    }
	public void run() {
		//在线程启动里面等待连接
		this.serverChannel.accept(this,new AcceptHandler());//等待客户端连接
		try {
			this.latch.await();
			System.err.println("服务器连接失败!服务器停止运行....");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}//持续等待状态
		
	}
	
}
public class AIOEchoServer {
	public static void main(String[] args) throws IOException {
		new Thread(new AIOServerThread()).start();
	}

}
