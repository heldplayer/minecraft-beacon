package net.specialattack.beacon.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ExtendedInputStream extends DataInputStream {
    public ExtendedInputStream(InputStream stream) throws IOException {
        super(stream);
    }

    public String readString(int max) throws IOException {
        int length = readVarInt();
        if (length * 4 > max) {
            throw new IOException(String.format("String exceeds allowed length: %d * 4 = %d > %d", length, length * 4, max));
        }
        byte[] bytes = new byte[length];
        readFully(bytes);
        String result = new String(bytes, Charset.forName("UTF8"));
        if (result.length() > max) {
            throw new IOException(String.format("String exceeds allowed length: %d > %d", result.length(), max));
        }
        return result;
    }

    public int readVarInt() throws IOException {
        // 32       24       16       8      1
        // EEEEDDDD DDDCCCCC CCBBBBBB BAAAAAAA
        int result;

        int read = this.readByte();
        result = read & 0x7F;
        if ((read & 0x80) == 0) {
            return result;
        }

        read = this.readByte();
        result |= (read & 0x7F) << 7;
        if ((read & 0x80) == 0) {
            return result;
        }

        read = this.readByte();
        result |= (read & 0x7F) << 14;
        if ((read & 0x80) == 0) {
            return result;
        }

        read = this.readByte();
        result |= (read & 0x7F) << 21;
        if ((read & 0x80) == 0) {
            return result;
        }

        read = this.readByte();
        result |= (read & 0x0F) << 28;
        if ((read & 0xF0) == 0) {
            return result;
        }

        throw new IOException("Invalid VarInt read");
    }

    public long readVarLong() throws IOException {
        // 64       56       48       40       32       24       16       8      1
        // JIIIIIII HHHHHHHG GGGGGGFF FFFFFEEE EEEEDDDD DDDCCCCC CCBBBBBB BAAAAAAA
        long result;

        long read = this.readByte();
        result = read & 0x7F;
        if ((read & 0x80) == 0) {
            return result;
        }

        read = this.readByte();
        result |= (read & 0x7F) << 7;
        if ((read & 0x80) == 0) {
            return result;
        }

        read = this.readByte();
        result |= (read & 0x7F) << 14;
        if ((read & 0x80) == 0) {
            return result;
        }

        read = this.readByte();
        result |= (read & 0x7F) << 21;
        if ((read & 0x80) == 0) {
            return result;
        }

        read = this.readByte();
        result |= (read & 0x7F) << 28;
        if ((read & 0x80) == 0) {
            return result;
        }

        read = this.readByte();
        result |= (read & 0x7F) << 35;
        if ((read & 0x80) == 0) {
            return result;
        }

        read = this.readByte();
        result |= (read & 0x7F) << 41;
        if ((read & 0x80) == 0) {
            return result;
        }

        read = this.readByte();
        result |= (read & 0x7F) << 48;
        if ((read & 0x80) == 0) {
            return result;
        }

        read = this.readByte();
        result |= (read & 0x7F) << 55;
        if ((read & 0x80) == 0) {
            return result;
        }
        read = this.readByte();
        result |= (read & 0x7F) << 62;
        if ((read & 0x80) == 0) {
            return result;
        }

        throw new IOException("Invalid VarLong read");
    }
}
