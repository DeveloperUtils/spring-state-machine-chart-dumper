package net.workingdeveloper.java.spring.statemachine.dumper.testdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;

import java.util.EnumSet;

/**
 * Created by Christoph Graupner on 8/16/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
@Configuration
public class T1 {
    public enum States {
        SI, S1, S2, SE, S3
    }

    public enum Events {
        E2, E1
    }

    @Autowired
    private ApplicationContext appContext;

//    public T1(ApplicationContext aAppContext) {
//        appContext = aAppContext;
//    }

    public StateMachine<States, Events> buildMachine() throws Exception {
        StateMachineBuilder.Builder<States, Events> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
               .withConfiguration()
               .taskExecutor(new SyncTaskExecutor())
        ;

        builder.configureStates()
               .withStates()
               .initial(States.SI)
               .state(States.S1, new Action<States, Events>() {
                   @Override
                   public void execute(StateContext<States, Events> context) {
                       System.out.print("test");
                   }
               }, null)
               .states(EnumSet.allOf(States.class))
        ;

        builder.configureTransitions()
               .withExternal()
               .source(States.SI).target(States.S1)
               .event(Events.E1)
               .and()
               .withLocal()
               .source(States.S2).target(States.S3)
               .event(Events.E1)
               .and()
               .withExternal()
               .source(States.S2).target(States.SE)
               .event(Events.E2)
               .and()
               .withExternal()
               .source(States.S1).target(States.SE)
               .event(Events.E2);

        return builder.build();
    }

    public StateMachine<States, Events> buildMachineNoTransition() throws Exception {
        StateMachineBuilder.Builder<States, Events> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
               .withConfiguration()
               .taskExecutor(new SyncTaskExecutor())
        ;

        builder.configureStates()
               .withStates()
               .initial(States.SI)
               .state(States.S1)
               .states(EnumSet.allOf(States.class))
        ;


        return builder.build();
    }

    public StateMachine<States, Events> buildMachineOnlyNormalStates() throws Exception {
        StateMachineBuilder.Builder<States, Events> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
               .withConfiguration()
               .taskExecutor(new SyncTaskExecutor())
        ;

        builder.configureStates()
               .withStates()
               .states(EnumSet.allOf(States.class))
        ;


        return builder.build();
    }

}
