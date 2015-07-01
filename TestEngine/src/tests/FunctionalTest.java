package tests;

import beans.CourseInfo;
import beans.SchoolInfo;
import beans.ServerAnswer;
import beans.StudentInfo;
import beans.Time;
import testcase.EasyClient;

public class FunctionalTest {
	
	private static String testName;
	private static String errorInfo;
	private static ServerAnswer answer;

	public static void main(String[] args) {
		System.out.println("FunctionalTest for Group" + TestVariables.GROUP_NUM);
		EasyClient client = new EasyClient(TestVariables.SERVERADDRESS);
		client.clearData();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.err.println("Sleep Failed");
		}
		
		testName = "addSchoolInfo";
		answer = client.addSchoolInfo(new SchoolInfo("SS", 32));
		if (checkNormalCaseAnswer()) {
			errorInfo = "院系名称已存在";
			answer = client.addSchoolInfo(new SchoolInfo("SS", 30));
			checkErrorCaseAnswer();
		} else {
			return;
		}
		
		testName = "addCourseInfo";
		answer = client.addCourseInfo(new CourseInfo("SS01", "SS", "SE", "PX", 4, "Z2204", new Time(1, 1), 100));
		if (checkNormalCaseAnswer()) {
			errorInfo = "选课号已存在";
			answer = client.addCourseInfo(new CourseInfo("SS01", "SS", "SE", "PX", 4, "Z2204", new Time(1, 1), 100));
			checkErrorCaseAnswer();
			
			errorInfo = "时间地点冲突";
			answer = client.addCourseInfo(new CourseInfo("SS02", "SS", "SE", "PX", 4, "Z2204", new Time(1, 1), 100));
			checkErrorCaseAnswer();
			
			errorInfo = "教师时间冲突";
			answer = client.addCourseInfo(new CourseInfo("SS02", "SS", "SE", "PX", 4, "Z2203", new Time(1, 1), 100));
			checkErrorCaseAnswer();
			
			errorInfo = "院系不存在";
			answer = client.addCourseInfo(new CourseInfo("SS02", "EE", "SE", "PX", 4, "Z2204", new Time(2, 1), 100));
			checkErrorCaseAnswer();
		} else {
			return;
		}
		
		testName = "addStudentInfo";
		answer = client.addStudentInfo(new StudentInfo("00000000000", "Alice", "女", "SS"));
		if (checkNormalCaseAnswer()) {
			errorInfo = "学号已存在";
			answer = client.addStudentInfo(new StudentInfo("00000000000", "Alice", "女", "SS"));
			checkErrorCaseAnswer();
			
			errorInfo = "院系不存在";
			answer = client.addStudentInfo(new StudentInfo("00001000000", "Alice", "女", "ENG"));
			checkErrorCaseAnswer();
		}else {
			return;
		}
		
		testName = "queryCourseByTime";
		CourseInfo[] courses = client.queryCourseByTime(new Time(1, 1));
		if (courses != null && courses.length == 1 && courses[0].equals(
				new CourseInfo("SS01", "SS", "SE", "PX", 4, "Z2204", new Time(1, 1), 100))) {
			printNormalPass();
		} else {
			printNormalFail();
		}
		testName += " empty";
		courses = client.queryCourseByTime(new Time(2, 2));
		if (courses != null && courses.length == 0) {
			printNormalPass();
		} else {
			printNormalFail();
		}
		
		testName = "queryCourseById";
		CourseInfo course = client.queryCourseById("SS01");
		if (course != null && course.equals(new CourseInfo("SS01", "SS", "SE", "PX", 4, "Z2204", new Time(1, 1), 100))) {
			printNormalPass();
		} else {
			printNormalFail();
		}
		testName += " null";
		course = client.queryCourseById("XYZ");
		if (course == null) {
			printNormalPass();
		} else {
			printNormalFail();
		}
		
		testName = "querySchedule before selectCourse";
		courses = client.querySchedule("00000000000");
		if (courses != null && courses.length == 0) {
			printNormalPass();
		} else {
			printNormalFail();
		}
		
		testName = "selectCourse";
		answer = client.selectCourse("SS01", "00000000000");
		if (checkNormalCaseAnswer()) {
			errorInfo = "学生不存在";
			answer = client.selectCourse("SS01", "1234");
			checkErrorCaseAnswer();
			
			errorInfo = "课程不存在";
			answer = client.selectCourse("XYZ", "00000000000");
			checkErrorCaseAnswer();
			
			errorInfo = "学分已满";
			
			errorInfo = "选课时间地点冲突";
			
			errorInfo = "选课人数已满";
		} else {
			return;
		}
		
		testName = "querySchedule after selectCourse";
		courses = client.querySchedule("00000000000");
		if (course != null && courses.length == 1 && courses[0].equals(
				new CourseInfo("SS01", "SS", "SE", "PX", 4, "Z2204", new Time(1, 1), 100))) {
			printNormalPass();
		} else {
			printNormalFail();
		}
		
		testName = "dropCourse";
		answer = client.dropCourse("SS01", "00000000000");
		if (checkNormalCaseAnswer()) {
			errorInfo = "学生不存在";
			answer = client.dropCourse("ZZZ", "1111");
			checkErrorCaseAnswer();
			
			errorInfo = "课程不存在";
			answer = client.dropCourse("ZZZ", "00000000000");
			checkErrorCaseAnswer();
			
			errorInfo = "学生未选课";
			
		} else {
			return;
		}
		
		testName = "querySchedule after dropCourse";
		courses = client.querySchedule("00000000000");
		if (courses != null && courses.length == 0) {
			printNormalPass();
		} else {
			printNormalFail();
		}
		
		client.clearData();
		
		System.out.println("The End of FunctionalTest");
	}
	
	private static boolean checkNormalCaseAnswer() {
		if (answer.isSuccess() && answer.getFailReason().equals("")) {
			printNormalPass();
			return true;
		}
		printNormalFail();
		return false;
	}
	
	private static void checkErrorCaseAnswer(){
		if (!answer.isSuccess() && answer.getFailReason().equals(errorInfo)) {
			printErrorPass();
		} else {
			printErrorFail();
		}
	}
	
	private static void printNormalPass() {
		System.out.println(testName + " Normal case \tpassed");
	}

	private static void printNormalFail() {
		System.err.println(testName + " Naomal case \tfailed");
	}
	
	private static void printErrorPass(){
		System.out.println(testName + " Error case: " + errorInfo + " \tpassed");
	}
	
	private static void printErrorFail() {
		System.err.println(testName + " Error case: " + errorInfo + " \tfailed");
	}
	
}
