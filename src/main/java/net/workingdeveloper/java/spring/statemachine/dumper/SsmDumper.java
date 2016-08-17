package net.workingdeveloper.java.spring.statemachine.dumper;

import org.springframework.statemachine.StateMachine;

/**
 * Created by Christoph Graupner on 8/16/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
abstract public class SsmDumper<S, E> {

    StateMachine<S, E> fStateMachine;

    public SsmDumper(StateMachine<S, E> aStateMachine) {
        fStateMachine = aStateMachine;
    }
}
