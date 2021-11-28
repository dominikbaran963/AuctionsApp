package pl.auctions.auctions_app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wygrane_aukcje")
public class WinAuctions {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idWygrane;
	private Long idKlienta;
	private Long idAukcji;
	
	public Long getIdWygrane() {
		return idWygrane;
	}
	public void setIdWygrane(Long idWygrane) {
		this.idWygrane = idWygrane;
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
