package net.workingdeveloper.java.spring.statemachine.dumper.papyrus_uml.impl.w3m;

import java.util.UUID;

/**
 * Created by Christoph Graupner on 8/23/16.
 *
 * @author Christoph Graupner <christoph.graupner@workingdeveloper.net>
 */
public class UuidId implements IId {
    private final UUID fUuid;

    public UuidId() {
        fUuid = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return fUuid.toString();
    }
}
