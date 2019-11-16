package com.office.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name="beacons")
public class Beacon {

	@Id
  private int id;
  private String name;
	private float posx;
  private float posy;
  
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
  } 
  public float getPosX(){
    return posx;
  }
  public float getPosY(){
    return posy;
  }
}