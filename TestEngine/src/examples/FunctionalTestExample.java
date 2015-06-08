package examples;

import testcase.EasyClientWithOutput;
import beans.CourseInfo;
import beans.SchoolInfo;
import beans.StudentInfo;
import beans.Time;

public class FunctionalTestExample {	
	
	public static void main(String[] args) {
		EasyClientWithOutput client = new EasyClientWithOutput("localhost:8080/EasyServer");

		client.addSchoolInfo(new SchoolInfo("SS", 32));
		// Fail Case
		client.addSchoolInfo(new SchoolInfo("SS", 32));

		client.addStudentInfo(new StudentInfo("16302010001", "Leo", "男", "SS"));
		// Fail Case
		client.addStudentInfo(new StudentInfo("16302010001", "Leo", "男", "SS"));
		client.addStudentInfo(new StudentInfo("16202010001", "Leo", "男", "EE"));

		client.addCourseInfo(new CourseInfo("SS01", "SS", "ICSE", "Zang", 4, "Z2201", new Time(1, 1), 100));
		// Fail Case
		client.addCourseInfo(new CourseInfo("SS01", "SS", "ICSE", "Zang", 4, "Z2201", new Time(1, 1), 100));
		client.addCourseInfo(new CourseInfo("SS02", "SS", "ICSE", "Zang", 4, "Z2201", new Time(1, 1), 100));
		client.addCourseInfo(new CourseInfo("SS03", "SS", "ICSE", "Zang", 4, "Z2203", new Time(1, 1), 100));

	}
	
}
