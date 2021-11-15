package jp.rouh.totp.client;

/**
 * 画面ID。
 * <p>画面遷移時に指定します。
 *
 * @author Rouh
 * @version 1.0
 * @see SceneContext
 */
enum SceneId {

    /**
     * メニュー画面
     */
    MENU,

    /**
     * ユーザ登録画面
     */
    REGISTER,

    /**
     * ワンタイムパスワード認証用QRコード表示画面
     */
    OTP_QR,

    /**
     * ログイン画面
     */
    LOG_IN,

    /**
     * ワンタイムパスワード認証画面
     */
    OTP_VERIFY,

    /**
     * ユーザ画面
     */
    USER
}
