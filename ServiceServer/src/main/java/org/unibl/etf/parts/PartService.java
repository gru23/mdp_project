package org.unibl.etf.parts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

import org.unibl.etf.exceptions.NotFoundException;

public class PartService {
	private final PartDAO partDAO;
	
	public PartService() {
		this.partDAO = new PartDAO();
	}
	
	/**
	 * Adding quantity of new adding part to quantity of old part, if old part is not null.
	 * @param part new part to add
	 * @return updated state of part
	 */
	public PartEntity add(PartEntity part) {
		int newQuantity = part.getQuantity();
		PartEntity old = partDAO.getPart(part.getCode());
		if(old != null)
			newQuantity += old.getQuantity();
		part.setQuantity(newQuantity);
		partDAO.savePart(part);
		return part;
	}
	
	public PartEntity read(String code) throws NotFoundException {
		PartEntity part = partDAO.getPart(code);
		if(part == null)
			throw new NotFoundException("Part with code " + code + " does not exist.");
		return part;
	}
	
	public ArrayList<PartEntity> readAll() {
	    return partDAO.getAllParts()
	            .stream()
	            .sorted(Comparator.comparing(PartEntity::getCode))
	            .collect(Collectors.toCollection(ArrayList::new));
	}
	
	public PartEntity update(String code, PartEntity newPart) throws NotFoundException {
		PartEntity existing = partDAO.getPart(code);
		if (existing == null) {
		    throw new NotFoundException("Part with code " + code + " does not exist.");
		}
		partDAO.savePart(newPart);
		return newPart;

	}
	
	public void delete(String code) throws NotFoundException {
		PartEntity existing = partDAO.getPart(code);
		if (existing == null) {
		    throw new NotFoundException("Part with code " + code + " does not exist.");
		}
		partDAO.deletePart(code);
	}
	
	public PartEntity sellRandomPart() {
		ArrayList<PartEntity> allParts = readAll();
        PartEntity randomPart = allParts.get((new Random()).nextInt(allParts.size()));
        randomPart.setQuantity(randomPart.getQuantity() - 1);
        try {
			update(randomPart.getCode(), randomPart);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
        randomPart.setQuantity(1);
        return randomPart;
	}
}
