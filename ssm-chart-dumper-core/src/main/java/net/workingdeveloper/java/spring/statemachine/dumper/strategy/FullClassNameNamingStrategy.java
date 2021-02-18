package net.workingdeveloper.java.spring.statemachine.dumper.strategy;

public class FullClassNameNamingStrategy implements INamingStrategy {
    @Override
    public String getName(Object aO) throws Exception {
        return aO.getClass().getName();
    }
}
