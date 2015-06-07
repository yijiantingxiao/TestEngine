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
import framework.RandomGenerator;

public class EasyClient {

private String host;
	
	public EasyClient(String serverAddress){
		this.host = "http://" + serverAddress;
	}
	
	public ServerAnswer addSchoolInfo(SchoolInfo schoolInfo){
		String uri = host + "/addSchoolInfo";
		JSONObject jsonSchoolInfo;
		if (schoolInfo == null) {
			jsonSchoolInfo = RandomGenerator.randomSchoolInfo();
		} else {
			jsonSchoolInfo = JSONObject.fromObject(schoolInfo);
		}
		
		try {
			return (ServerAnswer) JSONObject.toBean(sendRequest(uri, jsonSchoolInfo.toString()), ServerAnswer.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ServerAnswer addCourseInfo(CourseInfo courseInfo){
		String uri = host + "/addCourseInfo";
		JSONObject jsonCourse;
		if (courseInfo == null) {
			jsonCourse = RandomGenerator.randomCourseInfo();
		} else {
			jsonCourse = new JSONObject();
			jsonCourse.accumulate("course", courseInfo);
		}
		
		try {
			return (ServerAnswer) JSONObject.toBean(sendRequest(uri, jsonCourse.toString()), ServerAnswer.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ServerAnswer addStudentInfo(StudentInfo studentInfo) {
		String uri = host + "/addStudentInfo";
		JSONObject jsonStudentInfo;
		if (studentInfo == null) {
			jsonStudentInfo = RandomGenerator.randomStudentInfo();
		} else {
			jsonStudentInfo = JSONObject.fromObject(studentInfo);
		}
		
		try {
			return (ServerAnswer) JSONObject.toBean(sendRequest(uri, jsonStudentInfo.toString()), ServerAnswer.class);
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
		
		try {
			JSONObject jsonCourses = sendRequest(uri, jsonTime.toString());
			return (CourseInfo[]) JSONArray.toArray(jsonCourses.getJSONArray("courses"), CourseInfo.class);
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

		try {
			return (CourseInfo) JSONObject.toBean(sendRequest(uri, json.toString()), CourseInfo.class);
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

		try {
			JSONObject jsonCourses = sendRequest(uri, json.toString());
			return (CourseInfo[]) JSONArray.toArray(jsonCourses.getJSONArray("courses"), CourseInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ServerAnswer selectCourse(String courseId, String studentId) {
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
		
		try {
			return (ServerAnswer) JSONObject.toBean(sendRequest(uri, json.toString()), ServerAnswer.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ServerAnswer dropCourse(String courseId, String studentId) {
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
		
		try {
			return (ServerAnswer) JSONObject.toBean(sendRequest(uri, json.toString()), ServerAnswer.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void clearData() {
		String uri = host + "/clearData";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uri);
		try {
			httpClient.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
		} 
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
