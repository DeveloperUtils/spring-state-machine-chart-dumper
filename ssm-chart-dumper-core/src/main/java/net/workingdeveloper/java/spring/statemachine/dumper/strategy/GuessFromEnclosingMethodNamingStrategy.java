package net.workingdeveloper.java.spring.statemachine.dumper.strategy;

import java.lang.reflect.Method;

public class GuessFromEnclosingMethodNamingStrategy implements INamingStrategy {
    @Override
    public String getName(Object aO) throws Exception {
        Class<?> aClass = aO.getClass();
        if (aClass.isAnonymousClass() || aClass.isLocalClass()) {
            Method lMethod = aClass.getEnclosingMethod();
            if (lMethod != null)
                return lMethod.getName();
        }
        return null;
    }
}
