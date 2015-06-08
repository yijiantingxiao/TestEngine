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

import beans.CourseInfo;
import beans.SchoolInfo;
import beans.ServerAnswer;
import beans.StudentInfo;
import beans.Time;
import exception.ServerException;
import framework.RandomGenerator;

public class EasyClientWithOutput {

	private String host;
	
	public EasyClientWithOutput(String serverAddress){
		this.host = "http://" + serverAddress;
	}
	
	public ServerAnswer addSchoolInfo(SchoolInfo schoolInfo) throws ServerException{
		String uri = host + "/addSchoolInfo";
		JSONObject jsonSchoolInfo;
		if (schoolInfo == null) {
			jsonSchoolInfo = RandomGenerator.randomSchoolInfo();
		} else {
			jsonSchoolInfo = JSONObject.fromObject(schoolInfo);
		}
		System.out.println("addSchoolInfo: " + jsonSchoolInfo + ":");
		
		try {
			ServerAnswer answer = (ServerAnswer) JSONObject.toBean(sendRequest(uri, jsonSchoolInfo.toString()), ServerAnswer.class);
			if (answer == null || answer.getFailReason() == null) {
				System.out.println("Server Error");
				throw new ServerException("addSchoolInfo");
			}
			boolean success = answer.isSuccess();
			String failReason = answer.getFailReason();
			if (success) {
				if (failReason.equals("")) {
					System.out.println("SUCCESS");
				}
			} else if (failReason.equals("院系名称已存在")) {
				System.out.println(failReason);
			} else {
				System.out.println("EXCEPTION");
			}
			return answer;
		} catch (Exception e) {
			System.out.println("Server Error");
			throw new ServerException("addSchoolInfo");
		}
	}
	
