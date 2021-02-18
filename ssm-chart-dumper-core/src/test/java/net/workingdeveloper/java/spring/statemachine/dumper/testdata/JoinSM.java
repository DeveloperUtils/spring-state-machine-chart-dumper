package net.workingdeveloper.java.spring.statemachine.dumper.testdata;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

public class JoinSM {
    @Configuration
    @EnableStateMachine
    public static class Config15
            extends EnumStateMachineConfigurerAdapter<Config15.States2, Config15.Events> {
        public enum States2 {S3F, S5, S1, S3, S4, S2I, S21, S22, S3I, S31, S32, S6, S2F}

        public enum Events {E_S2I__S21, E_S21__S22, E_S22__S2F, E_S3I__S31, E_S31__S32, E_S32__S3F, E_S3__S4, E_S4__S5, E_S5__S6, E_S1__S3}

        @Override
        public void configure(StateMachineStateConfigurer<States2, Events> states)
                throws Exception {
            states
                    .withStates()
                    .initial(States2.S1, new Action<States2, Events>() {
                        @Override
                        public void execute(StateContext<States2, Events> context) {
                            ;
                        }

                        public String getName() {
                            return "testEntry";
                        }
                    })
                    .state(States2.S3)
                    .join(States2.S4)
                    .state(States2.S5, new Action<States2, Events>() {
                        @Override
                        public void execute(StateContext<States2, Events> context) {

                        }

                        public String getName() {
                            return "testEntryS5";
                        }
                    }, new Action<States2, Events>() {
                        @Override
                        public void execute(StateContext<States2, Events> context) {

                        }

                        protected String getName() {
                            return "testExit";
                        }
                    })
                    .state(States2.S6)
                    .and()
                    .withStates()
                    .parent(States2.S3)
                    .initial(States2.S2I)
                    .state(States2.S21)
                    .state(States2.S22)
                    .end(States2.S2F)
                    .and()
                    .withStates()
                    .parent(States2.S3)
                    .initial(States2.S3I)
                    .state(States2.S31)
                    .state(States2.S32)
                    .end(States2.S3F);
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<States2, Events> transitions)
                throws Exception {
            transitions
                    .withJoin()
                    .source(States2.S2F)
                    .source(States2.S3F)
                    .target(States2.S4); //this is wrong in doc, there is S5, that is not declared in states
            transitions
                    .withExternal()
                    .source(States2.S1).target(States2.S3)
                    .event(Events.E_S1__S3)
                    .action(new Action<States2, Events>() {
                        @Override
                        public void execute(StateContext<States2, Events> context) {
                            ;
                        }

                        public String getName() {
                            return "testTransition";
                        }
                    })
                    .and()
                    .withExternal()
                    .source(States2.S3).target(States2.S4)
                    .event(Events.E_S3__S4)
                    .and()
                    .withExternal()
                    .source(States2.S4).target(States2.S5)
                    .event(Events.E_S4__S5)
                    .and()
                    .withExternal()
                    .source(States2.S5).target(States2.S6)
                    .event(Events.E_S5__S6)
            ;
            transitions
                    .withExternal()
                    .source(States2.S2I).target(States2.S21)
                    .event(Events.E_S2I__S21)
                    .and()
                    .withExternal()
                    .source(States2.S21).target(States2.S22)
                    .event(Events.E_S21__S22)
                    .and()
                    .withExternal()
                    .source(States2.S22).target(States2.S2F)
                    .event(Events.E_S22__S2F)
            ;
            transitions
                    .withExternal()
                    .source(States2.S3I).target(States2.S31)
                    .event(Events.E_S3I__S31)
                    .and()
                    .withExternal()
                    .source(States2.S31).target(States2.S32)
                    .event(Events.E_S31__S32)
                    .and()
                    .withExternal()
                    .source(States2.S32).target(States2.S3F)
                    .event(Events.E_S32__S3F)
            ;
        }
    }

    @Configuration
    @EnableStateMachine
    public static class Config14
            extends EnumStateMachineConfigurerAdapter<Config14.States2, Config14.Events> {
        public enum States2 {S3F, S5, S1, S3, S4, S2I, S21, S22, S3I, S31, S32, S6, S2, S2F}

        public enum Events {E_S2I__S21, E_S21__S22, E_S22__S2F, E_S3I__S31, E_S31__S32, E_S32__S3F, E_S3__S4, E_S4__S5, E_S5__S6, E_S1__S2, E_S2__S3, E_S1__S3}

        @Override
        public void configure(StateMachineStateConfigurer<States2, Events> states)
                throws Exception {
            states
                    .withStates()
                    .initial(States2.S1)
                    .fork(States2.S2)
                    .state(States2.S3)
                    .join(States2.S4)
                    .state(States2.S5)
                    .state(States2.S6)
                    .and()
                    .withStates()
                    .parent(States2.S3)
                    .initial(States2.S2I)
                    .state(States2.S21)
                    .state(States2.S22)
                    .end(States2.S2F)
                    .and()
                    .withStates()
                    .parent(States2.S3)
                    .initial(States2.S3I)
                    .state(States2.S31)
                    .state(States2.S32)
                    .end(States2.S3F);
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<States2, Events> transitions)
                throws Exception {
            transitions
                    .withFork()
                    .source(States2.S2)
                    .target(States2.S2I)
                    .target(States2.S3I);
            transitions
                    .withJoin()
                    .source(States2.S2F)
                    .source(States2.S3F)
                    .target(States2.S4); //this is wrong in doc, there is S5, that is not declared in states
            transitions
                    .withExternal()
                    .source(States2.S1).target(States2.S2)
                    .event(Events.E_S1__S2)
                    .and()
                    .withExternal()
                    .source(States2.S3).target(States2.S4)
                    .event(Events.E_S3__S4)
                    .and()
                    .withExternal()
                    .source(States2.S4).target(States2.S5)
                    .event(Events.E_S4__S5)
                    .and()
                    .withExternal()
                    .source(States2.S5).target(States2.S6)
                    .event(Events.E_S5__S6)
            ;
            transitions
                    .withExternal()
                    .source(States2.S2I).target(States2.S21)
                    .event(Events.E_S2I__S21)
                    .and()
                    .withExternal()
                    .source(States2.S21).target(States2.S22)
                    .event(Events.E_S21__S22)
                    .and()
                    .withExternal()
                    .source(States2.S22).target(States2.S2F)
                    .event(Events.E_S22__S2F)
            ;
            transitions
                    .withExternal()
                    .source(States2.S3I).target(States2.S31)
                    .event(Events.E_S3I__S31)
                    .and()
                    .withExternal()
                    .source(States2.S31).target(States2.S32)
                    .event(Events.E_S31__S32)
                    .and()
                    .withExternal()
                    .source(States2.S32).target(States2.S3F)
                    .event(Events.E_S32__S3F)
            ;
        }
    }
}
