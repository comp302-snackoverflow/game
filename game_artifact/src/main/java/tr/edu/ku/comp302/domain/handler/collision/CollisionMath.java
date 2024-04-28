package tr.edu.ku.comp302.domain.handler.collision;

public class CollisionMath {

    public static double triangleArea(double Ax, double Ay,
                                      double Bx, double By,
                                      double Cx, double Cy) {
        return (0.5) * Math.abs(Ax*(By-Cy) + Bx*(Cy-Ay) + Cx*(Ay-By));
    }

    public static double lineLength(double Ax, double Ay, double Bx, double By) {
        return Math.sqrt(Math.pow(Bx - Ax, 2) + Math.pow(By - Ay, 2));
    }

}
