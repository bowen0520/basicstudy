package adapter_model.adapter;

import adapter_model.duck.Duck;
import adapter_model.turkey.Turkey;

public class TurkeyAdapter implements Duck{
    private Turkey turkey;

    public TurkeyAdapter(Turkey turkey){
        this.turkey=turkey;
    }

    @Override
    public void quack(){
        turkey.gobble();
    }

    @Override
    public void fly(){
        for(int i=0;i<6;i++){
            turkey.fly();
        }
    }
}
