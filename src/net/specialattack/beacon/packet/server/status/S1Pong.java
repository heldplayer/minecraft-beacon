package net.specialattack.beacon.packet.server.status;

import net.specialattack.beacon.Client;
import net.specialattack.beacon.io.ExtendedOutputStream;
import net.specialattack.beacon.packet.server.ServerPacket;

import java.io.IOException;

public class S1Pong implements ServerPacket {
    private final long payload;

    public S1Pong(long payload) {
        this.payload = payload;
    }

    @Override
    public int getTypeId() {
        return 1;
    }

    @Override
    public void writePacket(Client client, ExtendedOutputStream output) throws IOException {
        output.writeLong(this.payload);
    }
}
