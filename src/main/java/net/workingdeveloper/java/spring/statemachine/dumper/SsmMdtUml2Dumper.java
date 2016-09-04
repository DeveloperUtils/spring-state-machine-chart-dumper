package net.workingdeveloper.java.spring.statemachine.dumper;

import net.workingdeveloper.java.spring.statemachine.dumper.mdt_uml2.IId;
import net.workingdeveloper.java.spring.statemachine.dumper.mdt_uml2.IMdtUml2Model;
import net.workingdeveloper.java.spring.statemachine.dumper.mdt_uml2.Sha1Id;
import net.workingdeveloper.java.spring.statemachine.dumper.mdt_uml2.UuidId;
import net.workingdeveloper.java.spring.statemachine.dumper.mdt_uml2.impl.w3m.MdtUml2Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.region.Region;
import org.springframework.statemachine.state.*;
import org.springframework.statemachine.transition.Transition;
import org.springframework.statemachine.transition.TransitionKind;
import org.springframework.statemachine.trigger.TimerTrigger;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christoph Graupner on 8/20/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
public class SsmMdtUml2Dumper<S, E> extends SsmDumper<S, E> {
    private static final Logger logger = LoggerFactory.getLogger(SsmMdtUml2Dumper.class);
    private IMdtUml2Model fModel;
    private Map<String, IId> fUUIDMap = new HashMap<>();

    public SsmMdtUml2Dumper(StateMachine<S, E> aStateMachine) {
        super(aStateMachine);
    }

    @Override
    public String asString() {
        return fModel.asString();
    }

    @Override
    public <T extends SsmDumper> T dump() {
        try {
            fModel = new MdtUml2Model();
        } catch (ParserConfigurationException aE) {
            aE.printStackTrace();
        }

        processMachine(fModel, getStateMachine());

        return (T) this;
    }

    @Override
    public void save(File aFile) throws IOException {
        fModel.save(aFile);
    }

    private void processMachine(IMdtUml2Model aSM, StateMachine<S, E> aStateMachine) {
        IMdtUml2Model.IMUStateMachine lStateMachine = aSM.getRootState(getStateMachine().getId());
        processMachine(lStateMachine, aStateMachine, null);
    }

    private void processMachine(IMdtUml2Model.IMUStateMachine aSM, StateMachine<S, E> aStateMachine, State<S, E> aSsmParent) {
        IMdtUml2Model.IMURegionState lRootRegion = aSM.addRegion(
                new UuidId(aStateMachine.getUuid()),
                aSsmParent == null ? "root" : aSsmParent.getId() + "r0"
        );
        processRegion(lRootRegion, aStateMachine, aStateMachine.getInitialState());
    }

    private void processRegion(IMdtUml2Model.IMURegionState aRegionState, Region<S, E> aSERegion, State<S, E> aInitialState) {
        Collection<State<S, E>> lStates = aSERegion.getStates();
        for (State<S, E> lState : lStates) {
            processState(aRegionState, lState);
        }
        processTransitions(aRegionState, aSERegion);
    }

