package net.specialattack.beacon.packet.server;

import net.specialattack.beacon.Client;
import net.specialattack.beacon.io.ExtendedOutputStream;

import java.io.IOException;

public interface ServerPacket {
    int getTypeId();

    void writePacket(Client client, ExtendedOutputStream output) throws IOException;
}
