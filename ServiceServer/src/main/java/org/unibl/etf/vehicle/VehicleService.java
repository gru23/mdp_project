package org.unibl.etf.vehicle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import org.unibl.etf.exceptions.InternalServerError;
import org.unibl.etf.exceptions.NotFoundException;

public class VehicleService {
	private final VehicleDAO vehicleDAO;
	
	public VehicleService() {
		this.vehicleDAO = new VehicleDAO();
	}

	public ArrayList<VehicleEntity> getAll() throws InternalServerError {
		try {
			return vehicleDAO.findAll();
			
		} catch(FileNotFoundException e) {
			throw new InternalServerError();
		}
	}
	
	public VehicleEntity getById(String id) throws InternalServerError, NotFoundException {
		try {
			Optional<VehicleEntity> vehicleOptional = vehicleDAO.findById(id);
			if(vehicleOptional.isEmpty())
				throw new NotFoundException("NOT FOUND - Client with id " + id + " not found!");	//ubaciti log!!
			return vehicleOptional.get();
		} catch(FileNotFoundException e) {
			throw new InternalServerError();
		}
	}
	
	public VehicleEntity add(VehicleEntity request) throws InternalServerError {
		try {
			return vehicleDAO.save(request);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new InternalServerError();
		}
	}
	
	public VehicleEntity update(String id, VehicleEntity vehicle) throws InternalServerError, NotFoundException {
		try {
			Optional<VehicleEntity> entity = vehicleDAO.findById(id);
			if(entity.isEmpty())
				throw new NotFoundException("NOT FOUND - Vehicle with id " + id + " not found!");
			vehicleDAO.update(id, vehicle);
			return vehicle;
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new InternalServerError();
		}
	}
	
	public void delete(String id) throws InternalServerError, NotFoundException {
		try {
			vehicleDAO.deleteById(id);
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new InternalServerError();
		}
		
	}
}
