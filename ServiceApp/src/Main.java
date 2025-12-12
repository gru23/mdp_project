import chat.UnicastChat;
import enums.AccountStatus;
import enums.AppointmentStatus;
import enums.AppointmentType;
import exceptions.ServerError;
import gui.MainWindow;
import models.Appointment;
import models.Client;
import models.Order;
import services.AppointmentService;
import services.ClientService;
import services.SupplierService;

public class Main {

	public static void main(String[] args) {
		new MainWindow();
//		ClientService clientService = new ClientService();
//		try {
//			clientService.getAllClients().stream().forEach(System.out::println);
////			Client update = new Client("1b42b991-19d8-4da7-962b-04469269e189", "Dragan", "Jovic",
////					"dragan", "dragan123", "Banja Luka, Krfska 11", "065/121-212", "dragan@mail.com", AccountStatus.APPROVED);
////			clientService.updateClient(update.getId(), update);
//			
//			//brisanje klijenta
////			clientService.deleteClient("de0b5579-b8ad-42a8-872c-67a659761b1f");
//			
//			AppointmentService appService = new AppointmentService();
//			//ispis termina
////			appService.getAllApointments().stream().forEach(System.out::println);
////			Appointment appointment = new Appointment("a89c3b3a-160f-4b35-afd2-5d8739963876", 
////					"2026-01-05", "09:00", AppointmentType.REGULAR_SERVICE, AppointmentStatus.REPAIRED, 
////					"Oil and oil filter has been chaged.", "bd900027-81b6-42c4-a809-9b25130d07ae");
////			appService.updateAppointment(appointment.getId(), appointment);
//			
////			UnicastChat.main(args);
//			
//			//dobijanje svih dobavljaca
//			SupplierService suppService = new SupplierService();
////			suppService.getAllSuppliers().stream().forEach(System.out::println);
//			
//			//slanje narudzbe
////			Order narudzba = suppService.order(new Order("Supplier 1", 2, "nesto id "));
////			System.out.println(narudzba);
//			
//		} catch (ServerError e) {
//			e.printStackTrace();
//		}
	}

}