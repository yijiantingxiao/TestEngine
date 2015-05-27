package testcase;

public abstract class TestCase {
	
	private int threadNum;
	
	protected TestCase(int threadNum){
		this.threadNum = threadNum;
	}

	public void setUp(){
	}
	
	public abstract void run();
	
	public void tearDown() {
	}
	
	public int getThreadNum() {
		return threadNum;
	}

}
