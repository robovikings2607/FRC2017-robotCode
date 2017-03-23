package org.usfirst.frc.team2607.robot;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
/*
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
*/
import org.usfirst.frc.team2607.robot.auto.AutonomousEngine;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	Climber climber;
	Shooter shooter;
	Turret turret;
	public GearHandler gearHandler;
	public Transmission leftTrans , rightTrans;
	RobovikingStick driveController , opController;
	RobotDrive robotDrive;
	AutonomousEngine autoEngine;
	public Solenoid shifter;
	Talon pickup;
	Thread Autothread = null;
	
	double targetSpeed = 0.0, rightVoltage = 0.0, leftVoltage = 0.0;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		climber = new Climber(Constants.climberMotor);
		shooter = new Shooter();
		turret = new Turret();
		gearHandler = new GearHandler();
		leftTrans = new Transmission(Constants.leftMotorA , Constants.leftMotorB , "Left Transmission");
		rightTrans = new Transmission(Constants.rightMotorA , Constants.rightMotorB , "Right Transmission");
		shifter = new Solenoid(Constants.pcmDeviceID , Constants.shifterSolenoid);
		pickup = new Talon(Constants.pickupMotor);
		robotDrive = new RobotDrive(leftTrans , rightTrans);
		robotDrive.setSafetyEnabled(false);
		
		driveController = new RobovikingStick(Constants.driverController);
		opController = new RobovikingStick(Constants.operatorController);
		autoEngine=new AutonomousEngine(this);
		autoEngine.loadSavedMode();
		
		SmartDashboard.putNumber("targetSpeed", targetSpeed);
		SmartDashboard.putNumber("rightVoltage", rightVoltage);
		SmartDashboard.putNumber("leftVoltage", leftVoltage);

		/*
		// for tuning....webserver to view PID logs
    	Server server = new Server(5801);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(5801);
        server.addConnector(connector);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "/home/lvuser/index.html" });

        resource_handler.setResourceBase(".");
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
        server.setHandler(handlers);
        try {
			server.start();
			server.join();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	int autoCount = 0;
	@Override
	public void autonomousInit() {
	Autothread=new Thread(autoEngine);	
	Autothread.start();
	autonModeRan=true;
	
/*	
	leftTrans.enablePID();
	rightTrans.enablePID();
	double speed = SmartDashboard.getNumber("targetSpeed", 0.0);
	leftTrans.set(-speed);
	rightTrans.set(speed);
*/	
//	leftTrans.enablePID(true, true);
//	rightTrans.enablePID(true, true);
	//shifter.set(true);
	autoCount = 0;
	}
	
	boolean autonModeRan=false;

	@Override
	public void autonomousPeriodic() {
/*
			if (++autoCount < 10) return;
			rightVoltage = SmartDashboard.getNumber("rightVoltage",0.0);
			leftVoltage = SmartDashboard.getNumber("leftVoltage",0.0);
			double speed = SmartDashboard.getNumber("targetSpeed",0.0);
			leftTrans.set(-speed);
			rightTrans.set(speed);
			SmartDashboard.putNumber("leftSpeed", leftTrans.getRate());
			SmartDashboard.putNumber("rightSpeed", rightTrans.getRate());
*/	
	}


	
	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void disabledPeriodic() {
		leftTrans.disablePID();
		rightTrans.disablePID();
		if (autonModeRan) {
			autonModeRan=false;
					if (Autothread.isAlive()) {
						System.out.println("autoThread alive, interrupting");
						Autothread.interrupt();
					}else{
						System.out.println("autoThread not alive");
					}
		}
		if (driveController.getButtonPressedOneShot(RobovikingStick.xBoxButtonStart)) {
			autoEngine.selectMode();
		}
	}

	
	@Override
	public void teleopInit() {
		leftTrans.enablePID(true, false);
		rightTrans.enablePID(true, false);
		shifter.set(Constants.highGear);
		shooter.usePID(true);
		turret.useMagic(false);
	}
	
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
		//DRIVING
		robotDrive.arcadeDrive(driveController.getRawAxisWithDeadzone(RobovikingStick.xBoxLeftStickY) , 
				driveController.getRawAxisWithDeadzone(RobovikingStick.xBoxRightStickX));
		shifter.set(driveController.getToggleButton(RobovikingStick.xBoxButtonLeftStick));
		leftTrans.setHighGear(!driveController.getToggleButton(RobovikingStick.xBoxButtonLeftStick), false);
		rightTrans.setHighGear(!driveController.getToggleButton(RobovikingStick.xBoxButtonLeftStick), false);
		
		//CLIMBER
		if(opController.getRawButton(RobovikingStick.xBoxButtonB)) {
			climber.stop();
		}
		else {
			if(Math.abs(opController.getRawAxis(RobovikingStick.xBoxLeftStickY)) > 0.1 ) {
				climber.run(opController.getRawAxis(RobovikingStick.xBoxLeftStickY));
			} else{
				climber.stop();
			}
		}
		
		//BALL PICKUP
		if(opController.getRawButton(RobovikingStick.xBoxRightBumper)) {
			pickup.set(1.0);
		} else if(opController.getRawButton(RobovikingStick.xBoxLeftBumper)) {
			pickup.set(-1.0);
		} else {
			pickup.set(0.0);
		}
		
		//GEARS
		gearHandler.openDoors(driveController.getRawButton(RobovikingStick.xBoxButtonA));
		gearHandler.openRamp(driveController.getRawButton(RobovikingStick.xBoxButtonB));
		
		//SHOOTER
		switch(opController.getPOV(0)) {
		case 0:
			shooter.set(3800.0);
			break;
		case 90:
			shooter.set(3600.0);
			break;
		case 180:
			shooter.set(3400.0);
			break;
		case 270:
			shooter.set(4000.0);
			break;
		default:
			shooter.set(0.0);
			break;
		}
		
		shooter.load(opController.getTriggerPressed(RobovikingStick.xBoxRightTrigger));
		
		//TURRET
		turret.set(opController.getRawAxisWithDeadzone(RobovikingStick.xBoxRightStickX));
	}

	/**
	 * This function is called periodically during test mode
	 */
}

