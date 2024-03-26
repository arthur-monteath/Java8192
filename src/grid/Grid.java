package grid;

import Utils.Utils;

import java.awt.*;
import java.util.Random;

public class Grid {

    private int[][] grid;

    private Random random;

    public Grid(int rows, int cols)
    {
        grid = new int[rows][cols];

        random = new Random();
    }

    public int getRows()
    {
        return grid.length;
    }

    public int getColumns()
    {
        return grid[0].length;
    }

    public int getValue(int row, int col)
    {
        return grid[row][col];
    }

    public void setValue(int row, int col, int value)
    {
        grid[row][col] = value;
    }

    private Color[] colors =
    {
        new Color(177, 170, 129),
        new Color(151, 231, 225),
        new Color(106, 212, 221),
        new Color(122, 162, 227),
        new Color(78, 96, 182),
        new Color(45, 49, 134),
        new Color(54, 37, 117)
    };

    public Color getColor(int row, int col)
    {
        int colorIndex = (int) (Math.log(grid[row][col]) / Math.log(2));

        if (colorIndex < 0) colorIndex = 0;

        if (colorIndex >= colors.length) colorIndex = colors.length - 1;

        return colors[colorIndex];
    }

    public void spawnRandomTiles(int amount)
    {
        for(int i = 0; i < amount; i++)
        {
            spawnRandomTile();
        }
    }

    private void spawnRandomTile()
    {
        int i = 100;
        while(i > 0)
        {
            int row = random.nextInt(0, getRows()),
                    col = random.nextInt(0, getColumns());

            if(grid[row][col] == 0)
            {
                grid[row][col] = random.nextInt(1,3) * 2; break;
            }

            i--;
        }
    }

    public enum Directions
    {
        LEFT,
        UP,
        RIGHT,
        DOWN
    }

    public void moveTiles(Directions direction)
    {
        int[] dir = switch(direction) {
            case LEFT -> new int[]{0, -1};
            case UP -> new int[]{-1, 0};
            case RIGHT -> new int[]{0, 1};
            case DOWN -> new int[]{1, 0};
        };

        boolean hasChanged = false;

        for(int i = 0; i < getRows(); i++)
        {
            for(int j = 0; j < getColumns(); j++)
            {
                int value = getValue(i,j);

                if(value == 0) continue;

                // How do you activate DNA that is in histones?

                int row = i, col = j;

                for(int k = 1; k < getRows(); k++)
                {
                    int r = i + dir[0] * k, c = j + dir[1] * k;

                    if(Utils.inBounds(r, c, this))
                    {
                        if(getValue(r, c) == 0)
                        {
                            row = r;
                            col = c;

                            hasChanged = true;
                        }
                        else if(getValue(r, c) == value)
                        {
                            row = r;
                            col = c;

                            hasChanged = true;

                            break;
                        }
                        else
                        {
                            break;
                        }
                    }

                    // get maximum bound
                }

                setValue(i, j, 0);
                setValue(row, col, getValue(row, col) + value);
            }
        }

        if(hasChanged) {
            moveTiles(direction);
        } else spawnRandomTiles(4);
    }
}
