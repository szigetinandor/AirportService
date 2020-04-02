package com.szigetinandor.AirportService.db;

import javax.persistence.*;

@Entity
public class Flight {

	public Flight(
			String flight,
			String from_terminal,
			String to_terminal,
			int from_id,
			int to_id,
			String departure,
			String arrival,
			int from_gate,
			int to_gate,
			String remark
	) {
		this.flight = flight;
		this.from_terminal = from_terminal;
		this.to_terminal = to_terminal;
		this.from_id = from_id;
		this.to_id = to_id;
		this.departure = departure;
		this.arrival = arrival;
		this.from_gate = from_gate;
		this.to_gate = to_gate;
		this.remark = remark;
	}

	public Flight() {
	}

	@Id
	@Column
	public String flight;

	@Column
	public String from_terminal;

	@Column
	public String to_terminal;

	@Column
	public int from_id;

	@Column
	public int to_id;

	@Column
	public String departure;

	@Column
	public String arrival;

	@Column
	public int from_gate;

	@Column
	public int to_gate;

	@Column
	public String remark;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="from_id", insertable = false, updatable = false)
	public Airport from;

	@ManyToOne
	@JoinColumn(name="to_id", insertable = false, updatable = false)
	public Airport to;
}
