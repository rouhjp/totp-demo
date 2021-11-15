package jp.rouh.totp.client;

import javax.swing.*;
import java.awt.*;

/**
 * 画面抽象クラス。
 * <p>画面の共通処理を記述します。
 *
 * @author Rouh
 * @version 1.0
 */
abstract class Scene extends JPanel {

    /**
     * 画面サイズ
     */
    static final Dimension SCENE_SIZE = new Dimension(500, 300);

    private final SceneId id;
    private final SceneContext context;

    /**
     * コンストラクタ
     *
     * @param id      この画面の画面ID
     * @param context 画面コンテキストへの参照
     */
    Scene(SceneId id, SceneContext context) {
        this.context = context;
        this.id = id;
    }

    /**
     * 画面遷移時に実行される処理。
     * <p>各画面が必要に応じて実装します。
     *
     * @param argument 各画面が初期化に必要な情報。
     */
    void init(String argument) {
        //pass
    }

    /**
     * この画面のIDを取得します。
     *
     * @return 画面ID
     */
    SceneId getId() {
        return id;
    }

    /**
     * 画面コンテキストの参照を取得します。
     *
     * @return 画面コンテキスト
     */
    SceneContext getContext() {
        return context;
    }

    @Override
    public Dimension getPreferredSize() {
        return SCENE_SIZE;
    }
}
