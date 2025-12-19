import java.io.IOException;

import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.order.OrderServer;

public class ServiceServerMain {

	public static void main(String[] args) throws NotFoundException {
		try {
			OrderServer orderServer = new OrderServer();
			orderServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
