package net.workingdeveloper.java.spring.statemachine.dumper.testdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Service;

/**
 * Created by Christoph Graupner on 8/22/16.
 *
 * @author Christoph Graupner <ch.graupner@workingdeveloper.net>
 */
@Service
public class AllStates {
    public enum States {
        SI, S1, S2, SE, S3_I, S3_E, S3_1, S4, S4_1_I, S4_1_E, S4_1_1, S4_2_I, S4_2_E, S4_2_1, S4_1_SHD, S4_2_SHS, S5_1_CHOICE, S5, S5_1_I, S5_1_E, S5_1_CHOICE_1, S5_1_CHOICE_2, S5_1_AFTER_CHOICE, S5_2_I, S5_2_E, S5_2_1, S6_2_FORK, S6_2_FORK_1, S6_2_FORK_2, S6_2_AFTER_FORK, S5_2_JUNCTION, S5_2_JUNCTION_1, S5_2_JUNCTION_2, S5_2_AFTER_JUNCTION, S6, S6_2_I, S6_2_E, S6_2_1, S6_1_I, S6_1_E, S6_1_1, S6_2_FORK_SUB, S6_2_FORK_SUB_1_I, S6_2_FORK_SUB_1_E, S6_2_FORK_SUB_2_I, S6_2_FORK_SUB_2_E, S6_2_FORK_SUB_1_1, S6_2_FORK_SUB_2_1, S3_2, S3
    }

    public enum Events {
        E2, E_SI_S1, E_S1_S2, E_S2_S3, E_S3_S4, E_S4_SE, E_S4_S5, E_S5_S6, E_S6_SE, E_S3_I__S3_1, E_S3_2__S3_E, E_S3_1__S3_2, E1
    }

    @Autowired
    private ApplicationContext appContext;

    public StateMachine<AllStates.States, AllStates.Events> buildMachine() throws Exception {
        StateMachineBuilder.Builder<AllStates.States, AllStates.Events> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
               .withConfiguration()
               .taskExecutor(new SyncTaskExecutor())
        ;

        builder.configureStates()
               .withStates()
               .initial(States.SI)
               .end(States.SE)
               .state(States.SI)
               .state(States.S1)
               .state(States.S2)
               .state(States.S3)
               .state(States.S4)
               .state(States.S5)
               .state(States.S6)
               .and()

               .withStates()
               .parent(States.S3)
               .initial(States.S3_I)
               .end(States.S3_E)
               .state(States.S3_1)
               .state(States.S3_2)
               .state(States.S3_I)
               .state(States.S3_E)

               .and()
               //S4 intern

               .withStates()
               .parent(States.S4)
               .initial(States.S4_1_I)
               .end(States.S4_1_E)
               .state(States.S4_1_1)
               .history(States.S4_1_SHD, StateConfigurer.History.DEEP)
               .and()

               .withStates()
               .parent(States.S4)
               .initial(States.S4_2_I)
               .end(States.S4_2_E)
               .state(States.S4_2_1)
               .history(States.S4_2_SHS, StateConfigurer.History.SHALLOW)

               .and()
               //S5 intern

               .withStates()
               .parent(States.S5)
               .initial(States.S5_1_I)
               .end(States.S5_1_E)
               .choice(States.S5_1_CHOICE)
               .state(States.S5_1_CHOICE_1)
               .state(States.S5_1_CHOICE_2)
               .state(States.S5_1_AFTER_CHOICE)
               .and()

               .withStates()
               .parent(States.S5)
               .initial(States.S5_2_I)
               .end(States.S5_2_E)
               .junction(States.S5_2_JUNCTION)
               .state(States.S5_2_JUNCTION_1)
               .state(States.S5_2_JUNCTION_2)
               .state(States.S5_2_AFTER_JUNCTION)

               .and()
               //S6 intern
               .withStates()
               .parent(States.S6)
               .initial(States.S6_1_I)
               .end(States.S6_1_E)
               .state(States.S6_1_1)

               .and()

               .withStates()
               .parent(States.S6)
               .initial(States.S6_2_I)
               .end(States.S6_2_E)
               .state(States.S6_2_1)
               .fork(States.S6_2_FORK)
               .state(States.S6_2_FORK_SUB)
               .state(States.S6_2_AFTER_FORK)
               .and()
               .withStates()
               .parent(States.S6_2_FORK_SUB)
               .initial(States.S6_2_FORK_SUB_1_I)
               .end(States.S6_2_FORK_SUB_1_E)
               .state(States.S6_2_FORK_SUB_1_1)
               .and()
               .withStates()
               .parent(States.S6_2_FORK_SUB)
               .initial(States.S6_2_FORK_SUB_2_I)
               .end(States.S6_2_FORK_SUB_2_E)
               .state(States.S6_2_FORK_SUB_2_1)

        ;

        builder.configureTransitions()
               .withExternal()
               .source(States.SI).target(States.S1)
               .event(Events.E_SI_S1)
               .and()
               .withExternal()
               .source(States.S1).target(States.S2)
               .event(Events.E_S1_S2)
               .and()
               .withExternal()
               .source(States.S2).target(States.S3)
               .event(Events.E_S2_S3)
               .and()
               .withExternal()
               .source(States.S3).target(States.S4)
               .event(Events.E_S3_S4)
               .and()
               .withExternal()
               .source(States.S4).target(States.S5)
               .event(Events.E_S4_S5)
               .and()
               .withExternal()
               .source(States.S5).target(States.S6)
               .event(Events.E_S5_S6)
               .and()
               .withExternal()
               .source(States.S6).target(States.SE)
               .event(Events.E_S6_SE)
        .and()
               // S3
               .withExternal()
               .source(States.S3_I).target(States.S3_1)
               .event(Events.E_S3_I__S3_1)
               .and()
               .withLocal()
               .source(States.S3_1).target(States.S3_2)
               .event(Events.E_S3_1__S3_2)
               .and()
               .withExternal()
               .source(States.S3_2).target(States.S3_E)
               .event(Events.E_S3_2__S3_E)
        .and()
               //S5
               .withChoice()
               .source(States.S5_1_CHOICE)
               .first(States.S5_1_CHOICE_1, new Guard<States, Events>() {
                   @Override
                   public boolean evaluate(StateContext<States, Events> context) {
                       return false;
                   }
               })
               .then(States.S5_1_CHOICE_2, new Guard<States, Events>() {
                   @Override
                   public boolean evaluate(StateContext<States, Events> context) {
                       return false;
                   }
               })
               .last(States.S5_1_AFTER_CHOICE)

               .and()
               .withJunction()
               .source(States.S5_2_JUNCTION)
               .first(States.S5_2_JUNCTION_1, new Guard<States, Events>() {
                   @Override
                   public boolean evaluate(StateContext<States, Events> context) {
                       return false;
                   }
               })
               .then(States.S5_2_JUNCTION_2, new Guard<States, Events>() {
                   @Override
                   public boolean evaluate(StateContext<States, Events> context) {
                       return false;
                   }
               })
               .last(States.S5_2_AFTER_JUNCTION)
               .and()
               .withFork()
               .source(States.S6_2_FORK)
               .target(States.S6_2_FORK_SUB_1_I)
               .target(States.S6_2_FORK_SUB_2_I)
        ;
        return builder.build();
    }
}
