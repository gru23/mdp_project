package enums;

public enum AccountStatus {
	APPROVED("Approved"), 
	BLOCKED("Blocked"), 
	UNAPPROVED("Unapproved");
	
	private final String displayName;
	
	private AccountStatus(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}