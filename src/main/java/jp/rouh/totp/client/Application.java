package jp.rouh.totp.client;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ワンタイムパスワードデモアプリケーション。
 * <p>画面上でユーザ登録と、パスワード及びOTP認証によるログインを試すことができるアプリケーション。
 * <p>OTP認証には、別途スマートフォン上で認証ソフト(Google Authenticator)が必要。
 *
 * @author Rouh
 * @version 1.0
 */
public class Application implements SceneContext {
    private final CardLayout cardLayout;
    private final JPanel rootPanel;
    private final Map<SceneId, Scene> scenes = new HashMap<>();

    private Application() {
        var frame = new JFrame();
        frame.setTitle("ワンタイムパスワード デモ");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        rootPanel = new JPanel(cardLayout);

        addScene(new MenuScene(this));
        addScene(new RegisterScene(this));
        addScene(new QRCodeScene(this));
        addScene(new LoginScene(this));
        addScene(new LoginOTPScene(this));
        addScene(new UserScene(this));

        moveTo(SceneId.MENU);

        frame.add(rootPanel);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * 画面をフレームに追加します。
     * <p>追加された画面には{@link #moveTo}で遷移することができます。
     *
     * @param scene 画面
     */
    private void addScene(Scene scene) {
        rootPanel.add(scene, scene.getId().toString());
        scenes.put(scene.getId(), scene);
    }

    @Override
    public void moveTo(SceneId sceneId, String argument) {
        cardLayout.show(rootPanel, sceneId.toString());
        scenes.get(sceneId).init(argument);
    }

    /**
     * メインメソッド。
     * <p>アプリケーションのフレームを立ち上げます。
     *
     * @param args 使用しない
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}
