package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m;

import net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.IPapyrusModel;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Christoph Graupner on 8/22/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
public class PapyrusModel implements IPapyrusModel {
    public class StateMachine extends PMRegionMachineSharedState<ModelUml.StateMachineState, ModelNotation.MNStateMachine, PMRegionMachineSharedState> implements IPMStateMachine {

        public StateMachine(ModelUml.StateMachineState aUmlState, ModelNotation.MNStateMachine aNotationState, PMRegionMachineSharedState aParent) {
            super(aUmlState, aNotationState, aParent);
        }

        @Override
        public IPMRegionState addRegion(UUID aUUID, String aName) {
            ModelUml.RegionState lUmlState = fUmlState.addRegionState(aUUID, aName);
            return new RegionState(lUmlState, fNotationState.addRegionState(lUmlState), this);
        }

    }

    public class State<UMLMODEL extends ModelUml.MUNode, NOTATIONMODEL extends ModelNotation.MNNode, PARENT extends State> {
        NOTATIONMODEL fNotationState;
        PARENT        fParent;
        UMLMODEL      fUmlState;

        public State(UMLMODEL aUmlState, NOTATIONMODEL aNotationState, PARENT aParent) {
            fUmlState = aUmlState;
            fNotationState = aNotationState;
            fParent = aParent;
            fStateMap.put(aUmlState.getUuid(), this);
        }

        public String getId() {
            return fUmlState.getId();
        }

        public String getName() {
            return fUmlState.getName();
        }

        public UUID getUuid() {
            return fUmlState.getUuid();
        }
    }

    public class SimpleState extends State<ModelUml.MUNode, ModelNotation.MNNode, PMRegionMachineSharedState> implements IPMState {

        public SimpleState(ModelUml.MUNode aUmlMUNode, ModelNotation.MNNode aNotationState, PMRegionMachineSharedState aParent) {
            super(aUmlMUNode, aNotationState, aParent);
        }

    }

    abstract public class PMRegionMachineSharedState<UMLMODEL extends ModelUml.MURegionMachineShared, NOTATIONMODEL extends ModelNotation.MNRegionMachineShared, PARENT extends State> extends State<UMLMODEL, NOTATIONMODEL, PARENT> implements IPMRegionState {

        public PMRegionMachineSharedState(UMLMODEL aUmlState, NOTATIONMODEL aNotationState, PARENT aParent) {
            super(aUmlState, aNotationState, aParent);
        }

        @Override
        public IPMPseudoState addPseudoState(UUID aUUID, String aName, UmlType aFinalstate) {
            switch (aFinalstate) {

                case FINALSTATE:
                    ModelUml.PseudoState lUmlMUNode = fUmlState.addPseudoState(PseudoKind.FINAL, aUUID, aName);
                    return new PseudoState(lUmlMUNode, fNotationState.addPseudoState(lUmlMUNode), this);
                case PSEUDO_STATE:
                    break;
                case STATE:
                    break;
            }
            return null;
        }

        @Override
        public IPMPseudoState addPseudoState(UUID aUUID, String aName, PseudoKind aKind) {
            ModelUml.PseudoState lUmlMUNode = fUmlState.addPseudoState(aKind, aUUID, aName);
            return new PseudoState(lUmlMUNode, fNotationState.addPseudoState(lUmlMUNode), this);
        }

        @Override
        public IPMState addState(UUID aUUID, String aS) {
            ModelUml.SimpleState lUmlMUNode = fUmlState.addState(aUUID, aS);
            return new SimpleState(lUmlMUNode, fNotationState.addSimpleState(lUmlMUNode), this);
        }


        @Override
        public IPMStateMachine addSubMachine(UUID aUUID, String aS) {
            ModelUml.StateMachineState lUmlState = fUmlState.addSubMachine(aUUID, aS);
            return new StateMachine(lUmlState, fNotationState.addSubMachine(lUmlState), this);
        }

        @Override
        public IPMTransition addTransition(IPMState aSourceState, IPMState aTargetState) {

            IPMTransition lTransition = new Transaction(
                    this,
                    fUmlState.addTransition(
                            aSourceState.getUuid(),
                            aTargetState.getUuid()
                    )
            );
            lTransition.setName(aSourceState.getName() + "__" + aTargetState.getName());

            return lTransition;
        }

        @Override
        public IPMRegionState setName(String aName) {
            fUmlState.setName(aName);
            return this;
        }
    }

    public class RegionState extends PMRegionMachineSharedState<ModelUml.RegionState, ModelNotation.MNRegionState, PMRegionMachineSharedState> implements IPMRegionState {

        public RegionState(ModelUml.RegionState aUmlState, ModelNotation.MNRegionState aNotationState, PMRegionMachineSharedState aParent) {
            super(aUmlState, aNotationState, aParent);
        }


    }

    class Transaction implements IPMTransition {
        IPMRegionState      fParent;
        ModelUml.Transition fTransition;

        public Transaction(IPMRegionState aParent, ModelUml.Transition aTransition) {
            fParent = aParent;
            fTransition = aTransition;
        }

        @Override
        public IPMTransition setName(String aName) {
            fTransition.setName(aName);
            return this;
        }

        @Override
        public IPMTransition setSource(IPMState aState) {
            fTransition.setSource(aState.getId());
            return this;
        }

        @Override
        public IPMTransition setTarget(IPMState aTarget) {
            fTransition.setTarget(aTarget.getId());
            return this;
        }
    }

    public class PseudoState extends State<ModelUml.MUNode, ModelNotation.MNNode, PMRegionMachineSharedState> implements IPMPseudoState {
        PseudoKind fKind;

        public PseudoState(ModelUml.MUNode aUmlMUNode, ModelNotation.MNNode aNotationState, PMRegionMachineSharedState aParent) {
            super(aUmlMUNode, aNotationState, aParent);
        }


        @Override
        public IPMPseudoState setKind(PseudoKind aKind) {
            fKind = aKind;
            return this;
        }
    }

    ModelDi       fDi;
    ModelNotation fNotation;
    HashMap<UUID, State> fStateMap = new HashMap<>();
    ModelUml fUml;
    private StateMachine fRootState;

    public PapyrusModel() throws ParserConfigurationException {
        fUml = new ModelUml();
        fNotation = new ModelNotation(fUml);
        fDi = new ModelDi();
    }

    @Override
    public String asString() {
        return fUml.asString();
    }

    @Override
    public IPMState find(UUID aSource) {
        return (IPMState) fStateMap.get(aSource);
    }

    @Override
    public IPMStateMachine getRootState(String aString) {
        if (fRootState == null) {
            fRootState = new StateMachine(fUml.getRootState(), fNotation.getRootState(fUml.getRootState()), null);
            fStateMap.put(fRootState.getUuid(), fRootState);
        }
        return fRootState;
    }

    @Override
    public void save(File aFilename) throws IOException {
        fUml.save(aFilename);
        fNotation.save(aFilename);
        fDi.save(aFilename);
    }
}
