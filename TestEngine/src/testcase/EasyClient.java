package testcase;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import framework.RandomGenerator;

public class EasyClient {

	private String host;
	
	public EasyClient(String serverAddress){
		this.host = "http://" + serverAddress;
	}
	
	public void addSchoolInfo(JSONObject schoolInfo){
		String uri = host + "/addSchoolInfo";
		if (schoolInfo == null) {
			schoolInfo = RandomGenerator.randomSchoolInfo();
		}
		System.out.println("addSchoolInfo: " + schoolInfo + ":");
		
		try {
			JSONObject result = sendRequest(uri, schoolInfo.toString());
			if (result != null) {
				boolean success = result.getBoolean("success");
				String failReason = result.getString("failReason");
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addCourseInfo(JSONObject course){
		String uri = host + "/addCourseInfo";
		if (course == null) {
			course = RandomGenerator.randomCourseInfo();
		}
		System.out.println("addCourseInfo: " + course + ":");
		
		try {
			JSONObject result = sendRequest(uri, course.toString());
			if (result != null) {
				boolean success = result.getBoolean("success");
				String failReason = result.getString("failReason");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addStudentInfo(JSONObject studentInfo) {
		String uri = host + "/addStudentInfo";
		if (studentInfo == null) {
			studentInfo = RandomGenerator.randomStudentInfo();
		}
		System.out.println("addStudentInfo: " + studentInfo + ":");
		
		try {
			JSONObject result = sendRequest(uri, studentInfo.toString());
			if (result != null) {
				boolean success = result.getBoolean("success");
				String failReason = result.getString("failReason");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static JSONObject sendRequest(String uri, String content) throws IOException {
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
