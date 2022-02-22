package factory_model.pizzastore.absfactory;

import factory_model.pizzastore.pizza.Pizza;

public interface AbsFactory{
    public Pizza CreatePizza(String ordertype);
}
