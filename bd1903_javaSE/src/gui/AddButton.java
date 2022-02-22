package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddButton {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setBounds(200,120,200,80);
        frame.setSize(500,400);
        frame.setTitle("添加按钮");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        JButton button = new JButton("按钮");
        button.setBounds(0,0,20,10);
        button.setSize(50,40);
        frame.add(button);
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //单击事件
                System.out.println("单击了一次");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //按下事件
                System.out.println("按下了一次");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //按下之后释放事件
                System.out.println("释放了一次");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //移动到他上面事件
                System.out.println("放在上面了一次");
                int x = button.getX();
                if(x==0) {
                    button.setBounds(300,200,20,20);
                }else{
                    button.setBounds(0,0,20,20);
                }
                button.setSize(20,20);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //移出事件
                System.out.println("移出了一次");
            }
        });
    }
}
