package org.unibl.etf.vehicle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.util.Config;
import org.unibl.etf.util.XMLSerialization;

public class VehicleDAO {
	private static final Logger LOGGER = Logger.getLogger(VehicleDAO.class.getName());
	
	private String vehiclesXMLPath;
	
	public VehicleDAO() {
		vehiclesXMLPath = XMLSerialization.getDataPath(Config.get("file.name.vehicles"));
	}
	
	public ArrayList<VehicleEntity> findAll() throws FileNotFoundException { 
		return XMLSerialization.readAll(vehiclesXMLPath);
	}
	
	public Optional<VehicleEntity> findById(String id) throws FileNotFoundException {
		ArrayList<VehicleEntity> vehicles =  XMLSerialization.readAll(vehiclesXMLPath);
    	return vehicles.stream()
    			.filter(v -> id.equals(v.getId()))
    			.findFirst();
    }

    public Optional<VehicleEntity> findByClientId(String clientId) throws FileNotFoundException {
    	ArrayList<VehicleEntity> vehicles =  XMLSerialization.readAll(vehiclesXMLPath);
    	return vehicles.stream()
    			.filter(v -> clientId.equals(v.getClientId()))
    			.findFirst();
    }
    
    public VehicleEntity save(VehicleEntity entity) throws FileNotFoundException {
    	ArrayList<VehicleEntity> vehicles = XMLSerialization.readAll(vehiclesXMLPath);
//    	if(vehicles.stream().anyMatch(v -> entity.getClientId().equals(v.getClientId())))
//    		throw new DuplicateException("Client already has a vehicle");
    	vehicles.add(entity);
		XMLSerialization.writeAll(vehicles, vehiclesXMLPath);
		return entity;
    }
    
    public VehicleEntity update(String id, VehicleEntity entity) throws FileNotFoundException, NotFoundException {
    	ArrayList<VehicleEntity> vehicles =  XMLSerialization.readAll(vehiclesXMLPath);
    	Optional<VehicleEntity> optionalEntity = findById(id);
    	if(optionalEntity.isEmpty()) {
    		LOGGER.log(Level.SEVERE, "Trying to update unexisting vehicle with id" + id);
    		throw new NotFoundException("Trying to update unexisting vehicle with id " + id);
    	}
    	VehicleEntity oldEntity = optionalEntity.get();
    	int index = vehicles.indexOf(oldEntity);
    	vehicles.set(index, entity);
		XMLSerialization.writeAll(vehicles, vehiclesXMLPath);
		return entity;
    }

    public void deleteById(String id) throws FileNotFoundException, NotFoundException { 
    	ArrayList<VehicleEntity> vehicles = XMLSerialization.readAll(vehiclesXMLPath);
    	Optional<VehicleEntity> vehicle = vehicles.stream()
									    		.filter(c -> id.equals(c.getId()))
									    		.findFirst();
    	if(vehicle.isEmpty()) {
    		LOGGER.log(Level.SEVERE, "Trying to delete unexisting vehicle with id" + id);
    		throw new NotFoundException("Trying to delete unexisting vehicle with id " + id);
    	}
    	vehicles.remove(vehicle.get());
    	XMLSerialization.writeAll(vehicles, vehiclesXMLPath);
    }
}
