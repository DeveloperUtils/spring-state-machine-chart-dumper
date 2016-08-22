package net.workingdeveloper.java.spring.statemachine.dumper;

import net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.IPapyrusModel;
import net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m.PapyrusModel;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.region.Region;
import org.springframework.statemachine.state.AbstractState;
import org.springframework.statemachine.state.RegionState;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Christoph Graupner on 8/20/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
public class SsmPapyrusUmlDumper<S, E> extends SsmDumper<S, E> {
    IPapyrusModel fModel;
    Map<String, UUID> fUUIDMap = new HashMap<>();

    public SsmPapyrusUmlDumper(StateMachine<S, E> aStateMachine) {
        super(aStateMachine);

    }

    @Override
    public String asString() {
        return fModel.asString();
    }

    @Override
    public <T extends SsmDumper> T dump() {
        try {
            fModel = new PapyrusModel();
        } catch (ParserConfigurationException aE) {
            aE.printStackTrace();
        }

        processMachine(fModel, getStateMachine());
        fModel.save("test");

        return (T) this;
    }

    private void processMachine(IPapyrusModel aSM, StateMachine<S, E> aStateMachine) {
        IPapyrusModel.IPMStateMachine lStateMachine = aSM.getRootState(getStateMachine().getId());
        IPapyrusModel.IPMRegionState  lRootRegion   = lStateMachine.addRegion(aStateMachine.getUuid(), "Root");
        processMachine(lRootRegion, aStateMachine);
    }

    private void processMachine(IPapyrusModel.IPMRegionState aSM, StateMachine<S, E> aStateMachine) {
        processRegion(aSM, aStateMachine, aStateMachine.getInitialState());
    }

    private void processRegion(IPapyrusModel.IPMRegionState aRegionState, Region<S, E> aSERegion, State<S, E> aInitialState) {
        Collection<State<S, E>> lStates = aSERegion.getStates();
        for (State<S, E> lState : lStates) {
            processState(aRegionState, lState);
        }
        processTransitions(aRegionState, aSERegion);
    }

    private void processState(IPapyrusModel.IPMRegionState aParentState, State<S, E> aStateSsm) {
        IPapyrusModel.IPMState lStatePM;

        if (aStateSsm.isSimple()) {
            if (aStateSsm.getPseudoState() != null) {
                switch (aStateSsm.getPseudoState().getKind()) {
                    case END:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IPapyrusModel.UmlType.FINALSTATE
                        );
                        break;
                    case HISTORY_SHALLOW:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IPapyrusModel.PseudoKind.SHALLOW_HISTORY
                        );
                        break;
                    case HISTORY_DEEP:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IPapyrusModel.PseudoKind.DEEP_HISTORY
                        );
                        break;
                    case FORK:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IPapyrusModel.PseudoKind.FORK
                        );
                        break;
                    case INITIAL:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IPapyrusModel.PseudoKind.INITIAL
                        );
                        break;
                    case CHOICE:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IPapyrusModel.PseudoKind.CHOICE
                        );
                        break;
                    case JUNCTION:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IPapyrusModel.PseudoKind.JUNCTION
                        );
                        break;
                    case JOIN:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IPapyrusModel.PseudoKind.JOIN
                        );
                        break;
                    case ENTRY:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IPapyrusModel.PseudoKind.ENTRY_POINT
                        );
                        break;
                    case EXIT:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IPapyrusModel.PseudoKind.EXIT_POINT
                        );
                        break;
                }
            } else {
                lStatePM = aParentState.addState(
                        uuidFromState(aStateSsm), aStateSsm.getId().toString());
            }
        } else {
            if (aStateSsm.isSubmachineState()) {
                if (aStateSsm instanceof AbstractState) {
                    lStatePM = aParentState.addSubMachine(uuidFromState(aStateSsm), "");
                    processMachine(
                            (IPapyrusModel.IPMStateMachine) lStatePM,
                            ((AbstractState<S, E>) aStateSsm).getSubmachine()
                    );
                }
            } else if (aStateSsm.isComposite()) {
                if (aStateSsm instanceof RegionState) {
                    lStatePM = aParentState.addSubMachine(
                            uuidFromState(aStateSsm), aStateSsm.getId().toString());
                    processRegionState((IPapyrusModel.IPMStateMachine) lStatePM, ((RegionState<S, E>) aStateSsm));
                }
//                if (aStateSsm.isOrthogonal()) {
//
//                }
            }

        }
    }

    private void processActions(Element aParentXml, State<S, E> aState) {

    }

    private void processTransitions(IPapyrusModel.IPMRegionState aParentRegionPM, Region<S, E> aRegion) {
        Collection<Transition<S, E>> lTransitions = aRegion.getTransitions();
        for (Transition<S, E> lTransition : lTransitions) {
            aParentRegionPM.addTransition(
                    fModel.find(uuidFromState(lTransition.getSource())),
                    fModel.find(uuidFromState(lTransition.getTarget()))
            );
        }
    }

    private void processRegionState(IPapyrusModel.IPMStateMachine aXml, RegionState<S, E> aRegionState) {
        int regCount = 0;
        for (Region<S, E> lRegion : aRegionState.getRegions()) {
            IPapyrusModel.IPMRegionState lRegionPM = aXml.addRegion(uuidFromState(aRegionState), aRegionState.
                                                                                                                     getId()
                                                                                                             .toString() + "r" + regCount);
            Collection<State<S, E>> lStates = lRegion.getStates();
            for (State<S, E> lState : lStates) {
                processState(lRegionPM, lState);
            }
            processTransitions(lRegionPM, lRegion);
            regCount++;
        }
    }

    private UUID uuidFromState(State<S, E> aId) {
        if (!fUUIDMap.containsKey(aId.toString()))
            fUUIDMap.put(aId.toString(), UUID.randomUUID());
        return fUUIDMap.get(aId.toString());
    }
}
