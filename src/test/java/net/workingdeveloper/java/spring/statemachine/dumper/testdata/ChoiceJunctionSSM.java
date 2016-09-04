package net.workingdeveloper.java.spring.statemachine.dumper.testdata;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;

/**
 * Created by Christoph Graupner on 2016-09-04.
 *
 * @author Christoph Graupner <ch.graupner@workingdeveloper.net>
 */
public class ChoiceJunctionSSM {
    @Configuration
    @EnableStateMachine
    public static class ConfigChoice
            extends EnumStateMachineConfigurerAdapter<ConfigChoice.States, ConfigChoice.Events> {
        public enum States {S1, SF, S2, S3, S4, SI}

        public enum Events {S3__SF, S4__SF, SI__S1, S2__SF}

        @Override
        public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
            states
                    .withStates()
                    .initial(States.SI)
                    .choice(States.S1)
                    .end(States.SF)
                    .states(EnumSet.allOf(States.class));
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
            super.configure(transitions);
            transitions
                    .withChoice()
                    .source(States.S1)
                    .first(States.S2, s2Guard())
                    .then(States.S3, s3Guard())
                    .last(States.S4);
            transitions
                    .withExternal()
                    .source(States.SI).target(States.S1)
                    .event(Events.SI__S1)
                    .and()
                    .withExternal()
                    .source(States.S2).target(States.SF)
                    .event(Events.S2__SF)
                    .and()
                    .withExternal()
                    .source(States.S3).target(States.SF)
                    .event(Events.S3__SF)
                    .and()
                    .withExternal()
                    .source(States.S4).target(States.SF)
                    .event(Events.S4__SF)
            ;
        }

        private Guard<States, Events> s2Guard() {
            return new Guard<States, Events>() {
                @Override
                public boolean evaluate(StateContext<States, Events> context) {
                    return false;
                }

                public String getName() {
                    return "s2Guard.getName()";
                }
            };
        }

        private Guard<States, Events> s3Guard() {
            return new Guard<States, Events>() {
                @Override
                public boolean evaluate(StateContext<States, Events> context) {
                    return true;
                }
            };
        }
    }

    @Configuration
    @EnableStateMachine
    public static class ConfigJunction
            extends EnumStateMachineConfigurerAdapter<ConfigJunction.States, ConfigJunction.Events> {
        public enum States {S1, SF, S2, S3, S4, S5, SI}

        public enum Events {S3__SF, S4__SF, SI__S1, S5__SF, S2__SF}

        public class NamedGuard implements Guard<States, Events> {

            @Override
            public boolean evaluate(StateContext<States, Events> context) {
                return false;
            }
        }

        @Override
        public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
            states
                    .withStates()
                    .initial(States.SI)
                    .junction(States.S1)
                    .end(States.SF)
                    .states(EnumSet.allOf(States.class));
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
            super.configure(transitions);
            transitions
                    .withJunction()
                    .source(States.S1)
                    .first(States.S2, s2Guard())
                    .then(States.S3, s3Guard())
                    .then(States.S4, new NamedGuard())
                    .last(States.S5);
            transitions
                    .withExternal()
                    .source(States.SI).target(States.S1)
                    .event(Events.SI__S1)
                    .and()
                    .withExternal()
                    .source(States.S2).target(States.SF)
                    .event(Events.S2__SF)
                    .and()
                    .withExternal()
                    .source(States.S3).target(States.SF)
                    .event(Events.S3__SF)
                    .and()
                    .withExternal()
                    .source(States.S4).target(States.SF)
                    .event(Events.S4__SF)
                    .and()
                    .withExternal()
                    .source(States.S5).target(States.SF)
                    .event(Events.S5__SF)
            ;
        }

        private Guard<States, Events> s2Guard() {
            return new Guard<States, Events>() {
                @Override
                public boolean evaluate(StateContext<States, Events> context) {
                    return false;
                }

                public String getName() {
                    return "s2Guard.getName()";
                }
            };
        }

        private Guard<States, Events> s3Guard() {
            return new Guard<States, Events>() {
                @Override
                public boolean evaluate(StateContext<States, Events> context) {
                    return true;
                }
            };
        }
    }
}
