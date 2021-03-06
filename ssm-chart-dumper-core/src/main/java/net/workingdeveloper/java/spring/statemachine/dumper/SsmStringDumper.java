package net.workingdeveloper.java.spring.statemachine.dumper;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

/**
 * Created by Christoph Graupner on 8/16/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
@Service
public class SsmStringDumper<S, E> extends SsmDumper<S, E> {

    private String fResult;

    public SsmStringDumper(StateMachine<S, E> aStateMachine) {
        super(aStateMachine);
    }

    @Override
    public String asString() {
        return fResult;
    }

    @Override
    public <T extends SsmDumper> T dump() {
        Collection<State<S, E>> lStates =
                fStateMachine.getStates();
        fResult = processStates(lStates);
        return (T) this;
    }

    private String processStates(Collection<State<S, E>> aStates) {
        StringBuilder sb = new StringBuilder();
        for (State<S, E> lState : aStates) {
            sb.append(lState.getId()).append('{');

            if (lState.isComposite()) {
                sb.append("Composite");
            }
            if (lState.isOrthogonal()) {
                sb.append("Ortho");
            }
            if (lState.isSimple()) {
                sb.append("Simple");
            }
            if (lState.isSubmachineState()) {
                sb.append("hasSubMachine{");
                sb.append(processSubMachine(lState));
                sb.append('}');
            }
            sb.append('}');
        }
        return sb.toString();
    }

    private String processSubMachine(State<S, E> aState) {
        StringBuilder           sb      = new StringBuilder();
        Collection<State<S, E>> lStates = aState.getStates();
        lStates.remove(aState);
        return processStates(lStates);
    }
}
