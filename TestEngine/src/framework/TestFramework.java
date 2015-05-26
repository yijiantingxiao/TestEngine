package framework;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import testcase.TestCase;

public class TestFramework {
	
	private String host;
	private List<TestCase> tests;
	
	public void setUp() {
		for (TestCase test : tests) {
			test.setUp();
		}
	}
	
	public void run() {		
		for (final TestCase test : tests) {
			int threadNum = test.getThreadNum();
			Runnable run = new Runnable() {				
				@Override
				public void run() {
					test.run();
				}
			};
			for (int i = 0; i < threadNum; i++) {
				Thread thread = new Thread(run);
				thread.start();
			}
		}
	}
	
	public void addSchoolInfo(JSONObject schoolInfo) throws IOException {
		String uri = host + "/addSchoolInfo";
		if (schoolInfo == null) {
			schoolInfo = RandomGenerator.randomSchoolInfo();
		}
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uri);
		StringEntity requestEntity;
		CloseableHttpResponse response = null;
		try {
			requestEntity = new StringEntity(schoolInfo.toString());
			httpPost.setEntity(requestEntity);
			response = httpClient.execute(httpPost);
			
			HttpEntity responseEntity = response.getEntity();
			JSONObject result = JSONObject.fromObject(EntityUtils.toString(responseEntity, "UTF-8"));
			
			boolean success = result.getBoolean("success");
			String failReason = result.getString("failReason:");
			if (success) {
				if (failReason.equals("")) {
					System.out.println("SUCCESS");
				}
			}else if (failReason.equals("院系名称已存在")) {
				System.out.println("DUPLICATE");
			}else {
				System.out.println("EXCEPTION");
			}
		}  catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	public void setTests(List<TestCase> tests) {
		this.tests = tests;
	}

	public void setServerIp(String serverIp) {
		this.host = "http://" + serverIp;
	}
	
	
	public static void main(String[] args) {
		TestFramework framework = new TestFramework();
		framework.setServerIp("localhost:8080/EasyServer");
		try {
			framework.addSchoolInfo(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
