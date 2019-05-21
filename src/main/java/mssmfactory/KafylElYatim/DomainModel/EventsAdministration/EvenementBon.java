package mssmfactory.KafylElYatim.DomainModel.EventsAdministration;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EVENEMENTS_BONS")
public class EvenementBon extends Evenement{
	private double coutBon;
	private String etablissementValidation;
	
	@Basic
	@Column(name = "ETABLISSEMENT_VALIDATION")
	public String getEtablissementValidation() {
		return etablissementValidation;
	}

	public void setEtablissementValidation(String etablissementValidation) {
		this.etablissementValidation = etablissementValidation;
	}

	@Basic
	@Column(name = "COUT_BON")
	public double getCoutBon() {
		return coutBon;
	}

	public void setCoutBon(double coutBon) {
		this.coutBon = coutBon;
	}
}
