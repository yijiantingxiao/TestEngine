package examples;

import java.util.concurrent.atomic.AtomicInteger;

import testcase.TestCase;
import beans.ServerAnswer;

public class TestCaseExample extends TestCase {
	
	private AtomicInteger success;

	protected TestCaseExample(String serverAddress, int threadNum) {
		super(serverAddress, threadNum);
		success = new AtomicInteger();
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
			updateTime(time);
		}
	}

	@Override
	public void tearDown() {
		System.out.println("MinTime: " + getMinTime() + "ms");
		System.out.println("MaxTime: " + getMaxTime() + "ms");
		if (success.get() == getThreadNum() * 1000) {
			System.out.println("TestCaseExample: Pass");
		} else {
			System.out.println("TestCaseExample: Failed");
		}
	}

}
