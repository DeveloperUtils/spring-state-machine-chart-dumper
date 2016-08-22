package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml;

/**
 * Created by Christoph Graupner on 8/22/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
public interface IPapyrusModel {
    enum UmlType {

        FINALSTATE, PSEUDO_STATE, STATE
    }

    enum PseudoKind {
        SHALLOW_HISTORY,
        FORK, INITIAL, CHOICE, JUNCTION, JOIN, ENTRY_POINT, EXIT_POINT, DEEP_HISTORY,
        FINAL
    }

    interface IPMState {

    }

    interface IPMRegionState extends IPMState {

        IPMPseudoState addPseudoState(String aName, UmlType aFinalstate);

        IPMPseudoState addPseudoState(String aName, PseudoKind aDeepHistory);

        IPMState addState(String aS);

        IPMStateMachine addSubMachine(String aId);

        IPMRegionState setName(String aId);
    }

    interface IPMPseudoState extends IPMState {
        IPMPseudoState setKind(PseudoKind aKind);
    }

    interface IPMPShallowHistoryState extends IPMPseudoState {
    }

    interface IPMPForkState extends IPMPseudoState {
    }

    interface IPMPInitialState extends IPMPseudoState {
    }

    interface IPMPChoiceState extends IPMPseudoState {
    }

    interface IPMPJunctionState extends IPMPseudoState {
    }

    interface IPMPJoinState extends IPMPseudoState {
    }

    interface IPMPEntryPointState extends IPMPseudoState {
    }

    interface IPMPExitPointState extends IPMPseudoState {
    }

    interface IPMPDeppHistoryState extends IPMPseudoState {
    }

    interface IPMStateMachine extends IPMRegionState {
        IPMRegionState addRegion(String aName);
    }

    interface IPMTransition {

    }

    String asString();

    IPMStateMachine getRootState(String aString);

    void save(String aFilename);

}
