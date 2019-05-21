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
@Table(name = "ApparencesPhysiques")
public class ApparencePhysique {
	private int id;
	private String pointure;
	private LocalDate ddMaj;
	private String observation;
	private Orphelin orphelin;
		
	@Id
	@Column(name = "ID_APPARENCE_PHYSIQUE")
	@GeneratedValue(generator = "idDossierFamilialGenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "idDossierFamilialGenerator", strategy = "increment")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "POINTURE")
	public String getPointure() {
		return pointure;
	}
	public void setPointure(String pointure) {
		this.pointure = pointure;
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
	@Column(name = "OBSERVATION_PHYSIQUE")
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	
	@Basic
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORPHELIN")
	public Orphelin getOrphelin() {
		return orphelin;
	}
	public void setOrphelin(Orphelin orphelin) {
		this.orphelin = orphelin;
	}	
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof ApparencePhysique)
			return this.getId() == (((ApparencePhysique) o).getId());
		return false;
	}
}
