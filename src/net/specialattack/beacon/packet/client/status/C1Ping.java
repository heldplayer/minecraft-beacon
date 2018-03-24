package net.specialattack.beacon.packet.client.status;

import net.specialattack.beacon.Client;
import net.specialattack.beacon.io.ExtendedInputStream;
import net.specialattack.beacon.packet.client.ClientPacket;
import net.specialattack.beacon.packet.server.status.S1Pong;

import java.io.IOException;

public class C1Ping implements ClientPacket {
    public long payload;

    @Override
    public void readPacket(ExtendedInputStream in) throws IOException {
        this.payload = in.readLong();
    }

    @Override
    public void handle(Client client, int length) {
        client.sendPacket(new S1Pong(this.payload));
    }
}
