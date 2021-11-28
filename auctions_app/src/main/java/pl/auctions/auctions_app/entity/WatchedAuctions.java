package pl.auctions.auctions_app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "obserwowane_aukcje")
public class WatchedAuctions {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idObserwowane;
	private Long idKlienta;
	private Long idAukcji;
	
	public Long getIdObserwowane() {
		return idObserwowane;
	}
	public void setIdObserwowane(Long idObserwowane) {
		this.idObserwowane = idObserwowane;
	}
	public Long getIdKlienta() {
		return idKlienta;
	}
	public void setIdKlienta(Long idKlienta) {
		this.idKlienta = idKlienta;
	}
	public Long getIdAukcji() {
		return idAukcji;
	}
	public void setIdAukcji(Long idAukcji) {
		this.idAukcji = idAukcji;
	}
}
