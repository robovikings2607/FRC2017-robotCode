package org.usfirst.frc.team2607.robot;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	Climber itsTheCliiiiiiiiiiiiiiiiiiiiiiimb;
	Transmission leftTrans , rightTrans;
	RobovikingStick driveController , opController;
	RobotDrive robotDrive;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		itsTheCliiiiiiiiiiiiiiiiiiiiiiimb = new Climber(Constants.climberMotor);
		leftTrans = new Transmission(Constants.leftMotorA , Constants.leftMotorB , "Left Transmission");
		rightTrans = new Transmission(Constants.rightMotorA , Constants.rightMotorB , "Right Transmission");
		
		robotDrive = new RobotDrive(leftTrans , rightTrans);
		
		driveController = new RobovikingStick(Constants.driverController);
		opController = new RobovikingStick(Constants.operatorController);
		
		
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
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
		//TELEOP STUFF------------
		robotDrive.arcadeDrive(driveController.getRawAxisWithDeadzone(RobovikingStick.xBoxLeftStickY) , 
				driveController.getRawAxisWithDeadzone(RobovikingStick.xBoxRightStickX));
		
		if(opController.getRawButton(RobovikingStick.xBoxLeftBumper)) {
			itsTheCliiiiiiiiiiiiiiiiiiiiiiimb.runForward();
		} else if(opController.getRawButton(RobovikingStick.xBoxRightBumper)) {
			itsTheCliiiiiiiiiiiiiiiiiiiiiiimb.runBackwards();
		} else {
			itsTheCliiiiiiiiiiiiiiiiiiiiiiimb.stop();
		}
		
		
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		
	}
}

