package framework;

import java.util.UUID;

import beans.CourseInfo;
import beans.SchoolInfo;
import beans.StudentInfo;
import beans.Time;
import net.sf.json.JSONObject;

public class RandomGenerator {

	public static JSONObject randomSchoolInfo() {
		SchoolInfo schoolInfo = new SchoolInfo(UUID.randomUUID().toString(), 32);
		return JSONObject.fromObject(schoolInfo);
	}
	
	public static JSONObject randomCourseInfo() {
		CourseInfo courseInfo = new CourseInfo();
		courseInfo.setCourseId(UUID.randomUUID().toString());
		courseInfo.setSchoolName(UUID.randomUUID().toString());
		courseInfo.setCourseName(UUID.randomUUID().toString());
		courseInfo.setTeacherName(UUID.randomUUID().toString());
		courseInfo.setCredit(2);
		courseInfo.setLocation(UUID.randomUUID().toString());
		courseInfo.setTime(new Time(1, 1));
		courseInfo.setCapacity(50);
		JSONObject course = new JSONObject();
		course.accumulate("course", courseInfo);
		return course;
	}
	
	public static JSONObject randomStudentInfo() {
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setStudentId(UUID.randomUUID().toString());
		studentInfo.setName(UUID.randomUUID().toString());
		studentInfo.setGender("男");
		studentInfo.setSchoolName(UUID.randomUUID().toString());
		return JSONObject.fromObject(studentInfo);
	}
	
	public static void main(String[] args) {
		System.out.println(randomStudentInfo());
	}
}
