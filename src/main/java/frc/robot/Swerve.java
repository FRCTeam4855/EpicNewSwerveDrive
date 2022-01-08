package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Swerve {

    public static SwerveOutput convertControllerToSwerve(double x1, double y1, double x2, double theta) {
        if (Math.abs(x1) < .17) x1 = 0;
        if (Math.abs(y1) < .17) y1 = 0;
        if (Math.abs(x2) < .17) x2 = 0;

        SwerveOutput output = new SwerveOutput();
        double L = 29.5;
        double W = 29.5;
        double R = Math.sqrt(Math.pow(L,2) + Math.pow(W,2));

        x1 = (-y1 * Math.sin(theta)) + (x1 * Math.cos(theta));
        double temp = (y1 * Math.cos(theta)) + (x1 * Math.sin(theta)); 
        y1 = temp;

        SmartDashboard.putNumber("x1", x1);
        SmartDashboard.putNumber("y1", y1);
        SmartDashboard.putNumber("x2", x2);

        x1 = -x1;
        y1 = -y1;
        x2 = -x2;

        double A = x1 - x2 * (L/R);
        double B = x1 + x2 * (L/R);
        double C = y1 - x2 * (W/R);
        double D = y1 + x2 * (W/R);
        double pi = 3.14159265358979323846264338;

        double ws1 = Math.sqrt(Math.pow(B,2) + Math.pow(C,2)); double wa1 = Math.atan2(B,C) * 180/pi;
        double ws2 = Math.sqrt(Math.pow(B,2) + Math.pow(D,2)); double wa2 = Math.atan2(B,D) * 180/pi;
        double ws3 = Math.sqrt(Math.pow(A,2) + Math.pow(D,2)); double wa3 = Math.atan2(A,D) * 180/pi;
        double ws4 = Math.sqrt(Math.pow(A,2) + Math.pow(C,2)); double wa4 = Math.atan2(A,C) * 180/pi; 

        output.wheelSpeeds[0] = ws1;
        output.wheelSpeeds[1] = ws2;
        output.wheelSpeeds[2] = ws3;
        output.wheelSpeeds[3] = ws4;
        output.wheelAngles[0] = wa1 / 360;
        output.wheelAngles[1] = wa2 / 360;
        output.wheelAngles[2] = wa3 / 360;
        output.wheelAngles[3] = wa4 / 360;


        return output;
    }

}