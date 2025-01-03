package nep.timeline.re_telegram.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import nep.timeline.re_telegram.Utils;

public class FileNameUtils {
    private static final String BASE_FOLDER_NAME = "Re-Telegram";
    private static final String CONFIG_FILE_NAME = "configs.cfg";
    private static final String DB_FILE_NAME = "deletedMessages.db";

    private static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().substring(0, 16); // Take first 16 chars
        } catch (NoSuchAlgorithmException e) {
            Utils.log(e);
            return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        }
    }

    public static String getRandomizedFolderName() {
        return "."+hashString(BASE_FOLDER_NAME + UUID.randomUUID().toString());
    }

    public static String getRandomizedConfigName() {
        return "."+hashString(CONFIG_FILE_NAME + UUID.randomUUID().toString()) + ".dat";
    }

    public static String getRandomizedDBName() {
        return "."+hashString(DB_FILE_NAME + UUID.randomUUID().toString()) + ".dat";
    }
}
