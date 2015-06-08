package testcase;

import java.util.concurrent.atomic.AtomicLong;

public abstract class TestCase {
	
	private EasyClient client;
	private int threadNum;
	private AtomicLong minTime;
	private AtomicLong maxTime;
	
	protected TestCase(String serverAddress, int threadNum){
		client = new EasyClient(serverAddress);
		this.threadNum = threadNum;
		minTime = new AtomicLong(Long.MAX_VALUE);
		maxTime = new AtomicLong();
	}

	public boolean setUp(){
		return true;
	}
	
	public abstract void run();
	
	public void tearDown() {
	}
	
	protected void updateTime(long time) {
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

	public EasyClient getClient() {
		return client;
	}
	
	public int getThreadNum() {
		return threadNum;
	}

	public AtomicLong getMinTime() {
		return minTime;
	}

	public AtomicLong getMaxTime() {
		return maxTime;
	}

}
