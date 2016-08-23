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

    public UuidId(UUID aUuid) {
        fUuid = aUuid;
    }

    @Override
    public boolean equals(Object laO) {
        if (this == laO) return true;
        if (!(laO instanceof UuidId)) return false;

        UuidId llUuidId = (UuidId) laO;

        return fUuid.equals(llUuidId.fUuid);

    }

    @Override
    public int hashCode() {
        return fUuid.hashCode();
    }

    @Override
    public String toString() {
        return fUuid.toString();
    }
}
