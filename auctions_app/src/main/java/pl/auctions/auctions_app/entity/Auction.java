package pl.auctions.auctions_app.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "aukcja")
public class Auction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idAukcji;
	private Long idProduktu;
	private String nazwaAukcji;
	private Integer idStanuAukcji;
	private Date dataRozpoczecia;
	private Date dataZakonczenia;
	private Double cenaBlyskawiczna;
	private Double cenaWywolawcza;
	@OneToOne
	@JoinColumn(name = "idProduktu", referencedColumnName = "idProduktu" , insertable=false, updatable=false)
	private Product product;
	@OneToOne
	@JoinColumn(name = "idAukcji", referencedColumnName = "idAukcji" , insertable=false, updatable=false)
	private ClientAuction clientAuction;
	
	@OneToOne
	@JoinColumn(name = "idStanuAukcji", referencedColumnName = "idStanuAukcji" , insertable=false, updatable=false)
	private AuctionStatus auctionStatus;
	
	
	public Long getIdAukcji() {
		return idAukcji;
	}
	public void setIdAukcji(Long idAukcji) {
		this.idAukcji = idAukcji;
	}
	public Long getIdProduktu() {
		return idProduktu;
	}
	public void setIdProduktu(Long idProduktu) {
		this.idProduktu = idProduktu;
	}
	public Integer getIdStanuAukcji() {
		return idStanuAukcji;
	}
	public void setIdStanuAukcji(Integer idStanuAukcji) {
		this.idStanuAukcji = idStanuAukcji;
	}
	public String getNazwaAukcji() {
		return nazwaAukcji;
	}
	public void setNazwaAukcji(String nazwaAukcji) {
		this.nazwaAukcji = nazwaAukcji;
	}
	public Date getDataRozpoczecia() {
		return dataRozpoczecia;
	}
	public void setDataRozpoczecia(Date dataRozpoczecia) {
		this.dataRozpoczecia = dataRozpoczecia;
	}
	public Date getDataZakonczenia() {
		return dataZakonczenia;
	}
	public void setDataZakonczenia(Date dataZakonczenia) {
		this.dataZakonczenia = dataZakonczenia;
	}
	public Double getCenaBlyskawiczna() {
		return cenaBlyskawiczna;
	}
	public void setCenaBlyskawiczna(Double cenaBlyskawiczna) {
		this.cenaBlyskawiczna = cenaBlyskawiczna;
	}
	public Double getCenaWywolawcza() {
		return cenaWywolawcza;
	}
	public void setCenaWywolawcza(Double cenaWywolawcza) {
		this.cenaWywolawcza = cenaWywolawcza;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public ClientAuction getClientAuction() {
		return clientAuction;
	}
	public void setClientAuction(ClientAuction clientAuction) {
		this.clientAuction = clientAuction;
	}
	public AuctionStatus getAuctionStatus() {
		return auctionStatus;
	}
	public void setAuctionStatus(AuctionStatus auctionStatus) {
		this.auctionStatus = auctionStatus;
	}
}
