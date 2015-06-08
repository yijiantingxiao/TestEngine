package framework;

import java.util.LinkedList;
import java.util.List;

import testcase.TestCase;

public class TestFramework {
	
	private List<TestCase> tests;
	
	public TestFramework(){
		tests = new LinkedList<TestCase>();
	}
	
	private boolean setUp() {
		for (TestCase test : tests) {
			if (!test.setUp()) {
				return false;
			}
		}
		return true;
	}
	
	public void start() {		
		if (!setUp()) {
			return;
		}
		List<Thread> threads = new LinkedList<Thread>();
		for (final TestCase test : tests) {
			int threadNum = test.getThreadNum();
			Runnable run = new Runnable() {				
				@Override
				public void run() {
					test.loopRun();
				}
			};
			for (int i = 0; i < threadNum; i++) {
				Thread thread = new Thread(run);
				threads.add(thread);
				thread.start();
			}
		}
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		tearDown();
	}
	
	private void tearDown() {
		for (TestCase test : tests) {
			test.tearDown();
		}
		tests.clear();
	}	

	public void addTest(TestCase test) {
		tests.add(test);
	}
	
}
