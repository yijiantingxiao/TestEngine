package framework;

import java.util.Random;
import java.util.UUID;

import beans.CourseInfo;
import beans.SchoolInfo;
import beans.StudentInfo;
import beans.Time;
import net.sf.json.JSONObject;

public class RandomGenerator {
	private static Random random = new Random();

	public static JSONObject randomSchoolInfo() {
		return JSONObject.fromObject(getRandomSchoolInfo());
	}

	public static SchoolInfo getRandomSchoolInfo() {
		return new SchoolInfo(UUID.randomUUID().toString(), random.nextInt(50) + 1);
	}
	
	public static JSONObject randomCourseInfo(String schoolName) {
		CourseInfo courseInfo = getRandomCourseInfo(schoolName);
		JSONObject course = new JSONObject();
		course.accumulate("course", courseInfo);
		return course;
	}

	public static CourseInfo getRandomCourseInfo(String schoolName) {
		CourseInfo courseInfo = new CourseInfo();
		courseInfo.setCourseId(UUID.randomUUID().toString());
		if (schoolName != null) {
			courseInfo.setSchoolName(schoolName);
		} else {
			courseInfo.setSchoolName(UUID.randomUUID().toString());
		}
		courseInfo.setCourseName(UUID.randomUUID().toString());
		courseInfo.setTeacherName(UUID.randomUUID().toString());
		courseInfo.setCredit(random.nextInt(4) + 1);
		courseInfo.setLocation(UUID.randomUUID().toString());
		courseInfo.setTime(getRandomTime());
		courseInfo.setCapacity(random.nextInt(100) + 1);
		return courseInfo;
	}

	public static Time getRandomTime() {
		return new Time(random.nextInt(5) + 1, random.nextInt(5) + 1);
	}
	
	public static JSONObject randomStudentInfo(String schoolName) {
		return JSONObject.fromObject(getRandomStudentInfo(schoolName));
	}

	public static StudentInfo getRandomStudentInfo(String schoolName) {
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setStudentId(UUID.randomUUID().toString());
		studentInfo.setName(UUID.randomUUID().toString());
		studentInfo.setGender(random.nextDouble() < 0.5 ? "男" : "女");
		if (schoolName != null) {
			studentInfo.setSchoolName(schoolName);
		} else {
			studentInfo.setSchoolName(UUID.randomUUID().toString());
		}
		return studentInfo;
	}
	
}
