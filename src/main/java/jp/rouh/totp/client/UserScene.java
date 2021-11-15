package jp.rouh.totp.client;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SpringLayout.*;

/**
 * ユーザ情報画面。
 * <p>パスワード認証及びOTP認証に成功した場合に遷移される画面。
 *
 * @author Rouh
 * @version 1.0
 */
class UserScene extends Scene {
    private final JLabel userMessageLabel = new JLabel();

    /**
     * コンストラクタ
     *
     * @param context コンテキストの参照
     */
    UserScene(SceneContext context) {
        super(SceneId.USER, context);
        var layout = new SpringLayout();
        setLayout(layout);

        var backButton = new FlatButton("戻る", () -> getContext().moveTo(SceneId.MENU));
        backButton.setPreferredSize(new Dimension(200, 30));

        layout.putConstraint(HORIZONTAL_CENTER, userMessageLabel, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, backButton, 0, HORIZONTAL_CENTER, this);

        layout.putConstraint(NORTH, userMessageLabel, 10, NORTH, this);
        layout.putConstraint(NORTH, backButton, 10, SOUTH, userMessageLabel);

        add(userMessageLabel);
        add(backButton);
    }

    /**
     * 指定されたユーザ用に初期化します。
     *
     * @param userName ユーザ名
     */
    @Override
    void init(String userName) {
        userMessageLabel.setText("ログイン成功！こんにちは" + userName + "さん！");
    }
}
