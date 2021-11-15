package jp.rouh.totp.client;

import jp.rouh.totp.model.ApplicationService;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SpringLayout.*;

/**
 * ワンタイムパスワード認証用QRコード表示画面。
 * <p>ユーザ登録画面から遷移します。
 * <p>ユーザ登録時に生成された秘密鍵に対応するOTP認証URLを生成し、QRコード画像として画面に表示します。
 *
 * @author Rouh
 * @version 1.0
 */
class QRCodeScene extends Scene {
    private final ApplicationService service = ApplicationService.getInstance();
    private final JLabel qrLabel = new JLabel();

    /**
     * コンストラクタ
     *
     * @param context コンテキストの参照
     */
    QRCodeScene(SceneContext context) {
        super(SceneId.OTP_QR, context);
        var layout = new SpringLayout();
        setLayout(layout);

        var messageLabel = new JLabel("認証用QRコード(他人に共有しないでください)");
        var okButton = new FlatButton("OK", () -> getContext().moveTo(SceneId.MENU));
        okButton.setPreferredSize(new Dimension(200, 30));

        layout.putConstraint(HORIZONTAL_CENTER, messageLabel, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, qrLabel, 0, HORIZONTAL_CENTER, messageLabel);
        layout.putConstraint(HORIZONTAL_CENTER, okButton, 0, HORIZONTAL_CENTER, qrLabel);
        layout.putConstraint(NORTH, messageLabel, 10, NORTH, this);
        layout.putConstraint(NORTH, qrLabel, 10, SOUTH, messageLabel);
        layout.putConstraint(NORTH, okButton, 10, SOUTH, qrLabel);

        add(messageLabel);
        add(qrLabel);
        add(okButton);
    }

    /**
     * 指定のユーザ名に対応するQRコード画像で画面を初期化します。
     *
     * @param userName ユーザ名
     */
    @Override
    void init(String userName) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                var image = service.getAuthQRImage(userName, 200);
                qrLabel.setIcon(new ImageIcon(image));
                return null;
            }
        }.execute();
    }
}
