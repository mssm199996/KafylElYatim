package mssmfactory.KafylElYatim.DomainModel.OrphensAdministration;

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
@Table(name = "DossiersFamilliaux")
public class DossierFamilial {

	private int id;
	private TypeOrphelinat typeOrphelinat;
	private String nomPere;
	private String prenomPere;
	private String nomMere;
	private String prenomMere;
	private String situationFamilliale;
	private Orphelin orphelin;
	
	
	@Id
	@Column(name = "ID_DOSSIER_FAMILIAL")
	@GeneratedValue(generator = "idDossierFamilialGenerator", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "idDossierFamilialGenerator", strategy = "increment")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "TYPE_ORPHELINAT")
	public TypeOrphelinat getTypeOrphelinat() {
		return typeOrphelinat;
	}
	public void setTypeOrphelinat(TypeOrphelinat typeOrphelinat) {
		this.typeOrphelinat = typeOrphelinat;
	}
	
	@Basic
	@Column(name = "NOM_PERE")
	public String getNomPere() {
		return nomPere;
	}
	public void setNomPere(String nomPere) {
		this.nomPere = nomPere;
	}
	
	@Basic
	@Column(name = "PRENOM_PERE")
	public String getPrenomPere() {
		return prenomPere;
	}
	public void setPrenomPere(String prenomPere) {
		this.prenomPere = prenomPere;
	}
	
	@Basic
	@Column(name = "NOM_MERE")
	public String getNomMere() {
		return nomMere;
	}
	public void setNomMere(String nomMere) {
		this.nomMere = nomMere;
	}
	
	@Basic
	@Column(name = "PRENOM_MERE")
	public String getPrenomMere() {
		return prenomMere;
	}
	public void setPrenomMere(String prenomMere) {
		this.prenomMere = prenomMere;
	}
	
	@Basic
	@Column(name = "SITUATION_FAMILLIALE")
	public String getSituationFamilliale() {
		return this.situationFamilliale;
	}
	public void setSituationFamilliale(String situationFamilliale) {
		this.situationFamilliale = situationFamilliale;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORPHELIN")
	public Orphelin getOrphelin() {
		return orphelin;
	}
	public void setOrphelin(Orphelin orphelin) {
		this.orphelin = orphelin;
	}

	public static enum TypeOrphelinat{
		Pere,
		Mere,
		Pere_Mere
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof DossierFamilial)
			return this.getId() == (((DossierFamilial) o).getId());
		return false;
	}
}
