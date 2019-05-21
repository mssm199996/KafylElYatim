package mssmfactory.KafylElYatim.DomainModel.BenevolsAdministration;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Vehicules")
public class Vehicule {
	private int id;
	private String type;
	
	@Id
	@Column(name = "ID_VEHICULE")
	@GeneratedValue(generator = "idVehiculeGenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "idVehiculeGenerator", strategy = "increment")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "TYPE_VEHICULE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Vehicule)
			return this.getId() == (((Vehicule) o).getId());
		return false;
	}
	
	@Override
	public String toString() {
		return this.getType();
	}
}
