package org.unibl.etf.client;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.unibl.etf.appointment.AppointmentService;
import org.unibl.etf.exceptions.DuplicateException;
import org.unibl.etf.exceptions.InternalServerError;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.vehicle.VehicleDAO;

public class ClientService {
	private final ClientDAO clientDAO;
	private final VehicleDAO vehicleDAO;
	private final AppointmentService appointmentService;
	
	public ClientService() {
		this.clientDAO = new ClientDAO();
		this.vehicleDAO = new VehicleDAO();
		this.appointmentService = new AppointmentService();
	}
	
	public ArrayList<Client> getAll() throws InternalServerError {
		try {
			return clientDAO.findAll()
	                .stream()
	                .map(Client::new)
	                .collect(Collectors.toCollection(ArrayList::new));
			
		} catch(FileNotFoundException e) {
			throw new InternalServerError();
		}
	}
	
	public ArrayList<Client> getAllByStatus(String status) throws InternalServerError {
		try {
			return clientDAO.findAllByStatus(AccountStatus.valueOf(status.toUpperCase()))
	                .stream()
	                .map(Client::new)
	                .collect(Collectors.toCollection(ArrayList::new));
			
		} catch(FileNotFoundException e) {
			throw new InternalServerError();
		}
	}
	
	public Client getById(String id) throws InternalServerError, NotFoundException {
		try {
			Optional<ClientEntity> clientOptional = clientDAO.findById(id);
			if(clientOptional.isEmpty())
				throw new NotFoundException("NOT FOUND - Client with id " 
						+ id + " not found!");	//ubaciti log!!
			return new Client(clientOptional.get());
		} catch(FileNotFoundException e) {
			throw new InternalServerError();
		}
	}
	
	public Client add(ClientRequest request) throws InternalServerError, DuplicateException {
		try {
			ClientEntity entity = clientDAO.save(new ClientEntity(request));
			return new Client(entity);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new InternalServerError();
		}
	}
	
	public Client update(String id, Client client) throws InternalServerError, NotFoundException {
		try {
			Optional<ClientEntity> entity = clientDAO.findById(id);
			if(entity.isEmpty())
				throw new NotFoundException("NOT FOUND - Client with id " + id + " not found!");
			ClientEntity newEntity = new ClientEntity(client, entity.get().getPassword(), entity.get().getVehicleId());
			clientDAO.update(id, newEntity);
			return client;
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new InternalServerError();
		}
	}
	
	public void delete(String id) throws InternalServerError, NotFoundException {
		try {
			Optional<ClientEntity> clientEntity = clientDAO.findById(id);
			clientDAO.deleteById(id);
			String vehicleId = clientEntity.get().getVehicleId();
			vehicleDAO.deleteById(vehicleId);
			appointmentService.deleteAllByClientId(id);
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new InternalServerError();
		}		
	}
}
