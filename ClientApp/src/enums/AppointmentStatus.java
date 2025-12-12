package enums;

public enum AppointmentStatus {
	CONFIRMED("Confirmed"), 
	REJECTED("Rejected"), 
	WAITING("Waiting"), 
	REPAIRED("Repaired");
	
	private final String displayName;
	
	private AppointmentStatus(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
        return displayName;
    }
}
