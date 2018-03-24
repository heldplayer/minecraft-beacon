package net.specialattack.beacon;

import net.specialattack.beacon.io.ExtendedInputStream;
import net.specialattack.beacon.packet.Registry;
import net.specialattack.beacon.packet.client.ClientPacket;

import java.io.EOFException;
import java.io.IOException;

public class ReaderThread implements Runnable {

    private final Client client;

    public ReaderThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        System.out.printf("[%s] Running reader%n", this.client.getUuid());

        try (ExtendedInputStream input = this.client.getInput()) {
            while (this.client.connected()) {
                int length = input.readVarInt();
                int packetType = input.readVarInt();

                System.out.printf("[%s] Got packet type 0x%H%n", this.client.getUuid(), packetType);

                ClientPacket packet = Registry.getClientPacket(this.client.getState(), packetType);

                packet.readPacket(input);
                packet.handle(this.client, length);
            }

            System.out.printf("[%s] Client disconnected%n", this.client.getUuid());
        } catch (EOFException ignored) {
            System.out.printf("[%s] Client disconnected%n", this.client.getUuid());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.client.stopped();
        }
    }
}
