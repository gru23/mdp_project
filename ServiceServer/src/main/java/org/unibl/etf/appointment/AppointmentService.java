package org.unibl.etf.appointment;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import org.unibl.etf.appointment.enums.AppointmentStatus;
import org.unibl.etf.exceptions.AppointmentConflictException;
import org.unibl.etf.exceptions.InternalServerError;
import org.unibl.etf.exceptions.NotFoundException;


public class AppointmentService {
	private final AppointmentDAO appointmentDAO;
	
	public AppointmentService() {
		this.appointmentDAO = new AppointmentDAO();	
	}
	
	public ArrayList<AppointmentEntity> getAll() throws InternalServerError {
		try {
			return appointmentDAO
					.findAll()
					.stream()
					.sorted(Comparator
	                        .comparing(AppointmentEntity::getDate)
	                        .thenComparing(AppointmentEntity::getTime))
	                .collect(Collectors.toCollection(ArrayList::new));
			
		} catch(FileNotFoundException e) {
			throw new InternalServerError();
		}
	}
	
	public ArrayList<AppointmentEntity> getAllByStatus(String status) throws InternalServerError {
		try {
			return appointmentDAO
					.findAllByStatus(AppointmentStatus.valueOf(status.toUpperCase()))
					.stream()
					.sorted(Comparator
	                        .comparing(AppointmentEntity::getDate)
	                        .thenComparing(AppointmentEntity::getTime))
	                .collect(Collectors.toCollection(ArrayList::new));
			
		} catch(FileNotFoundException e) {
			throw new InternalServerError();
		}
	}
	
	public ArrayList<AppointmentEntity> getAllByClientId(String clientId) throws InternalServerError {
		try {
			return appointmentDAO
					.findAllByClientId(clientId)
					.stream()
					.sorted(Comparator
	                        .comparing(AppointmentEntity::getDate)
	                        .thenComparing(AppointmentEntity::getTime))
	                .collect(Collectors.toCollection(ArrayList::new));
		} catch(FileNotFoundException e) {
			throw new InternalServerError();
		}
	}
	
	public AppointmentEntity getById(String id) throws InternalServerError, NotFoundException {
		try {
			Optional<AppointmentEntity> appointmentOptional = appointmentDAO.findById(id);
			if(appointmentOptional.isEmpty())
				throw new NotFoundException("NOT FOUND - Appointment with id " 
						+ id + " not found!");	//ubaciti log!!
			return appointmentOptional.get();
		} catch(FileNotFoundException e) {
			throw new InternalServerError();
		}
	}
	
	public AppointmentEntity add(AppointmentRequest request) throws InternalServerError, AppointmentConflictException {
		try {
			LocalTime requestTime = LocalTime.parse(request.getTime());
			ArrayList<LocalTime> sameDateAppointment = getAll()
	                .stream()
	                .filter(a -> request.getDate().equals(a.getDate()))
	                .map(a -> LocalTime.parse(a.getTime()))
	                .collect(Collectors.toCollection(ArrayList::new));
			boolean conflict = sameDateAppointment.stream()
		            .anyMatch(t -> Math.abs(Duration.between(t, requestTime).toMinutes()) < 60);

	        if (conflict) {
	            throw new AppointmentConflictException("The appointment is already booked within one hour of the requested time.");
	        }
			return appointmentDAO.save(new AppointmentEntity(request));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new InternalServerError();
		}
	}
	
	public AppointmentEntity update(String id, AppointmentEntity updatedAppointment) throws InternalServerError, NotFoundException {
		try {
			Optional<AppointmentEntity> entity = appointmentDAO.findById(id);
			if(entity.isEmpty())
				throw new NotFoundException("NOT FOUND - Appointment with id " + id + " not found!");
			return appointmentDAO.update(id, updatedAppointment);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new InternalServerError();
		}
	}
	
	public void delete(String id) throws InternalServerError, NotFoundException {
		try {
			appointmentDAO.deleteById(id);
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new InternalServerError();
		}
	}
	
	public void deleteAllByClientId(String clientId) throws InternalServerError, NotFoundException {
		ArrayList<AppointmentEntity> appointments = getAllByClientId(clientId);
		for(AppointmentEntity a : appointments)
			delete(a.getId());
	}
	
}
