package mssmfactory.KafylElYatim.DomainModel.TutorsAdministration;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Regions")
public class Region {
	private int id;
	private String nom;
	
	@Id
	@GeneratedValue(generator = "regionIdGenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "regionIdGenerator", strategy = "increment")
	@Column(name = "ID_REGION")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "NOM_REGION")
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	@Override
	public String toString() {
		return this.getNom();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Region)
			return this.getId() == ((Region)o).getId();
		return false;
	}
}
