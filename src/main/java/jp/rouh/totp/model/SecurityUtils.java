package jp.rouh.totp.model;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * セキュリティ関連処理ユーティリティクラス。
 *
 * @author Rouh
 * @version 1.0
 */
final class SecurityUtils {
    private SecurityUtils() {
        throw new AssertionError("instantiate utility class");
    }

    /**
     * TOTP認証用URLを文字列で取得します。
     * <p>アルゴリズムはSHA1、桁数は6桁、更新時間は30秒とします。
     *
     * @param issuer    発行者
     * @param userName  ユーザ名
     * @param secretKey 鍵(Base32形式の文字列)
     * @return 認証用URL文字列
     */
    @SuppressWarnings("all")
    static String getTotpUrlString(String issuer, String userName, String secretKey) {
        return "otpauth://totp/" + issuer + ":" + userName + "?secret=" + secretKey +
                "&issuer=" + issuer + "&algorithm=SHA1&digits=6&period=30";
    }

    /**
     * ランダムな80bitの鍵を生成し、Base32エンコードした長さ16の文字列を取得します。
     *
     * @return 鍵文字列(Base32形式16文字)
     */
    static String generateKey() {
        try {
            //AESで鍵を生成し、長さをTOTP用に80bitに切り詰める
            var generator = KeyGenerator.getInstance("AES");
            var keyByteArray = generator.generateKey().getEncoded();
            var base32encoder = new Base32();
            return base32encoder.encodeAsString(Arrays.copyOf(keyByteArray, 10));
        } catch (NoSuchAlgorithmException e) {
            throw new InternalError(e);
        }
    }

    /**
     * 現在のタイムコードをバイト列で取得します。
     * <p>タイムコードは30秒を一単位としたUNIXエポック時間からの単位です。
     *
     * @return タイムコード(長さ8のバイト列)
     */
    private static byte[] getCurrentTimeCodeByteArray() {
        long currentUnixTime = Instant.now().toEpochMilli() / 1000;
        long timeCode = currentUnixTime / 30;
        //long型をバイト列に変換し、8バイト固定長になるよう0でパディング
        byte[] byteArray = BigInteger.valueOf(timeCode).toByteArray();
        byte[] fixedByteArray = new byte[8];
        for (int i = 1; i <= byteArray.length; i++) {
            fixedByteArray[fixedByteArray.length - i] = byteArray[byteArray.length - i];
        }
        return fixedByteArray;
    }

    /**
     * 秘密鍵に対応する現在のワンタイムパスワードを文字列で取得します。
     *
     * @param base32Key 鍵文字列(Base32形式16文字)
     * @return ワンタイムパスワード(数字6文字)
     */
    static String calculateOneTimePassword(String base32Key) {
        //秘密鍵とタイムコードからHMACダイジェスト値を取得
        var base32encoder = new Base32();
        var secretKey = base32encoder.decode(base32Key);
        byte[] timeCodeByteArray = getCurrentTimeCodeByteArray();
        byte[] digest = calculateDigest(secretKey, timeCodeByteArray);
        //HMACダイジェスト値から末尾4ビットをオフセット値として、4バイトの数値(anchoredNumber)を取得
        var last4BytesBinaryString = binaryStringOf(digest[19]).substring(4);
        int offset = Integer.parseInt(last4BytesBinaryString, 2);
        var anchoredNumberBinaryString = IntStream.range(offset, offset + 4)
                .mapToObj(i -> binaryStringOf(digest[i]))
                .collect(Collectors.joining());
        //正の値となるよう、先頭1ビットを0へ変更
        int anchoredNumber = Integer.parseInt("0".concat(anchoredNumberBinaryString.substring(1)), 2);
        //下6桁をOTPパスワードとして利用
        int otp = anchoredNumber % 1_000_000;
        return String.format("%06d", otp);
    }

    /**
     * バイトを8文字のバイナリ文字列に変換します。
     *
     * @param b バイト
     * @return バイナリ文字列
     */
    private static String binaryStringOf(byte b) {
        int code = Byte.toUnsignedInt(b);
        var bs = Integer.toBinaryString(code);
        return String.format("%8s", bs).replace(" ", "0");
    }

    /**
     * HMAC(SHA-1)を用いてメッセージのダイジェスト値を取得します。
     *
     * @param keyByteArray     鍵
     * @param messageByteArray メッセージ
     * @return ダイジェスト値(長さ20のバイト列)
     */
    private static byte[] calculateDigest(byte[] keyByteArray, byte[] messageByteArray) {
        try {
            var hmac = Mac.getInstance("HmacSHA1");
            hmac.init(new SecretKeySpec(keyByteArray, "HmacSHA1"));
            return hmac.doFinal(messageByteArray);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalError(e);
        }
    }

    /**
     * SHA256のハッシュ値を取得します。
     *
     * @param original 元テキスト
     * @return ハッシュ値
     */
    static String digestOf(String original) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            md.update(original.getBytes());
            byte[] digest = md.digest();
            return IntStream.range(0, digest.length)
                    .mapToObj(i -> String.format("%02x", 0xff & digest[i]))
                    .collect(Collectors.joining());
        } catch (NoSuchAlgorithmException e) {
            throw new InternalError(e);
        }
    }
}
