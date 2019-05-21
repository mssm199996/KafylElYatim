package mssmfactory.KafylElYatim.DomainModel.OrphensAdministration;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;

@Entity
@Table(name = "Orphelins")
public class Orphelin {
	
	private int id;
	private String nom;
	private String prenom;
	private LocalDate ddn;
	private Genre genre;
	private String obsevation;
	private Tuteur tuteur;
	private String relationOrphelinTuteur;
	private DossierFamilial dossierFamilial;
	private DossierScolaire dossierScolaire;
	private DossierMedical dossierMedical;
	private ApparencePhysique apparencePhysique;
	private boolean authorized;
			
	@Id
	@GeneratedValue(generator = "orphelinIdGenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "orphelinIdGenerator", strategy = "increment")
	@Column(name = "ID_ORPHELIN")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "NOM_ORPHELIN")
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	@Basic
	@Column(name = "PRENOM_ORPHELIN")
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	@Basic
	@Column(name = "DDN_ORPHELIN")
	public LocalDate getDdn() {
		return ddn;
	}
	public void setDdn(LocalDate ddn) {
		this.ddn = ddn;
	}
	
	@Transient
	public int getAge() {
		LocalDate intermediaire = 
				((LocalDate.now().minusYears(this.getDdn().getYear()))
							     .minusMonths(this.getDdn().getMonthValue() - 1))
							     .minusDays(this.getDdn().getDayOfMonth() - 1);		
		return intermediaire.getYear();
	}
	
	@Basic
	@Column(name = "GENRE_ORPHELIN")
	public Genre getGenre() {
		return genre;
	}
	public void setGenre(Genre genre) {
		this.genre = genre;
	}
	
	@Basic
	@Column(name = "OBSERVATION_ORPHELIN")
	public String getObsevation() {
		return obsevation;
	}
	public void setObsevation(String obsevation) {
		this.obsevation = obsevation;
	}
	
	@Basic(optional = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TUTEUR")
	public Tuteur getTuteur() {
		return tuteur;
	}
	public void setTuteur(Tuteur tuteur) {
		this.tuteur = tuteur;
	}
	
	@Basic
	@Column(name = "RELATION_TUTEUR")
	public String getRelationOrphelinTuteur() {
		return relationOrphelinTuteur;
	}
	public void setRelationOrphelinTuteur(String relationOrphelinTuteur) {
		this.relationOrphelinTuteur = relationOrphelinTuteur;
	}
	
	@Basic
	@OneToOne(mappedBy = "orphelin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public DossierFamilial getDossierFamilial() {
		return dossierFamilial;
	}
	public void setDossierFamilial(DossierFamilial dossierFamilial) {
		this.dossierFamilial = dossierFamilial;
	}
	
	@Basic
	@OneToOne(mappedBy = "orphelin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public DossierScolaire getDossierScolaire() {
		return dossierScolaire;
	}
	public void setDossierScolaire(DossierScolaire dossierScolaire) {
		this.dossierScolaire = dossierScolaire;
	}
	
	@Basic
	@OneToOne(mappedBy = "orphelin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public DossierMedical getDossierMedical() {
		return dossierMedical;
	}
	public void setDossierMedical(DossierMedical dossierMedical) {
		this.dossierMedical = dossierMedical;
	}
	
	@Basic
	@OneToOne(mappedBy = "orphelin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public ApparencePhysique getApparencePhysique() {
		return apparencePhysique;
	}
	public void setApparencePhysique(ApparencePhysique apparencePhysique) {
		this.apparencePhysique = apparencePhysique;
	}
	
	@Basic
	@Column(name = "AUTORISE")
	public boolean isAuthorized() {
		return authorized;
	}
	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Orphelin)
			return this.getId() == (((Orphelin) o).getId());
		return false;
	}
	
	@Override
	public String toString() {
		return this.getNom() + " " + this.getPrenom();
	}

	public static enum Genre{
		Masculin,
		Feminin
	}
	
	public static enum RelationOrphelinTuteur{
		Pere,
		Mere
	}
}
