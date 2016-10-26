package me.dabpessoa.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dabpessoa [dabpessoa@gmail.com]
 * @since 2016.10.25
 */
public class ThreadUtils {
	
	public static void main(String[] args) {
		
		ThreadUtils.doThreadPooling(new ThreadListener() {
			
			int valor = 10;
			
			@Override
			public void proccess(ThreadBean threadBean, int threadNumber) {
				System.out.println("imprimindo: "+threadNumber);
				valor += 20;
				System.out.println(valor);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void threadFinished(ThreadBean threadBean, int threadNumber) {
				System.out.println("Thread Finished: "+threadNumber);
			}
			
			@Override
			public void allThreadsFinished(ThreadBean threadBean) {
				System.out.println("FIM...");
				
			}
		}, 2, 7);
		
	}
	
	public static void doThreadPooling(ThreadListener threadListener, int maxThreadsNumberAtSameTime, int totalThreadsToExecute) {
		ThreadBean threadBean = new ThreadBean(threadListener, maxThreadsNumberAtSameTime, totalThreadsToExecute, 0);
		proccess(threadBean);
		
	}
	
	private static void proccess(ThreadBean threadBean) {
		
		threadBean.clearThreads();
		if  (threadBean.getTotalThreadsToExecute() > threadBean.getThreadCount()) {
			while (threadBean.threadsSize() < threadBean.getMaxThreadsNumberAtSameTime() && threadBean.getThreadCount() < threadBean.getTotalThreadsToExecute()) {
				
				MyRunnable runnable = new ThreadUtils().new MyRunnable(threadBean);
				runnable.setLogicListener(threadBean.getLogicListener());
				threadBean.addThread(new Thread(runnable));
				
			}
			
			threadBean.startThreads();
		}
		
		if (threadBean.isThreadsEmpty()) {
			threadBean.getLogicListener().allThreadsFinished(threadBean);
		}
		
	}
	
	class MyRunnable implements Runnable {
		
		private ThreadListener listener;
		private ThreadBean threadBean;
		private int threadNumber;
		
		public MyRunnable(ThreadBean threadBean) {
			this.threadBean = threadBean;
			this.threadBean.setThreadCount(this.threadBean.getThreadCount()+1);
			this.threadNumber = this.threadBean.getThreadCount();
		}
		
		@Override
		public void run() {
			listener.proccess(threadBean, threadNumber);
			endVerify();
		}
		
		public void endVerify() {
			threadBean.getLogicListener().threadFinished(threadBean, threadNumber);
			if (threadBean.isOnlyOneThreadAlive()) {
				proccess(threadBean);
			}
		}
		
		public void setLogicListener(ThreadListener listener) {
			this.listener = listener;
		}
		
	}
	
}

class ThreadBean {
	private List<Thread> threads;
	private ThreadListener logicListener;
	private int maxThreadsNumberAtSameTime;
	private int totalThreadsToExecute;
	private int threadCount;
	protected ThreadBean() {}
	protected ThreadBean(ThreadListener logicListener, int maxThreadsNumberAtSameTime, int totalThreadsToExecute, int threadCount) {
		this.threads = new ArrayList<Thread>();
		this.logicListener = logicListener;
		this.maxThreadsNumberAtSameTime = maxThreadsNumberAtSameTime;
		this.totalThreadsToExecute = totalThreadsToExecute;
		this.threadCount = threadCount;
	}
	protected void addThread(Thread thread) {
		threads.add(thread);
	}
	protected void clearThreads() {
		threads.clear();
	}
	protected int threadsSize() {
		return threads.size();
	}
	protected boolean isThreadsEmpty() {
		return threads.isEmpty();
	}
	protected synchronized boolean isOnlyOneThreadAlive() {
		int aliveCount = 0;
		for (Thread thread : threads) {
			if (thread.isAlive()) aliveCount++;			
		}
		
		if (aliveCount == 1) return true;
		else return false;
	}
	protected synchronized void startThreads() {
		if (threads != null) {
			for (Thread thread : threads) {
				thread.start();
			}
		}
	}
	public ThreadListener getLogicListener() {
		return logicListener;
	}
	protected void setLogicListener(ThreadListener logicListener) {
		this.logicListener = logicListener;
	}
	public int getMaxThreadsNumberAtSameTime() {
		return maxThreadsNumberAtSameTime;
	}
	protected void setMaxThreadsNumberAtSameTime(int maxThreadsNumberAtSameTime) {
		this.maxThreadsNumberAtSameTime = maxThreadsNumberAtSameTime;
	}
	public int getTotalThreadsToExecute() {
		return totalThreadsToExecute;
	}
	protected void setTotalThreadsToExecute(int totalThreadsToExecute) {
		this.totalThreadsToExecute = totalThreadsToExecute;
	}
	public int getThreadCount() {
		return threadCount;
	}
	protected void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	protected void setThreads(List<Thread> threads) {
		this.threads = threads;
	}
}

interface ThreadListener {
	void proccess(ThreadBean threadBean, int threadNumber);
	void threadFinished(ThreadBean threadBean, int threadNumber);
	void allThreadsFinished(ThreadBean threadBean);
}
