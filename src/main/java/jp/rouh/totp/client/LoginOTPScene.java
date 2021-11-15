package jp.rouh.totp.client;

import jp.rouh.totp.model.ApplicationService;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.regex.Pattern;

import static javax.swing.SpringLayout.*;

/**
 * ワンタイムパスワード認証画面。
 * <p>ログイン画面でパスワード認証に成功した場合遷移する画面。
 * <p>ユーザにワンタイムパスワードの入力を求め、成功した場合はユーザ情報画面へ遷移する。
 *
 * @author Rouh
 * @version 1.0
 */
class LoginOTPScene extends Scene {
    private final ApplicationService service = ApplicationService.getInstance();
    private final JTextField oneTimePasswordField = new OneTimePasswordField();
    private final JLabel errorMessageLabel = new JLabel();
    private String userName;

    /**
     * コンストラクタ
     *
     * @param context コンテキストの参照
     */
    LoginOTPScene(SceneContext context) {
        super(SceneId.OTP_VERIFY, context);
        var layout = new SpringLayout();
        setLayout(layout);

        oneTimePasswordField.setColumns(16);

        var messageLabel = new JLabel("OTP認証アプリに表示されているワンタイムパスワードを入力してください");
        errorMessageLabel.setForeground(Color.RED);

        var loginButton = new FlatButton("完了", this::loginButtonPressed);
        var cancelButton = new FlatButton("キャンセル", () -> getContext().moveTo(SceneId.MENU));

        loginButton.setPreferredSize(new Dimension(200, 30));
        cancelButton.setPreferredSize(new Dimension(200, 30));

        layout.putConstraint(HORIZONTAL_CENTER, messageLabel, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, errorMessageLabel, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, oneTimePasswordField, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, loginButton, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, cancelButton, 0, HORIZONTAL_CENTER, this);

        layout.putConstraint(NORTH, messageLabel, 10, NORTH, this);
        layout.putConstraint(NORTH, errorMessageLabel, 10, SOUTH, messageLabel);
        layout.putConstraint(NORTH, oneTimePasswordField, 10, SOUTH, errorMessageLabel);
        layout.putConstraint(NORTH, loginButton, 10, SOUTH, oneTimePasswordField);
        layout.putConstraint(NORTH, cancelButton, 10, SOUTH, loginButton);

        add(messageLabel);
        add(errorMessageLabel);
        add(oneTimePasswordField);
        add(loginButton);
        add(cancelButton);
    }

    /**
     * ログインボタン押下時の処理。
     */
    private void loginButtonPressed() {
        if (service.verifyOneTimePassword(userName, oneTimePasswordField.getText())) {
            getContext().moveTo(SceneId.USER, userName);
        } else {
            errorMessageLabel.setText("OTP認証に失敗しました");
        }
    }

    /**
     * 数字(最大6桁)の入力のみを受け付ける制限付きテキストフィールド
     */
    private static class OneTimePasswordField extends JTextField {
        private static final Pattern OTP_PATTERN = Pattern.compile("[0-9]*");

        private OneTimePasswordField() {
            var document = (PlainDocument) getDocument();
            document.setDocumentFilter(new DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    var document = fb.getDocument();
                    var tmpText = document.getText(0, document.getLength()).concat(text);
                    if (tmpText.length() <= 6 && OTP_PATTERN.matcher(tmpText).matches()) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            });
        }
    }

    /**
     * 指定ユーザのOTP用に初期化します。
     *
     * @param userName ユーザ名
     */
    @Override
    void init(String userName) {
        this.userName = userName;
        oneTimePasswordField.setText("");
    }
}
