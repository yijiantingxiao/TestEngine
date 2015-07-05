package tests;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import beans.CourseInfo;
import beans.SchoolInfo;
import beans.ServerAnswer;
import beans.Time;
import framework.RandomGenerator;
import testcase.TestCase;

public class TestForQueryCourseByTime extends TestCase {
	
	private Map<Time, List<CourseInfo>> timeCountMap;
	private AtomicInteger success;

	protected TestForQueryCourseByTime(int threadNum) {
		super(TestVariables.SERVERADDRESS, threadNum, 128);
		timeCountMap = new HashMap<Time, List<CourseInfo>>();
		success = new AtomicInteger();
	}

	@Override
	public boolean setUp() {
		SchoolInfo schoolInfo = RandomGenerator.getRandomSchoolInfo();
		ServerAnswer answer = getClient().addSchoolInfo(schoolInfo);
		if (!checkAnswer(answer)) {
			System.err.println("setUp @ TestForQueryCourseByTime: addSchoolInfo failed");
			return false;
		}
		for (int i = 0; i < 200; i++) {
			CourseInfo courseInfo = RandomGenerator.getRandomCourseInfo(schoolInfo.getSchoolName());
			answer = getClient().addCourseInfo(courseInfo);
			if (!checkAnswer(answer)) {
				System.err.println("setUp @ TestForQueryCourseByTime: addCourseInfo " + (i + 1) + " failed");
				return false;
			}
			addCourseToMap(courseInfo);
		}
		return true;
	}
	
	private void addCourseToMap(CourseInfo courseInfo) {
		Time time = courseInfo.getTime();
		List<CourseInfo> list = timeCountMap.get(time);
		if (list == null) {
			list = new LinkedList<CourseInfo>();
			timeCountMap.put(time, list);
		}
		list.add(courseInfo);
	}

	@Override
	protected void run() {
		Time time = RandomGenerator.getRandomTime();
		CourseInfo[] courses = getClient().queryCourseByTime(time);
		List<CourseInfo> list = timeCountMap.get(time);
		if (checkQueryResult(courses, list)) {
			success.getAndIncrement();
		}
	}
	
	private boolean checkQueryResult(CourseInfo[] answer, List<CourseInfo> list) {
		if (answer == null) {
			return false;
		}
		if (list == null) {
			if (answer.length != 0) {
				return false;
			}
		} else if (answer.length == list.size()) {
			for (CourseInfo courseInfo : answer) {
				if (!list.contains(courseInfo)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void tearDown() {
		super.tearDown();
		if (success.get() == getThreadNum() * getLoopTime()) {
			System.out.println("TestForQueryCourseByTime: Passed");
		} else {
			System.err.println("TestForQueryCourseByTime: Failed");
		}
	}

}