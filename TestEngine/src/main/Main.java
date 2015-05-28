package main;

import testcase.EasyClient;
import beans.Time;

public class Main {	
	
	public static void main(String[] args) {
		EasyClient client = new EasyClient("localhost:8080/EasyServer");
		client.queryCourseByTime(new Time(1, 1));
	}
	
}
