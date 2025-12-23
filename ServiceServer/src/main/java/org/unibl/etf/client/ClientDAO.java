package org.unibl.etf.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.unibl.etf.exceptions.DuplicateException;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.util.Config;
import org.unibl.etf.util.XMLSerialization;

public class ClientDAO {
	public ArrayList<ClientEntity> findAll() throws FileNotFoundException { 
		return readAll();
	}
	
	public ArrayList<ClientEntity> findAllByStatus(AccountStatus status) throws FileNotFoundException {
		return readAll().stream()
				.filter(c -> status.equals(c.getStatus()))
				.collect(Collectors.toCollection(ArrayList::new));
	}

    public Optional<ClientEntity> findById(String id) throws FileNotFoundException {
    	return readAll()
    		.stream()
    		.filter(c -> id.equals(c.getId()))
    		.findFirst();
    }
    
    public Optional<ClientEntity> findByUsername(String username) throws FileNotFoundException {
    	return readAll()
    			.stream()
    			.filter(c -> username.equals(c.getUsername()))
    			.findFirst();
    }

    public ClientEntity save(ClientEntity entity) throws FileNotFoundException, DuplicateException {
    	ArrayList<ClientEntity> clients = readAll();
    	if(clients.stream().anyMatch(c -> entity.getUsername().equals(c.getUsername())))
    		throw new DuplicateException("Client already exists with username " + entity.getUsername());
		clients.add(entity);
		writeAll(clients);
		return entity;
    }
    
    public ClientEntity update(String id, ClientEntity entity) throws FileNotFoundException, NotFoundException {
    	ArrayList<ClientEntity> clients = readAll();
    	Optional<ClientEntity> optionalEntity = findById(id);
    	if(optionalEntity.isEmpty())
    		throw new NotFoundException("Trying to update unexisting client with id " + id);
    	ClientEntity oldEntity = optionalEntity.get();
    	int index = clients.indexOf(oldEntity);
    	clients.set(index, entity);
		writeAll(clients);
		return entity;
    }

    public void deleteById(String id) throws FileNotFoundException, NotFoundException { 
    	ArrayList<ClientEntity> clients = readAll();
    	Optional<ClientEntity> client = clients.stream()
									    		.filter(c -> id.equals(c.getId()))
									    		.findFirst();
    	if(client.isEmpty())
    		throw new NotFoundException("Trying to delete unexisting client with id " + id);
    	clients.remove(client.get());
    	writeAll(clients);
    }
	
	private ArrayList<ClientEntity> readAll() throws FileNotFoundException {
		try {
			return XMLSerialization.deserializeXML(getDataPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("Trying to read data from file, but file not found at " + getDataPath());
		}
	}

	private void writeAll(ArrayList<ClientEntity> clients) throws FileNotFoundException {
		try {
			XMLSerialization.serializeXML(clients, getDataPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("Trying to write data in directory, but directory not found at " + getDataPath());
		}
	}
	
	private String getDataPath() {
	    String base = System.getProperty("catalina.base");
	    return base + File.separator + Config.get("file.root.path")
	                + File.separator + Config.get("file.name.clients");
	}
}
