 package mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration;

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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Benevoles")
public class Benevole {
	
	private int id;
	private String nom;
	private String prenom;
	private Genre genre;
	private String telephone;
	private String email;
	private String facebook;
	private List<JourneeDisponible> journeesDisponibles = new ArrayList<JourneeDisponible>();
	private List<Vehicule> vehicules = new ArrayList<Vehicule>();
	
	@Id
	@Column(name = "ID_BENEVOLE")
	@GenericGenerator(name = "benevoleIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "benevoleIdGenerator", strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "NOM_BENEVOLE")
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	@Basic
	@Column(name = "PRENOM_BENEVOLE")
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	@Basic
	@Column(name = "GENRE")
	public Genre getGenre() {
		return genre;
	}
	public void setGenre(Genre genre) {
		this.genre = genre;
	}
	
	@Basic
	@Column(name = "TELEPHONE")
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	@Basic
	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Basic
	@Column(name = "FACEBOOK")
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	@Basic
	@OneToMany(mappedBy = "benevole", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<JourneeDisponible> getJourneesDisponibles() {
		return journeesDisponibles;
	}
	public void setJourneesDisponibles(List<JourneeDisponible> journeesDisponibles) {
		this.journeesDisponibles = journeesDisponibles;
	}
	
	public void addJourneeDisponible(JourneeDisponible journeeDisponible) {
		this.getJourneesDisponibles().add(journeeDisponible);
		journeeDisponible.setBenevole(this);
	}
	
	public void removeJourneeDisponible(JourneeDisponible journeeDisponible) {
		this.getJourneesDisponibles().remove(journeeDisponible);
		journeeDisponible.setBenevole(null);
	}
	
	@Basic
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "Benevoles_Vehicules",
		joinColumns = @JoinColumn(name = "ID_BENEVOLE"),
		inverseJoinColumns = @JoinColumn(name = "ID_VEHICULE")
	)
	public List<Vehicule> getVehicules() {
		return vehicules;
	}
	public void setVehicules(List<Vehicule> vehicules) {
		this.vehicules = vehicules;
	}
	
	public void addVehicule(Vehicule vehicule) {
		this.getVehicules().add(vehicule);
	}
	
	public void removeVehicule(Vehicule vehicule) {
		this.getVehicules().remove(vehicule);
	}

	public static enum Genre{
		Masculin,
		Feminin
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Benevole)
			return this.getId() == (((Benevole) o).getId());
		return false;
	}
	
	@Override
	public String toString() {
		return this.getNom() + " " + this.getPrenom();
	}
}
