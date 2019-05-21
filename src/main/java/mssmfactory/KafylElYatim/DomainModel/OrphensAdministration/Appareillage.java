package mssmfactory.KafylElYatim.DomainModel.OrphensAdministration;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Appareillages")
public class Appareillage {
	
	private int id;
	private String type;
	
	@Id
	@Column(name = "ID_APPAREILLAGE")
	@GeneratedValue(generator = "idDossierFamilialGenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "idDossierFamilialGenerator", strategy = "increment")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "TYPE_APPAREILLAGE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Appareillage)
			return this.getId() == (((Appareillage) o).getId());
		return false;
	}
	
	@Override
	public String toString() {
		return this.getType();
	}
}
