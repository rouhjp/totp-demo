package jp.rouh.totp.client;

import jp.rouh.totp.model.ApplicationService;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static javax.swing.SpringLayout.*;

/**
 * 新規登録画面。
 * <p>ユーザ名とパスワードの入力を求め、ユーザを新規登録します。
 * <p>入力が正しく完了した場合、OTP認証用QRコード表示画面に遷移します。
 *
 * @author Rouh
 * @version 1.0
 */
class RegisterScene extends Scene {
    private final ApplicationService service = ApplicationService.getInstance();
    private final JTextField userNameField = new JTextField();
    private final JPasswordField firstPasswordField = new JPasswordField();
    private final JPasswordField secondPasswordField = new JPasswordField();
    private final JLabel errorMessageLabel = new JLabel();

    /**
     * コンストラクタ
     *
     * @param context 画面コンテキスト
     */
    RegisterScene(SceneContext context) {
        super(SceneId.REGISTER, context);
        var layout = new SpringLayout();
        setLayout(layout);

        var messageLabel = new JLabel("ユーザを新規登録します");
        errorMessageLabel.setForeground(Color.RED);

        var userNameLabel = new JLabel("ユーザ名: ");
        var firstPasswordLabel = new JLabel("パスワード: ");
        var secondPasswordLabel = new JLabel("パスワード(再): ");
        userNameField.setColumns(16);
        firstPasswordField.setColumns(16);
        secondPasswordField.setColumns(16);

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
                                        .addComponent(firstPasswordLabel)
                                        .addComponent(firstPasswordField)
                        ).addGroup(
                                groupLayout.createParallelGroup()
                                        .addComponent(secondPasswordLabel)
                                        .addComponent(secondPasswordField)
                        )
        );
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(
                                groupLayout.createParallelGroup()
                                        .addComponent(userNameLabel)
                                        .addComponent(firstPasswordLabel)
                                        .addComponent(secondPasswordLabel)
                        ).addGroup(
                                groupLayout.createParallelGroup()
                                        .addComponent(userNameField)
                                        .addComponent(firstPasswordField)
                                        .addComponent(secondPasswordField)
                        )
        );

        var registerButton = new FlatButton("登録", () -> {
            if (userNameField.getText().isEmpty()) {
                errorMessageLabel.setText("ユーザ名を入力してください");
                return;
            }
            if (!userNameField.getText().matches("^[0-9a-z_]+$")) {
                errorMessageLabel.setText("ユーザ名は半角英小文字、数字、アンダーバーのみ使用可能です");
                return;
            }
            if (userNameField.getText().length() > 16) {
                errorMessageLabel.setText("ユーザ名が長すぎます(最大16文字)");
                return;
            }
            if (firstPasswordField.getPassword().length == 0) {
                errorMessageLabel.setText("パスワードを入力してください");
                return;
            }
            if (!Arrays.equals(firstPasswordField.getPassword(), secondPasswordField.getPassword())) {
                errorMessageLabel.setText("パスワードが一致しません");
                return;
            }
            if (service.userExists(userNameField.getText())) {
                errorMessageLabel.setText("このユーザ名は既に使用されています");
                return;
            }
            service.register(userNameField.getText(), new String(firstPasswordField.getPassword()));
            getContext().moveTo(SceneId.OTP_QR, userNameField.getText());
        });

        var cancelButton = new FlatButton("キャンセル", () -> getContext().moveTo(SceneId.MENU));

        registerButton.setPreferredSize(new Dimension(200, 30));
        cancelButton.setPreferredSize(new Dimension(200, 30));

        layout.putConstraint(HORIZONTAL_CENTER, messageLabel, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, errorMessageLabel, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, inputPanel, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, registerButton, 0, HORIZONTAL_CENTER, this);
        layout.putConstraint(HORIZONTAL_CENTER, cancelButton, 0, HORIZONTAL_CENTER, this);

        layout.putConstraint(NORTH, messageLabel, 10, NORTH, this);
        layout.putConstraint(NORTH, errorMessageLabel, 10, SOUTH, messageLabel);
        layout.putConstraint(NORTH, inputPanel, 10, SOUTH, errorMessageLabel);
        layout.putConstraint(NORTH, registerButton, 10, SOUTH, inputPanel);
        layout.putConstraint(NORTH, cancelButton, 10, SOUTH, registerButton);

        add(messageLabel);
        add(errorMessageLabel);
        add(inputPanel);
        add(registerButton);
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
        firstPasswordField.setText("");
        secondPasswordField.setText("");
        errorMessageLabel.setText("");
    }
}
