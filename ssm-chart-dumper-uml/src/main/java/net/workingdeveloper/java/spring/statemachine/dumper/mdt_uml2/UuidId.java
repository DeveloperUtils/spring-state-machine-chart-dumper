package net.workingdeveloper.java.spring.statemachine.dumper.mdt_uml2;

import java.util.UUID;

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
