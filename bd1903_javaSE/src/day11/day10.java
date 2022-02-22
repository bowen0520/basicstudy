package day11;

public class day10 {

}
abstract class Role{
	private String name;
	private int age;
	private boolean genden;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public boolean isGenden() {
		return genden;
	}
	public void setGenden(boolean genden) {
		this.genden = genden;
	}
	
	public Role() {
		super();
	}
	public Role(String name, int age, boolean genden) {
		super();
		this.name = name;
		this.age = age;
		this.genden = genden;
	}
	abstract void play(); 
}

class Employee extends Role{
	static String id;
	private double salary;
	
	
	
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Employee(String name, int age, boolean genden) {
		super(name, age, genden);
		// TODO Auto-generated constructor stub
	}

	final void sing() {
		
	}

	@Override
	void play() {
		// TODO Auto-generated method stub
		
	}
}

class Manager extends Employee{
	private final int vehicle = 0;
	
	public static void main(String[] args) {
		Employee e = new Manager();
		e.sing();
		e.play();
	}
}
