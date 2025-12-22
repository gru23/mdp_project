package org.unibl.etf.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.unibl.etf.rmi.model.Bill;

public interface BookkeepingInterface extends Remote {
	
	void bookOrder(Bill bill) throws RemoteException;
}
