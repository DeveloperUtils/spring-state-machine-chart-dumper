package net.workingdeveloper.java.spring.statemachine.dumper.testdata;

import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.uml.UmlStateMachineModelFactory;
import org.springframework.stereotype.Service;

@Service
public class SMHierarch1 {
    public enum States {
        S1, SE, S1_1, S1_2, S2, S2I, S21, S2F, S3I, S31, S3F, SI

    }

    public enum Events {
        E_S1_SE, E_S1_1__S1_2, E_S1_S1, E_S1_S2, E_S31__S3F, E_S2__SE, E_S3I__S31, E_S21_S21, E_S21_S2F, E_S2I_S21, E_SI_S1

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

    public StateMachine<SMHierarch1.States, SMHierarch1.Events> buildRegionMachine() throws Exception {
        StateMachineBuilder.Builder<SMHierarch1.States, SMHierarch1.Events> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
               .withConfiguration()
               .taskExecutor(new SyncTaskExecutor())
        ;

        builder.configureStates()
               .withStates()
               .initial(States.S1)
               .state(States.S2)
               .end(States.SE)
               .and()
               .withStates()
               .parent(States.S2)
               .initial(States.S2I)
               .state(States.S21)
               .end(States.S2F)
               .and()
               .withStates()
               .parent(States.S2)
               .initial(States.S3I)
               .state(States.S31)
               .end(States.S3F)
        ;

        builder.configureTransitions()
               .withExternal()
               .source(States.S1).target(States.S2)
               .event(Events.E_S1_S2)
               .and()
               .withExternal()
               .source(States.S2I).target(States.S21)
               .event(Events.E_S2I_S21)
               .and()
               .withExternal()
               .source(States.S21).target(States.S2F)
               .event(Events.E_S21_S2F)
               .and()
               .withInternal()
               .source(States.S21)
               .event(Events.E_S21_S21)
               .and()
               .withExternal()
               .source(States.S3I).target(States.S31)
               .event(Events.E_S3I__S31)
               .and()
               .withExternal()
               .source(States.S2).target(States.SE)
               .event(Events.E_S2__SE)
               .and()
               .withExternal()
               .source(States.S31).target(States.S3F)
               .event(Events.E_S31__S3F)
               .and()
               .withInternal()
               .source(States.S31)
               .event(Events.E_S1_S1)
        ;

        return builder.build();
    }

    public StateMachine<String, String> buildUmlMachine(String aPath) throws Exception {
        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();

        builder.configureModel().withModel().factory(new UmlStateMachineModelFactory(
                aPath
        ));
        builder.configureConfiguration()
               .withConfiguration()
               .taskExecutor(new SyncTaskExecutor())
        ;
        return builder.build();
    }
}
