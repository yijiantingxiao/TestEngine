package testcase;

public class TestExample extends TestCase {

	public TestExample(int threadNum) {
		super(threadNum);
	}

	@Override
	public void run() {
		EasyClient client = new EasyClient("localhost:8080/EasyServer");
		client.addSchoolInfo(null);
	}

}
