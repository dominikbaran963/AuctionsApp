package pl.auctions.auctions_app.payload.request;

public class PasswordChangeRequest {
	
	private Long userId;
	private String oldPassword;
	private String newPassword;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassord) {
		this.newPassword = newPassord;
	}
	
	
}
