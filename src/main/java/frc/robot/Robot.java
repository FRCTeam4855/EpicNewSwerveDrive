/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpiutil.math.MathUtil;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType; 

import edu.wpi.first.wpilibj.Joystick;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the 
 * project. E
 */ 
public class Robot extends TimedRobot { 
  private static final String kDefaultAuto = "Default"; 
  private static final String kCustomAuto = "My Auto"; 
  private String m_autoSelected; 
  private final SendableChooser<String> m_chooser = new SendableChooser<>(); 

  //CANEncoder encoder;

  Joystick joystick = new Joystick(0); 

  Wheel wheelFL = new Wheel(1, 2, 0, -0.758);
  Wheel wheelBL = new Wheel(3, 4, 1, -0.454);
  Wheel wheelBR = new Wheel(5, 6, 2, -0.143);
  Wheel wheelFR = new Wheel(7, 8, 3, -0.077);

  AHRS gyro = new AHRS(SPI.Port.kMXP);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Encoder FL", wheelFL.absoluteEncoder.get());
    SmartDashboard.putNumber("Encoder BL", wheelBL.absoluteEncoder.get());
    SmartDashboard.putNumber("Encoder BR", wheelBR.absoluteEncoder.get());
    SmartDashboard.putNumber("Encoder FR", wheelFR.absoluteEncoder.get());
    SmartDashboard.putNumber("Gyro", gyro.getYaw());
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    double x1 = joystick.getRawAxis(0);
    double x2 = joystick.getRawAxis(4);
    double y1 = joystick.getRawAxis(1);

    double theta = 0 ;// gyro.getYaw(); // * Math.PI / 180;
    SwerveOutput swerve = Swerve.convertControllerToSwerve(x1, y1, x2, theta);
    
    //SmartDashboard.putNumber("SetPoint BL", swerve.wheelAngles[3] - wheelBL.offSet0);
    //SmartDashboard.putNumber("Math", Math.sin(45));
    
    wheelFL.set(swerve.wheelAngles[0], swerve.wheelSpeeds[0]);
    wheelFR.set(swerve.wheelAngles[1], swerve.wheelSpeeds[1]);
    wheelBR.set(swerve.wheelAngles[2], swerve.wheelSpeeds[2]);
    wheelBL.set(swerve.wheelAngles[3], swerve.wheelSpeeds[3]);

    SmartDashboard.putNumber("AngleFL", swerve.wheelAngles[0]);
    SmartDashboard.putNumber("AngleFR", swerve.wheelAngles[1]);
    SmartDashboard.putNumber("AngleBR", swerve.wheelAngles[2]);
    SmartDashboard.putNumber("AngleBL", swerve.wheelAngles[3]);

    if (joystick.getRawButtonPressed(4)) {
      gyro.reset();
    }

  /*  if (joystick.getRawButtonPressed(6)) {
      gyro.reset();
    } */

  
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
