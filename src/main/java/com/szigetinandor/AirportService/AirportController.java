package com.szigetinandor.AirportService;

import com.szigetinandor.AirportService.db.Airport;
import com.szigetinandor.AirportService.db.Flight;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(new Airport("BUD"));
		session.save(new Airport("LAX"));
		session.save(new Airport("ATL"));
		session.save(new Airport("PEK"));
		session.save(new Airport("HND"));
		session.getTransaction().commit();
		session.close();
	}

	@RequestMapping(value = "/api/airports")
	public List<Airport> airports() {
		Session session = sessionFactory.openSession();
		List<Airport> airports = session.createQuery("SELECT a FROM Airport a", Airport.class).getResultList();
		session.close();
		for(Airport a : airports)
			System.out.println(a.name);
		return airports;
	}

	@RequestMapping(value = "/api/flights")
	public List<Flight> flights() {
		Session session = sessionFactory.openSession();
		List<Flight> flights = session.createQuery("SELECT a FROM Flight a", Flight.class).getResultList();
		session.close();
		return flights;
	}

	@RequestMapping(value = "/api/airports/{id}/departures")
	public List<Flight> getDepartures(@PathVariable int id) throws JSONException {
		Session session = sessionFactory.openSession();
		Airport airport = session.get(Airport.class, id);
		session.detach(airport);
		List<Flight> departures = airport.departures;
		session.close();
		return departures;
	}

	@RequestMapping(value = "/api/airports/{id}/arrivals")
	public List<Flight> getArrivals(@PathVariable int id) {
		Session session = sessionFactory.openSession();
		Airport airport = session.get(Airport.class, id);
		session.detach(airport);
		List<Flight> arrivals = airport.arrivals;
		session.close();
		return arrivals;
	}


	/*@RequestMapping(value = "/api/flights/{id}")
	public Flight flight(@PathVariable String id) {
		Session session = sessionFactory.openSession();
		Flight flight = session.createQuery("SELECT a FROM Flight a", Flight.class).getSingleResult();
		session.close();
		return flight;
	}*/

	@PostMapping(value = "/api/flights/add")
	public RedirectView addFlight(
			@RequestParam String flight,
			@RequestParam String from_terminal,
			@RequestParam String to_terminal,
			@RequestParam int from_id,
			@RequestParam int to_id,
			@RequestParam String arrival,
			@RequestParam String departure,
			@RequestParam int from_gate,
			@RequestParam int to_gate,
			@RequestParam String remark,
			HttpServletResponse response
	) throws IOException {
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

		return new RedirectView("/api/flights");
	}
}
