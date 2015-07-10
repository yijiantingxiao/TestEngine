package tests;

import framework.TestFramework;

public class StressTest {

	public static void main(String[] args) {
		TestFramework framework = new TestFramework();
		framework.addTest(new TestForQueryCourseByTime(100));
		//framework.addTest(new TestForQueryCourseByTime(200));
		//framework.addTest(new TestForQueryCourseByTime(500));
		//framework.addTest(new TestForQueryCourseByTime(1000));
		//framework.addTest(new TestForSelectCourse(100));
		//framework.addTest(new TestForSelectCourse(200));
		//framework.addTest(new TestForSelectCourse(500));
		//framework.addTest(new TestForSelectCourse(1000));
		framework.start();
	}

}
