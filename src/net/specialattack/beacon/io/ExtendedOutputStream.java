package net.specialattack.beacon.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@SuppressWarnings("Duplicates")
public class ExtendedOutputStream extends DataOutputStream {
    public ExtendedOutputStream(OutputStream stream) throws IOException {
        super(stream);
    }

    public void writeString(String str, int max) throws IOException {
        if (str.length() > max) {
            throw new IOException(String.format("String exceeds allowed length: %d > %d", str.length(), max));
        }

        byte[] bytes = str.getBytes();
        writeVarInt(bytes.length);
        write(bytes);
    }

    public void writeVarInt(int value) throws IOException {
        do {
            byte temp = (byte) (value & 0x7F);
            value >>>= 7;
            if (value != 0) {
                temp |= 0x80;
            }
            writeByte(temp);
        } while (value != 0);
    }

    public void writeVarLong(long value) throws IOException {
        do {
            byte temp = (byte) (value & 0x7F);
            value >>>= 7;
            if (value != 0) {
                temp |= 0x80;
            }
            writeByte(temp);
        } while (value != 0);
    }
}
