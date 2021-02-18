package net.workingdeveloper.java.spring.statemachine.dumper;

import net.workingdeveloper.java.spring.statemachine.dumper.strategy.*;
import org.springframework.statemachine.StateMachine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

abstract public class SsmDumper<S, E> {
    INamingStrategy fNamingStrategyAction;
    INamingStrategy fNamingStrategyGuard;
    StateMachine<S, E> fStateMachine;

    public SsmDumper(StateMachine<S, E> aStateMachine, INamingStrategy aNamingStrategyAction, INamingStrategy aNamingStrategyGuard) {
        fNamingStrategyAction = aNamingStrategyAction;
        fNamingStrategyGuard = aNamingStrategyGuard;
        fStateMachine = aStateMachine;
    }

    public SsmDumper(StateMachine<S, E> aStateMachine) {
        this(aStateMachine, new ChainedNamingStrategy(new INamingStrategy[]{
                     new GetFromMethodNamingStrategy("getName"),
                     new SimpleClassNameNamingStrategy(),
                     new GuessFromEnclosingMethodNamingStrategy(),
                     new ToStringNamingStrategy()
             }),
             new ChainedNamingStrategy(new INamingStrategy[]{
                     new GetFromMethodNamingStrategy("getName"),
                     new SimpleClassNameNamingStrategy(),
                     new GuessFromEnclosingMethodNamingStrategy(),
                     new ToStringNamingStrategy()
             })
        );
    }

    abstract public String asString();

    abstract public <T extends SsmDumper> T dump();

    public <T extends SsmDumper> T dump(File aFile) throws IOException {
        dump().save(aFile);
        return (T) this;
    }

    public INamingStrategy getNamingStrategyAction() {
        return fNamingStrategyAction;
    }

    public void setNamingStrategyAction(INamingStrategy aNamingStrategyAction) {
        fNamingStrategyAction = aNamingStrategyAction;
    }

    public INamingStrategy getNamingStrategyGuard() {
        return fNamingStrategyGuard;
    }

    public void setNamingStrategyGuard(INamingStrategy aNamingStrategyGuard) {
        fNamingStrategyGuard = aNamingStrategyGuard;
    }

    public StateMachine<S, E> getStateMachine() {
        return fStateMachine;
    }

    public void save(File aFile) throws IOException {
        Files.write(aFile.toPath(), asString().getBytes());
    }
}
