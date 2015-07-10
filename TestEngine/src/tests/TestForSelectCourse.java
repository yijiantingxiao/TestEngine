package tests;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import testcase.TestCase;
import beans.CourseInfo;
import beans.SchoolInfo;
import beans.ServerAnswer;
import beans.StudentInfo;
import beans.Time;
import framework.RandomGenerator;

public class TestForSelectCourse extends TestCase {
	
	private AtomicIntegerArray standards;
	private AtomicIntegerArray results;
	private String[] cases;
	private AtomicInteger number;
	private SchoolInfo schoolInfo;
	private CourseInfo courseInfo;
	private CourseInfo courseInfo2;
	private CourseInfo courseInfo3;

	protected TestForSelectCourse(int threadNum) {
		super(TestVariables.SERVERADDRESS, threadNum, 64);
		standards = new AtomicIntegerArray(6);
		results = new AtomicIntegerArray(6);
		cases = new String[] {"success", "student not exist", "course not exist", "credit exceed", "conflict", "maximum number"};
		number = new AtomicInteger();
	}

	@Override
	public boolean setUp() {
		SchoolInfo schoolInfo = RandomGenerator.getRandomSchoolInfo();
		schoolInfo.setCreditRequirement(6);
		ServerAnswer answer = getClient().addSchoolInfo(schoolInfo);
		if (!checkAnswer(answer)) {
			System.err.println("setUp @ TestForSelectCourse: addSchoolInfo failed");
			return false;
		}
		this.schoolInfo = schoolInfo;
		CourseInfo courseInfo = RandomGenerator.getRandomCourseInfo(schoolInfo.getSchoolName());
		courseInfo.setCredit(4);
		courseInfo.setTime(new Time(1, 1));
		answer = getClient().addCourseInfo(courseInfo);
		if (!checkAnswer(answer)) {
			System.err.println("setUp @ TestForSelectCourse: addCourseInfo 1 failed");
			return false;
		}
		this.courseInfo = courseInfo;
		CourseInfo courseInfo2 = RandomGenerator.getRandomCourseInfo(schoolInfo.getSchoolName());
		courseInfo2.setCredit(4);
		courseInfo2.setTime(new Time(2, 2));
		courseInfo2.setCapacity(Integer.MAX_VALUE);
		answer = getClient().addCourseInfo(courseInfo2);
		if (!checkAnswer(answer)) {
			System.err.println("setUp @ TestForSelectCourse: addCourseInfo 2 failed");
			return false;
		}
		this.courseInfo2 = courseInfo2;
		CourseInfo courseInfo3 = RandomGenerator.getRandomCourseInfo(schoolInfo.getSchoolName());
		courseInfo3.setCredit(1);
		courseInfo3.setTime(new Time(1, 1));
		courseInfo3.setCapacity(Integer.MAX_VALUE);
		answer = getClient().addCourseInfo(courseInfo3);
		if (!checkAnswer(answer)) {
			System.err.println("setUp @ TestForSelectCourse: addCourseInfo 3 failed");
			return false;
		}
		this.courseInfo3 = courseInfo3;
		return true;
	}

	@Override
	protected void run() {
		int caseNumber = number.getAndIncrement() / 5;
		switch (caseNumber) {
		case 0:
			logicForCase1();
			break;
		case 1:
			logicForCase2();
			break;
		case 2:
			logicForCase3();
			break;
		case 3:
			logicForCase4();
			break;
		case 4:
			logicForCase5();
			break;
		default:
			break;
		}
	}
	
	private void logicForCase1() {
		StudentInfo studentInfo = RandomGenerator.getRandomStudentInfo(schoolInfo.getSchoolName());
		ServerAnswer answer = getClient().addStudentInfo(studentInfo);
		if (!checkAnswer(answer)) {
			System.err.println("logicForCase1 @ TestForSelectCourse: addStudentInfo failed");
			return;
		}
		int success;
		boolean flag;
		do {
			success = standards.get(0);
			if (success < courseInfo.getCapacity()) {
				flag = standards.compareAndSet(0, success, success + 1);
			} else {
				standards.getAndIncrement(5);
				flag = true;
			}
		} while (!flag);
		answer = getClient().selectCourse(courseInfo.getCourseId(), studentInfo.getStudentId());
		checkSelectAnswer(answer);
	}

