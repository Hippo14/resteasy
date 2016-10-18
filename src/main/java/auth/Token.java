package auth;

import auth.parts.Header;
import auth.parts.Payload;
import auth.parts.Signature;
import model.Users;
import model.UsersKeys;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by MSI on 2016-10-17.
 */
public class Token {

    final Header header;
    final Payload payload;
    final Signature signature;

    final Users user;

    public Token(Header header, Payload payload, Signature signature, Users user) {
        this.header = header;
        this.payload = payload;
        this.signature = signature;
        this.user = user;
    }

    public Header getHeader() {
        return header;
    }

    public Payload getPayload() {
        return payload;
    }

    public Signature getSignature() {
        return signature;
    }

    public Users getUser() {
        return user;
    }

    @Override
    public String toString() {
        try {
            return header.toBase64() + "." + payload.toBase64() + "." + signature.toBase64();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class TokenBuilder {
        private Header nestedHeader;
        private Payload nestedPayload;
        private Signature nestedSignature;
        private Users nestedUser;

        public TokenBuilder() {
        }

        public TokenBuilder header(final Header header) {
            this.nestedHeader = header;
            return this;
        }

        public TokenBuilder payload(final Payload payload) {
            this.nestedPayload = payload;
            return this;
        }

        public TokenBuilder signature(final byte[] key) throws UnsupportedEncodingException {
            String sHeader = nestedHeader.toBase64();
            String sPayload = nestedPayload.toBase64();
            this.nestedSignature = new Signature(sHeader, sPayload, key);
            return this;
        }

        public TokenBuilder user(final Users user) {
            this.nestedUser = user;
            return this;
        }

        public Token build() {
            return new Token(nestedHeader, nestedPayload, nestedSignature, nestedUser);
        }
    }

}
