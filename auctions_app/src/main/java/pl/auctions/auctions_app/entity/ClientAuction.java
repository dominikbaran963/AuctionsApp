package pl.auctions.auctions_app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "klient_aukcja")
public class ClientAuction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idKlientAukcja;
	private Long idKlienta;
	private Long idAukcji;
	@ManyToOne
	@JoinColumn(name = "idKlienta", referencedColumnName = "idKlienta" , insertable=false, updatable=false)
	private Client client;
	
	public Long getIdKlientAukcja() {
		return idKlientAukcja;
	}
	public void setIdKlientAukcja(Long idKlientAukcja) {
		this.idKlientAukcja = idKlientAukcja;
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
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
}
