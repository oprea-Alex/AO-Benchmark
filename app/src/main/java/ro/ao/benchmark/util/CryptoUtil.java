package ro.ao.benchmark.util;

import org.mindrot.jbcrypt.BCrypt;

public abstract class CryptoUtil {

    public static String encryptMessage(String message) {
        return BCrypt.hashpw(message, BCrypt.gensalt(4));
    }

}
