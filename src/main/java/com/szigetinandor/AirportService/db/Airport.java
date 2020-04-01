package com.szigetinandor.AirportService.db;

import javax.persistence.*;
import java.util.List;

@Entity
public class Airport {

	@Id
	@GeneratedValue
	public int id;

	@Column
	public String name;

	@OneToMany(mappedBy = "from_id")
	public List<Flight> arrivals;

	@OneToMany(mappedBy = "to_id")
	public List<Flight> departures;
}
