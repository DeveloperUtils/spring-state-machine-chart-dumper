package net.workingdeveloper.java.spring.statemachine.dumper.strategy;

/**
 * Created by Christoph Graupner on 2016-09-07.
 *
 * @author Christoph Graupner <ch.graupner@workingdeveloper.net>
 */
public class FullClassNameNamingStrategy implements INamingStrategy {
    @Override
    public String getName(Object aO) throws Exception {
        return aO.getClass().getName();
    }
}
