package org.usfirst.frc.team2607.robot;
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
	
	Climber itsTheCliiiiiiiiiiiiiiiiiiiiiiimb;
	GearHandler gearHandler;
	public Transmission leftTrans , rightTrans;
	RobovikingStick driveController , opController;
	RobotDrive robotDrive;
	AutonomousEngine autoEngine;
	Solenoid shifter;
	Talon pickup;
	Thread Autothread = null;
	
	double targetSpeed = 0.0;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		itsTheCliiiiiiiiiiiiiiiiiiiiiiimb = new Climber(Constants.climberMotor);
		gearHandler = new GearHandler(Constants.gearSolenoid);
		leftTrans = new Transmission(Constants.leftMotorA , Constants.leftMotorB , "Left Transmission");
		rightTrans = new Transmission(Constants.rightMotorA , Constants.rightMotorB , "Right Transmission");
		shifter = new Solenoid(Constants.pcmDeviceID , Constants.shifterSolenoid);
		pickup = new Talon(Constants.pickupMotor);
		robotDrive = new RobotDrive(leftTrans , rightTrans);
		
		driveController = new RobovikingStick(Constants.driverController);
		opController = new RobovikingStick(Constants.operatorController);
		autoEngine=new AutonomousEngine(this);
		autoEngine.loadSavedMode();
		
		SmartDashboard.putNumber("targetSpeed", targetSpeed);
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
	@Override
	public void autonomousInit() {
	Autothread=new Thread(autoEngine);	
	Autothread.start();
	autonModeRan=true;
	
	leftTrans.enablePID();
	rightTrans.enablePID();
	double speed = SmartDashboard.getNumber("targetSpeed", 0.0);
	leftTrans.set(-speed);
	rightTrans.set(speed);
	
	}
	boolean autonModeRan=false;
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

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
		//TELEOP STUFF------------
		robotDrive.arcadeDrive(driveController.getRawAxisWithDeadzone(RobovikingStick.xBoxLeftStickY) , 
				driveController.getRawAxisWithDeadzone(RobovikingStick.xBoxRightStickX));
		
		if(opController.getPOV(0) == 0 )   {
			itsTheCliiiiiiiiiiiiiiiiiiiiiiimb.runForward();
		} else if(opController.getPOV(0) == 180) {
			itsTheCliiiiiiiiiiiiiiiiiiiiiiimb.runBackwards();
		} else {
			itsTheCliiiiiiiiiiiiiiiiiiiiiiimb.stop();
		}
		
		itsTheCliiiiiiiiiiiiiiiiiiiiiiimb.lockInPlace(opController.getToggleButton(RobovikingStick.xBoxButtonY));
		
		if(opController.getRawButton(RobovikingStick.xBoxRightBumper)) {
			pickup.set(0.5);
		} else if(opController.getRawButton(RobovikingStick.xBoxLeftBumper)) {
			pickup.set(-0.5);
		} else {
			pickup.set(0.0);
		}
		
		shifter.set(driveController.getToggleButton(RobovikingStick.xBoxButtonLeftStick));
		gearHandler.set(opController.getRawButton(RobovikingStick.xBoxButtonA));
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void autonomousPeriodic() {
			
	}
}

