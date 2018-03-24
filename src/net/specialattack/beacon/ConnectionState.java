package net.specialattack.beacon;

import java.util.Map;
import java.util.TreeMap;

public enum ConnectionState {
    UNKNOWN(-1), HANDSHAKING(0), STATUS(1), LOGIN(2);

    public final int code;

    ConnectionState(int code) {
        this.code = code;
    }

    private static Map<Integer, ConnectionState> codeMap = new TreeMap<>();

    static {
        for (ConnectionState state : values()) {
            if (codeMap.containsKey(state.code)) {
                throw new IllegalStateException("Duplicate state code: " + state.code);
            }
            codeMap.put(state.code, state);
        }
    }

    public static ConnectionState getForCode(int code) {
        return codeMap.getOrDefault(code, UNKNOWN);
    }
}
