package tests;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import framework.RandomGenerator;
import beans.CourseInfo;
import beans.SchoolInfo;
import beans.ServerAnswer;
import beans.StudentInfo;
import testcase.TestCase;

public class TestForSelectCourse extends TestCase {
		
	Random random;
	private AtomicInteger[] standards;
	private AtomicInteger[] results;
	private String[] cases;
	private SchoolInfo schoolInfo;
	private CourseInfo courseInfo;

	protected TestForSelectCourse(int threadNum) {
		super(TestVariables.SERVERADDRESS, threadNum, 100);
		random = new Random();
		standards = new AtomicInteger[6];
		results = new AtomicInteger[6];
		for (int i = 0; i < 6; i++) {
			standards[i] = new AtomicInteger();
			results[i] = new AtomicInteger();
		}
		cases = new String[] {"success", "student not exist", "course not exist", "credit exceed", "conflict", "maximum number"};
	}

	@Override
	public boolean setUp() {
		SchoolInfo schoolInfo = RandomGenerator.getRandomSchoolInfo();
		ServerAnswer answer = getClient().addSchoolInfo(schoolInfo);
		if (!checkAnswer(answer)) {
			System.err.println("setUp @ TestForSelectCourse: addSchoolInfo failed");
			return false;
		}
		this.schoolInfo = schoolInfo;
		CourseInfo courseInfo = RandomGenerator.getRandomCourseInfo(schoolInfo.getSchoolName());
		answer = getClient().addCourseInfo(courseInfo);
		if (!checkAnswer(answer)) {
			System.err.println("setUp @ TestForSelectCourse: addCourseInfo failed");
			return false;
		}
		this.courseInfo = courseInfo;
		return true;
	}

	@Override
	protected void run() {
		int caseNumber = random.nextInt(5);
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
			success = standards[0].get();
			if (success < courseInfo.getCapacity()) {
				flag = standards[0].compareAndSet(success, success + 1);
			} else {
				standards[5].getAndIncrement();
				flag = true;
			}
		} while (!flag);
		answer = getClient().selectCourse(courseInfo.getCourseId(), studentInfo.getStudentId());
		checkSelectAnswer(answer);
	}

	private void logicForCase2() {
		
	}
	
	private void logicForCase3() {
		
	}
	
	private void logicForCase4() {
		
	}
	
	private void logicForCase5() {
		
	}
	
	private void checkSelectAnswer(ServerAnswer answer) {
		if (checkAnswer(answer)) {
			results[0].getAndIncrement();
		}
		
	}

	@Override
	public void tearDown() {
		super.tearDown();
		List<Integer> failure = new LinkedList<Integer>();
		for (int i = 0; i < 6; i++) {
			if (results[i].get() != standards[i].get()) {
				failure.add(i);
			}
		}
		if (failure.isEmpty()) {
			System.out.println("TestForSelectCourse: Passed");
		} else {
			System.err.println("TestForSelectCourse: Failed");
			System.out.println("Failure Cases:");
			for (Integer fail : failure) {
				System.out.println("\t" + cases[fail]);
			}
		}
	}

}
