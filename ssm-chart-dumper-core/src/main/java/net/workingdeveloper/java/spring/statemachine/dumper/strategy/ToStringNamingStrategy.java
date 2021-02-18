package net.workingdeveloper.java.spring.statemachine.dumper.strategy;

public class ToStringNamingStrategy implements INamingStrategy {
    @Override
    public String getName(Object aO) throws Exception {
        return aO.toString();
    }
}
