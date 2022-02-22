package factory_model.pizzastore.simplefactory;

import factory_model.pizzastore.pizza.CheesePizza;
import factory_model.pizzastore.pizza.GreekPizza;
import factory_model.pizzastore.pizza.PepperPizza;
import factory_model.pizzastore.pizza.Pizza;

public class SimplePizzaFactory{
    public Pizza CreatePizza(String ordertype){
        Pizza pizza=null;

        if(ordertype.equals("cheese")){
            pizza=new CheesePizza();
        }else if(ordertype.equals("greek")){
            pizza=new GreekPizza();
        }else if(ordertype.equals("pepper")){
            pizza=new PepperPizza();
        }
        return pizza;
    }
}
