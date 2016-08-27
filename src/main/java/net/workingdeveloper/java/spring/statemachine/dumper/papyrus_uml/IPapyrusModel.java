package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml;

import net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m.IId;
import org.springframework.statemachine.transition.TransitionKind;

import java.io.File;
import java.io.IOException;

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

        String getName();

        IId getUuid();
    }

    interface IPMRegionState extends IPMState {

        IPMPseudoState addPseudoState(IId aUUID, String aName, UmlType aFinalstate);

        IPMPseudoState addPseudoState(IId aUUID, String aName, PseudoKind aDeepHistory);

        IPMState addState(IId aUUID, String aS);

        IPMStateMachine addSubMachine(IId aUUID, String aId);

        IPMTransition addTransition(IPMState aSourceState, IPMState aTargetState, TransitionKind aKind, IPMTrigger aTrigger);

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
        IPMTransition setName(String aName);

        IPMTransition setSource(IPMState aState);

        IPMTransition setTarget(IPMState aTarget);
    }

    interface IPMTrigger {
        enum Type {
            TIMER, EVENT
        }

        IId getId();

        String getName();
    }

    IPMTrigger addTrigger(String aEvent, IPMTrigger.Type aType);

    String asString();

    IPMState find(IId aSource);

    IPMStateMachine getRootState(String aString);

    void save(File aFilename) throws IOException;
}
