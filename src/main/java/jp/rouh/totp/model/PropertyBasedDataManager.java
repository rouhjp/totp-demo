package jp.rouh.totp.model;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * プロパティファイルを用いたデータ管理オブジェクト。
 * <p>データをプロパティファイルに書き出して永続化します。
 * <p>プロパティファイルはOSの一時ディレクトリに totp_demo.properties として保存されます。
 *
 * @author Rouh
 * @version 1.0
 */
class PropertyBasedDataManager implements DataManager {
    private static final Path PROP_PATH = Paths.get(System.getProperty("java.io.tmpdir"), "totp_demo.properties");
    private static final PropertyBasedDataManager INSTANCE = new PropertyBasedDataManager();
    private static final Pattern USER_NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
    private final Properties properties;

    private PropertyBasedDataManager() {
        properties = new Properties();
        if (Files.exists(PROP_PATH)) {
            try (BufferedReader reader = Files.newBufferedReader(PROP_PATH, StandardCharsets.UTF_8)) {
                properties.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void store() {
        try (BufferedWriter writer = Files.newBufferedWriter(PROP_PATH, StandardCharsets.UTF_8)) {
            properties.store(writer, "TOTP demo user data file. you can delete this file!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean userExists(String userName) {
        return properties.containsKey("user." + userName);
    }

    @Override
    public void registerUser(String userName, String password) {
        if (!USER_NAME_PATTERN.matcher(userName).matches()) {
            throw new IllegalArgumentException("invalid user name: " + userName);
        }
        properties.setProperty("user." + userName, password);
        store();
    }

    @Override
    public void registerUserKey(String userName, String secretKey) {
        if (!userExists(userName)) {
            throw new IllegalArgumentException("user not found: " + userName);
        }
        properties.setProperty("user." + userName + ".key", secretKey);
        store();
    }

    @Override
    public String getPassword(String userName) {
        if (!userExists(userName)) {
            throw new IllegalArgumentException("user not found: " + userName);
        }
        return properties.getProperty("user." + userName);
    }

    @Override
    public String getSecretKey(String userName) {
        if (!userExists(userName)) {
            throw new IllegalArgumentException("user not found: " + userName);
        }
        if (!properties.containsKey("user." + userName + ".key")) {
            throw new IllegalStateException("user key not registered: " + userName);
        }
        return properties.getProperty("user." + userName + ".key");
    }

    @Override
    public void dropAll() {
        properties.clear();
        store();
    }

    /**
     * この実装のシングルトンインスタンスを取得します。
     *
     * @return インスタンス
     */
    static PropertyBasedDataManager getInstance() {
        return INSTANCE;
    }
}
