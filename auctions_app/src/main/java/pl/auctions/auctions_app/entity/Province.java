package pl.auctions.auctions_app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wojewodztwo")
public class Province {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idWojewodztwa;
	private String nazwa;

	public Long getIdWojewodztwa() {
		return idWojewodztwa;
	}

	public void setIdWojewodztwa(Long idWojewodztwa) {
		this.idWojewodztwa = idWojewodztwa;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
}
