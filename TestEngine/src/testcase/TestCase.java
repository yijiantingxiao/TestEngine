package testcase;

public abstract class TestCase {
	
	private EasyClient client;
	private int threadNum;
	
	protected TestCase(String serverAddress, int threadNum){
		client = new EasyClient(serverAddress);
		this.threadNum = threadNum;
	}

	public void setUp(){
	}
	
	public abstract void run();
	
	public void tearDown() {
	}

	public EasyClient getClient() {
		return client;
	}
	
	public int getThreadNum() {
		return threadNum;
	}

}
