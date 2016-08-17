package net.workingdeveloper.java.spring.statemachine.dumper;

import net.workingdeveloper.java.spring.statemachine.dumper.testdata.SMHierarch1;
import net.workingdeveloper.java.spring.statemachine.dumper.testdata.T1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by Christoph Graupner on 8/16/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {T1.class, SMHierarch1.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
                                     DirtiesContextTestExecutionListener.class,
                                     TransactionalTestExecutionListener.class})
public class SsmStringDumperTest {

    @Autowired
    private ApplicationContext fApplicationContext;

    @Test
    public void dumpHierarchy() throws Exception {
        SsmStringDumper<SMHierarch1.States, SMHierarch1.Events> sut = new SsmStringDumper<>(
                fApplicationContext.getBean(SMHierarch1.class).buildMachine()
        );

        String ret = sut.dump();
        System.out.println(ret);
        assertThat(ret, is(not(emptyOrNullString())));
    }

    @Test
    public void dumpSimple() throws Exception {
        SsmStringDumper<T1.States, T1.Events> sut = new SsmStringDumper<>(
                fApplicationContext.getBean(T1.class).buildMachine()
        );

        String ret = sut.dump();
        System.out.println(ret);
        assertThat(ret, is(not(emptyOrNullString())));
    }
}
