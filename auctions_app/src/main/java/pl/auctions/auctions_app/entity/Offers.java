package pl.auctions.auctions_app.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oferty")
public class Offers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idOferty;
	private Long idAukcji;
	private Long idKlienta;
	private Date data;
	private Double kwota;
	
	public Long getIdOferty() {
		return idOferty;
	}
	public void setIdOferty(Long idOferty) {
		this.idOferty = idOferty;
	}
	public Long getIdAukcji() {
		return idAukcji;
	}
	public void setIdAukcji(Long idAukcji) {
		this.idAukcji = idAukcji;
	}
	public Long getIdKlienta() {
		return idKlienta;
	}
	public void setIdKlienta(Long idKlienta) {
		this.idKlienta = idKlienta;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Double getKwota() {
		return kwota;
	}
	public void setKwota(Double kwota) {
		this.kwota = kwota;
	}
}
