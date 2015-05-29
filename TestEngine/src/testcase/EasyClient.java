package testcase;

import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import beans.AddResponse;
import beans.CourseInfo;
import beans.SchoolInfo;
import beans.StudentInfo;
import beans.Time;
import framework.RandomGenerator;

public class EasyClient {

	private String host;
	
	public EasyClient(String serverAddress){
		this.host = "http://" + serverAddress;
	}
	
	public AddResponse addSchoolInfo(SchoolInfo schoolInfo){
		String uri = host + "/addSchoolInfo";
		JSONObject jsonSchoolInfo;
		if (schoolInfo == null) {
			jsonSchoolInfo = RandomGenerator.randomSchoolInfo();
		} else {
			jsonSchoolInfo = JSONObject.fromObject(schoolInfo);
		}
		System.out.println("addSchoolInfo: " + jsonSchoolInfo + ":");
		
		try {
			AddResponse result = (AddResponse) JSONObject.toBean(sendRequest(uri, jsonSchoolInfo.toString()), AddResponse.class);
			boolean success = result.isSuccess();
			String failReason = result.getFailReason();
			if (failReason == null) {
				System.out.println("Server Error");
				return null;
			}
			if (success) {
				if (failReason.equals("")) {
					System.out.println("SUCCESS");
				}
			} else if (failReason.equals("院系名称已存在")) {
				System.out.println(failReason);
			} else {
				System.out.println("EXCEPTION");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public AddResponse addCourseInfo(CourseInfo courseInfo){
		String uri = host + "/addCourseInfo";
		JSONObject jsonCourse;
		if (courseInfo == null) {
			jsonCourse = RandomGenerator.randomCourseInfo();
		} else {
			jsonCourse = new JSONObject();
			jsonCourse.accumulate("course", courseInfo);
		}
		System.out.println("addCourseInfo: " + jsonCourse + ":");
		
		try {
			AddResponse result = (AddResponse) JSONObject.toBean(sendRequest(uri, jsonCourse.toString()), AddResponse.class);
			boolean success = result.isSuccess();
			String failReason = result.getFailReason();
			if (failReason == null) {
				System.out.println("Server Error");
				return null;
			}
			if (success) {
				if (failReason.equals("")) {
					System.out.println("SUCCESS");
				}
			} else if (failReason.equals("选课号已存在")
					|| failReason.equals("时间地点冲突")
					|| failReason.equals("教师时间冲突")) {
				System.out.println(failReason);
			} else {
				System.out.println("EXCEPTION");
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public AddResponse addStudentInfo(StudentInfo studentInfo) {
		String uri = host + "/addStudentInfo";
		JSONObject jsonStudentInfo;
		if (studentInfo == null) {
			jsonStudentInfo = RandomGenerator.randomStudentInfo();
		} else {
			jsonStudentInfo = JSONObject.fromObject(studentInfo);
		}
		System.out.println("addStudentInfo: " + jsonStudentInfo + ":");
		
		try {
			AddResponse result = (AddResponse) JSONObject.toBean(sendRequest(uri, jsonStudentInfo.toString()), AddResponse.class);
			boolean success = result.isSuccess();
			String failReason = result.getFailReason();
			if (failReason == null) {
				System.out.println("Server Error");
				return null;
			}
			if (success) {
				if (failReason.equals("")) {
					System.out.println("SUCCESS");
				}
			} else if (failReason.equals("学号已存在") || failReason.equals("院系不存在")) {
				System.out.println(failReason);
			} else {
				System.out.println("EXCEPTION");
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public CourseInfo[] queryCourseByTime(Time time) {
		String uri = host + "/queryCourseByTime";
		if (time == null) {
			System.out.println("queryCourseByTime: time is null");
			return null;
		}
		
		JSONObject jsonTime = new JSONObject();
		jsonTime.accumulate("time", time);
		System.out.println("queryCourseByTime: " + jsonTime + ":");
		
		try {
			JSONObject jsonCourses = sendRequest(uri, jsonTime.toString());
			CourseInfo[] courses = (CourseInfo[]) JSONArray.toArray(jsonCourses.getJSONArray("courses"), CourseInfo.class);
			System.out.println("\t" + courses.length);
			return courses;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public CourseInfo queryCourseById(String courseId) {
		String uri = host + "/queryCourseById";
		if (courseId == null || courseId.equals("")) {
			System.out.println("queryCourseById: courseId is null or empty");
			return null;
		} 
		
		JSONObject json = new JSONObject();
		json.accumulate("courseId", courseId);
		System.out.println("queryCourseById: " + json + ":");

		try {
			CourseInfo courseInfo = (CourseInfo) JSONObject.toBean(sendRequest(uri, json.toString()), CourseInfo.class);
			if (courseInfo.getCourseId() != null) {
				System.out.println("\tExist");
			} else {
				System.out.println("\tNot Exist");
			}
			return courseInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	public CourseInfo[] querySchedule(String studentId) {
		String uri = host + "/querySchedule";
		if (studentId == null || studentId.equals("")) {
			System.out.println("querySchedule: studentId is null or empty");
			return null;
		} 
		
		JSONObject json = new JSONObject();
		json.accumulate("studentId", studentId);
		System.out.println("querySchedule: " + json + ":");

		try {
			JSONObject jsonCourses = sendRequest(uri, json.toString());
			CourseInfo[] courses = (CourseInfo[]) JSONArray.toArray(jsonCourses.getJSONArray("courses"), CourseInfo.class);
			System.out.println("\t" + courses.length);
			return courses;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public AddResponse selectCourse(String courseId, String studentId) {
		String uri = host + "/selectCourse";
		if (courseId == null || courseId.equals("")) {
			System.out.println("selectCourse: courseId is null or empty");
			return null;
		}
		if (studentId == null || studentId.equals("")) {
			System.out.println("selectCourse: studentId is null or empty");
			return null;
		}
		
		JSONObject json = new JSONObject();
		json.accumulate("courseId", courseId);
		json.accumulate("studentId:", studentId);
		System.out.println("selectCourse: " + json + ":");
		
		try {
			AddResponse result = (AddResponse) JSONObject.toBean(sendRequest(uri, json.toString()), AddResponse.class);
			boolean success = result.isSuccess();
			String failReason = result.getFailReason();
			if (failReason == null) {
				System.out.println("Server Error");
				return null;
			}
			if (success) {
				if (failReason.equals("")) {
					System.out.println("SUCCESS");
				}
			} else if (failReason.equals("学生不存在") || failReason.equals("课程不存在")
					|| failReason.equals("学分已满")
					|| failReason.equals("选课时间地点冲突")
					|| failReason.equals("选课人数已满")) {
				System.out.println(failReason);
			} else {
				System.out.println("EXCEPTION");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public AddResponse dropCourse(String courseId, String studentId) {
		String uri = host + "/dropCourse";
		if (courseId == null || courseId.equals("")) {
			System.out.println("dropCourse: courseId is null or empty");
			return null;
		}
		if (studentId == null || studentId.equals("")) {
			System.out.println("dropCourse: studentId is null or empty");
			return null;
		}
		
		JSONObject json = new JSONObject();
		json.accumulate("courseId", courseId);
		json.accumulate("studentId", studentId);
		System.out.println("dropCourse: " + json + ":");
		
		try {
			AddResponse result = (AddResponse) JSONObject.toBean(sendRequest(uri, json.toString()), AddResponse.class);
			boolean success = result.isSuccess();
			String failReason = result.getFailReason();
			if (failReason == null) {
				System.out.println("Server Error");
				return null;
			}
			if (success) {
				if (failReason.equals("")) {
					System.out.println("SUCCESS");
				}
			} else if (failReason.equals("学生不存在") || failReason.equals("课程不存在") || failReason.equals("学生未选课")) {
				System.out.println(failReason);
			} else {
				System.out.println("EXCEPTION");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private JSONObject sendRequest(String uri, String content) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uri);
		CloseableHttpResponse response = null;
		try {
			StringEntity requestEntity = new StringEntity(content);
			httpPost.setEntity(requestEntity);
			response = httpClient.execute(httpPost);
			
			HttpEntity responseEntity = response.getEntity();
			return JSONObject.fromObject(EntityUtils.toString(responseEntity, "UTF-8"));
		} catch (Exception e){
			e.printStackTrace();
			return null;
		} finally {
			if (response != null) {
				response.close();
			}
		}
	} 

}
