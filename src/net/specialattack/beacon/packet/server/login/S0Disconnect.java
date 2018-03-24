package net.specialattack.beacon.packet.server.login;

import net.specialattack.beacon.Client;
import net.specialattack.beacon.io.ExtendedOutputStream;
import net.specialattack.beacon.packet.server.ServerPacket;

import java.io.IOException;

public class S0Disconnect implements ServerPacket {
    private final String reason;

    public S0Disconnect(String reason) {
        this.reason = reason;
    }

    @Override
    public int getTypeId() {
        return 0;
    }

    @Override
    public void writePacket(Client client, ExtendedOutputStream output) throws IOException {
        output.writeString(this.reason, 32767);
    }
}
