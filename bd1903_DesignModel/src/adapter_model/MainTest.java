package adapter_model;

import adapter_model.adapter.TurkeyAdapter2;
import adapter_model.duck.Duck;
import adapter_model.duck.GreenHeadDuck;
import adapter_model.turkey.WildTurkey;

public class MainTest{
    public static void main(String[] args){
        GreenHeadDuck duck=new GreenHeadDuck();

        WildTurkey turkey=new WildTurkey();

        Duck duck2turkeyAdapter=new TurkeyAdapter2();
        turkey.gobble();
        turkey.fly();
        duck.quack();
        duck.fly();
        duck2turkeyAdapter.quack();
        duck2turkeyAdapter.fly();
    }
}
