package frc.robot;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpiutil.math.MathUtil;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;

public class Wheel {
    CANSparkMax driveController;
    CANSparkMax steerController;
    CANEncoder encoder;
    DutyCycleEncoder absoluteEncoder;
    public double offSet0;

    // This is for wheel flipping
    boolean isFlipped = false;
    double flipOffset = 0;
    double previousAngle = 0;
    
    PIDController pid = new PIDController(2, 0, 0);

    private double getFlippedAngle() {
        if (isFlipped) {
            return .5;  // approximately the value of one rotation
        } else {
            return 0;
        }
    }

    private void turnToAngle(double desiredAngle) {
        desiredAngle += flipOffset + getFlippedAngle() - offSet0;
        
        // If the wheel needs to turn more than 90 degrees to reach the target, flip the direction of the wheel
        double encoderSetpointDiff = Math.abs(absoluteEncoder.get() - desiredAngle);
        if (encoderSetpointDiff > .25 && encoderSetpointDiff < .75) {
            desiredAngle -= getFlippedAngle();
            isFlipped = !isFlipped;
            desiredAngle += getFlippedAngle();
        }

        if (previousAngle - desiredAngle > .5) { //.5 previously 185
			flipOffset += 1; //1 previously 360
			desiredAngle += 1; //1 previously 360
        }
        if (previousAngle - desiredAngle < -.5) { //-.5 previously -185
            flipOffset -= 1; //1 previously 360
            desiredAngle -= 1; //1 previously 360
        }
        if (absoluteEncoder.get() - desiredAngle > 1) { //1 previously 380
            flipOffset += 1; //1 previously 360
            desiredAngle += 1; //1 previously 360
        }
        if (absoluteEncoder.get() - desiredAngle < -1) { //-1 previously -380
            flipOffset -= 1; //1 previously 360
            desiredAngle -= 1; //1 previously 360
        }
        previousAngle = desiredAngle;

        double desiredSpeed = pid.calculate(absoluteEncoder.get(), desiredAngle);
        SmartDashboard.putNumber("WheelSpeed", desiredSpeed);
        steerController.set(MathUtil.clamp(desiredSpeed, -0.1, 0.1));
    }

    private void setSpeed(double motorSpeed) {
        if (isFlipped) motorSpeed *= -1;
        driveController.set(motorSpeed * 1);
    }

    public void set(double setAngle, double speed) {
        turnToAngle(setAngle);
        setSpeed(speed);
    }

    public Wheel(int driveControllerID, int steerControllerID, int absolutePort, double offSet1) {
        driveController = new CANSparkMax(driveControllerID, MotorType.kBrushless);
        steerController = new CANSparkMax(steerControllerID, MotorType.kBrushless);
        encoder = new CANEncoder(steerController);
        absoluteEncoder = new DutyCycleEncoder(absolutePort);
        offSet0 = offSet1;
    }
}
