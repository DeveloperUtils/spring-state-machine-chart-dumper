package net.workingdeveloper.java.spring.statemachine.dumper.testdata;

import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by Christoph Graupner on 8/17/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
@Service
public class SMHierarch1 {
    public enum States {
        S1, SE, S1_1, S1_2, SI

    }

    public enum Events {
        E_S1_SE, E_S1_1__S1_2, E_S1_S1, E_SI_S1

    }

    public StateMachine<SMHierarch1.States, SMHierarch1.Events> buildMachine() throws Exception {
        StateMachineBuilder.Builder<SMHierarch1.States, SMHierarch1.Events> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
               .withConfiguration()
               .taskExecutor(new SyncTaskExecutor())
        ;

        builder.configureStates()
               .withStates()
               .initial(States.SI)
               .state(States.S1)
               .end(States.SE)

               .and()
               .withStates()
               .parent(States.S1)
               .initial(States.S1_1)
               .state(States.S1_1)
               .state(States.S1_2)
        ;

        builder.configureTransitions()
               .withExternal()
               .source(States.SI).target(States.S1)
               .event(Events.E_SI_S1)
               .and()
               .withInternal()
               .source(States.S1)
               .event(Events.E_S1_S1)
               .and()
               .withExternal()
               .source(States.S1).target(States.SE)
               .event(Events.E_S1_SE)
               .and()
               .withExternal()
               .source(States.S1_1).target(States.S1_2)
               .event(Events.E_S1_1__S1_2)
        ;

        return builder.build();
    }

}
