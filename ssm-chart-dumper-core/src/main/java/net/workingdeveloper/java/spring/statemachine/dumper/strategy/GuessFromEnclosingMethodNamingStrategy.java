package net.workingdeveloper.java.spring.statemachine.dumper.strategy;

import java.lang.reflect.Method;

/**
 * Created by Christoph Graupner on 2016-09-07.
 *
 * @author Christoph Graupner <ch.graupner@workingdeveloper.net>
 */
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
