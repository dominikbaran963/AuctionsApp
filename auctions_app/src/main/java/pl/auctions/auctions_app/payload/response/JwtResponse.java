package pl.auctions.auctions_app.payload.response;
import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long userId;
	private String email;
	private List<String> roles;

	public JwtResponse(String accessToken, Long id, String email, List<String> roles) {
		this.token = accessToken;
		this.userId = id;
		this.email = email;
		this.roles = roles;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}
}
