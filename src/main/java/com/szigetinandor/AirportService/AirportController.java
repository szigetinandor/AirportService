package com.szigetinandor.AirportService;

import com.szigetinandor.AirportService.db.Airport;
import com.szigetinandor.AirportService.db.Flight;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AirportController {

	private SessionFactory sessionFactory;

	public AirportController() {
		sessionFactory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Airport.class)
				.addAnnotatedClass(Flight.class)
				.buildSessionFactory();
	}

	@RequestMapping(value = "/api/flights")
	public List<Flight> flights() {
		Session session = sessionFactory.openSession();
		List<Flight> flights = session.createQuery("SELECT a FROM Flight a", Flight.class).getResultList();
		session.close();
		return flights;
	}

	@RequestMapping(value = "/api/flights/{id}")
	public Flight flight(@PathVariable String id) {
		Session session = sessionFactory.openSession();
		Flight flight = session.createQuery("SELECT a FROM Flight a", Flight.class).getSingleResult();
		session.close();
		return flight;
	}

	@PostMapping(value = "/api/flights/add")
	public String addFlight(
			@RequestParam String flight,
			@RequestParam String from_terminal,
			@RequestParam String to_terminal,
			@RequestParam int from_id,
			@RequestParam int to_id,
			@RequestParam String arrival,
			@RequestParam String departure,
			@RequestParam int from_gate,
			@RequestParam int to_gate,
			@RequestParam String remark
	) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Flight f = new Flight();
		f.flight = flight;
		f.from_terminal = from_terminal;
		f.to_terminal = to_terminal;
		f.from_id = from_id;
		f.to_id = to_id;
		f.arrival = arrival;
		f.departure = departure;
		f.from_gate = from_gate;
		f.to_gate = to_gate;
		f.remark = remark;
		session.save(f);
		session.getTransaction().commit();
		session.close();

		return "redirect:/api/flights/" + f.flight;
	}
}
