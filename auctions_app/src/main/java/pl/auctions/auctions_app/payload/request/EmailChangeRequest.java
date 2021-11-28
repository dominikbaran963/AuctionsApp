package pl.auctions.auctions_app.payload.request;

public class EmailChangeRequest {

		private Long userId;
		private String oldEmail;
		private String newEmail;
		
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public String getOldEmail() {
			return oldEmail;
		}
		public void setOldEmail(String oldEmail) {
			this.oldEmail = oldEmail;
		}
		public String getNewEmail() {
			return newEmail;
		}
		public void setNewEmail(String newEmail) {
			this.newEmail = newEmail;
		}
}
