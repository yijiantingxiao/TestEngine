package beans;

public class Time {

	private int weekday;
	private int period;
	
	public Time(){
	}
	
	public Time(int weekday, int period) {
		super();
		this.weekday = weekday;
		this.period = period;
	}

	public int getWeekday() {
		return weekday;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + period;
		result = prime * result + weekday;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Time other = (Time) obj;
		if (period != other.period)
			return false;
		if (weekday != other.weekday)
			return false;
		return true;
	}
	
}
