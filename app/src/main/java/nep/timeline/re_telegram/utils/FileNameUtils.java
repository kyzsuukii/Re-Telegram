package nep.timeline.re_telegram.utils;

import android.content.Context;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import nep.timeline.re_telegram.Utils;

public class FileNameUtils {
    private static final String KEY_FOLDER = "folder_name";
    private static final String KEY_CONFIG = "config_name";
    private static final String KEY_DB = "db_name";

    private static String folderName;
    private static String configName;
    private static String dbName;
    private static DatabaseHelper dbHelper;

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

    private static void initializeNames(Context context) {
        if (folderName != null) return; // Already initialized

        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
        }

        folderName = dbHelper.getValue(KEY_FOLDER);
        configName = dbHelper.getValue(KEY_CONFIG);
        dbName = dbHelper.getValue(KEY_DB);

        if (folderName == null || configName == null || dbName == null) {
            folderName = "." + hashString("folder" + UUID.randomUUID().toString());
            configName = "." + hashString("config" + UUID.randomUUID().toString()) + ".dat";
            dbName = "." + hashString("db" + UUID.randomUUID().toString()) + ".dat";

            dbHelper.setValue(KEY_FOLDER, folderName);
            dbHelper.setValue(KEY_CONFIG, configName);
            dbHelper.setValue(KEY_DB, dbName);
        }
    }

    public static String getRandomizedFolderName(Context context) {
        initializeNames(context);
        return folderName;
    }

    public static String getRandomizedConfigName(Context context) {
        initializeNames(context);
        return configName;
    }

    public static String getRandomizedDBName(Context context) {
        initializeNames(context);
        return dbName;
    }
}
