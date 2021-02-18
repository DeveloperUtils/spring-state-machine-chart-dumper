package net.workingdeveloper.java.spring.statemachine.dumper.strategy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GetFromMethodNamingStrategy implements INamingStrategy {
    private String fNamingMethod;

    public GetFromMethodNamingStrategy(String aNamingMethod) {
        fNamingMethod = aNamingMethod;
    }

    public GetFromMethodNamingStrategy() {
        fNamingMethod = "getName";
    }

    @Override
    public String getName(Object aO) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method lMethod = aO.getClass().getMethod(getNamingMethod());
        lMethod.setAccessible(true);
        Object lRet = lMethod.invoke(aO);
        return lRet.toString();
    }

    public String getNamingMethod() {
        return fNamingMethod;
    }

    public void setNamingMethod(String aNamingMethod) {
        fNamingMethod = aNamingMethod;
    }
}
