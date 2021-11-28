package pl.auctions.auctions_app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "zdjecie")
public class Photo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idZdjecia;
	private Long idProduktu;
	private byte[] zdjecie;
	
	public Long getIdZdjecia() {
		return idZdjecia;
	}
	public void setIdZdjecia(Long idZdjecia) {
		this.idZdjecia = idZdjecia;
	}
	public Long getIdProduktu() {
		return idProduktu;
	}
	public void setIdProduktu(Long idProduktu) {
		this.idProduktu = idProduktu;
	}
	public byte[] getZdjecie() {
		return zdjecie;
	}
	public void setZdjecie(byte[] zdjecie) {
		this.zdjecie = zdjecie;
	}
	
}
