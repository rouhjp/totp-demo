package jp.rouh.totp.client;

import jp.rouh.totp.model.ApplicationService;

import javax.swing.*;

import java.awt.*;

import static javax.swing.SpringLayout.*;

/**
 * ログイン画面。
 * <p>メニュー画面から遷移します。
 * <p>ユーザにユーザ名とパスワードの入力を求め、認証に成功した場合さらにOTP認証画面に遷移します。
 *
 * @author Rouh
 * @version 1.0
 */
class LoginScene extends Scene {
    private final ApplicationService service = ApplicationService.getInstance();
    private final JTextField userNameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JLabel errorMessageLabel = new JLabel();

    /**
     * コンストラクタ
     *
     * @param context コンテキストの参照
     */
    LoginScene(SceneContext context) {
        super(SceneId.LOG_IN, context);
        var layout = new SpringLayout();
        setLayout(layout);

        var messageLabel = new JLabel("ユーザ名とパスワードを入力してください");
        errorMessageLabel.setForeground(Color.RED);

        var userNameLabel = new JLabel("ユーザ名: ");
        var passwordLabel = new JLabel("パスワード: ");
        userNameField.setColumns(16);
        passwordField.setColumns(16);

        var inputPanel = new JPanel();
        var groupLayout = new GroupLayout(inputPanel);
        inputPanel.setLayout(groupLayout);
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(
                                groupLayout.createParallelGroup()
                                        .addComponent(userNameLabel)
                                        .addComponent(userNameField)
                        ).addGroup(
                                groupLayout.createParallelGroup()
                                        .addComponent(passwordLabel)
                                        .addComponent(passwordField)
                        )
        );
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(
                                groupLayout.createParallelGroup()
                                        .addComponent(userNameLabel)
                                        .addComponent(passwordLabel)
                        ).addGroup(
                                groupLayout.createParallelGroup()
                                        .addComponent(userNameField)
                                        .addComponent(passwordField)
                        )
        );

        var loginButton = new FlatButton("ログイン", () -> {
            if (userNameField.getText().isEmpty()) {
                errorMessageLabel.setText("ユーザ名を入力してください");
                return;
            }
            if (passwordField.getPassword().length == 0) {
                errorMessageLabel.setText("パスワードを入力してください");
                return;
            }
            if (service.userExists(userNameField.getText())) {
                if (service.verifyPassword(userNameField.getText(), new String(passwordField.getPassword()))) {
                    getContext().moveTo(SceneId.OTP_VERIFY, userNameField.getText());
                }
            }
            errorMessageLabel.setText("認証に失敗しました");
        });

        var cancelButton = new FlatButton("キャンセル", () -> getContext().moveTo(SceneId.MENU));

        loginButton.setPreferredSize(new Dimension(200, 30));
        cancelButton.setPreferredSize(new Dimension(200, 30));

        layout.putConstraint(HORIZONTAL_CENTER, messageLabel, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, errorMessageLabel, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, inputPanel, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, loginButton, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, cancelButton, 0, HORIZONTAL_CENTER, this);

        layout.putConstraint(NORTH, messageLabel, 10, NORTH, this);
        layout.putConstraint(NORTH, errorMessageLabel, 10, SOUTH, messageLabel);
        layout.putConstraint(NORTH, inputPanel, 10, SOUTH, errorMessageLabel);
        layout.putConstraint(NORTH, loginButton, 10, SOUTH, inputPanel);
        layout.putConstraint(NORTH, cancelButton, 10, SOUTH, loginButton);

        add(messageLabel);
        add(errorMessageLabel);
        add(inputPanel);
        add(loginButton);
        add(cancelButton);
    }

    /**
     * テキストフィールドを初期化します。
     *
     * @param ignored 使用しない
     */
    @Override
    void init(String ignored) {
        userNameField.setText("");
        passwordField.setText("");
        errorMessageLabel.setText("");
    }
}
