package pl.auctions.auctions_app.payload.request;

public class AuctionBuyRequest {
	
	private Long auctionId;
	private Long userId;
	
	public Long getAuctionId() {
		return auctionId;
	}
	public void setAuctionId(Long auctionId) {
		this.auctionId = auctionId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
