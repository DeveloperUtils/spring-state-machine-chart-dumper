package net.workingdeveloper.java.spring.statemachine.dumper.strategy;

/**
 * Created by Christoph Graupner on 2016-09-07.
 *
 * @author Christoph Graupner <ch.graupner@workingdeveloper.net>
 */
public class SimpleClassNameNamingStrategy implements INamingStrategy {
    @Override
    public String getName(Object aO) throws Exception {
        String lSimpleName = aO.getClass().getSimpleName();
        return "".equals(lSimpleName) ? null : lSimpleName;
    }
}
