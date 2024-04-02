package Utils;

import grid.Grid;

public class Utils {

    public static boolean inBounds(int i, int j, Grid grid)
    {
        return (i < grid.getRows() && i >= 0 &&
                j < grid.getColumns() && j >= 0);
    }

    public static int lerp(int a, int b, double t)
    {
        return (int)(a + (b - a) * t);
    }
}