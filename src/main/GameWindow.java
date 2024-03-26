package main;

import java.awt.Toolkit;

import javax.swing.JFrame;

public class GameWindow {

    private JFrame frame;

    public GameWindow(GamePanel gamePanel)
    {
        frame = new JFrame();

        //frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getHeight());

        frame.setSize(612,612);

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.setVisible(true);

        //device.setFullScreenWindow(frame);
    }

    public void setPos(int x, int y)
    {
        frame.setLocation(x, y);
    }
}