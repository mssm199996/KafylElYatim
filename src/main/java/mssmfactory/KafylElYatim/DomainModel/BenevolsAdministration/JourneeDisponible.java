package mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration;

import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "JourneesDisponibles")
public class JourneeDisponible {
	private int id;
	private JourDeSemaine jourSemaine;
	private LocalTime heureDebut;
	private LocalTime heureFin;
	private Benevole benevole;
	
	@Id
	@Column(name = "ID_JOURNEE_DISPONIBLE")
	@GenericGenerator(name = "journeeDisponibleIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "journeeDisponibleIdGenerator", strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "JOUR_SEMAINE")
	public JourDeSemaine getJourSemaine() {
		return jourSemaine;
	}
	public void setJourSemaine(JourDeSemaine jourSemaine) {
		this.jourSemaine = jourSemaine;
	}
	
	@Basic
	@Column(name = "HEURE_DEBUT")
	public LocalTime getHeureDebut() {
		return heureDebut;
	}
	public void setHeureDebut(LocalTime heureDebut) {
		this.heureDebut = heureDebut;
	}
	
	@Basic
	@Column(name = "HEURE_FIN")
	public LocalTime getHeureFin() {
		return heureFin;
	}
	public void setHeureFin(LocalTime heureFin) {
		this.heureFin = heureFin;
	}
	
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_BENEVOLE")
	public Benevole getBenevole() {
		return benevole;
	}
	public void setBenevole(Benevole benevole) {
		this.benevole = benevole;
	}	
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof JourneeDisponible)
			return this.getId() == (((JourneeDisponible) o).getId());
		return false;
	}
	
	@Override
	public String toString() {
		return "Le " + this.getJourSemaine() + ", de: " + this.getHeureDebut() + " à " + this.getHeureFin();
	}
	
	public static enum JourDeSemaine{
		Samedi,
		Dimanche,
		Lundi,
		Mardi,
		Mercredi,
		Jeudi,
		Vendredi
	}
}
