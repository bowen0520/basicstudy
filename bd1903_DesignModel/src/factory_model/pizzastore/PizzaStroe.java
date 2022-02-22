package factory_model.pizzastore;



public class PizzaStroe {
	/*public static void main(String[] args) {

		OrderPizza mOrderPizza;
		mOrderPizza=new	OrderPizza();
		int i = 1;
		System.out.println(i = 2);
	}*/

	public static void main(String[] args) {
		System.out.println("value = " + switchIt(4));
	}

	public static int switchIt(int x) {
		int j = 1;
		switch (x) {
			case 1: j++;
			case 2: j++;
			case 3: j++;
			case 4: j++;
			case 5: j++;
			default: j++;
		}
		return j + x;
	}
}
