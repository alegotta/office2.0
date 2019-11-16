package com.office.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;


class MovimentoKey implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @Column(name = "timestamp")
  long timestamp;

  // standard constructors, getters, and setters
  // hashcode and equals implementation
}

@Entity
@IdClass(MovimentoKey.class)
@Table(name = "movimenti")
public class Movimento {

  @Id
  private int id;
  @Id
  private long timestamp;
  private double posx;
  private double posy;

  public int getId() {
    return id;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public double getPosX() {
    return posx;
  }

  public double getPosY() {
    return posy;
  }

  public void setId(int id) {
    this.id = id;
  }
  public void setTimestamp(long t) {
    this.timestamp = t;
  }

  public void setPosX(double x) {
    this.posx = x;
  }

  public void setPosY(double y) {
    this.posy = y;
  }

  @ManyToOne
  @MapsId("id")
  @JoinColumn(name = "id")
  Utente utente;

}