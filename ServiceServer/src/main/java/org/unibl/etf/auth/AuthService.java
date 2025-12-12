package org.unibl.etf.auth;

import java.io.FileNotFoundException;
import java.util.Optional;

import org.unibl.etf.client.AccountStatus;
import org.unibl.etf.client.Client;
import org.unibl.etf.client.ClientDAO;
import org.unibl.etf.client.ClientEntity;
import org.unibl.etf.client.ClientRequest;
import org.unibl.etf.client.login.LoginRequest;
import org.unibl.etf.exceptions.DuplicateException;
import org.unibl.etf.exceptions.InternalServerError;
import org.unibl.etf.exceptions.InvalidCredentialsException;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.vehicle.VehicleDAO;
import org.unibl.etf.vehicle.VehicleEntity;

public class AuthService {
	private final ClientDAO clientDAO;
	private final VehicleDAO vehicleDAO;
	
	public AuthService() {
		this.clientDAO = new ClientDAO();
		this.vehicleDAO = new VehicleDAO();
	}
	
	
	//add some password crypting
	public Client login(LoginRequest request) throws InternalServerError, InvalidCredentialsException {
		try {
			Optional<ClientEntity> optionalEntity = clientDAO.findByUsername(request.getUsername());
			//throws exception if username does not exist, exist but password is invalid or account is not approved
			if(optionalEntity.isEmpty() 
					|| !isPasswordValid(optionalEntity.get(), request.getPassword())
					|| !optionalEntity.get().getStatus().equals(AccountStatus.APPROVED))
				throw new InvalidCredentialsException("Entered credentials are invalid!");
			return new Client(optionalEntity.get());
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new InternalServerError();
		}
	}
	
	public Client registration(ClientRequest request, VehicleEntity vehicle) throws InternalServerError, DuplicateException {
		ClientEntity result = null;
		try {
			if(isUsernameDuplicate(request.getUsername()))
				throw new DuplicateException("Username is already taken.");
			//additional validation of request if needed
			ClientEntity entity = clientDAO.save(new ClientEntity(request));
			VehicleEntity newVehicle = new VehicleEntity(vehicle);
			newVehicle.setClientId(entity.getId());
			VehicleEntity savedVehicle = vehicleDAO.save(newVehicle);
			//adding vehicle's id to client
			entity.setVehicleId(savedVehicle.getId());
			result = clientDAO.update(entity.getId(), entity);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new InternalServerError();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Client(result);
	}
	
	private boolean isPasswordValid(ClientEntity client, String enteredPassword) {
		return enteredPassword.equals(client.getPassword());
	}
	
	private boolean isUsernameDuplicate(String username) throws FileNotFoundException {
		Optional<ClientEntity> optionalEntity = clientDAO.findByUsername(username);
		return optionalEntity.isPresent();
	}
}
