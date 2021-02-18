package net.workingdeveloper.java.spring.statemachine.dumper.strategy;

public class SimpleClassNameNamingStrategy implements INamingStrategy {
    @Override
    public String getName(Object aO) throws Exception {
        String lSimpleName = aO.getClass().getSimpleName();
        return "".equals(lSimpleName) ? null : lSimpleName;
    }
}
