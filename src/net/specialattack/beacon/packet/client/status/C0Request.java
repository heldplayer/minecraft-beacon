package net.specialattack.beacon.packet.client.status;

import net.specialattack.beacon.Client;
import net.specialattack.beacon.Motd;
import net.specialattack.beacon.io.ExtendedInputStream;
import net.specialattack.beacon.packet.client.ClientPacket;
import net.specialattack.beacon.packet.server.status.S0Response;

import java.io.IOException;

public class C0Request implements ClientPacket {
    @Override
    public void readPacket(ExtendedInputStream in) throws IOException {
    }

    @Override
    public void handle(Client client, int length) {
        S0Response response = new S0Response(Motd.getMotd(client.getProtocolVersion()));

        client.sendPacket(response);
    }
}
