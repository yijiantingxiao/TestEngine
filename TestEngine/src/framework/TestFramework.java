package framework;

import java.util.LinkedList;
import java.util.List;

import testcase.TestCase;

public class TestFramework {
	
	private List<TestCase> tests;
	
	public TestFramework(){
		tests = new LinkedList<TestCase>();
	}
	
	public void setUp() {
		for (TestCase test : tests) {
			test.setUp();
		}
	}
	
	public void start() {		
		for (final TestCase test : tests) {
			int threadNum = test.getThreadNum();
			Runnable run = new Runnable() {				
				@Override
				public void run() {
					test.run();
				}
			};
			for (int i = 0; i < threadNum; i++) {
				Thread thread = new Thread(run);
				thread.start();
			}
		}
	}
	
	public void tearDown() {
		for (TestCase test : tests) {
			test.tearDown();
		}
		tests.clear();
	}	

	public void addTest(TestCase test) {
		tests.add(test);
	}
	
}
