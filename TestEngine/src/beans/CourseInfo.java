package beans;

public class CourseInfo {
	
	private String courseId;
	private String schoolName;
	private String courseName;
	private String teacherName;
	private int credit;
	private String location;
	private Time time;
	private int capacity;
	
	public CourseInfo(){		
	}
	
	public CourseInfo(String courseId, String schoolName, String courseName,
			String teacherName, int credit, String location, Time time,
			int capacity) {
		super();
		this.courseId = courseId;
		this.schoolName = schoolName;
		this.courseName = courseName;
		this.teacherName = teacherName;
		this.credit = credit;
		this.location = location;
		this.time = time;
		this.capacity = capacity;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
}
