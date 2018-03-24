package net.specialattack.beacon;

import net.specialattack.beacon.io.ExtendedInputStream;
import net.specialattack.beacon.io.ExtendedOutputStream;
import net.specialattack.beacon.packet.server.ServerPacket;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private final Thread reader;
    private final Socket socket;
    private final ExtendedInputStream input;
    private final ExtendedOutputStream output;
    private final Runnable onClosed;

    private final String uuid;

    private boolean stopping = false;
    private boolean closed = false;
    private ConnectionState state = ConnectionState.HANDSHAKING;

    private int protocolVersion = -1;

    public Client(Socket socket, String uuid, Runnable onClosed) throws IOException {
        this.uuid = uuid;

        System.out.printf("[%s] Client connected%n", this.uuid);

        this.reader = new Thread(new ReaderThread(this), "Server Read Thread " + uuid);
        this.socket = socket;
        this.input = new ExtendedInputStream(this.socket.getInputStream());
        this.output = new ExtendedOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
        this.onClosed = onClosed;

        this.reader.start();
    }

    public void stop() throws InterruptedException {
        if (this.stopping) {
            return;
        }
        this.stopping = true;

        try {
            this.reader.interrupt();
            this.reader.join();
        } finally {
            try {
                this.socket.close();
            } catch (IOException ignored) {
            } finally {
                this.closed = true;
                this.onClosed.run();
            }
        }
    }

    public void stopped() {
        if (this.closed) {
            return;
        }
        this.stopping = true;

        try {
            this.socket.close();
        } catch (IOException ignored) {
        } finally {
            this.stopping = this.closed = true;
            this.onClosed.run();
        }
    }

    public boolean connected() {
        if (this.closed) {
            return true;
        }

        if (!this.socket.isConnected() || this.socket.isClosed()) {
            this.stopping = this.closed = true;
            return false;
        }

        if (this.socket.isInputShutdown() || this.socket.isOutputShutdown()) {
            this.stopped();
            return false;
        }
        return true;
    }

    public ExtendedInputStream getInput() {
        return this.input;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void transitionState(ConnectionState state) {
        System.out.printf("[%s] State changing to %s%n", this.uuid, state.name());
        this.state = state;
    }

    public ConnectionState getState() {
        return state;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public synchronized void sendPacket(ServerPacket packet) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(256);
            ExtendedOutputStream wrapped = new ExtendedOutputStream(out);
            wrapped.writeVarInt(packet.getTypeId());

            packet.writePacket(this, wrapped);
            byte[] data = out.toByteArray();

            this.output.writeVarInt(data.length);
            this.output.write(data);
            this.output.flush();

            System.out.printf("[%s] Sent packet with type 0x%H and length %d%n", this.uuid, packet.getTypeId(), data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
