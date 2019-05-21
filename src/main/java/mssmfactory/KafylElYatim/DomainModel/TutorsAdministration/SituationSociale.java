package mssmfactory.KafylElYatim.DomainModel.TutorsAdministration;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SituationsSociales")
public class SituationSociale {
	private int id;
	private NiveauVie niveauVie;
	private String fonction;
	private double retraite;
	private double convertureSociale;
	private double salaire;
	private String niveauEtude;
	private String competances;
	private Enquete enquete;
	private Tuteur tuteur;
	
	@Id
	@GeneratedValue(generator = "situationSocialeIdGenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "situationSocialeIdGenerator", strategy = "increment")
	@Column(name = "ID_SITUATION_SOCIALE")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "NIVEAU_VIE")
	public NiveauVie getNiveauVie() {
		return niveauVie;
	}
	public void setNiveauVie(NiveauVie niveauVie) {
		this.niveauVie = niveauVie;
	}
	
	@Basic
	@Column(name = "FONCTION")
	public String getFonction() {
		return fonction;
	}
	public void setFonction(String fonction) {
		this.fonction = fonction;
	}
	
	@Basic
	@Column(name = "RETRAIRE")
	public double getRetraite() {
		return retraite;
	}
	public void setRetraite(double retraite) {
		this.retraite = retraite;
	}
	
	@Basic
	@Column(name = "CONVERTURE_SOCIALE")
	public double getConvertureSociale() {
		return convertureSociale;
	}
	public void setConvertureSociale(double convertureSociale) {
		this.convertureSociale = convertureSociale;
	}
	
	@Basic
	@Column(name = "SALAIRE")
	public double getSalaire() {
		return salaire;
	}
	public void setSalaire(double salaire) {
		this.salaire = salaire;
	}
	
	@Basic
	@Column(name = "NIVEAU_ETUDE")
	public String getNiveauEtude() {
		return niveauEtude;
	}
	public void setNiveauEtude(String niveauEtude) {
		this.niveauEtude = niveauEtude;
	}
	
	@Basic
	@Column(name = "COMPETANCES")
	public String getCompetances() {
		return competances;
	}
	public void setCompetances(String competances) {
		this.competances = competances;
	}
	
	@Basic
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "situationSociale", fetch = FetchType.LAZY)
	public Enquete getEnquete() {
		return enquete;
	}
	public void setEnquete(Enquete enquete) {
		this.enquete = enquete;
	}
	
	@Basic
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TUTEUR")
	public Tuteur getTuteur() {
		return tuteur;
	}
	public void setTuteur(Tuteur tuteur) {
		this.tuteur = tuteur;
	}
	
	public static enum NiveauVie {
		Bon,
		Moyen,
		Critique
	}
}
