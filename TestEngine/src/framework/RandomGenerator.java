package framework;

import java.util.UUID;

import beans.CourseInfo;
import beans.SchoolInfo;
import beans.StudentInfo;
import beans.Time;
import net.sf.json.JSONObject;

public class RandomGenerator {

	public static JSONObject randomSchoolInfo() {
		return JSONObject.fromObject(getRandomSchoolInfo());
	}

	public static SchoolInfo getRandomSchoolInfo() {
		return new SchoolInfo(UUID.randomUUID().toString(), 32);
	}
	
	public static JSONObject randomCourseInfo() {
		CourseInfo courseInfo = getRandomCourseInfo();
		JSONObject course = new JSONObject();
		course.accumulate("course", courseInfo);
		return course;
	}

	public static CourseInfo getRandomCourseInfo() {
		CourseInfo courseInfo = new CourseInfo();
		courseInfo.setCourseId(UUID.randomUUID().toString());
		courseInfo.setSchoolName(UUID.randomUUID().toString());
		courseInfo.setCourseName(UUID.randomUUID().toString());
		courseInfo.setTeacherName(UUID.randomUUID().toString());
		courseInfo.setCredit(2);
		courseInfo.setLocation(UUID.randomUUID().toString());
		courseInfo.setTime(new Time(1, 1));
		courseInfo.setCapacity(50);
		return courseInfo;
	}
	
	public static JSONObject randomStudentInfo() {
		return JSONObject.fromObject(getRandomStudentInfo());
	}

	public static StudentInfo getRandomStudentInfo() {
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setStudentId(UUID.randomUUID().toString());
		studentInfo.setName(UUID.randomUUID().toString());
		studentInfo.setGender("男");
		studentInfo.setSchoolName(UUID.randomUUID().toString());
		return studentInfo;
	}
	
}
