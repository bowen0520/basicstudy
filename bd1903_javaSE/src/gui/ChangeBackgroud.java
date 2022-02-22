package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ColorModel;

public class ChangeBackgroud {
    JFrame jFrame;
    private String name;
    private int weight;
    private int height;
    private int x;
    private int y;

    public ChangeBackgroud(String name, int height, int weight, int x, int y) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.x = x;
        this.y = y;
        jFrame = new JFrame(name);
        jFrame.setBounds(this.x,this.y,this.weight,this.height);
        jFrame.setSize(this.weight,this.height);
        jFrame.setVisible(true);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.addButton(jFrame);
    }

    public ChangeBackgroud(String name){
        this(name,300,400,400,300);
    }

    public JButton getButton(MouseAdapter m,String name,int R,int G,int B){
        JButton button = new JButton(name);
        button.setSize(40,30);
        button.setBackground(new Color(R,G,B));
        button.addMouseListener(m);
        return button;
    }

    public void addButton(JFrame frame){
        JPanel panel = new JPanel();
        frame.add(panel,BorderLayout.CENTER);
        MouseAdapter m = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Component component = e.getComponent();
                panel.setBackground(component.getBackground());
            }
        };
        frame.add(getButton(m,"红色",209,26,45),BorderLayout.WEST);
        frame.add(getButton(m,"灰色",247,244,237),BorderLayout.NORTH);
        frame.add(getButton(m,"紫色",128,118,163),BorderLayout.EAST);
        frame.add(getButton(m,"蓝色",22,119,179),BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        ChangeBackgroud changeBackgroud = new ChangeBackgroud("F");
    }

}
