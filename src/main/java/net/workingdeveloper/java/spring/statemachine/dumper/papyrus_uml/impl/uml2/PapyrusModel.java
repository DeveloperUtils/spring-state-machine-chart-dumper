package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.uml2;

import net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.IPapyrusModel;

/**
 * Created by Christoph Graupner on 8/21/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
public class PapyrusModel implements IPapyrusModel {
    public class StateMachine extends RegionState implements IPMStateMachine {

        @Override
        public IPMRegionState addRegion(String aName) {
            return null;
        }
    }

    public class State implements IPMState {

        public void setType(String aType) {


        }
    }

    public class RegionState extends State implements IPMRegionState {
        @Override
        public IPMPseudoState addPseudoState(String aName, UmlType aFinalstate) {
            return null;
        }

        @Override
        public IPMPseudoState addPseudoState(String aName, PseudoKind aDeepHistory) {
            return null;
        }

        @Override
        public IPMState addState(String aS) {
            return null;
        }

        @Override
        public IPMStateMachine addSubMachine(String aId) {
            return null;
        }

        @Override
        public IPMRegionState setName(String aId) {
            return null;
        }
    }

    public class PseudoState extends State implements IPMPseudoState {

        @Override
        public IPMPseudoState setKind(PseudoKind aKind) {
            return null;
        }
    }

    ModelDi       fDi;
    ModelNotation fNotation;
    ModelUml      fUml;

    public PapyrusModel() {
        fUml = new ModelUml();
        fNotation = new ModelNotation();
        fDi = new ModelDi();
    }

    @Override
    public StateMachine getRootState(String aString) {
        return new StateMachine();
    }

    @Override
    public String asString() {
        return null;
    }

    public void save(String aFilename) {
        fUml.save(aFilename);
    }
}
