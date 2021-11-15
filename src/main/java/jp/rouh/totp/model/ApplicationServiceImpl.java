package jp.rouh.totp.model;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;

/**
 * サービスクラスの実装。
 *
 * @author Rouh
 * @version 1.0
 * @see ApplicationService
 */
class ApplicationServiceImpl implements ApplicationService {
    private static final ApplicationServiceImpl INSTANCE = new ApplicationServiceImpl();
    private final DataManager data = PropertyBasedDataManager.getInstance();

    private ApplicationServiceImpl() {
    }

    @Override
    public boolean userExists(String userName) {
        return data.userExists(userName);
    }

    /**
     * {@inheritDoc}
     * <p>パスワードはSHA256ハッシュ化して永続化します。
     */
    @Override
    public void register(String userName, String password) {
        var hashedPassword = SecurityUtils.digestOf(password);
        data.registerUser(userName, hashedPassword);
        var secretKey = SecurityUtils.generateKey();
        data.registerUserKey(userName, secretKey);
    }

    @Override
    public BufferedImage getAuthQRImage(String userName, int size) {
        var secretKey = data.getSecretKey(userName);
        var authUrl = SecurityUtils.getTotpUrlString("totp-demo", userName, secretKey);
        try {
            var writer = new QRCodeWriter();
            var bitMatrix = writer.encode(authUrl, BarcodeFormat.QR_CODE, size, size, null);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            throw new InternalError(e);
        }
    }

    @Override
    public boolean verifyPassword(String userName, String password) {
        var hashedPassword = SecurityUtils.digestOf(password);
        return data.getPassword(userName).equals(hashedPassword);
    }

    @Override
    public boolean verifyOneTimePassword(String userName, String oneTimePassword) {
        var serviceSideOtp = SecurityUtils.calculateOneTimePassword(data.getSecretKey(userName));
        return serviceSideOtp.equals(oneTimePassword);
    }

    @Override
    public void reset() {
        data.dropAll();
    }

    /**
     * この実装のシングルトンインスタンスを取得します。
     *
     * @return インスタンス
     */
    static ApplicationServiceImpl getInstance() {
        return INSTANCE;
    }
}
