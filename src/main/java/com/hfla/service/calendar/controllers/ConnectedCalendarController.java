package com.hfla.service.calendar.controllers;

import com.hfla.service.calendar.pojos.Availability;
import com.hfla.service.calendar.pojos.ConnectedCalendar;
import com.hfla.service.calendar.pojos.ConnectedEvent;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/calendar")
public class ConnectedCalendarController {
	@Autowired
	Availability availability;

	@Autowired
	ConnectedCalendar calendar;

	private static Logger logger = LogManager.getLogManager().getLogger("ConnectedCalendarController.class");

	@GetMapping(value = "/events", consumes = "application/json", produces = "application/json")
	ResponseEntity<List<ConnectedEvent>> getEvents(@RequestParam(name="nylasId") String nylasId,
												   @RequestParam(name = "calendarId") String calendarId) {

		return new ResponseEntity<>(calendar.getEvents(nylasId, calendarId),
				new HttpHeaders(),
				HttpStatus.OK);
	}

	@GetMapping(value = "/calendars", consumes = "application/json", produces = "application/json")
	ResponseEntity<List<ConnectedCalendar>> getConnectedCalendar(@RequestParam(name="nylasId") String nylasId) {

		return new ResponseEntity<>(calendar.getCalendars(nylasId),
				new HttpHeaders(),
				HttpStatus.OK);
	}

	@PostMapping(value = "/event", consumes = "application/json", produces = "application/json")
	ResponseEntity<String> createEvent(@RequestBody ConnectedEvent nylasEvent, @RequestParam String nylasId) {

		return new ResponseEntity<>(ConnectedEvent.addEvent(nylasEvent,
				calendar.getAccessToken(nylasId)),
				new HttpHeaders(),
				HttpStatus.OK);

	}

	@DeleteMapping(value = "/event/{id}", consumes = "application/json", produces = "application/json")
	ResponseEntity<Void> deleteEvent(@PathVariable(name = "id") String eventId, @RequestParam String nylasId) {
		return new ResponseEntity<>(ConnectedEvent.deleteEvent(eventId, calendar.getAccessToken(nylasId)),
				new HttpHeaders(),
				HttpStatus.OK);
	}

	@GetMapping(value = "/availability", consumes = "application/json", produces = "application/json")
	ResponseEntity<List<Availability>> getAvailability(@RequestParam String nylasId,
													  @RequestParam String calendarId,
													  @RequestParam long startTime) {

		List<ConnectedEvent> events = calendar.getEvents(nylasId, calendarId);
		return new ResponseEntity<>(availability.calculate(events.stream()),
				new HttpHeaders(),
				HttpStatus.OK);


	}


}