	public ServerAnswer addCourseInfo(CourseInfo courseInfo) throws ServerException{
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
			ServerAnswer answer = (ServerAnswer) JSONObject.toBean(sendRequest(uri, jsonCourse.toString()), ServerAnswer.class);
			if (answer == null || answer.getFailReason() == null) {
				System.out.println("Server Error");
				throw new ServerException("addCourseInfo");
			}
			boolean success = answer.isSuccess();
			String failReason = answer.getFailReason();
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
			return answer;
		} catch (IOException e) {
			System.out.println("Server Error");
			throw new ServerException("addCourseInfo");
		}
	}
	
	public ServerAnswer addStudentInfo(StudentInfo studentInfo) throws ServerException {
		String uri = host + "/addStudentInfo";
		JSONObject jsonStudentInfo;
		if (studentInfo == null) {
			jsonStudentInfo = RandomGenerator.randomStudentInfo();
		} else {
			jsonStudentInfo = JSONObject.fromObject(studentInfo);
		}
		System.out.println("addStudentInfo: " + jsonStudentInfo + ":");
		
		try {
			ServerAnswer answer = (ServerAnswer) JSONObject.toBean(sendRequest(uri, jsonStudentInfo.toString()), ServerAnswer.class);
			if (answer == null || answer.getFailReason() == null) {
				System.out.println("Server Error");
				throw new ServerException("addStudentInfo");
			}
			boolean success = answer.isSuccess();
			String failReason = answer.getFailReason();
			if (success) {
				if (failReason.equals("")) {
					System.out.println("SUCCESS");
				}
			} else if (failReason.equals("学号已存在") || failReason.equals("院系不存在")) {
				System.out.println(failReason);
			} else {
				System.out.println("EXCEPTION");
			}
			return answer;
		} catch (IOException e) {
			System.out.println("Server Error");
			throw new ServerException("addStudentInfo");
		}
	}
	
	public CourseInfo[] queryCourseByTime(Time time) throws IllegalArgumentException, ServerException {
		String uri = host + "/queryCourseByTime";
		if (time == null) {
			System.out.println("queryCourseByTime: time is null");
			throw new IllegalArgumentException("queryCourseByTime: time is null");
		}
		
		JSONObject jsonTime = new JSONObject();
		jsonTime.accumulate("time", time);
		System.out.println("queryCourseByTime: " + jsonTime + ":");
		
		try {
			JSONObject jsonCourses = sendRequest(uri, jsonTime.toString());
			CourseInfo[] courses = (CourseInfo[]) JSONArray.toArray(jsonCourses.getJSONArray("courses"), CourseInfo.class);
			if (courses == null) {
				System.out.println("Server Error");
				throw new ServerException("queryCourseByTime");
			}
			System.out.println("\t" + courses.length);
			return courses;
		} catch (Exception e) {
			System.out.println("Server Error");
			throw new ServerException("queryCourseByTime");
		}
	}
	
	public CourseInfo queryCourseById(String courseId) throws IllegalArgumentException, ServerException {
		String uri = host + "/queryCourseById";
		if (courseId == null || courseId.equals("")) {
			System.out.println("queryCourseById: courseId is null or empty");
			throw new IllegalArgumentException("queryCourseById: courseId is null or empty");
		} 
		
		JSONObject json = new JSONObject();
		json.accumulate("courseId", courseId);
		System.out.println("queryCourseById: " + json + ":");

		try {
			CourseInfo courseInfo = (CourseInfo) JSONObject.toBean(sendRequest(uri, json.toString()), CourseInfo.class);
			if (courseInfo != null && courseInfo.getCourseId() != null) {
				System.out.println("\tExist");
			} else {
				System.out.println("\tNot Exist");
			}
			return courseInfo;
		} catch (Exception e) {
			System.out.println("Server Error");
			throw new ServerException("queryCourseById");
		}
	}
	
	public CourseInfo[] querySchedule(String studentId) throws IllegalArgumentException, ServerException {
		String uri = host + "/querySchedule";
		if (studentId == null || studentId.equals("")) {
			System.out.println("querySchedule: studentId is null or empty");
			throw new IllegalArgumentException("querySchedule: studentId is null or empty");
		} 
		
		JSONObject json = new JSONObject();
		json.accumulate("studentId", studentId);
		System.out.println("querySchedule: " + json + ":");

		try {
			JSONObject jsonCourses = sendRequest(uri, json.toString());
			CourseInfo[] schedule = (CourseInfo[]) JSONArray.toArray(jsonCourses.getJSONArray("courses"), CourseInfo.class);
			if (schedule == null) {
				System.out.println("Server Error");
				throw new ServerException("querySchedule");
			}
			System.out.println("\t" + schedule.length);
			return schedule;
		} catch (Exception e) {
			System.out.println("Server Error");
			throw new ServerException("querySchedule");
		}
	}
	
	public ServerAnswer selectCourse(String courseId, String studentId) throws IllegalArgumentException, ServerException {
		String uri = host + "/selectCourse";
		if (courseId == null || courseId.equals("")) {
			System.out.println("selectCourse: courseId is null or empty");
			throw new IllegalArgumentException("selectCourse: courseId is null or empty");
		}
		if (studentId == null || studentId.equals("")) {
			System.out.println("selectCourse: studentId is null or empty");
			throw new IllegalArgumentException("selectCourse: studentId is null or empty");
		}
		
		JSONObject json = new JSONObject();
		json.accumulate("courseId", courseId);
		json.accumulate("studentId", studentId);
		System.out.println("selectCourse: " + json + ":");
		
		try {
			ServerAnswer answer = (ServerAnswer) JSONObject.toBean(sendRequest(uri, json.toString()), ServerAnswer.class);
			if (answer == null || answer.getFailReason() == null) {
				System.out.println("Server Error");
				throw new ServerException("selectCourse");
			}
			boolean success = answer.isSuccess();
			String failReason = answer.getFailReason();
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
			return answer;
		} catch (Exception e) {
			System.out.println("Server Error");
			throw new ServerException("selectCourse");
		}
	}
	
	public ServerAnswer dropCourse(String courseId, String studentId) throws IllegalArgumentException, ServerException {
		String uri = host + "/dropCourse";
		if (courseId == null || courseId.equals("")) {
			System.out.println("dropCourse: courseId is null or empty");
			throw new IllegalArgumentException("dropCourse: courseId is null or empty");
		}
		if (studentId == null || studentId.equals("")) {
			System.out.println("dropCourse: studentId is null or empty");
			throw new IllegalArgumentException("dropCourse: studentId is null or empty");
		}
		
		JSONObject json = new JSONObject();
		json.accumulate("courseId", courseId);
		json.accumulate("studentId", studentId);
		System.out.println("dropCourse: " + json + ":");
		
		try {
			ServerAnswer answer = (ServerAnswer) JSONObject.toBean(sendRequest(uri, json.toString()), ServerAnswer.class);
			if (answer == null || answer.getFailReason() == null) {
				System.out.println("Server Error");
				throw new ServerException("dropCourse");
			}
			boolean success = answer.isSuccess();
			String failReason = answer.getFailReason();
			if (success) {
				if (failReason.equals("")) {
					System.out.println("SUCCESS");
				}
			} else if (failReason.equals("学生不存在") || failReason.equals("课程不存在") || failReason.equals("学生未选课")) {
				System.out.println(failReason);
			} else {
				System.out.println("EXCEPTION");
			}
			return answer;
		} catch (Exception e) {
			System.out.println("Server Error");
			throw new ServerException("dropCourse");
		}
	}
	
	public void clearData() throws ServerException {
		String uri = host + "/clearData";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uri);
		try {
			httpClient.execute(httpPost);
		} catch (Exception e) {
			System.out.println("Server Error");
			throw new ServerException("clearData");
		} 
	}
	
	private JSONObject sendRequest(String uri, String content) throws IOException, ServerException {
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
			throw new ServerException("");
		} finally {
			if (response != null) {
				response.close();
			}
		}
	} 

}
