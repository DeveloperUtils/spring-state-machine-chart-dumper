package net.workingdeveloper.java.spring.statemachine.dumper;

import org.springframework.statemachine.StateMachine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

    abstract public String asString();

    abstract public <T extends SsmDumper> T dump();

    public <T extends SsmDumper> T dump(File aFile) throws IOException {
        dump().save(aFile);
        return (T) this;
    }

    public StateMachine<S, E> getStateMachine() {
        return fStateMachine;
    }

    public void save(File aFile) throws IOException {
        Files.write(aFile.toPath(), asString().getBytes());
    }
}
