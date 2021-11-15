package jp.rouh.totp.client;

import jp.rouh.totp.model.ApplicationService;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SpringLayout.*;

/**
 * メニュー画面。
 * <p>アプリケーション立ち上げ時のデフォルトの画面。
 * <p>ユーザ登録画面かログイン画面に遷移することができます。
 * <p>またこれまでに追加したユーザ情報をリセットすることができます。
 *
 * @author Rouh
 * @version 1.0
 */
class MenuScene extends Scene {

    /**
     * コンストラクタ
     *
     * @param context 画面コンテキスト
     */
    MenuScene(SceneContext context) {
        super(SceneId.MENU, context);
        var layout = new SpringLayout();
        setLayout(layout);

        var registerButton = new FlatButton("新規登録", () -> getContext().moveTo(SceneId.REGISTER));
        var loginButton = new FlatButton("ログイン", () -> getContext().moveTo(SceneId.LOG_IN));
        var resetButton = new FlatButton("ユーザデータ初期化", () -> {
            var answer = JOptionPane.showConfirmDialog(this, "本当に初期化しますか？");
            if (answer == JOptionPane.YES_OPTION) {
                var service = ApplicationService.getInstance();
                service.reset();
            }
        });

        layout.putConstraint(HORIZONTAL_CENTER, registerButton, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, loginButton, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, resetButton, 0, HORIZONTAL_CENTER, this);

        layout.putConstraint(SOUTH, registerButton, -10, NORTH, loginButton);
        layout.putConstraint(VERTICAL_CENTER, loginButton, 0, VERTICAL_CENTER, this);
        layout.putConstraint(NORTH, resetButton, 10, SOUTH, loginButton);

        registerButton.setPreferredSize(new Dimension(350, 50));
        loginButton.setPreferredSize(new Dimension(350, 50));
        resetButton.setPreferredSize(new Dimension(350, 50));

        add(registerButton);
        add(loginButton);
        add(resetButton);
    }
}
