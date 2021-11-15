package jp.rouh.totp.client;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * フラットデザインのボタン。
 * <p>マウスホバー時に色が変わります。
 *
 * @author Rouh
 * @version 1.0
 */
class FlatButton extends JLabel {
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color FG_COLOR = Color.BLACK;
    private static final Color HOVERED_BG_COLOR = Color.BLACK;
    private static final Color HOVERED_FG_COLOR = Color.WHITE;

    /**
     * コンストラクタ
     *
     * @param text   ボタンに記述するテキスト
     * @param action ボタン押下時の処理
     */
    FlatButton(String text, Runnable action) {
        setText(text);
        setOpaque(true);
        setBorder(new LineBorder(Color.BLACK));
        setBackground(BG_COLOR);
        setForeground(FG_COLOR);
        setHorizontalAlignment(CENTER);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(HOVERED_BG_COLOR);
                setForeground(HOVERED_FG_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(BG_COLOR);
                setForeground(FG_COLOR);
            }
        });
    }
}