package org.unibl.etf.appointment;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.unibl.etf.appointment.enums.AppointmentStatus;
import org.unibl.etf.chats.ClientHandler;
import org.unibl.etf.client.Client;
import org.unibl.etf.client.ClientDAO;
import org.unibl.etf.exceptions.AppointmentConflictException;
import org.unibl.etf.exceptions.InternalServerError;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.invoice.Invoice;
import org.unibl.etf.invoice.InvoiceService;
import org.unibl.etf.parts.PartEntity;
import org.unibl.etf.parts.PartService;
import org.unibl.etf.vehicle.VehicleDAO;
import org.unibl.etf.vehicle.VehicleEntity;


public class AppointmentService {
	private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
	
	private final AppointmentDAO appointmentDAO;
	private final ClientDAO clientDAO;
	private final VehicleDAO vehicleDAO;
	private final PartService partService;
	private final InvoiceService invoiceService;
	
	public AppointmentService() {
		this.appointmentDAO = new AppointmentDAO();
		this.clientDAO = new ClientDAO();
		this.vehicleDAO = new VehicleDAO();
		this.partService = new PartService();
		this.invoiceService = new InvoiceService();	
	}
	
	public ArrayList<AppointmentEntity> getAll() throws InternalServerError {
		try {
			LOGGER.info("Fetching all appointments");
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
			LOGGER.info("Fetching all appointments by status " + status);
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
			LOGGER.info("Fetching all appointments by client's id " + clientId);
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
						+ id + " not found!");
			LOGGER.info("Fetching appointment by id " + id);
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
			LOGGER.log(Level.SEVERE, "Appointment file not found", e);
			throw new InternalServerError();
		}
	}
	
	public AppointmentEntity update(String id, AppointmentEntity updatedAppointment) throws InternalServerError, NotFoundException {
		try {
			Optional<AppointmentEntity> entity = appointmentDAO.findById(id);
			if(entity.isEmpty())
				throw new NotFoundException("NOT FOUND - Appointment with id " + id + " not found!");
			if(AppointmentStatus.REPAIRED == updatedAppointment.getStatus())
				emailInvoice(updatedAppointment);
			return appointmentDAO.update(id, updatedAppointment);
		} catch(FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Appointment file not found", e);
			throw new InternalServerError();
		}
	}
	
	public void delete(String id) throws InternalServerError, NotFoundException {
		try {
			appointmentDAO.deleteById(id);
		}
		catch(FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Appointment file not found", e);
			throw new InternalServerError();
		}
	}
	
	public void deleteAllByClientId(String clientId) throws InternalServerError, NotFoundException {
		ArrayList<AppointmentEntity> appointments = getAllByClientId(clientId);
		for(AppointmentEntity a : appointments)
			delete(a.getId());
	}
	
	private void emailInvoice(AppointmentEntity updatedAppointment) {
		try {
			Client client = new Client(clientDAO.findById(updatedAppointment.getClientId()).get());
			VehicleEntity vehicle = vehicleDAO.findByClientId(client.getId()).get();
			ArrayList<PartEntity> parts = new ArrayList<>();
			for(int i = 0; i <= new Random().nextInt(2); i++) 
				parts.add(partService.sellRandomPart());
			Invoice invoice = new Invoice(parts,  updatedAppointment.getType(), client, vehicle);
			invoiceService.writeAndEmailInvoice(invoice);
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Appointment file not found", e);
		}
	}
	
}
