package grid;

import Utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Grid {

    private int[][] grid;

    private int randomTileSpawnRate = 1;

    private Random random;

    public Grid(int rows, int cols)
    {
        grid = new int[rows][cols];

        movementGrid = new int[rows][cols];

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

    public Color getColor(int value)
    {
        int colorIndex = (int) (Math.log(value) / Math.log(2));

        if (colorIndex < 0) colorIndex = 0;

        if (colorIndex >= colors.length) colorIndex = colors.length - 1;

        return colors[colorIndex];
    }

    ArrayList<Integer[]> spawnedList;

    public ArrayList<Integer[]> getSpawnedList()
    {
        return spawnedList;
    }

    public void spawnRandomTiles(int amount)
    {
        spawnedList = new ArrayList<Integer[]>();

        for(int i = 0; i < amount; i++)
        {
            spawnedList.add(spawnRandomTile());
        }
    }

    private Integer[] spawnRandomTile()
    {
        int row = 0, col = 0;

        int i = 100;
        while(i > 0)
        {
            row = random.nextInt(0, getRows());
            col = random.nextInt(0, getColumns());

            if(grid[row][col] == 0)
            {
                grid[row][col] = random.nextInt(1,3) * 2; break;
            }

            i--;
        }

        return new Integer[]{row,col};
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
        oldGrid = new int[getRows()][getColumns()];

        movementGrid = new int[getRows()][getColumns()];

        for(int i = 0; i < getRows(); i++)
        {
            for(int j = 0; j < getColumns(); j++)
            {
                oldGrid[i][j] = grid[i][j];
            }
        }

        lastDirection = direction;

        int[] dir = switch(direction) {
            case LEFT -> new int[]{0, -1};
            case UP -> new int[]{-1, 0};
            case RIGHT -> new int[]{0, 1};
            case DOWN -> new int[]{1, 0};
        };

        boolean hasChanged = false;

        int row, col;

        for(int i = 0; i < getRows(); i++)
        {
            for(int j = 0; j < getColumns(); j++)
            {
                int value = getValue(i,j);

                if(value == 0) continue;

                row = i; col = j;

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

                movementGrid[i][j] += switch(direction)
                {
                    case LEFT, RIGHT -> col - j;
                    case UP, DOWN -> row - i;
                };

                /*System.out.println("This is the table: ");
                for(int z = 0; z < movementGrid.length; z++)
                {
                    System.out.print("{");
                    for(int h = 0; h < movementGrid[0].length; h++)
                    {
                        System.out.print(movementGrid[z][h] + ", ");
                    }
                    System.out.print("}");
                    System.out.println(",");
                }*/

                setValue(i, j, 0);
                if(getValue(row, col) > 0) spawnedList.add(new Integer[]{row,col});
                setValue(row, col, getValue(row, col) + value);
            }
        }

        if(hasChanged) {
            moveTiles(direction, false);
        } else spawnRandomTiles(randomTileSpawnRate);
    }

    public void moveTiles(Directions direction, boolean first)
    {
        if(first) {
            oldGrid = new int[getRows()][getColumns()];

            for (int i = 0; i < getRows(); i++) {
                for (int j = 0; j < getColumns(); j++) {
                    oldGrid[i][j] = grid[i][j];
                }
            }

            lastDirection = direction;
        }

        int[] dir = switch(direction) {
            case LEFT -> new int[]{0, -1};
            case UP -> new int[]{-1, 0};
            case RIGHT -> new int[]{0, 1};
            case DOWN -> new int[]{1, 0};
        };

        boolean hasChanged = false;

        int row, col;

        for(int i = 0; i < getRows(); i++)
        {
            for(int j = 0; j < getColumns(); j++)
            {
                int value = getValue(i,j);

                if(value == 0) continue;

                row = i; col = j;

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

                movementGrid[i][j] += switch(direction)
                {
                    case LEFT, RIGHT -> col - j;
                    case UP, DOWN -> row - i;
                };

                if(movementGrid[i][j] != 0) System.out.println("KALA: " + movementGrid[i][j]);

                System.out.println("This is the table: ");
                for(int z = 0; z < movementGrid.length; z++)
                {
                    System.out.print("{");
                    for(int h = 0; h < movementGrid[0].length; h++)
                    {
                        System.out.print(movementGrid[z][h] + ", ");
                    }
                    System.out.print("}");
                    System.out.println(",");
                }


                setValue(i, j, 0);
                if(getValue(row, col) > 0) spawnedList.add(new Integer[]{row,col});
                setValue(row, col, getValue(row, col) + value);
            }
        }

        if(hasChanged) {
            moveTiles(direction, false);
        } else spawnRandomTiles(randomTileSpawnRate);
    }

    private int[][] movementGrid;
    private int[][] oldGrid;
    private Directions lastDirection;

    public Directions getLastDirection()
    {
        return lastDirection;
    }

    public int[][] getMovementGrid()
    {
        return movementGrid;
    }

    public int[][] getOldGrid()
    {
        return oldGrid;
    }
}
