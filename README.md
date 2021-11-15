# TOTP認証のデモアプリケーション

TOTP(時間ベース・ワンタイムパスワード)を独自で実装し、
デスクトップアプリケーション上で実際にOTP認証によるログインを試すアプリケーション。

### 実際の画面

(1)アプリケーションを実行する

![メニュー画面](./documents/screenshots/menu.jpg)

(2)ユーザを新規追加する

![ユーザ登録画面](./documents/screenshots/register.jpg)

(3)QRコードを Google Authenticator で読み込む

![QRコード画面](./documents/screenshots/qrcode.jpg)

(4)Authenticator 上で6桁のOTPが表示される(30秒ごとに更新される)

![Authenticator画面](./documents/screenshots/authenticator.jpg)

(4)ログインする

![ログイン画面](./documents/screenshots/login.jpg)

(5)Authenticator に表示されたOTPを入力する

![OTPログイン画面](./documents/screenshots/otp.jpg)

(6)ログインに成功する

![ログイン成功画面](./documents/screenshots/success.jpg)

### 開発技術

開発言語:
* Java SE 11

統合開発環境:
* IntelliJ IDEA

ビルドツール:
* Gradle

使用外部ライブラリ:
* apache-commons(Base32エンコーダ)
* zxing(QRコード画像生成)

使用Java標準ライブラリ:
* swing (javax.swing)
* HMAC-SHA1 (javax.crypto)


