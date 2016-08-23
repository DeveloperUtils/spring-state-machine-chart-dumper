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
    public class StateMachine extends RegionState implements IPMStateMachine {

        public StateMachine(ModelUml.RegionState aUmlState, ModelNotation.State aNotationState, RegionState aParent) {
            super(aUmlState, aNotationState, aParent);
        }

        @Override
        public IPMRegionState addRegion(UUID aUUID, String aName) {
            return new RegionState(fUmlState.addRegionState(aUUID, aName), null, this);
        }

    }

    public class State<UMLMODEL extends ModelUml.State, NOTATIONMODEL extends ModelNotation.State, PARENT extends State> {
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

        public UUID getUuid() {
            return fUmlState.getUuid();
        }
    }

    public class SimpleState extends State<ModelUml.State, ModelNotation.State, RegionState> implements IPMState {

        public SimpleState(ModelUml.State aUmlState, ModelNotation.State aNotationState, RegionState aParent) {
            super(aUmlState, aNotationState, aParent);
        }

        public void setType(String aType) {
            String fType = aType;
        }
    }

    public class RegionState extends State<ModelUml.RegionState, ModelNotation.State, RegionState> implements IPMRegionState {

        public RegionState(ModelUml.RegionState aUmlState, ModelNotation.State aNotationState, RegionState aParent) {
            super(aUmlState, aNotationState, aParent);
        }

        @Override
        public IPMPseudoState addPseudoState(UUID aUUID, String aName, UmlType aFinalstate) {
            switch (aFinalstate) {

                case FINALSTATE:
                    return new PseudoState(fUmlState.addPseudoState(PseudoKind.FINAL, aUUID, aName), null, this);
                case PSEUDO_STATE:
                    break;
                case STATE:
                    break;
            }
            return null;
        }

        @Override
        public IPMPseudoState addPseudoState(UUID aUUID, String aName, PseudoKind aKind) {
            return new PseudoState(fUmlState.addPseudoState(aKind, aUUID, aName), null, this);
        }

        @Override
        public IPMState addState(UUID aUUID, String aS) {
            return new SimpleState(fUmlState.addState(aUUID, aS), null, this);
        }


        @Override
        public IPMStateMachine addSubMachine(UUID aUUID, String aS) {
            return new StateMachine(fUmlState.addSubMachine(aUUID, aS), null, this);
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


            return lTransition;
        }

        @Override
        public IPMRegionState setName(String aName) {
            fUmlState.setName(aName);
            return this;
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

    public class PseudoState extends State<ModelUml.State, ModelNotation.State, RegionState> implements IPMPseudoState {
        PseudoKind fKind;

        public PseudoState(ModelUml.State aUmlState, ModelNotation.State aNotationState, RegionState aParent) {
            super(aUmlState, aNotationState, aParent);
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
        fNotation = new ModelNotation();
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
            fRootState = new StateMachine(fUml.getRootState(), null, null);
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
