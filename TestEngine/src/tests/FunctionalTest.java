package tests;

import beans.CourseInfo;
import beans.SchoolInfo;
import beans.ServerAnswer;
import beans.StudentInfo;
import beans.Time;
import testcase.EasyClient;

public class FunctionalTest {
	
	private static final SchoolInfo SCHOOL_ONE = new SchoolInfo("SS", 6);
	private static final CourseInfo COURSE_ONE = new CourseInfo("SS01", SCHOOL_ONE.getSchoolName(), "SE", "PX", 4, "Z2204", new Time(1, 1), 100);
	private static final CourseInfo COURSE_TWO = new CourseInfo("SS02", SCHOOL_ONE.getSchoolName(), "ICSE", "ZBY", 4, "Z2108", new Time(4, 2), 100);
	private static final CourseInfo COURSE_THREE = new CourseInfo("SS03", SCHOOL_ONE.getSchoolName(), "Web", "ZDL", 2, "Z2307A", new Time(1, 1), 40);
	private static final CourseInfo COURSE_FOUR = new CourseInfo("SS04", SCHOOL_ONE.getSchoolName(), "RE", "PX", 2, "Z2101", new Time(5, 1), 1);
	private static final StudentInfo STUDENT_ONE = new StudentInfo("00000000000", "Alice", "女", SCHOOL_ONE.getSchoolName());
	private static final StudentInfo STUDENT_TWO = new StudentInfo("11111111111", "Bob", "男", SCHOOL_ONE.getSchoolName());
	private static String testName;
	private static String errorInfo;
	private static String errorInfoAlter = null;
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
		
		System.out.println("==============");
		testName = "addSchoolInfo";
		answer = client.addSchoolInfo(SCHOOL_ONE);
		if (checkNormalCaseAnswer()) {
			errorInfo = "院系名称已存在";
			answer = client.addSchoolInfo(SCHOOL_ONE);
			checkErrorCaseAnswer();
		} else {
			return;
		}
		
		System.out.println("==============");
		testName = "addCourseInfo";
		answer = client.addCourseInfo(COURSE_ONE);
		if (checkNormalCaseAnswer()) {
			errorInfo = "选课号已存在";
			answer = client.addCourseInfo(COURSE_ONE);
			checkErrorCaseAnswer();
			
			errorInfo = "时间地点冲突";
			answer = client.addCourseInfo(new CourseInfo("T1", "SS", "SE", "PX", 4, "Z2204", new Time(1, 1), 100));
			checkErrorCaseAnswer();
			
			errorInfo = "教师时间冲突";
			answer = client.addCourseInfo(new CourseInfo("T2", "SS", "SE", "PX", 4, "Z2203", new Time(1, 1), 100));
			checkErrorCaseAnswer();
			
			errorInfo = "院系不存在";
			answer = client.addCourseInfo(new CourseInfo("T3", "EE", "SE", "PX", 4, "Z2204", new Time(2, 1), 100));
			checkErrorCaseAnswer();
		} else {
			return;
		}
		
		testName = "addCourseInfo 2";
		answer = client.addCourseInfo(COURSE_TWO);
		if (!checkNormalCaseAnswer()) {
			return;
		}
		
		testName = "addCourseInfo 3";
		answer = client.addCourseInfo(COURSE_THREE);
		if (!checkNormalCaseAnswer()) {
			return;
		}
		
		testName = "addCourseInfo 4";
		answer = client.addCourseInfo(COURSE_FOUR);
		if (!checkNormalCaseAnswer()) {
			return;
		}
		
		System.out.println("==============");
		testName = "addStudentInfo";
		answer = client.addStudentInfo(STUDENT_ONE);
		if (checkNormalCaseAnswer()) {
			errorInfo = "学号已存在";
			answer = client.addStudentInfo(STUDENT_ONE);
			checkErrorCaseAnswer();
			
			errorInfo = "院系不存在";
			answer = client.addStudentInfo(new StudentInfo("00001000000", "Alice", "女", "ENG"));
			checkErrorCaseAnswer();
		}else {
			return;
		}
		
		testName = "addStudentInfo 2";
		answer = client.addStudentInfo(STUDENT_TWO);
		if (!checkNormalCaseAnswer()) {
			return;
		}
		
