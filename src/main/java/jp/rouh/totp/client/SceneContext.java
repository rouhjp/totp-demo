package jp.rouh.totp.client;

/**
 * 画面コンテキストクラス。
 * <p>各画面(Scene)から扱うことができる操作を定義します。
 *
 * @author Rouh
 * @version 1.0
 */
interface SceneContext {

    /**
     * 指定したIDの画面に遷移します。
     *
     * @param sceneId 画面ID
     */
    default void moveTo(SceneId sceneId) {
        moveTo(sceneId, "");
    }

    /**
     * 指定したIDの画面に遷移します。
     * <p>追加の情報として遷移先の画面に文字列を渡します。
     *
     * @param sceneId  画面ID
     * @param argument 引数文字列
     */
    void moveTo(SceneId sceneId, String argument);

}
