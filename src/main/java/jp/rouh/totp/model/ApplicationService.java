package jp.rouh.totp.model;

import java.awt.image.BufferedImage;

/**
 * アプリケーションサービスクラス。
 * <p>ワンタイムパスワードデモアプリケーション上で必要な処理を記述します。
 *
 * @author Rouh
 * @version 1.0
 */
public interface ApplicationService {

    /**
     * ユーザが登録されているか検査します。
     *
     * @param userName true ユーザが登録されている場合
     *                 false ユーザが登録されていない場合
     */
    boolean userExists(String userName);

    /**
     * ユーザを新規作成します。
     * 新規作成にあたり、ユーザの秘密鍵が生成されます。
     *
     * @param userName ユーザ名
     * @param password パスワード
     */
    void register(String userName, String password);

    /**
     * OTP認証のためのQRコード画像を取得します。
     *
     * @param userName ユーザ名
     * @param size     一辺の幅
     * @return QRコード画像
     */
    BufferedImage getAuthQRImage(String userName, int size);

    /**
     * パスワード認証を検証します。
     *
     * @param userName ユーザ名
     * @param password パスワード
     * @return true 認証に成功した場合
     * false 認証に失敗した場合
     */
    boolean verifyPassword(String userName, String password);

    /**
     * ワンタイムパスワード認証を検証します。
     *
     * @param userName        ユーザ名
     * @param oneTimePassword ワンタイムパスワード
     * @return true 認証に成功した場合
     * false 認証に失敗した場合
     * @throws IllegalArgumentException ワンタイムパスワードのフォーマットが不正の場合
     */
    boolean verifyOneTimePassword(String userName, String oneTimePassword);

    /**
     * 登録情報をすべて破棄します。
     */
    void reset();

    /**
     * サービスの実装を取得します。
     *
     * @return サービスの実装
     */
    static ApplicationService getInstance() {
        return ApplicationServiceImpl.getInstance();
    }
}
