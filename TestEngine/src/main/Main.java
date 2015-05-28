package main;

import testcase.TestExample;
import framework.TestFramework;

public class Main {	
	
	public static void main(String[] args) {
		TestFramework framework = new TestFramework();
		TestExample example = new TestExample(1);
		framework.addTest(example);
		framework.start();	
	}
	
}
