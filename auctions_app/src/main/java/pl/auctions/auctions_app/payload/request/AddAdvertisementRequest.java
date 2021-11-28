package pl.auctions.auctions_app.payload.request;

public class AddAdvertisementRequest {

	private String category;
	private String description;
	private Double instantPrice;
	private String[] photosBlob;
	private int photosLength;
	private Double startingPrice;
	private String state;
	private String title;
	private Long userId;
	
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getInstantPrice() {
		return instantPrice;
	}
	public void setInstantPrice(Double instantPrice) {
		this.instantPrice = instantPrice;
	}

	public Double getStartingPrice() {
		return startingPrice;
	}
	public void setStartingPrice(Double startingPrice) {
		this.startingPrice = startingPrice;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String[] getPhotosBlob() {
		return photosBlob;
	}
	public void setPhotosBlob(String[] photosBlob) {
		this.photosBlob = photosBlob;
	}
	public int getPhotosLength() {
		return photosLength;
	}
	public void setPhotosLength(int photosLength) {
		this.photosLength = photosLength;
	}
	
}
