package net.specialattack.beacon.packet.server.status;

import net.specialattack.beacon.Client;
import net.specialattack.beacon.io.ExtendedOutputStream;
import net.specialattack.beacon.packet.server.ServerPacket;

import java.io.IOException;

public class S0Response implements ServerPacket {
    private final String payload;

    public S0Response(String payload) {
        this.payload = payload;
    }

    @Override
    public int getTypeId() {
        return 0;
    }

    @Override
    public void writePacket(Client client, ExtendedOutputStream output) throws IOException {
        output.writeString(this.payload, 32767);
    }
}
