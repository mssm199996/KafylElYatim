package mssmfactory.KafylElYatim.DomainModel.EventsAdministration;

import java.time.LocalDate;

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

import mssmfactory.KafylElYatim.DomainModel.TutorsAdministration.Tuteur;

@Entity
@Table(name = "ACTIONS")
public class Action {	
	private int idAction;
	private LocalDate dateAction;
	private int quantiteAction;
	private Tuteur tuteur;
	private Evenement evenement;
	
	@Id
	@Column(name = "ID_ACTION")
	@GeneratedValue(generator = "actionIdGenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "actionIdGenerator", strategy = "increment")
	public int getIdAction() {
		return idAction;
	}
	public void setIdAction(int idAction) {
		this.idAction = idAction;
	}
	
	@Basic
	@Column(name = "DATE_ACTION")
	public LocalDate getDateAction() {
		return dateAction;
	}
	public void setDateAction(LocalDate dateAction) {
		this.dateAction = dateAction;
	}
	
	@Basic
	@Column(name = "QUANTITE_ACTION")
	public int getQuantiteAction() {
		return quantiteAction;
	}
	public void setQuantiteAction(int quantiteAction) {
		this.quantiteAction = quantiteAction;
	}
	
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TUTEUR")
	public Tuteur getTuteur() {
		return tuteur;
	}
	public void setTuteur(Tuteur tuteur) {
		this.tuteur = tuteur;
	}
	
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EVENEMENT")
	public Evenement getEvenement() {
		return evenement;
	}
	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Action)
			return this.getIdAction() == (((Action) o).getIdAction());
		return false;
	}
}
