package net.specialattack.beacon.packet.client.handshaking;

import net.specialattack.beacon.Client;
import net.specialattack.beacon.ConnectionState;
import net.specialattack.beacon.io.ExtendedInputStream;
import net.specialattack.beacon.packet.client.ClientPacket;

import java.io.IOException;

public class C0Handshake implements ClientPacket {
    public int protocolVersion;
    public String serverAddress;
    public int serverPort;
    public int nextState;

    @Override
    public void readPacket(ExtendedInputStream in) throws IOException {
        this.protocolVersion = in.readVarInt();
        this.serverAddress = in.readString(255);
        this.serverPort = in.readUnsignedShort();
        this.nextState = in.readVarInt();
    }

    @Override
    public void handle(Client client, int length) {
        client.setProtocolVersion(this.protocolVersion);
        client.transitionState(ConnectionState.getForCode(this.nextState));
    }
}
