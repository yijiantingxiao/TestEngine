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
			if (result != null) {
				boolean success = result.isSuccess();
				String failReason = result.getFailReason();
				if (success) {
					if (failReason.equals("")) {
						System.out.println("SUCCESS");
					}
				} else if (failReason.equals("院系名称已存在")) {
					System.out.println(failReason);
				} else {
					System.out.println("EXCEPTION");
				}
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
			if (result != null) {
				boolean success = result.isSuccess();
				String failReason = result.getFailReason();
				if (success) {
					if (failReason.equals("")) {
						System.out.println("SUCCESS");
					}
				} else if (failReason.equals("选课号已存在") || failReason.equals("时间地点冲突")
						|| failReason.equals("教师时间冲突")) {
					System.out.println(failReason);
				} else {
					System.out.println("EXCEPTION");
				}
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
			if (result != null) {
				boolean success = result.isSuccess();
				String failReason = result.getFailReason();
				if (success) {
					if (failReason.equals("")) {
						System.out.println("SUCCESS");
					}
				} else if (failReason.equals("学号已存在") || failReason.equals("院系不存在")) {
					System.out.println(failReason);
				} else {
					System.out.println("EXCEPTION");
				}
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public CourseInfo[] queryCourseByTime(Time time) {
		String uri = host + "/queryCourseByTime";
		JSONObject jsonTime;
		if (time == null) {
			System.out.println("queryCourseByTime: time is null");
			return null;
		} else {
			jsonTime = new JSONObject();
			jsonTime.accumulate("time", time);
		}
		System.out.println("queryCourseByTime: " + jsonTime + ":");
		
		try {
			JSONObject jsonCourses = sendRequest(uri, jsonTime.toString());
			System.out.println(jsonCourses);
			CourseInfo[] courses = (CourseInfo[]) JSONArray.toArray(jsonCourses.getJSONArray("courses"), CourseInfo.class);
			return courses;
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
