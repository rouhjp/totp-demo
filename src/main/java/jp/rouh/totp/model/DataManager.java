package jp.rouh.totp.model;

/**
 * データ管理オブジェクト。
 * <p>ユーザのパスワード、秘密鍵などの情報を永続化する操作のインターフェース。
 *
 * @author Rouh
 * @version 1.0
 */
interface DataManager {

    /**
     * ユーザが登録されているか検査します。
     *
     * @param userName true ユーザが登録されている場合
     *                 false ユーザが登録されていない場合
     */
    boolean userExists(String userName);

    /**
     * ユーザを新規登録します。
     * <p>ユーザ名は半角小文字英字、数字、アンダーバーが使用可能です。
     * <p>ユーザ名に使用不可能な文字列が含まれる場合、{@link IllegalArgumentException}が投げられます。
     * <p>ユーザ名がすでに使用されている場合は、{@link IllegalStateException}が投げられます。
     * <p>ユーザ名が使用可能かどうか判断するには事前に{@link #userExists}で検証する必要があります。
     *
     * @param userName ユーザ名
     * @param password パスワード
     * @throws IllegalArgumentException 使用不可文字列が含まれる場合
     * @throws IllegalStateException    同名のユーザがすでに登録されている場合
     */
    void registerUser(String userName, String password);

    /**
     * ユーザの秘密鍵を登録します。
     *
     * @param userName  ユーザ名
     * @param secretKey 秘密鍵
     * @throws IllegalArgumentException ユーザ名が不正の場合
     */
    void registerUserKey(String userName, String secretKey);

    /**
     * ユーザのパスワードを取得します。
     *
     * @param userName ユーザ名
     * @return パスワード
     * @throws IllegalArgumentException ユーザ名が不正の場合
     */
    String getPassword(String userName);

    /**
     * ユーザの秘密鍵を取得します。
     *
     * @param userName ユーザ名
     * @return 秘密鍵
     * @throws IllegalArgumentException ユーザ名が不正の場合
     * @throws IllegalStateException    秘密鍵が登録されていない場合
     */
    String getSecretKey(String userName);

    /**
     * 保存されたすべての情報を破棄します。
     */
    void dropAll();
}
