package net.specialattack.beacon.packet;

import net.specialattack.beacon.ConnectionState;
import net.specialattack.beacon.packet.client.ClientPacket;
import net.specialattack.beacon.packet.client.handshaking.C0Handshake;
import net.specialattack.beacon.packet.client.status.C0Request;
import net.specialattack.beacon.packet.client.status.C1Ping;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Registry {

    private static Map<ConnectionState, Map<Integer, Class<? extends ClientPacket>>> stateToPacketMap;

    static {
        stateToPacketMap = new TreeMap<>();

        Map<Integer, Class<? extends ClientPacket>> handshakingPackets = new HashMap<>();
        handshakingPackets.put(0, C0Handshake.class);
        stateToPacketMap.put(ConnectionState.HANDSHAKING, handshakingPackets);

        Map<Integer, Class<? extends ClientPacket>> statusPackets = new HashMap<>();
        statusPackets.put(0, C0Request.class);
        statusPackets.put(1, C1Ping.class);
        stateToPacketMap.put(ConnectionState.STATUS, statusPackets);

        Map<Integer, Class<? extends ClientPacket>> loginPackets = new HashMap<>();
        //loginPackets.put(0, C0Handshake.class);
        stateToPacketMap.put(ConnectionState.LOGIN, loginPackets);
    }

    public static ClientPacket getClientPacket(ConnectionState state, int packetId) {
        Map<Integer, Class<? extends ClientPacket>> packetTypes = stateToPacketMap.get(state);

        Class<? extends ClientPacket> type = packetTypes.get(packetId);
        if (type != null) {
            try {
                return type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Invalid packet ID received");
    }
}
