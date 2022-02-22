package factory_model.pizzastore.method;

import factory_model.pizzastore.pizza.Pizza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class OrderPizza{
    public OrderPizza(){
        Pizza pizza;
        String ordertype;

        do{
            ordertype=gettype();
            pizza=createPizza(ordertype);

            pizza.prepare();
            pizza.bake();
            pizza.cut();
            pizza.box();
        }while(true);
    }

    abstract Pizza createPizza(String ordertype);

    private String gettype(){
        try{
            BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));
            System.out.println("input pizza type:");
			return strin.readLine();
        }catch(IOException e){
            e.printStackTrace();
            return "";
        }
    }
}
