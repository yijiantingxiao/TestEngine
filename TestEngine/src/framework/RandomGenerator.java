package framework;

import net.sf.json.JSONObject;

public class RandomGenerator {

	public static JSONObject randomSchoolInfo() {
		JSONObject schoolInfo = new JSONObject();
		schoolInfo.accumulate("schoolName", "SE");
		schoolInfo.accumulate("creditRequirement", 32);
		return schoolInfo;
	}
}
