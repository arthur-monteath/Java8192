package main;

import grid.Grid;
import input.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static GamePanel instance;

    public static GamePanel getInstance()
    {
        return instance;
    }

    private MouseInput mouseInput;
    public static int x=Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int y=Toolkit.getDefaultToolkit().getScreenSize().height;

    private Grid grid;

    public GamePanel()
    {
        mouseInput = new MouseInput(this);
        addKeyListener(new KeyboardInput(this));

        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);

        setBackground(new Color(248, 246, 227,255));

        grid = new Grid(8, 8);
        grid.spawnRandomTiles(16);

        requestFocusInWindow();
        setFocusable(true);
    }

    public void updateMousePosition(MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
    }

    private int mx=0, my=0;

    public void updateGame()
    {

    }

    public int[] getMousePos()
    {
        int[] a = {mx,my};
        return a;
    }

    public void pressed(MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
    }

    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_A || e.getKeyCode()==KeyEvent.VK_LEFT)
        {
            grid.moveTiles(Grid.Directions.LEFT);
        }
        else if(e.getKeyCode()==KeyEvent.VK_W || e.getKeyCode()==KeyEvent.VK_UP)
        {
            grid.moveTiles(Grid.Directions.UP);
        }
        else if(e.getKeyCode()==KeyEvent.VK_D || e.getKeyCode()==KeyEvent.VK_RIGHT)
        {
            grid.moveTiles(Grid.Directions.RIGHT);
        }
        else if(e.getKeyCode()==KeyEvent.VK_S || e.getKeyCode()==KeyEvent.VK_DOWN)
        {
            grid.moveTiles(Grid.Directions.DOWN);
        }
        else if(e.getKeyCode()==KeyEvent.VK_R)
        {
            grid = new Grid(8,8);
            grid.spawnRandomTiles(8);
        }
    }

    public void keyReleased(KeyEvent e)
    {

    }

    public void clicked(MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
    }

    public void released(MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
    }
    AlphaComposite fullOpacity = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1);

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); // calls the JPanel's paint component method this is used to clean the surface

        g.setColor(new Color(177, 170, 129));
        g.fillRect(0, 0, 612, 612);

        g.setColor(new Color(248, 246, 227));
        g.fillRect(16, 16, 580, 580);

        int elementSize = 48, elementGap = 16;

        int gridSize = (elementSize + elementGap) * grid.getRows() - elementGap;

        int gridPosition = 306-gridSize/2;

        for(int i = 0; i < grid.getRows(); i++)
        {
            for(int j = 0; j < grid.getColumns(); j++) {

                int[] position = {gridPosition + (elementSize + elementGap) * j, gridPosition + (elementSize + elementGap) * i};

                String text = grid.getValue(i,j) > 0 ? Integer.toString(grid.getValue(i,j)) : "";

                drawElement(position, elementSize, text, grid.getColor(i,j), g);
            }
        }

        //((Graphics2D) g).setComposite(fullOpacity);
    }

    private void drawElement(int[] gridPosition, int elementSize, String text, Color color, Graphics g)
    {
        g.setColor(color);

        int x = gridPosition[0], y = gridPosition[1];

        g.fillRect(x, y, elementSize, elementSize);

        g.setColor(Color.white);

        Font font = new Font("Monospaced", Font.BOLD, 32);
        Rectangle rect = new Rectangle(x, y, elementSize, elementSize);

        FontMetrics metrics = g.getFontMetrics(font);

        String t = text;

        int xText = rect.x + (rect.width - metrics.stringWidth(t)) / 2;
        int yText = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setFont(font);

        g.drawString(t, xText, yText);
    }
}