    private void processState(IMdtUml2Model.IMURegionState aParentState, State<S, E> aStateSsm) {
        IMdtUml2Model.IMUState lStatePM;

        if (aStateSsm.isSimple()) {
            if (aStateSsm.getPseudoState() != null) {
                switch (aStateSsm.getPseudoState().getKind()) {
                    case END:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IMdtUml2Model.UmlType.FINALSTATE
                        );
                        break;
                    case HISTORY_SHALLOW:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IMdtUml2Model.PseudoKind.SHALLOW_HISTORY
                        );
                        break;
                    case HISTORY_DEEP:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IMdtUml2Model.PseudoKind.DEEP_HISTORY
                        );
                        break;
                    case INITIAL:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IMdtUml2Model.PseudoKind.INITIAL
                        );
                        break;
                    case CHOICE:
                        processPseudoStateChoice(aParentState, aStateSsm);
                        break;
                    case JUNCTION:
                        processPseudoStateJunction(aParentState, aStateSsm);
                        break;
                    case FORK:
                        processPseudoStateFork(aParentState, aStateSsm);
                        break;
                    case JOIN:
                        processPseudoStateJoin(aParentState, aStateSsm);
                        break;
                    case ENTRY:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IMdtUml2Model.PseudoKind.ENTRY_POINT
                        );
                        break;
                    case EXIT:
                        lStatePM = aParentState.addPseudoState(
                                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                                IMdtUml2Model.PseudoKind.EXIT_POINT
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
                    lStatePM = aParentState.addSubMachine(uuidFromState(aStateSsm), aStateSsm.getId().toString());
                    processMachine(
                            (IMdtUml2Model.IMUStateMachine) lStatePM,
                            ((AbstractState<S, E>) aStateSsm).getSubmachine(),
                            aStateSsm
                    );
                }
            } else if (aStateSsm.isComposite()) {
                if (aStateSsm instanceof RegionState) {
                    lStatePM = aParentState.addSubMachine(
                            uuidFromState(aStateSsm), aStateSsm.getId().toString());
                    processRegionState((IMdtUml2Model.IMUStateMachine) lStatePM, ((RegionState<S, E>) aStateSsm));
                } else {
                    logger.error("Here is wrong");
                }
            }
        }
    }

    private void processPseudoStateJunction(IMdtUml2Model.IMURegionState aParentState, State<S, E> aStateSsm) {
        assert aStateSsm.getPseudoState() != null && aStateSsm.getPseudoState() instanceof JunctionPseudoState;
        JunctionPseudoState<S, E> lPseudoState = (JunctionPseudoState<S, E>) aStateSsm.getPseudoState();
        IMdtUml2Model.IMUPseudoState lStatePM = aParentState.addPseudoState(
                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                IMdtUml2Model.PseudoKind.JUNCTION
        );
        try {
            Field lFieldChoices = JunctionPseudoState.class.getDeclaredField("junctions");
            lFieldChoices.setAccessible(true);
            List<JunctionPseudoState.JunctionStateData<S, E>> lFieldChoice = (List<JunctionPseudoState.JunctionStateData<S, E>>) lFieldChoices
                    .get(lPseudoState);

            State<S, E> lNextState;
            int         count = 0;
            for (JunctionPseudoState.JunctionStateData<S, E> lJunctionStateData : lFieldChoice) {
                count++;
                State<S, E> lSsmNextState   = lJunctionStateData.getState();
                final Guard<S, E> lSsmGuard = lJunctionStateData.getGuard();
                handlePseudoStateJunction("Junction", aParentState, lStatePM, lSsmNextState, lSsmGuard, count);
            }
        } catch (NoSuchFieldException aE) {
            logger.error(
                    "Class JunctionPseudoState doesn't have field 'junctions'. Compatible version of spring state machine used?",
                    aE
            );
        } catch (IllegalAccessException aE) {
            logger.error(
                    "Can't access field 'junctions' of class 'JunctionPseudoState'. Compatible version of spring state machine used?",
                    aE
            );
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException aE) {
            logger.error(aE.getLocalizedMessage(), aE);
        }
    }

    private void processPseudoStateChoice(IMdtUml2Model.IMURegionState aMUParentState, State<S, E> aStateSsm) {
        assert aStateSsm.getPseudoState() != null && aStateSsm.getPseudoState() instanceof ChoicePseudoState;
        ChoicePseudoState<S, E> lPseudoState = (ChoicePseudoState<S, E>) aStateSsm.getPseudoState();
        IMdtUml2Model.IMUPseudoState lMUState = aMUParentState.addPseudoState(
                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                IMdtUml2Model.PseudoKind.CHOICE
        );
        try {
            Field lFieldChoices = ChoicePseudoState.class.getDeclaredField("choices");
            lFieldChoices.setAccessible(true);
            List<ChoicePseudoState.ChoiceStateData<S, E>> lFieldChoice = (List<ChoicePseudoState.ChoiceStateData<S, E>>) lFieldChoices
                    .get(lPseudoState);

            State<S, E> lSsmNextState;
            int         count = 0;
            for (ChoicePseudoState.ChoiceStateData<S, E> lChoiceStateData : lFieldChoice) {
                count++;
                lSsmNextState = lChoiceStateData.getState();
                final Guard<S, E> lSsmGuard = lChoiceStateData.getGuard();
                handlePseudoStateJunction("Choice", aMUParentState, lMUState, lSsmNextState, lSsmGuard, count);
            }
        } catch (NoSuchFieldException aE) {
            logger.error(
                    "Class ChoicePseudoState doesn't have field 'choices'. Compatible version of spring state machine used?",
                    aE
            );
        } catch (IllegalAccessException aE) {
            logger.error(
                    "Can't access field 'choices' of class 'ChoicePseudoState'. Compatible version of spring state machine used?",
                    aE
            );
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException aE) {
            logger.error(aE.getLocalizedMessage(), aE);
        }
    }

    private void handlePseudoStateJunction(String aType, IMdtUml2Model.IMURegionState aMUParentState, IMdtUml2Model.IMUPseudoState aMUPseudoState, State<S, E> aSsmNextState, Guard<S, E> aSsmGuard, int aCount) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        IMdtUml2Model.IMUState lFound = fModel.find(uuidFromState(aSsmNextState));
        if (lFound != null) {
            IMdtUml2Model.IMUTransition lTransition = aMUParentState.addTransition(
                    aMUPseudoState, lFound, TransitionKind.EXTERNAL, null);
            lTransition.addComment("Connection " + aMUPseudoState.getName() + "#" + aType + aCount);
            if (aSsmGuard != null) {
                IMdtUml2Model.IMUGuard lGuard = lTransition.setGuard(
                        new Sha1Id(aSsmGuard.getClass()));
                lGuard.setName(aMUPseudoState.getName() + "#" + aType + aCount);
                lGuard.addBody(aMUPseudoState.getName() + "#" + aType + aCount);
            }
        } else {
            //TODO: create deferred transition
            logger.error(aType + ": No state found " + aSsmNextState);
        }
    }

    private void processPseudoStateFork(IMdtUml2Model.IMURegionState aParentState, State<S, E> aStateSsm) {
        assert aStateSsm.getPseudoState() != null && aStateSsm.getPseudoState() instanceof ForkPseudoState;
        ForkPseudoState<S, E> lPseudoState = (ForkPseudoState<S, E>) aStateSsm.getPseudoState();
        IMdtUml2Model.IMUPseudoState lStatePM = aParentState.addPseudoState(
                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                IMdtUml2Model.PseudoKind.FORK
        );
        for (State<S, E> lForkedState : lPseudoState.getForks()) {
            IMdtUml2Model.IMUState lFound = fModel.find(uuidFromState(lForkedState));
            if (lFound != null) {
                aParentState.addTransition(lStatePM, lFound, TransitionKind.EXTERNAL, null);
            } else {
                //TODO: create deferred transition
                logger.error("No state found " + lForkedState);
            }
        }
    }

    private void processPseudoStateJoin(IMdtUml2Model.IMURegionState aParentState, State<S, E> aStateSsm) {
        assert aStateSsm.getPseudoState() != null && aStateSsm.getPseudoState() instanceof JoinPseudoState;
        JoinPseudoState<S, E> lPseudoState = (JoinPseudoState<S, E>) aStateSsm.getPseudoState();
        IMdtUml2Model.IMUPseudoState lStatePM = aParentState.addPseudoState(
                uuidFromState(aStateSsm), aStateSsm.getId().toString(),
                IMdtUml2Model.PseudoKind.JOIN
        );
        for (State<S, E> lJoinedState : lPseudoState.getJoins()) {
            IMdtUml2Model.IMUState lFound = fModel.find(uuidFromState(lJoinedState));
            if (lFound != null) {
                aParentState.addTransition(lFound, lStatePM, TransitionKind.EXTERNAL, null);
            } else {
                //TODO: create deferred transition
                logger.error("No state found " + lJoinedState);
            }
        }
    }

    private void processActions(Element aParentXml, State<S, E> aState) {

    }

    private void processTransitions(IMdtUml2Model.IMURegionState aParentRegionPM, Region<S, E> aRegion) {
        Collection<Transition<S, E>> lTransitions = aRegion.getTransitions();
        for (Transition<S, E> lTransition : lTransitions) {
            IMdtUml2Model.IMUTrigger lTrigger = fModel.addTrigger(
                    lTransition.getTrigger().getEvent().toString(),
                    lTransition.getTrigger() instanceof TimerTrigger ? IMdtUml2Model.IMUTrigger.Type.TIMER
                                                                     : IMdtUml2Model.IMUTrigger.Type.EVENT
            );
            aParentRegionPM.addTransition(
                    fModel.find(uuidFromState(lTransition.getSource())),
                    fModel.find(uuidFromState(lTransition.getTarget())),
                    lTransition.getKind(),
                    lTrigger
            );
        }
    }

    private void processRegionState(IMdtUml2Model.IMUStateMachine aXml, RegionState<S, E> aRegionState) {
        int regCount = 0;
        for (Region<S, E> lRegion : aRegionState.getRegions()) {
            IMdtUml2Model.IMURegionState lRegionPM = aXml.addRegion(
                    uuidFromState(lRegion), aRegionState.getId().toString() + "r" + regCount);
            Collection<State<S, E>> lStates = lRegion.getStates();
            for (State<S, E> lState : lStates) {
                processState(lRegionPM, lState);
            }
            processTransitions(lRegionPM, lRegion);
            regCount++;
        }
    }

    private IId uuidFromState(Object aRegion) {
        if (!fUUIDMap.containsKey(aRegion.toString()))
            fUUIDMap.put(aRegion.toString(), new UuidId());
        return fUUIDMap.get(aRegion.toString());
    }
}
