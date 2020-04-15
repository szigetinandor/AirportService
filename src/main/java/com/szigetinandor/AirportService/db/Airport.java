package com.szigetinandor.AirportService.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Airport {

	@Id
	@GeneratedValue
	public int id;

	@Column
	public String name;

	@JsonIgnore
	@OneToMany(mappedBy = "from", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	public List<Flight> arrivals;

	@JsonIgnore
	@OneToMany(mappedBy = "to", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	public List<Flight> departures;

	public Airport(String name) {
		this.name = name;
	}

	public Airport() {}
}
