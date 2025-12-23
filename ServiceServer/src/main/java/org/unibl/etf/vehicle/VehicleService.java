package org.unibl.etf.vehicle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.exceptions.InternalServerError;
import org.unibl.etf.exceptions.NotFoundException;

public class VehicleService {
	private static final Logger LOGGER = Logger.getLogger(VehicleService.class.getName());
	
	private final VehicleDAO vehicleDAO;
	
	public VehicleService() {
		this.vehicleDAO = new VehicleDAO();
	}

	public ArrayList<VehicleEntity> getAll() throws InternalServerError {
		try {
			return vehicleDAO.findAll();
		} catch(FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Vehicle file not found", e);
			throw new InternalServerError();
		}
	}
	
	public VehicleEntity getById(String id) throws InternalServerError, NotFoundException {
		try {
			Optional<VehicleEntity> vehicleOptional = vehicleDAO.findById(id);
			if(vehicleOptional.isEmpty()) {
				LOGGER.log(Level.SEVERE, "Not found client with id " + id);
				throw new NotFoundException("NOT FOUND - Client with id " + id + " not found!");
			}
			return vehicleOptional.get();
		} catch(FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Vehicle file not found", e);
			throw new InternalServerError();
		}
	}
	
	public VehicleEntity add(VehicleEntity request) throws InternalServerError {
		try {
			return vehicleDAO.save(request);
		} catch(FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Vehicle file not found", e);
			throw new InternalServerError();
		}
	}
	
	public VehicleEntity update(String id, VehicleEntity vehicle) throws InternalServerError, NotFoundException {
		try {
			Optional<VehicleEntity> entity = vehicleDAO.findById(id);
			if(entity.isEmpty())
				throw new NotFoundException("NOT FOUND - Vehicle with id " + id + " not found!");
			vehicleDAO.update(id, vehicle);
			LOGGER.info("Updated vehicle " + id);
			return vehicle;
		} catch(FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Vehicle file not found", e);
			throw new InternalServerError();
		}
	}
	
	public void delete(String id) throws InternalServerError, NotFoundException {
		try {
			vehicleDAO.deleteById(id);
			LOGGER.info("Deleted vehicle " + id);
		}
		catch(FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Vehicle file not found", e);
			throw new InternalServerError();
		}
		
	}
}
