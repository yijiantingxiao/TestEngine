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

public class EasyClient {

private String host;
	
	public EasyClient(String serverAddress){
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
		
		try {
			ServerAnswer answer = (ServerAnswer) JSONObject.toBean(sendRequest(uri, jsonSchoolInfo.toString()), ServerAnswer.class);
			if (answer == null || answer.getFailReason() == null) {
				throw new ServerException("addSchoolInfo");
			}
			return answer;
		} catch (Exception e) {
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
		
		try {
			ServerAnswer answer = (ServerAnswer) JSONObject.toBean(sendRequest(uri, jsonCourse.toString()), ServerAnswer.class);
			if (answer == null || answer.getFailReason() == null) {
				throw new ServerException("addCourseInfo");
			}
			return answer;
		} catch (IOException e) {
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
		
		try {
			ServerAnswer answer = (ServerAnswer) JSONObject.toBean(sendRequest(uri, jsonStudentInfo.toString()), ServerAnswer.class);
			if (answer == null || answer.getFailReason() == null) {
				throw new ServerException("addStudentInfo");
			}
			return answer;
		} catch (IOException e) {
			throw new ServerException("addStudentInfo");
		}
	}
	
	public CourseInfo[] queryCourseByTime(Time time) throws IllegalArgumentException, ServerException {
		String uri = host + "/queryCourseByTime";
		if (time == null) {
			throw new IllegalArgumentException("queryCourseByTime: time is null");
		}
		
		JSONObject jsonTime = new JSONObject();
		jsonTime.accumulate("time", time);
		
		try {
			JSONObject jsonCourses = sendRequest(uri, jsonTime.toString());
			CourseInfo[] courses = (CourseInfo[]) JSONArray.toArray(jsonCourses.getJSONArray("courses"), CourseInfo.class);
			if (courses == null) {
				throw new ServerException("queryCourseByTime");
			}
			return courses;
		} catch (Exception e) {
			throw new ServerException("queryCourseByTime");
		}
	}
	
	public CourseInfo queryCourseById(String courseId) throws IllegalArgumentException, ServerException {
		String uri = host + "/queryCourseById";
		if (courseId == null || courseId.equals("")) {
			throw new IllegalArgumentException("queryCourseById: courseId is null or empty");
		} 
		
		JSONObject json = new JSONObject();
		json.accumulate("courseId", courseId);

		try {
			return (CourseInfo) JSONObject.toBean(sendRequest(uri, json.toString()), CourseInfo.class);
		} catch (Exception e) {
			throw new ServerException("queryCourseById");
		}
	}
	
	public CourseInfo[] querySchedule(String studentId) throws IllegalArgumentException, ServerException {
		String uri = host + "/querySchedule";
		if (studentId == null || studentId.equals("")) {
			throw new IllegalArgumentException("querySchedule: studentId is null or empty");
		} 
		
		JSONObject json = new JSONObject();
		json.accumulate("studentId", studentId);

		try {
			JSONObject jsonCourses = sendRequest(uri, json.toString());
			CourseInfo[] schedule = (CourseInfo[]) JSONArray.toArray(jsonCourses.getJSONArray("courses"), CourseInfo.class);
			if (schedule == null) {
				throw new ServerException("querySchedule");
			}
			return schedule;
		} catch (Exception e) {
			throw new ServerException("querySchedule");
		}
	}
	
	public ServerAnswer selectCourse(String courseId, String studentId) throws IllegalArgumentException, ServerException {
		String uri = host + "/selectCourse";
		if (courseId == null || courseId.equals("")) {
			throw new IllegalArgumentException("selectCourse: courseId is null or empty");
		}
		if (studentId == null || studentId.equals("")) {
			throw new IllegalArgumentException("selectCourse: studentId is null or empty");
		}
		
		JSONObject json = new JSONObject();
		json.accumulate("courseId", courseId);
		json.accumulate("studentId", studentId);
		
		try {
			ServerAnswer answer = (ServerAnswer) JSONObject.toBean(sendRequest(uri, json.toString()), ServerAnswer.class);
			if (answer == null || answer.getFailReason() == null) {
				throw new ServerException("selectCourse");
			}
			return answer;
		} catch (Exception e) {
			throw new ServerException("selectCourse");
		}
	}
	
	public ServerAnswer dropCourse(String courseId, String studentId) throws IllegalArgumentException, ServerException {
		String uri = host + "/dropCourse";
		if (courseId == null || courseId.equals("")) {
			throw new IllegalArgumentException("dropCourse: courseId is null or empty");
		}
		if (studentId == null || studentId.equals("")) {
			throw new IllegalArgumentException("dropCourse: studentId is null or empty");
		}
		
		JSONObject json = new JSONObject();
		json.accumulate("courseId", courseId);
		json.accumulate("studentId", studentId);
		
		try {
			ServerAnswer answer = (ServerAnswer) JSONObject.toBean(sendRequest(uri, json.toString()), ServerAnswer.class);
			if (answer == null || answer.getFailReason() == null) {
				throw new ServerException("dropCourse");
			}
			return answer;
		} catch (Exception e) {
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
