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

    public static int lerpEaseIn(int a, int b, double t)
    {
        double u = t;
        double uuu = u * u * u;

        double tt = t * t;
        double ttt = tt * t;

        // (1 - t)^3 * p0 + 3 * (1 - t) * t^2 * p2
        return lerp(a, b, uuu * 0.32 + 3 * u * tt * 0.32 + ttt * 0.3);
    }
}