		System.out.println("==============");
		testName = "queryCourseByTime";
		CourseInfo[] courses = client.queryCourseByTime(COURSE_ONE.getTime());
		if (courses != null && courses.length == 1 && COURSE_ONE.equals(courses[0])) {
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
		
		System.out.println("==============");
		testName = "queryCourseById";
		CourseInfo course = client.queryCourseById(COURSE_ONE.getCourseId());
		if (course != null && COURSE_ONE.equals(course)) {
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
		
		System.out.println("==============");
		testName = "querySchedule before selectCourse";
		courses = client.querySchedule(STUDENT_ONE.getStudentId());
		if (courses != null && courses.length == 0) {
			printNormalPass();
		} else {
			printNormalFail();
		}
		
		System.out.println("==============");
		testName = "selectCourse";
		answer = client.selectCourse(COURSE_ONE.getCourseId(), STUDENT_ONE.getStudentId());
		if (checkNormalCaseAnswer()) {
			errorInfo = "学生不存在";
			answer = client.selectCourse("XYZ", "1234");
			checkErrorCaseAnswer();
			
			errorInfo = "课程不存在";
			answer = client.selectCourse("XYZ", STUDENT_ONE.getStudentId());
			checkErrorCaseAnswer();
			
			errorInfo = "学分已满";
			answer = client.selectCourse(COURSE_TWO.getCourseId(), STUDENT_ONE.getStudentId());
			checkErrorCaseAnswer();
			
			errorInfo = "选课时间地点冲突";
			errorInfoAlter = "选课时间冲突";
			answer = client.selectCourse(COURSE_THREE.getCourseId(), STUDENT_ONE.getStudentId());
			checkErrorCaseAnswer();
			errorInfoAlter = null;
			
			errorInfo = "选课人数已满";
			answer = client.selectCourse(COURSE_FOUR.getCourseId(), STUDENT_TWO.getStudentId());
			if (!checkNormalCaseAnswer()) {
				return;
			}
			answer = client.selectCourse(COURSE_FOUR.getCourseId(), STUDENT_ONE.getStudentId());
			checkErrorCaseAnswer();
		} else {
			return;
		}
		
		System.out.println("==============");
		testName = "querySchedule after selectCourse";
		courses = client.querySchedule(STUDENT_ONE.getStudentId());
		if (course != null && courses.length == 1 && COURSE_ONE.equals(courses[0])) {
			printNormalPass();
		} else {
			printNormalFail();
		}
		
		System.out.println("==============");
		testName = "dropCourse";
		answer = client.dropCourse(COURSE_ONE.getCourseId(), STUDENT_ONE.getStudentId());
		if (checkNormalCaseAnswer()) {
			errorInfo = "学生不存在";
			answer = client.dropCourse("ZZZ", "1111");
			checkErrorCaseAnswer();
			
			errorInfo = "课程不存在";
			answer = client.dropCourse("ZZZ", STUDENT_ONE.getStudentId());
			checkErrorCaseAnswer();
			
			errorInfo = "学生未选课";
			answer = client.dropCourse(COURSE_ONE.getCourseId(), STUDENT_ONE.getStudentId());
			checkErrorCaseAnswer();
		} else {
			return;
		}
		
		System.out.println("==============");
		testName = "querySchedule after dropCourse";
		courses = client.querySchedule(STUDENT_ONE.getStudentId());
		if (courses != null && courses.length == 0) {
			printNormalPass();
		} else {
			printNormalFail();
		}
		
		client.clearData();
		
		System.out.println("==============");
		System.out.println("The End of FunctionalTest");
	}
	
	private static boolean checkNormalCaseAnswer() {
		if (answer.isSuccess() && answer.getFailReason() != null && answer.getFailReason().equals("")) {
			printNormalPass();
			return true;
		}
		printNormalFail();
		return false;
	}
	
	private static void checkErrorCaseAnswer(){
		if (!answer.isSuccess() && answer.getFailReason() != null 
				&& (answer.getFailReason().equals(errorInfo) || answer.getFailReason().equals(errorInfoAlter))) {
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
