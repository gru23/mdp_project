package servicerclientapp.models;

public class Supplier {
	private String name;

	public Supplier() {
		super();
	}

	public Supplier(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Supplier [name=" + name + "]";
	}
}
