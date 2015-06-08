package testcase;

import java.util.concurrent.atomic.AtomicLong;

public abstract class TestCase {
	
	private EasyClient client;
	private int threadNum;
	private int loopTime;
	private AtomicLong minTime;
	private AtomicLong maxTime;
	
	protected TestCase(String serverAddress, int threadNum, int loopTime){
		client = new EasyClient(serverAddress);
		this.threadNum = threadNum;
		this.loopTime = loopTime;
		minTime = new AtomicLong(Long.MAX_VALUE);
		maxTime = new AtomicLong();
	}

	public boolean setUp(){
		return true;
	}
	
	public void loopRun() {
		for (int i = 0; i < loopTime; i++) {
			long start = System.currentTimeMillis();
			run();
			long end = System.currentTimeMillis();
			long time = end - start;
			updateTime(time);
		}
	}
	
	protected abstract void run();
	
	public void tearDown() {
		System.out.println("MinTime: " + minTime + "ms");
		System.out.println("MaxTime: " + maxTime + "ms");
	}
	
	private void updateTime(long time) {
		long min, max;
		boolean flag = false;
		do {
			min = minTime.get();
			if (min > time) {
				flag = minTime.compareAndSet(min, time);
			} else {
				flag = true;
			}
		} while (!flag);
		flag = false;
		do {
			max = maxTime.get();
			if (max < time) {
				flag = maxTime.compareAndSet(max, time);
			} else {
				flag = true;
			}
		} while (!flag);
	}

	protected EasyClient getClient() {
		return client;
	}
	
	public int getThreadNum() {
		return threadNum;
	}

	protected int getLoopTime() {
		return loopTime;
	}

}
