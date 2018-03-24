package net.specialattack.beacon.packet.client;

import net.specialattack.beacon.Client;
import net.specialattack.beacon.io.ExtendedInputStream;

import java.io.IOException;

public interface ClientPacket {

    void readPacket(ExtendedInputStream in) throws IOException;

    void handle(Client client, int length);
}
