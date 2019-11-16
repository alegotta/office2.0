package com.office.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
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
  private float posx;
  private float posy;

  public int getId() {
    return id;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public float getPosX() {
    return posx;
  }

  public float getPosY() {
    return posy;
  }

  @ManyToOne
  @MapsId("id")
  @JoinColumn(name = "id")
  Utente utente;

}