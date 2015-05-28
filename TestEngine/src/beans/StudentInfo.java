package beans;

public class StudentInfo {
	
	private String studentId;
	private String name;
	private String gender;
	private String schoolName;
		
	public StudentInfo() {
	}

	public StudentInfo(String studentId, String name, String gender,
			String schoolName) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.gender = gender;
		this.schoolName = schoolName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
}
