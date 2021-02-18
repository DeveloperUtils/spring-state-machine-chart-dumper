package net.workingdeveloper.java.spring.statemachine.dumper.strategy;

/**
 * Created by Christoph Graupner on 2016-09-07.
 *
 * @author Christoph Graupner <ch.graupner@workingdeveloper.net>
 */
public interface INamingStrategy {
    String getName(Object aO) throws Exception;
}
