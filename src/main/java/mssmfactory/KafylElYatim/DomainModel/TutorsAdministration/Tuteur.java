package mssmfactory.KafylElYatim.DomainModel.TutorsAdministration;

import java.time.LocalDate;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import javafx.beans.property.SimpleBooleanProperty;
import mssmfactory.KafylElYatim.DomainModel.OrphensAdministration.Orphelin;

@Entity
@Table(name = "Tuteurs")
public class Tuteur {
	
	private int id;
	private int nbOrphelins;
	private String nom;
	private String prenom;
	private String nomPereOrphelins;
	private String prenomPereOrphelins;
	private LocalDate ddn;
	private String adresse;
	private String secondeAdresse;
	private String ncni;
	private String nDossierBureautique;
	private String nTelFix;
	private String nTelMob;
	private String nTel1;
	private String nTel2;
	private Region region;
	private String nccp;
	private String nCarteBanquaine;
	private List<Orphelin> orphelins = new ArrayList<Orphelin>();
	private Habitat habitat;
	private SituationSociale situationSociale;
	private DossierMedical dossierMedical;
	private String observation;
	
	@Id
	@GeneratedValue(generator = "tuteurIdGenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "tuteurIdGenerator", strategy = "increment")
	@Column(name = "ID_TUTEUR")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "NOM_TUTEUR")
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	@Basic
	@Column(name = "PRENOM_TUTEUR")
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	@Basic
	@Column(name = "NOM_PERE_ORPHELINS")
	public String getNomPereOrphelins() {
		return nomPereOrphelins;
	}
	public void setNomPereOrphelins(String nomPereOrphelins) {
		this.nomPereOrphelins = nomPereOrphelins;
	}
	
	@Basic
	@Column(name = "PRENOM_PERE_ORPHELINS")
	public String getPrenomPereOrphelins() {
		return prenomPereOrphelins;
	}
	public void setPrenomPereOrphelins(String prenomPereOrphelins) {
		this.prenomPereOrphelins = prenomPereOrphelins;
	}
	
	@Basic
	@Column(name = "DDN_TUTEUR")
	public LocalDate getDdn() {
		return ddn;
	}
	public void setDdn(LocalDate ddn) {
		this.ddn = ddn;
	}
	
	@Basic
	@Column(name = "ADRESSE_TUTEUR")
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	@Basic
	@Column(name = "SECONDE_ADRESSE_TUTEUR")
	public String getSecondeAdresse() {
		return secondeAdresse;
	}
	public void setSecondeAdresse(String secondeAdresse) {
		this.secondeAdresse = secondeAdresse;
	}
	
	@Basic
	@Column(name = "NCNI_TUTEUR")
	public String getNcni() {
		return ncni;
	}
	public void setNcni(String ncni) {
		this.ncni = ncni;
	}
	
	@Basic
	@Column(name = "NUMERO_TELEPHONE_FIXE_TUTEUR")
	public String getnTelFix() {
		return nTelFix;
	}
	public void setnTelFix(String nTelFix) {
		this.nTelFix = nTelFix;
	}
	
	@Basic
	@Column(name = "NUMERO_TELEPHONE_MOBILE_TUTEUR")
	public String getnTelMob() {
		return nTelMob;
	}
	public void setnTelMob(String nTelMob) {
		this.nTelMob = nTelMob;
	}
	
	@Basic
	@Column(name = "AUTRE_NUMERO_TELEPHONE_1")
	public String getnTel1() {
		return nTel1;
	}
	public void setnTel1(String nTel1) {
		this.nTel1 = nTel1;
	}
	
	@Basic
	@Column(name = "AUTRE_NUMERO_TELEPHONE_2")
	public String getnTel2() {
		return nTel2;
	}
	public void setnTel2(String nTel2) {
		this.nTel2 = nTel2;
	}
	@Basic
	@Column(name = "NB_ORPHELIN")
	public int getNbOrphelins() {
		return nbOrphelins;
	}
	public void setNbOrphelins(int nbOrphelins) {
		this.nbOrphelins = nbOrphelins;
	}
	
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_REGION_TUTEUR")
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	
	@Basic
	@Column(name = "NCCP_TUTEUR")
	public String getNccp() {
		return nccp;
	}
	public void setNccp(String nccp) {
		this.nccp = nccp;
	}
	
	@Basic
	@Column(name = "NCARTE_BANQUAIRE")
	public String getnCarteBanquaine() {
		return nCarteBanquaine;
	}
	public void setnCarteBanquaine(String nCarteBanquaine) {
		this.nCarteBanquaine = nCarteBanquaine;
	}

	@Basic
	@Column(name = "NDOSSIER_BUREAUTIQUE")
	public String getnDossierBureautique() {
		return nDossierBureautique;
	}
	public void setnDossierBureautique(String nDossierBureautique) {
		this.nDossierBureautique = nDossierBureautique;
	}
	
	@Basic
	@Column(name = "OBSERVATION")
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	
	@Basic
	@OneToMany(mappedBy = "tuteur", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Orphelin> getOrphelins() {
		return orphelins;
	}
	public void setOrphelins(List<Orphelin> orphelins) {
		this.orphelins = orphelins;
	}
	
	public void addOrphelin(Orphelin orphelin) {
		this.getOrphelins().add(orphelin);
		orphelin.setTuteur(this);
	}
	
	public void removeOrphelin(Orphelin orphelin) {
		this.getOrphelins().remove(orphelin);
		orphelin.setTuteur(null);
	}
	
	@Basic
	@OneToOne(mappedBy = "tuteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Habitat getHabitat() {
		return habitat;
	}
	public void setHabitat(Habitat habitat) {
		this.habitat = habitat;
	}
	
	@Basic
	@OneToOne(mappedBy = "tuteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public SituationSociale getSituationSociale() {
		return situationSociale;
	}
	public void setSituationSociale(SituationSociale situationSociale) {
		this.situationSociale = situationSociale;
	}
	
	@Basic
	@OneToOne(mappedBy = "tuteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public DossierMedical getDossierMedical() {
		return dossierMedical;
	}
	public void setDossierMedical(DossierMedical dossierMedical) {
		this.dossierMedical = dossierMedical;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Tuteur)
			return this.getId() == (((Tuteur) o).getId());
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getId();
	}
	
	@Override
	public String toString() {
		return this.getNom() + " " + this.getPrenom();
	}
}
