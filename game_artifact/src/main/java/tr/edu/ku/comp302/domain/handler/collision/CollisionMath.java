package tr.edu.ku.comp302.domain.handler.collision;

public class CollisionMath {

    public static double crossProduct(double Ax, double Ay, double Bx, double By) {
        return Ax * By - Ay * Bx;
    }
    public static double triangleArea(double Ax, double Ay,
                                      double Bx, double By,
                                      double Cx, double Cy) {
        double ABx = Bx - Ax;
        double ABy = By - Ay;
        double ACx = Cx - Ax;
        double ACy = Cy - Ay;

        return Math.abs(crossProduct(ABx, ABy, ACx, ACy)) / 2;
    }

    public static double lineLength(double Ax, double Ay, double Bx, double By) {
        return Math.sqrt(Math.pow(Bx - Ax, 2) + Math.pow(By - Ay, 2));
    }

    public static double getTangentAngle(double center1X, double center1Y, double r1,
                                         double center2X, double center2Y, double r2) {
        // this method also works if the two circles are overlapping since the formula does not depend on r
        // returns the angle that the tangent line makes with the x-axis counter-clockwise
        double dx = center2X - center1X;
        double dy = center2Y - center1Y;
        return Math.atan2(dx, dy); // TODO check if this is correct

    }
}
