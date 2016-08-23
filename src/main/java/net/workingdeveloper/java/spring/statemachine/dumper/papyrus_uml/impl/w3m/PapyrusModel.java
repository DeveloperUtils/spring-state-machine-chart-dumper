package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m;

import net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.IPapyrusModel;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Christoph Graupner on 8/22/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
public class PapyrusModel implements IPapyrusModel {
    class PMStateMachine extends PMRegionMachineSharedState<ModelUml.MUStateMachineState, ModelNotation.MNStateMachine, PMRegionMachineSharedState> implements IPMStateMachine {

        public PMStateMachine(ModelUml.MUStateMachineState aUmlState, ModelNotation.MNStateMachine aNotationState, PMRegionMachineSharedState aParent) {
            super(aUmlState, aNotationState, aParent);
        }

        @Override
        public IPMRegionState addRegion(IId aUUID, String aName) {
            ModelUml.MURegionState lUmlState = fUmlState.addRegionState(aUUID, aName);
            return new PMRegionState(lUmlState, fNotationState.addRegionState(lUmlState), this);
        }

    }

    abstract class PMState<UMLMODEL extends ModelUml.MUNode, NOTATIONMODEL extends ModelNotation.MNNode, PARENT extends PMState> {
        NOTATIONMODEL fNotationState;
        PARENT        fParent;
        UMLMODEL      fUmlState;

        PMState(UMLMODEL aUmlState, NOTATIONMODEL aNotationState, PARENT aParent) {
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

        public IId getUuid() {
            return fUmlState.getUuid();
        }
    }

    class PMSimpleState extends PMState<ModelUml.MUNode, ModelNotation.MNNode, PMRegionMachineSharedState> implements IPMState {

        public PMSimpleState(ModelUml.MUNode aUmlMUNode, ModelNotation.MNNode aNotationState, PMRegionMachineSharedState aParent) {
            super(aUmlMUNode, aNotationState, aParent);
        }

    }

    abstract class PMRegionMachineSharedState<UMLMODEL extends ModelUml.MURegionMachineShared, NOTATIONMODEL extends ModelNotation.MNRegionMachineShared, PARENT extends PMState> extends PMState<UMLMODEL, NOTATIONMODEL, PARENT> implements IPMRegionState {

        public PMRegionMachineSharedState(UMLMODEL aUmlState, NOTATIONMODEL aNotationState, PARENT aParent) {
            super(aUmlState, aNotationState, aParent);
        }

        @Override
        public IPMPseudoState addPseudoState(IId aUUID, String aName, UmlType aFinalstate) {
            switch (aFinalstate) {

                case FINALSTATE:
                    ModelUml.MUPseudoState lUmlMUNode = fUmlState.addPseudoState(PseudoKind.FINAL, aUUID, aName);
                    return new PMPseudoState(lUmlMUNode, fNotationState.addPseudoState(lUmlMUNode), this);
                case PSEUDO_STATE:
                    break;
                case STATE:
                    break;
            }
            return null;
        }

        @Override
        public IPMPseudoState addPseudoState(IId aUUID, String aName, PseudoKind aKind) {
            ModelUml.MUPseudoState lUmlMUNode = fUmlState.addPseudoState(aKind, aUUID, aName);
            return new PMPseudoState(lUmlMUNode, fNotationState.addPseudoState(lUmlMUNode), this);
        }

        @Override
        public IPMState addState(IId aUUID, String aName) {
            ModelUml.MUSimpleState lUmlMUNode = fUmlState.addState(aUUID, aName);
            return new PMSimpleState(lUmlMUNode, fNotationState.addSimpleState(lUmlMUNode), this);
        }


        @Override
        public IPMStateMachine addSubMachine(IId aUUID, String aName) {
            ModelUml.MUStateMachineState lUmlState = fUmlState.addSubMachine(aUUID, aName);
            return new PMStateMachine(lUmlState, fNotationState.addSubMachine(lUmlState), this);
        }

        @Override
        public IPMTransition addTransition(IPMState aSourceState, IPMState aTargetState) {

            IPMTransition lTransition = new PMTransaction(
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

    class PMRegionState extends PMRegionMachineSharedState<ModelUml.MURegionState, ModelNotation.MNRegionState, PMRegionMachineSharedState> implements IPMRegionState {

        PMRegionState(ModelUml.MURegionState aUmlState, ModelNotation.MNRegionState aNotationState, PMRegionMachineSharedState aParent) {
            super(aUmlState, aNotationState, aParent);
        }


    }

    class PMTransaction implements IPMTransition {
        IPMRegionState        fParent;
        ModelUml.MUTransition fMUTransition;

        public PMTransaction(IPMRegionState aParent, ModelUml.MUTransition aMUTransition) {
            fParent = aParent;
            fMUTransition = aMUTransition;
        }

        @Override
        public IPMTransition setName(String aName) {
            fMUTransition.setName(aName);
            return this;
        }

        @Override
        public IPMTransition setSource(IPMState aSource) {
            fMUTransition.setSource(aSource.getId());
            return this;
        }

        @Override
        public IPMTransition setTarget(IPMState aTarget) {
            fMUTransition.setTarget(aTarget.getId());
            return this;
        }
    }

    class PMPseudoState extends PMState<ModelUml.MUNode, ModelNotation.MNNode, PMRegionMachineSharedState> implements IPMPseudoState {
        PseudoKind fKind;

        public PMPseudoState(ModelUml.MUNode aUmlMUNode, ModelNotation.MNNode aNotationState, PMRegionMachineSharedState aParent) {
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
    HashMap<IId, PMState> fStateMap = new HashMap<>();
    ModelUml fUml;
    private PMStateMachine fRootState;

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
    public IPMState find(IId aSource) {
        return (IPMState) fStateMap.get(aSource);
    }

    @Override
    public IPMStateMachine getRootState(String aName) {
        if (fRootState == null) {
            fRootState = new PMStateMachine(fUml.getRootState(), fNotation.getRootState(fUml.getRootState()), null);
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
