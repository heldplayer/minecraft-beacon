package net.specialattack.beacon.packet.client.login;

import net.specialattack.beacon.Client;
import net.specialattack.beacon.Motd;
import net.specialattack.beacon.io.ExtendedInputStream;
import net.specialattack.beacon.packet.client.ClientPacket;
import net.specialattack.beacon.packet.server.login.S0Disconnect;

import java.io.IOException;

public class C0LoginStart implements ClientPacket {
    public String name;

    @Override
    public void readPacket(ExtendedInputStream in) throws IOException {
        this.name = in.readString(255);
    }

    @Override
    public void handle(Client client, int length) {
        client.sendPacket(new S0Disconnect(Motd.getDescription()));
    }
}
