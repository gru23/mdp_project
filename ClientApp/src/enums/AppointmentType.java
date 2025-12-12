package enums;

public enum AppointmentType {
	REGULAR_SERVICE("Regular service"),
    REPAIR("Repair");
	
	private final String displayName;
	
	AppointmentType(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
        return displayName;
    }
}
