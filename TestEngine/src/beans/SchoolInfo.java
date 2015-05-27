package beans;

public class SchoolInfo{

	private String schoolName;
	private int creditRequirement;
	
	public SchoolInfo(){	
	}
	
	public SchoolInfo(String schoolName, int creditRequirement) {
		this.schoolName = schoolName;
		this.creditRequirement = creditRequirement;
	}
	
	public String getSchoolName() {
		return schoolName;
	}
	
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
	public int getCreditRequirement() {
		return creditRequirement;
	}
	
	public void setCreditRequirement(int creditRequirement) {
		this.creditRequirement = creditRequirement;
	}
	
}
