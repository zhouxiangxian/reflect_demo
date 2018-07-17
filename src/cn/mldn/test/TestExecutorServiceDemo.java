package cn.mldn.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestExecutorServiceDemo {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
//		executorService.execute(new Runnable() {
//		    public void run() {
//		        System.out.println("Asynchronous task");
//		    }
//		});
//		executorService.shutdown();
//		Future future = executorService.submit(new Runnable() {
//		    public void run() {
//		        System.out.println("Asynchronous task");
//		    }
//		});
//		//如果任务结束执行则返回 null
//		System.out.println("future.get()=" + future.get());
		Future future = executorService.submit(new Callable(){
		    public Object call() throws Exception {
		        System.out.println("Asynchronous Callable");
		        return "Callable Result";
		    }
		});
		 
		System.out.println("future.get() = " + future.get());


	}

}
