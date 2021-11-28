package pl.auctions.auctions_app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "klient")
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idKlienta;
	private String miejscowosc;
	private Long idWojewodztwa;
	private String nrTelefonu;
	private String czyAktywny;
	@OneToOne
	@JoinColumn(name="idKlienta", referencedColumnName="user_id")
	private User user;
	@OneToOne
	@JoinColumn(name = "idWojewodztwa", referencedColumnName = "idWojewodztwa" , insertable=false, updatable=false)
	private Province wojewodztwo;

	public Long getIdKlienta() {
		return idKlienta;
	}

	public Province getWojewodztwo() {
		return wojewodztwo;
	}

	public void setWojewodztwo(Province wojewodztwo) {
		this.wojewodztwo = wojewodztwo;
	}

	public void setIdKlienta(Long idKlienta) {
		this.idKlienta = idKlienta;
	}

	public String getMiejscowosc() {
		return miejscowosc;
	}

	public void setMiejscowosc(String miejscowosc) {
		this.miejscowosc = miejscowosc;
	}

	public Long getIdWojewodztwa() {
		return idWojewodztwa;
	}

	public void setIdWojewodztwa(Long idWojewodztwa) {
		this.idWojewodztwa = idWojewodztwa;
	}

	public String getNrTelefonu() {
		return nrTelefonu;
	}

	public void setNrTelefonu(String nrTelefonu) {
		this.nrTelefonu = nrTelefonu;
	}

	public String getCzyAktywny() {
		return czyAktywny;
	}

	public void setCzyAktywny(String czyAktywny) {
		this.czyAktywny = czyAktywny;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}

enum Aktywny {
	tak, nie;
}