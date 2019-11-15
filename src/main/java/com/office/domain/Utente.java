package com.office.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


//YH: In order to work with JPA, we use @Entity & @Id
@XmlRootElement
@Entity
@Table(name="utenti")
public class Utente {

	// YH: Be careful that the fields name should be all lower case for JPA.
	@Id
	private int uid;
	private String name;
    private String sesso;
    private int annoNascita;
    private long cell;
    private String tipoUtente;
    
	public int getUid() {
		return uid;
	}
	public String getName() {
		return name;
    }
    public String getSesso() {
		return sesso;
    }
    public int getAnnoNascita() {
		return annoNascita;
    }
    public long getCell() {
		return cell;
    }
    public String getTipoUtente() {
		return tipoUtente;
    }
}