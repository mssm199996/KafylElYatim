package mssmfactory.KafylElYatim.DomainModel.OrphensAdministration;

import java.time.LocalDate;

import javax.persistence.Basic;
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
@Table(name = "DossiersScolaires")
public class DossierScolaire {

	private int id;
	private NiveauScolaire niveauScolaire;
	private int anneeScolaire;
	private LocalDate ddMaj;
	private boolean scolarise;
	private String observation;
	private Orphelin orphelin;
	
	@Id
	@Column(name = "ID_DOSSIER_SCOLAIRE")
	@GeneratedValue(generator = "idDossierScolaireGenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "idDossierScolaireGenerator", strategy = "increment")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "NIVEAU_SCOLAIRE")
	public NiveauScolaire getNiveauScolaire() {
		return niveauScolaire;
	}
	public void setNiveauScolaire(NiveauScolaire niveauScolaire) {
		this.niveauScolaire = niveauScolaire;
	}
	
	@Basic
	@Column(name = "ANNEE_SCOLAIRE")
	public int getAnneeScolaire() {
		return anneeScolaire;
	}
	public void setAnneeScolaire(int anneeScolaire) {
		this.anneeScolaire = anneeScolaire;
	}
	
	@Basic
	@Column(name = "DDMAJ")
	public LocalDate getDdMaj() {
		return ddMaj;
	}
	public void setDdMaj(LocalDate ddMaj) {
		this.ddMaj = ddMaj;
	}
	
	@Basic
	@Column(name = "SCOLARISE")
	public boolean isScolarise() {
		return scolarise;
	}
	public void setScolarise(boolean scolarise) {
		this.scolarise = scolarise;
	}

	@Basic
	@Column(name = "OBSERVATION")
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORPHELIN")
	public Orphelin getOrphelin() {
		return orphelin;
	}
	public void setOrphelin(Orphelin orphelin) {
		this.orphelin = orphelin;
	}

	public static enum NiveauScolaire {
		Sans_scolarite,
		Pres_scolaire,
		Primaire,
		Moyen,
		Secondaire,
		Universitaire,
		Centre_de_formation,		
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof DossierScolaire)
			return this.getId() == (((DossierScolaire) o).getId());
		return false;
	}
}
