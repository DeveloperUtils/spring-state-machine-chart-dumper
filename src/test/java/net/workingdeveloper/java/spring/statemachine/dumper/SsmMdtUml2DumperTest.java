package net.workingdeveloper.java.spring.statemachine.dumper;

import net.workingdeveloper.java.spring.statemachine.dumper.testdata.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * Created by Christoph Graupner on 8/20/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {T1.class, SMHierarch1.class, AllStates.class, JoinSM.class})
public class SsmMdtUml2DumperTest {

    @Autowired
    private ApplicationContext fApplicationContext;

    static <S extends Enum<S>, E extends Enum<E>> StateMachine<S, E> build(EnumStateMachineConfigurerAdapter<S, E> aClass) throws Exception {
        StateMachineBuilder.Builder<S, E> builder = StateMachineBuilder.builder();
        aClass.configure(builder.configureConfiguration());
        aClass.configure(builder.configureModel());
        aClass.configure(builder.configureStates());
        aClass.configure(builder.configureTransitions());
        return builder.build();
    }

    @Test
    @Ignore
    public void dump() throws Exception {
        SsmMdtUml2Dumper<T1.States, T1.Events> sut = new SsmMdtUml2Dumper<>((new T1()).buildMachine());
        sut.dump();
        System.out.println(sut.asString());
    }

    @Test
    public void dumpAll() throws Exception {

        StateMachine<AllStates.States, AllStates.Events> lSM = (new AllStates()).buildMachine();
        SsmMdtUml2Dumper<AllStates.States, AllStates.Events> sut = new SsmMdtUml2Dumper<>(
                lSM
        );

        sut.dump();
        System.out.println(sut.asString());
        sut.save(new File(getBuildPath() + "/test_model_all"));
    }

    @Test
    public void dumpChoiceJunction() throws Exception {
        StateMachine<ChoiceJunctionSSM.ConfigChoice.States, ChoiceJunctionSSM.ConfigChoice.Events> lSM = build(
                new ChoiceJunctionSSM.ConfigChoice());
        SsmMdtUml2Dumper<ChoiceJunctionSSM.ConfigChoice.States, ChoiceJunctionSSM.ConfigChoice.Events> sut = new SsmMdtUml2Dumper<>(
                lSM);

        sut.dump();
        System.out.println(sut.asString());
        sut.save(new File(getBuildPath() + "/test_model_choicejunction"));
    }

    @Test
    public void dumpForkJoin() throws Exception {
        StateMachine<JoinSM.Config14.States2, JoinSM.Config14.Events>     lSM = build(new JoinSM.Config14());
        SsmMdtUml2Dumper<JoinSM.Config14.States2, JoinSM.Config14.Events> sut = new SsmMdtUml2Dumper<>(lSM);

        sut.dump();
        System.out.println(sut.asString());
        sut.save(new File(getBuildPath() + "/test_model_forkjoin"));
    }

    @Test
    @Ignore
    public void dumpHierarchy() throws Exception {
        SsmMdtUml2Dumper<SMHierarch1.States, SMHierarch1.Events> sut = new SsmMdtUml2Dumper<>(
                fApplicationContext.getBean(SMHierarch1.class).buildMachine()
        );
        sut.dump();
        System.out.println(sut.asString());
    }

    @Test
    public void dumpJoin() throws Exception {
        StateMachine<JoinSM.Config15.States2, JoinSM.Config15.Events>     lSM = build(new JoinSM.Config15());
        SsmMdtUml2Dumper<JoinSM.Config15.States2, JoinSM.Config15.Events> sut = new SsmMdtUml2Dumper<>(lSM);

        sut.dump();
        System.out.println(sut.asString());
        sut.save(new File(getBuildPath() + "/test_model_join"));
    }

    private String getBuildPath() {
        return "build/test-results/dumps";
    }
}
