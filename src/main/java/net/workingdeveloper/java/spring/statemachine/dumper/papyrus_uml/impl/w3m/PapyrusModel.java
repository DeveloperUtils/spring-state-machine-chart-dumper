package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m;

import net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.IPapyrusModel;

import javax.xml.parsers.ParserConfigurationException;

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
        public IPMRegionState addRegion(String aName) {
            return new RegionState(fUmlState.addRegionState(aName), null, this);
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
        public IPMPseudoState addPseudoState(String aName, UmlType aFinalstate) {
            switch (aFinalstate) {

                case FINALSTATE:
                    return new PseudoState(fUmlState.addPseudoState(PseudoKind.FINAL, aName), null, this);
                case PSEUDO_STATE:
                    break;
                case STATE:
                    break;
            }
            return null;
        }

        @Override
        public IPMPseudoState addPseudoState(String aName, PseudoKind aKind) {
            return new PseudoState(fUmlState.addPseudoState(aKind, aName), null, this);
        }

        @Override
        public IPMState addState(String aS) {
            return new SimpleState(fUmlState.addState(aS), null, this);
        }


        @Override
        public IPMStateMachine addSubMachine(String aS) {
            return new StateMachine(fUmlState.addSubMachine(aS), null, this);
        }

        @Override
        public IPMRegionState setName(String aId) {
            fUmlState.setName(aId);
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
    ModelUml      fUml;
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
    public IPMStateMachine getRootState(String aString) {
        if (fRootState == null) {
            fRootState = new StateMachine(fUml.getRootState(), null, null);
        }
        return fRootState;
    }

    @Override
    public void save(String aFilename) {
        fUml.save(aFilename);
        fNotation.save(aFilename);
        fDi.save(aFilename);
    }
}
