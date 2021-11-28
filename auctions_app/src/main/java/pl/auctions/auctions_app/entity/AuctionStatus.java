package pl.auctions.auctions_app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stan_aukcji")
public class AuctionStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idStanuAukcji;
	private String stan;
	
	public Long getIdStanuAukcji() {
		return idStanuAukcji;
	}
	public void setIdStanuAukcji(Long idStanuAukcji) {
		this.idStanuAukcji = idStanuAukcji;
	}
	public String getStan() {
		return stan;
	}
	public void setStan(String stan) {
		this.stan = stan;
	}
}
