package mssmfactory.KafylElYatim.DomainModel.DonationsAdministration;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Donateurs")
public class Donator {
	private int id;
	private String nom;
	private String prenom;
	
	private List<Donation> donations = new ArrayList<Donation>();
	
	@Id
	@Column(name = "ID_DONATEUR")
	@GenericGenerator(name = "donateurIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "donateurIdGenerator", strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "NOM_DONATION")
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	@Basic
	@Column(name = "PRENOM_DONATEUR")
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	@Basic
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "donator")
	public List<Donation> getDonations() {
		return donations;
	}
	public void setDonations(List<Donation> donations) {
		this.donations = donations;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Donator && this.getId() == (((Donator) o).getId());
	}
	
	@Override
	public String toString() {
		return this.getNom() + " " + this.getPrenom();
	}
}
