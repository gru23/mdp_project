import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.parts.PartEntity;
import org.unibl.etf.parts.PartService;

public class Main {

	public static void main(String[] args) throws NotFoundException {
		PartService partService = new PartService();

		// 1. Brake Pad
//		PartEntity part1 = new PartEntity("BR123", "Brake Pad", "Bosch", 49.99, 20, "Front brake pad");
//		partService.add(part1);
//
//		// 2. Oil Filter
//		PartEntity part2 = new PartEntity("OF456", "Oil Filter", "Mann", 15.50, 50, "Engine oil filter");
//		partService.add(part2);
//
//		// 3. Air Filter
//		PartEntity part3 = new PartEntity("AF789", "Air Filter", "K&N", 25.00, 30, "High performance air filter");
//		partService.add(part3);
//
//		// 4. Spark Plug
//		PartEntity part4 = new PartEntity("SP321", "Spark Plug", "NGK", 8.99, 100, "Standard spark plug for gasoline engines");
//		partService.add(part4);
//
//		// 5. Battery
//		PartEntity part5 = new PartEntity("BAT654", "Car Battery", "Varta", 120.00, 15, "12V 60Ah car battery");
//		partService.add(part5);
		
		
		partService.delete("BAT654");
		PartEntity partUpdate = new PartEntity("UP003", "Air Filter", "K&N", 25.00, 30, "High performance air filter");
		partService.update("UP003", partUpdate);

		partService.readAll().stream().forEach(System.out::println);
	}

}
