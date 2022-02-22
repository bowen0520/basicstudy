package factory_model.pizzastore.method;

import factory_model.pizzastore.pizza.NYCheesePizza;
import factory_model.pizzastore.pizza.NYPepperPizza;
import factory_model.pizzastore.pizza.Pizza;

public class NYOrderPizza extends OrderPizza{
    @Override
	Pizza createPizza(String ordertype){
        Pizza pizza=null;

        if(ordertype.equals("cheese")){
            pizza=new NYCheesePizza();
        }else if(ordertype.equals("pepper")){
            pizza=new NYPepperPizza();
        }
        return pizza;
    }
}
