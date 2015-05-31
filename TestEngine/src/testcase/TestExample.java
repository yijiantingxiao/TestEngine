package testcase;

public class TestExample extends TestCase {

	protected TestExample(String serverAddress, int threadNum) {
		super(serverAddress, threadNum);
	}

	@Override
	public void run() {
		getClient().addSchoolInfo(null);
	}

}
