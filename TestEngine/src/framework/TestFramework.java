package framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import net.sf.json.JSONObject;
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
			schoolInfo = randomSchoolInfo();
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
			InputStream is = responseEntity.getContent();
			byte[] content = new byte[is.available()];
			is.read(content);
			String s = new String(content);
			
		}  catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}
	
	private JSONObject randomSchoolInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTests(List<TestCase> tests) {
		this.tests = tests;
	}

	public void setServerIp(String serverIp) {
		this.host = "http://" + serverIp;
	}
	
}
