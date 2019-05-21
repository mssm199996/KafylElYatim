package mssmfactory.KafylElYatim.DomainModel.EventsAdministration;

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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Evenement {
	private int idEvenement;
	private String designationEvenement;
	private String descriptionEvenement;
	private LocalDate dateEvenement;
	private List<Action> actions = new ArrayList<Action>();
	
	@Id
	@Column(name = "ID_EVENEMENT")
	@GeneratedValue(generator = "evenementIdGenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "evenementIdGenerator", strategy = "increment")
	public int getIdEvenement() {
		return idEvenement;
	}
	public void setIdEvenement(int idEvenement) {
		this.idEvenement = idEvenement;
	}
	
	@Basic
	@Column(name = "DESIGNATION_EVENEMENT")
	public String getDesignationEvenement() {
		return designationEvenement;
	}
	public void setDesignationEvenement(String designationEvenement) {
		this.designationEvenement = designationEvenement;
	}
	
	@Basic
	@Column(name = "DESCRIPTION_EVENEMENT")
	public String getDescriptionEvenement() {
		return descriptionEvenement;
	}
	public void setDescriptionEvenement(String descriptionEvenement) {
		this.descriptionEvenement = descriptionEvenement;
	}
	
	@Basic
	@Column(name = "DATE_EVENEMENT")
	public LocalDate getDateEvenement() {
		return dateEvenement;
	}
	public void setDateEvenement(LocalDate dateEvenement) {
		this.dateEvenement = dateEvenement;
	}
	
	@Basic
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "evenement")
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
	public void addAction(Action action) {
		this.getActions().add(action);
		action.setEvenement(this);
	}
	
	public void removeAction(Action action) {
		this.getActions().remove(action);
		action.setEvenement(null);
	}
	
	@Override
	public String toString() {
		return this.getDesignationEvenement();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Evenement)
			return this.getIdEvenement() == (((Evenement) o).getIdEvenement());
		return false;
	}
}
