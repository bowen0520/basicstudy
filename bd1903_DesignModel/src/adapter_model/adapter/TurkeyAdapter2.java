package adapter_model.adapter;

import adapter_model.duck.Duck;
import adapter_model.turkey.WildTurkey;

public class TurkeyAdapter2 extends WildTurkey implements Duck{
    @Override
    public void quack(){
        super.gobble();
    }

    @Override
    public void fly(){
        super.fly();
        super.fly();
        super.fly();
    }
}
