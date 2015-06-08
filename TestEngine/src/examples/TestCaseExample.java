package examples;

import java.util.concurrent.atomic.AtomicInteger;

import testcase.TestCase;
import beans.ServerAnswer;

public class TestCaseExample extends TestCase {
	
	private AtomicInteger success;

	protected TestCaseExample(String serverAddress, int threadNum, int loopTime) {
		super(serverAddress, threadNum, loopTime);
		success = new AtomicInteger();
	}

	@Override
	public void run() {
		ServerAnswer answer = getClient().addSchoolInfo(null);
		if (answer.isSuccess()) {
			success.getAndIncrement();
		}
	}

	@Override
	public void tearDown() {
		super.tearDown();
		if (success.get() == getThreadNum() * getLoopTime()) {
			System.out.println("TestCaseExample: Pass");
		} else {
			System.out.println("TestCaseExample: Failed");
		}
	}

}