	private void logicForCase2() {
		standards.getAndIncrement(1);
		ServerAnswer answer = getClient().selectCourse(courseInfo.getCourseId(), "= =");
		checkSelectAnswer(answer);
	}
	
	private void logicForCase3() {
		StudentInfo studentInfo = RandomGenerator.getRandomStudentInfo(schoolInfo.getSchoolName());
		ServerAnswer answer = getClient().addStudentInfo(studentInfo);
		if (!checkAnswer(answer)) {
			System.err.println("logicForCase3 @ TestForSelectCourse: addStudentInfo failed");
			return;
		}
		standards.getAndIncrement(2);
		answer = getClient().selectCourse("= =", studentInfo.getStudentId());
		checkSelectAnswer(answer);
	}
	
	private void logicForCase4() {
		StudentInfo studentInfo = RandomGenerator.getRandomStudentInfo(schoolInfo.getSchoolName());
		ServerAnswer answer = getClient().addStudentInfo(studentInfo);
		if (!checkAnswer(answer)) {
			System.err.println("logicForCase4 @ TestForSelectCourse: addStudentInfo failed");
			return;
		}
		standards.getAndIncrement(3);
		answer = getClient().selectCourse(courseInfo2.getCourseId(), studentInfo.getStudentId());
		if (!checkAnswer(answer)) {
			System.err.println("logicForCase4 @ TestForSelectCourse: selectCourse2 failed");
			return;
		}
		answer = getClient().selectCourse(courseInfo.getCourseId(), studentInfo.getStudentId());
		checkSelectAnswer(answer);
	}
	
	private void logicForCase5() {
		StudentInfo studentInfo = RandomGenerator.getRandomStudentInfo(schoolInfo.getSchoolName());
		ServerAnswer answer = getClient().addStudentInfo(studentInfo);
		if (!checkAnswer(answer)) {
			System.err.println("logicForCase5 @ TestForSelectCourse: addStudentInfo failed");
			return;
		}
		standards.getAndIncrement(4);
		answer = getClient().selectCourse(courseInfo3.getCourseId(), studentInfo.getStudentId());
		if (!checkAnswer(answer)) {
			System.err.println("logicForCase5 @ TestForSelectCourse: selectCourse3 failed");
			return;
		}
		answer = getClient().selectCourse(courseInfo.getCourseId(), studentInfo.getStudentId());
		checkSelectAnswer(answer);
		
	}
	
	private void checkSelectAnswer(ServerAnswer answer) {
		if (checkAnswer(answer)) {
			results.getAndIncrement(0);
		} else if (!answer.isSuccess()) {
			String failReason = answer.getFailReason();
			if (failReason.equals("学生不存在")) {
				results.getAndIncrement(1);
			} else if (failReason.equals("课程不存在")) {
				results.getAndIncrement(2);
			} else if (failReason.equals("学分已满")) {
				results.getAndIncrement(3);
			} else if (failReason.equals("选课时间地点冲突") || failReason.equals("选课时间冲突") || failReason.equals("学生此时间段已选课")) {
				results.getAndIncrement(4);
			} else if (failReason.equals("选课人数已满")) {
				results.getAndIncrement(5);
			}
		}
		
	}

	@Override
	public void tearDown() {
		super.tearDown();
		List<Integer> failure = new LinkedList<Integer>();
		for (int i = 0; i < 6; i++) {
			if (results.get(i) != standards.get(i)) {
				failure.add(i);
			}
		}
		if (failure.isEmpty()) {
			System.out.println("TestForSelectCourse: Passed");
			for (int i = 0; i < 6; i++) {
				System.out.println(standards.get(i));
			}
		} else {
			System.err.println("TestForSelectCourse: Failed");
			System.out.println("Failure Cases: " + failure.size() + "/" + cases.length);
			for (Integer fail : failure) {
				System.out.println("\t" + cases[fail]);
			}
		}
	}

}
