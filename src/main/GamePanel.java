package main;

import Utils.Utils;
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

    private Grid[] grids;

    public GamePanel()
    {
        mouseInput = new MouseInput(this);
        addKeyListener(new KeyboardInput(this));

        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);

        setBackground(new Color(248, 246, 227,255));

        grids = new Grid[]{
                new Grid(4, 4),
                new Grid(4, 4),
                new Grid(4, 4),
                new Grid(4, 4),
        };

        for (Grid grid : grids)
        {
            grid.spawnRandomTiles(2);
        }

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
            for (Grid grid : grids) {
                grid.moveTiles(Grid.Directions.LEFT);
            } framesSinceMove = 1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_W || e.getKeyCode()==KeyEvent.VK_UP)
        {
            for (Grid grid : grids) {
                grid.moveTiles(Grid.Directions.UP);
            } framesSinceMove = 1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_D || e.getKeyCode()==KeyEvent.VK_RIGHT)
        {
            for (Grid grid : grids) {
                grid.moveTiles(Grid.Directions.RIGHT);
            } framesSinceMove = 1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_S || e.getKeyCode()==KeyEvent.VK_DOWN)
        {
            for (Grid grid : grids) {
                grid.moveTiles(Grid.Directions.DOWN);
            } framesSinceMove = 1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_R)
        {
            grids = new Grid[]{
                    new Grid(4, 4),
                    new Grid(4, 4),
                    new Grid(4, 4),
                    new Grid(4, 4),
            };

            for (Grid grid : grids)
            {
                grid.spawnRandomTiles(2);
            }
        }

        /*for (int i = 0; i < grids.length; i++) {
            for (Grid grid : grids) {
                movementGrid = grid.getMovementGrid();
            }
        }*/
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

    int framesSinceMove = 0, animationLength = 30;

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); // calls the JPanel's paint component method this is used to clean the surface

        if(framesSinceMove > 0) framesSinceMove++;

        g.setColor(new Color(177, 170, 129));
        g.fillRect(0, 0, 612, 612);

        g.setColor(new Color(248, 246, 227));
        g.fillRect(16, 16, 580, 580);

        int elementSize = 36, elementGap = 16;

        int gridSize = (elementSize + elementGap) * grids[0].getRows() - elementGap;

        int screenSize = 612;
        int center = screenSize / 2;

        int borderSize = 32; // size of the window border

        for (int u = 0; u < grids.length; u++) {
            Grid grid = grids[u];

            int gridX = borderSize + center / 2 + center * (u % 2) - (2 * borderSize) * (u % 2) - gridSize / 2;
            int gridY = borderSize + center / 2 + center * (u / 2) - (2 * borderSize) * (u / 2) - gridSize / 2;

            for (int i = 0; i < grid.getRows(); i++) {
                for (int j = 0; j < grid.getColumns(); j++) {

                    int[] position = {gridX + (elementSize + elementGap) * j, gridY + (elementSize + elementGap) * i};

                    drawElement(position, elementSize, "", grid.getColor(0), g);
                }
            }
        }

        for (int u = 0; u < grids.length; u++) {
            Grid grid = grids[u];

            int gridX = borderSize + center/2 + center * (u%2) - (2*borderSize) * (u%2) - gridSize/2;
            int gridY = borderSize + center/2 + center * (u/2) - (2*borderSize) * (u/2) - gridSize/2;

            if (framesSinceMove >= 1) {
                int[][] movementGrid = grid.getMovementGrid();

                int[][] oldGrid = grid.getOldGrid();

                for (int i = 0; i < grid.getRows(); i++) {
                    for (int j = 0; j < grid.getColumns(); j++) {

                        if (oldGrid[i][j] == 0) continue;

                        int r = i, c = j;

                        switch (grid.getLastDirection()) {
                            case UP, DOWN -> r += movementGrid[i][j];
                            case LEFT, RIGHT -> c += movementGrid[i][j];
                        }

                        int x = Utils.lerp(gridX + (elementSize + elementGap) * j,
                                gridX + (elementSize + elementGap) * c,
                                (double) framesSinceMove / animationLength);

                        int y = Utils.lerp(gridY + (elementSize + elementGap) * i,
                                gridY + (elementSize + elementGap) * r,
                                (double) framesSinceMove / animationLength);

                        int[] position = {x, y};

                        String text = Integer.toString(oldGrid[i][j]);

                        drawElement(position, elementSize, text, grid.getColor(oldGrid[i][j]), g);
                    }
                }

                for (Integer[] obj : grid.getSpawnedList()) {
                    int size = Utils.lerpEaseIn(0, elementSize, (double) framesSinceMove / animationLength);

                    int x = gridX + (elementSize + elementGap) * obj[1] + (elementSize / 2 - size / 2);

                    int y = gridY + (elementSize + elementGap) * obj[0] + ((elementSize / 2 - size / 2));

                    int[] position = {x, y};

                    drawElement(position, size, Integer.toString(grid.getValue(obj[0], obj[1])), grid.getColor(obj[0], obj[1]), g);
                }

                if (framesSinceMove >= animationLength) {
                    framesSinceMove = 0;
                }
            } else {
                for (int i = 0; i < grid.getRows(); i++) {
                    for (int j = 0; j < grid.getColumns(); j++) {

                        if (grid.getValue(i, j) == 0) continue;

                        int[] position = {gridX + (elementSize + elementGap) * j, gridY + (elementSize + elementGap) * i};

                        String text = Integer.toString(grid.getValue(i, j));

                        drawElement(position, elementSize, text, grid.getColor(i, j), g);
                    }
                }
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

        Font font = new Font("Monospaced", Font.BOLD, (2*elementSize/3));
        Rectangle rect = new Rectangle(x, y, elementSize, elementSize);

        FontMetrics metrics = g.getFontMetrics(font);

        int xText = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int yText = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setFont(font);

        g.drawString(text, xText, yText);
    }
}