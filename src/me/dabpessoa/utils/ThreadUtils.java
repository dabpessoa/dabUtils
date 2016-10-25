package me.dabpessoa.utils;

import java.util.ArrayList;
import java.util.List;

public class ThreadUtils {
	
	public static void main(String[] args) {
		
		ThreadUtils.createAndStartThreads(new LogicListener() {
			
			int valor = 10;
			@Override
			public void doLogics(int threadNumber) {
				System.out.println("imprimindo: "+threadNumber);
				valor += 20;
				System.out.println(valor);
			}
		}, 5, 6);
		
	}
	
	private static List<Thread> threads;
	static {
		threads = new ArrayList<Thread>();
	}
	
	public static void createAndStartThreads(LogicListener logicListener, int maxThreadsNumberAtSameTime, int totalThreadsToExecute) {
		ThreadBean threadBean = new ThreadBean(logicListener, maxThreadsNumberAtSameTime, totalThreadsToExecute, 0);
		proccess(threadBean);
		
	}
	
	public static void proccess(ThreadBean threadBean) {
		
		threads.clear();
		while (threads.size() < threadBean.getMaxThreadsNumberAtSameTime() && threadBean.getThreadCount() < threadBean.getTotalThreadsToExecute()) {
			
			MyRunnable runnable = new MyRunnable(threadBean, threads);
			runnable.setLogicListener(threadBean.getLogicListener());
			threads.add(new Thread(runnable));
			
		}
		
		// start threads
		for (Thread thread : threads) {
			thread.start();
		}
		
		if (threads.isEmpty()) {
			System.out.println("FIM...");
		}
		
	}
	
}

class ThreadBean {
	private LogicListener logicListener;
	private int maxThreadsNumberAtSameTime;
	private int totalThreadsToExecute;
	private int threadCount;
	public ThreadBean() {}
	public ThreadBean(LogicListener logicListener, int maxThreadsNumberAtSameTime, int totalThreadsToExecute, int threadCount) {
		this.logicListener = logicListener;
		this.maxThreadsNumberAtSameTime = maxThreadsNumberAtSameTime;
		this.totalThreadsToExecute = totalThreadsToExecute;
		this.threadCount = threadCount;
	}
	public LogicListener getLogicListener() {
		return logicListener;
	}
	public void setLogicListener(LogicListener logicListener) {
		this.logicListener = logicListener;
	}
	public int getMaxThreadsNumberAtSameTime() {
		return maxThreadsNumberAtSameTime;
	}
	public void setMaxThreadsNumberAtSameTime(int maxThreadsNumberAtSameTime) {
		this.maxThreadsNumberAtSameTime = maxThreadsNumberAtSameTime;
	}
	public int getTotalThreadsToExecute() {
		return totalThreadsToExecute;
	}
	public void setTotalThreadsToExecute(int totalThreadsToExecute) {
		this.totalThreadsToExecute = totalThreadsToExecute;
	}
	public int getThreadCount() {
		return threadCount;
	}
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
}

class MyRunnable implements Runnable {
	
	private LogicListener listener;
	private List<Thread> threads;
	private ThreadBean threadBean;
	
	public MyRunnable(ThreadBean threadBean, List<Thread> threads) {
		this.threadBean = threadBean;
		this.threadBean.setThreadCount(this.threadBean.getThreadCount()+1);
		this.threads = threads;
	}
	
	@Override
	public void run() {
		listener.doLogics(threadBean.getThreadCount());
		endVerify();
	}
	
	public void endVerify() {
		if (isLastThreadFinishing()) {
			if  (threadBean.getTotalThreadsToExecute() > threadBean.getThreadCount()) {
				ThreadUtils.proccess(threadBean);
			}
		}
	}
	
	public synchronized boolean isLastThreadFinishing() {
		int aliveCount = 0;
		for (Thread thread : threads) {
			if (thread.isAlive()) aliveCount++;			
		}
		
		if (aliveCount == 1) return true;
		else return false;
	}
	
	public void setLogicListener(LogicListener listener) {
		this.listener = listener;
	}
	
}

interface LogicListener {
	void doLogics(int threadNumber);
}

interface EndListener {
	void endNotify();
}
