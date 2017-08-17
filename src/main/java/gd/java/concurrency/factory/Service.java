package gd.java.concurrency.factory;

public class Service {
    String name;

    public Service(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return "Service " + name;
    }
}
