package examples;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import testcase.TestCase;
import beans.ServerAnswer;

public class TestCaseExample extends TestCase {
	
	private AtomicInteger success;
	private AtomicLong minTime;
	private AtomicLong maxTime;

	protected TestCaseExample(String serverAddress, int threadNum) {
		super(serverAddress, threadNum);
		success = new AtomicInteger();
		minTime = new AtomicLong(Long.MAX_VALUE);
		maxTime = new AtomicLong();
	}

	@Override
	public void run() {
		for (int i = 0; i < 1000; i++) {
			long start = System.currentTimeMillis();
			ServerAnswer answer = getClient().addSchoolInfo(null);
			long end = System.currentTimeMillis();
			long time = end - start;
			if (answer.isSuccess()) {
				success.getAndIncrement();
			}
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
	}

	@Override
	public void tearDown() {
		System.out.println("MinTime: " + minTime + "ms");
		System.out.println("MaxTime: " + maxTime + "ms");
		if (success.get() == getThreadNum() * 1000) {
			System.out.println("TestCaseExample: Pass");
		} else {
			System.out.println("TestCaseExample: Failed");
		}
	}

}
