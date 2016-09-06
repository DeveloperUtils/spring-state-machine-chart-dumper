package net.workingdeveloper.java.spring.statemachine.dumper.mdt_uml2;

import org.springframework.statemachine.transition.TransitionKind;

import java.io.File;
import java.io.IOException;

/**
 * Created by Christoph Graupner on 8/22/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
public interface IMdtUml2Model {
    enum UmlType {
        FINALSTATE, PSEUDO_STATE, STATE
    }

    enum PseudoKind {
        SHALLOW_HISTORY,
        FORK, INITIAL, CHOICE, JUNCTION, JOIN, ENTRY_POINT, EXIT_POINT, DEEP_HISTORY,
        FINAL
    }

    interface IMUAction extends IMUNode {

    }

    interface IMUNode {
        <T extends IMUNode> T addComment(String aS);

        IId getId();

        String getName();

        <T extends IMUNode> T setName(String aName);
    }

    interface IMUState extends IMUNode {
        IMUAction addEntryAction(String aName);

        IMUAction addExitAction(String aName);
    }

    interface IMURegionState extends IMUState {

        IMUPseudoState addPseudoState(IId aUUID, String aName, UmlType aFinalstate);

        IMUPseudoState addPseudoState(IId aUUID, String aName, PseudoKind aDeepHistory);

        IMUState addState(IId aUUID, String aS);

        IMUStateMachine addSubMachine(IId aUUID, String aId);

        IMUTransition addTransition(IMUState aSourceState, IMUState aTargetState, TransitionKind aKind, IMUTrigger aTrigger);

        IMUTransition addTransition(IMUState aSourceState, IMUState aTargetState, TransitionKind aKind, String aConnectionName);
    }

    interface IMUPseudoState extends IMUState {
        IMUPseudoState setKind(PseudoKind aKind);
    }

    interface IMUPShallowHistoryState extends IMUPseudoState {
    }

    interface IMUPForkState extends IMUPseudoState {
    }

    interface IMUPInitialState extends IMUPseudoState {
    }

    interface IMUPChoiceState extends IMUPseudoState {
    }

    interface IMUPJunctionState extends IMUPseudoState {
    }

    interface IMUPJoinState extends IMUPseudoState {
    }

    interface IMUPEntryPointState extends IMUPseudoState {
    }

    interface IMUPExitPointState extends IMUPseudoState {
    }

    interface IMUPDeppHistoryState extends IMUPseudoState {
    }

    interface IMUStateMachine extends IMURegionState {
        IMURegionState addRegion(IId aUUID, String aName);
    }

    interface IMUTransition extends IMUNode {

        IMUAction addAction(String aName);

        IMUGuard setGuard(IId aGuardId);

        IMUTransition setName(String aName);

        IMUTransition setSource(IMUState aState);

        IMUTransition setTarget(IMUState aTarget);
    }

    interface IMUTrigger extends IMUNode {
        enum Type {
            TIMER, EVENT
        }
    }

    interface IMUGuard extends IMUNode {
        IMUGuard addBody(String aBody);

        IMUGuard addLanguage(String aLanguageName);
    }

    IMUTrigger addTrigger(String aEvent, IMUTrigger.Type aType);

    String asString();

    IMUState find(IId aSource);

    IMUStateMachine getRootState(String aString);

    void save(File aFilename) throws IOException;
}
