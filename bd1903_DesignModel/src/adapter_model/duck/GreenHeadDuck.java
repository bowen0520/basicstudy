package adapter_model.duck;

public class GreenHeadDuck implements Duck{
    @Override
    public void quack(){
        System.out.println(" Ga Ga");
    }

    @Override
    public void fly(){
        System.out.println("I am flying a long distance");
    }
}
