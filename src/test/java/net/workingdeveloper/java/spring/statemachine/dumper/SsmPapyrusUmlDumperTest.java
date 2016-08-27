package net.workingdeveloper.java.spring.statemachine.dumper;

import net.workingdeveloper.java.spring.statemachine.dumper.testdata.AllStates;
import net.workingdeveloper.java.spring.statemachine.dumper.testdata.SMHierarch1;
import net.workingdeveloper.java.spring.statemachine.dumper.testdata.T1;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * Created by Christoph Graupner on 8/20/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {T1.class, SMHierarch1.class, AllStates.class})
public class SsmPapyrusUmlDumperTest {

    @Autowired
    private ApplicationContext fApplicationContext;

    @Test
    @Ignore
    public void dump() throws Exception {
        SsmPapyrusUmlDumper<T1.States, T1.Events> sut = new SsmPapyrusUmlDumper<>((new T1()).buildMachine());
        sut.dump();
        System.out.println(sut.asString());
    }

    @Test
    public void dumpAll() throws Exception {
        StateMachine<AllStates.States, AllStates.Events> lSM = (new AllStates()).buildMachine();
        SsmPapyrusUmlDumper<AllStates.States, AllStates.Events> sut = new SsmPapyrusUmlDumper<>(
                lSM
        );

        sut.dump();
        System.out.println(sut.asString());
        sut.save(new File("/home/christoph/workspace/papyrus/test/test_model"));
    }

    @Test
    @Ignore
    public void dumpHierarchy() throws Exception {
        SsmPapyrusUmlDumper<SMHierarch1.States, SMHierarch1.Events> sut = new SsmPapyrusUmlDumper<>(
                fApplicationContext.getBean(SMHierarch1.class).buildMachine()
        );
        sut.dump();
        System.out.println(sut.asString());
    }
}
