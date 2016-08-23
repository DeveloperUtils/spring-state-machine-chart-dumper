package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml;

import net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m.IId;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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

        String getId();

        IId getUuid();

        String getName();
    }

    interface IPMRegionState extends IPMState {

        IPMPseudoState addPseudoState(IId aUUID, String aName, UmlType aFinalstate);

        IPMPseudoState addPseudoState(IId aUUID, String aName, PseudoKind aDeepHistory);

        IPMState addState(IId aUUID, String aS);

        IPMStateMachine addSubMachine(IId aUUID, String aId);

        IPMTransition addTransition(IPMState aSourceState, IPMState aTargetState);

        IPMRegionState setName(String aName);
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
        IPMRegionState addRegion(IId aUUID, String aName);
    }

    interface IPMTransition {
        IPMTransition setSource(IPMState aState);

        IPMTransition setTarget(IPMState aTarget);

        IPMTransition setName(String aName);
    }

    String asString();

    IPMState find(IId aSource);

    IPMStateMachine getRootState(String aString);

    void save(File aFilename) throws IOException;

}
