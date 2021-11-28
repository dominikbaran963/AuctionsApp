package pl.auctions.auctions_app.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "produkt")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idProduktu;
	private Long idKategorii;
	private String opis;
	private String nazwa;
	private String stan;
	@OneToOne
	@JoinColumn(name = "idKategorii", referencedColumnName = "idKategorii" , insertable=false, updatable=false)
	private Category category;
	
	@OneToMany
	@JoinColumn(name = "idProduktu", referencedColumnName = "idProduktu" , insertable=false, updatable=false)
	private List<Photo> photos = new ArrayList<Photo>();
	
	public Long getIdProduktu() {
		return idProduktu;
	}
	public void setIdProduktu(Long idProduktu) {
		this.idProduktu = idProduktu;
	}
	public Long getIdKategorii() {
		return idKategorii;
	}
	public void setIdKategorii(Long idKategorii) {
		this.idKategorii = idKategorii;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	public String getStan() {
		return stan;
	}
	public void setStan(String stan) {
		this.stan = stan;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public List<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
}
enum Stan {
	nowy, używany;
}