package ospp.bookinggui.networking;

import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.messages.HandshakeMsg;
import ospp.bookinggui.networking.messages.HandshakeRespMsg;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public abstract class Message {

    public static final int HEADER_SIZE = 5;
    public static final String ENCODING = "UTF8";
    public static final int AL_SIZE = 2;
    private static final Logger logger = Logger.getLogger(Message.class.getName());
    private final Type type;

    protected Message(Type t) {
        this.type = t;
    }

    public static Message parseMessage(short id, int[] body) throws UnsupportedEncodingException {

        Type type = Type.getType(id);

        switch (type) {
            case HANDSHAKE:
                return HandshakeMsg.parse(body);

            case HANDSHAKE_RESPONSE:
                return HandshakeRespMsg.parse(body);

            default:
                logger.severe("Unsupported message id!");
                logger.severe("ID: " + id);
                throw new IllegalArgumentException("Unsupported message id!");
        }
    }

    public static byte[] setALValue(byte[] m, int al, int offset) {
        for (int i = 0; i < AL_SIZE; i++) {
            int shift_mult = AL_SIZE - i - 1;
            int shift_amount = 8 * shift_mult;
            int shifted = al >> shift_amount;
            m[offset + i] = (byte) (shifted & 0xff);
        }
        return m;
    }

    public byte[] constructHeader(int body_size) {
        byte[] header = new byte[HEADER_SIZE];

        int message_length = 1 + body_size;

        header[0] = (byte) ((message_length & 0xff000000) >> 24);
        header[1] = (byte) ((message_length & 0x00ff0000) >> 16);
        header[2] = (byte) ((message_length & 0x0000ff00) >> 8);
        header[3] = (byte) (message_length & 0x000000ff);

        header[4] = type.ID;

        return header;
    }

    public abstract byte[] constructBody() throws UnsupportedEncodingException;

    public byte[] createMessage() throws UnsupportedEncodingException {
        byte[] body = this.constructBody();
        byte[] header = this.constructHeader(body.length);

        return Utils.concat(header, body);
    }

    public enum Type {

        HANDSHAKE,
        HANDSHAKE_RESPONSE,
        GET_PASSENGERS,
        GET_PASSENGERS_RESP,
        BOOK_SEAT,
        BOOK_SEAT_RESP,
        DISCONNECT,
        HEARTBEAT,
        GET_PASSENGER_INFO,
        GET_PASSENGER_INFO_RESP,
        GET_FLIGHT_LIST,
        GET_FLIGHT_LIST_RESP;

        public final byte ID;

        Type() {
            this.ID = (byte) ((this.ordinal() + 1) & 0xFF);
        }

        public static Type getType(int id) {
            if (id > Type.values().length) {
                throw new IllegalArgumentException("Incorrect ID! ID is too large!");
            }
            return Type.values()[id - 1];
        }
    }
}