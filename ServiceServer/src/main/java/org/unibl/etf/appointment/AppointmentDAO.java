package org.unibl.etf.appointment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.unibl.etf.appointment.enums.AppointmentStatus;
import org.unibl.etf.exceptions.AppointmentConflictException;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.util.Config;
import org.unibl.etf.util.XMLSerialization;

public class AppointmentDAO {
	
	public ArrayList<AppointmentEntity> findAll() throws FileNotFoundException { 
		return readAll();
	}
	
	public ArrayList<AppointmentEntity> findAllByStatus(AppointmentStatus status) throws FileNotFoundException {
		return readAll().stream()
				.filter(a -> status.equals(a.getStatus()))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public ArrayList<AppointmentEntity> findAllByClientId(String clientId) throws FileNotFoundException {
		return readAll().stream()
				.filter(a -> clientId.equals(a.getClientId()))
				.collect(Collectors.toCollection(ArrayList::new));
	}

    public Optional<AppointmentEntity> findById(String id) throws FileNotFoundException {
    	return readAll()
    		.stream()
    		.filter(a -> id.equals(a.getId()))
    		.findFirst();
    }
    
    public AppointmentEntity save(AppointmentEntity entity) throws FileNotFoundException, AppointmentConflictException {
    	ArrayList<AppointmentEntity> appointments = readAll();
    	if(appointments.stream().anyMatch(a -> 
    										entity.getDate().equals(a.getDate()) 
    										&& entity.getTime().equals(a.getTime())))
    		throw new AppointmentConflictException("Appointment is already scheduled for this time: " 
    				+ entity.getDate() + ", " + entity.getTime());
		appointments.add(entity);
		writeAll(appointments);
		return entity;
    }
    
    public AppointmentEntity update(String id, AppointmentEntity entity) throws FileNotFoundException, NotFoundException {
    	ArrayList<AppointmentEntity> appointments = readAll();
    	Optional<AppointmentEntity> optionalEntity = findById(id);
    	if(optionalEntity.isEmpty())
    		throw new NotFoundException("Trying to update unexisting appointment with id " + id);
    	AppointmentEntity oldEntity = optionalEntity.get();
    	int index = appointments.indexOf(oldEntity);
    	appointments.set(index, entity);
		writeAll(appointments);
		return entity;
    }

    public void deleteById(String id) throws FileNotFoundException, NotFoundException { 
    	ArrayList<AppointmentEntity> appointments = readAll();
    	Optional<AppointmentEntity> client = appointments.stream()
									    		.filter(c -> id.equals(c.getId()))
									    		.findFirst();
    	if(client.isEmpty())
    		throw new NotFoundException("Trying to delete unexisting appointment with id " + id);
    	appointments.remove(client.get());
    	writeAll(appointments);
    }
    
    
    
	private ArrayList<AppointmentEntity> readAll() throws FileNotFoundException {
		try {
			return XMLSerialization.deserializeXML(getDataPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("Trying to read data from file, but file not found at " + getDataPath());
		}
	}

	private void writeAll(ArrayList<AppointmentEntity> appointments) throws FileNotFoundException {
		try {
			XMLSerialization.serializeXML(appointments, getDataPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("Trying to write data in directory, but directory not found at " + getDataPath());
		}
	}
 
	private String getDataPath() {
	    String base = System.getProperty("catalina.base");
	    return base + File.separator + Config.get("file.root.path")
	                + File.separator + Config.get("file.name.appointments");
	}
}
