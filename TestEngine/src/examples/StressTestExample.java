package examples;

import framework.TestFramework;

public class StressTestExample {

	public static void main(String[] args) {
		TestFramework framework = new TestFramework();
		framework.addTest(new TestCaseExample("localhost:8080/EasyServer", 100, 1000));
		framework.start();
	}
	
}
