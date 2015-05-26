package testcase;

public abstract class TestCase {
	
	protected int threadNum;

	public abstract void setUp();
	
	public abstract void run();
	
	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	
}
