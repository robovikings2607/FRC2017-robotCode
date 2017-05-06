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

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	public Climber climber;
	public Shooter shooter;
	public GearHandler gearHandler;
	public Transmission leftTrans , rightTrans;
	RobovikingStick driveController , opController;
	RobotDrive robotDrive;
	AutonomousEngine autoEngine;
	public Solenoid shifter;
	Talon pickup;
	Thread Autothread = null;
	
	AHRS gyro;
		
	double targetSpeed = 0.0, rightVoltage = 0.0, leftVoltage = 0.0;
	double shooterSpeed = 1.0;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		//pdp = new PowerDistributionPanel();
		climber = new Climber(Constants.climberMotor);
		shooter = new Shooter();
		//turret = new Turret();
		gearHandler = new GearHandler();
		leftTrans = new Transmission(Constants.leftMotorA , Constants.leftMotorB , "Left Transmission");
		rightTrans = new Transmission(Constants.rightMotorA , Constants.rightMotorB , "Right Transmission");
		shifter = new Solenoid(Constants.pcmDeviceID , Constants.shifterSolenoid);
		robotDrive = new RobotDrive(leftTrans , rightTrans);
		robotDrive.setSafetyEnabled(false);
		
		gyro = new AHRS(Port.kMXP);
		
		driveController = new RobovikingStick(Constants.driverController);
		opController = new RobovikingStick(Constants.operatorController);
		autoEngine=new AutonomousEngine(this);
		autoEngine.loadSavedMode();
		
		CameraServer.getInstance().startAutomaticCapture(0);
		CameraServer.getInstance().startAutomaticCapture(1);
		
		SmartDashboard.putNumber("targetSpeed", targetSpeed);
		SmartDashboard.putNumber("rightVoltage", rightVoltage);
		SmartDashboard.putNumber("leftVoltage", leftVoltage);

		
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
		if(autoCount++ >= 50) {
			System.out.println(gyro.getYaw());
			autoCount = 0;
		}
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

	@Override
	public void disabledInit() {
		leftTrans.setBrakeMode(false);
		rightTrans.setBrakeMode(false);
		shooter.set(0.0);
		shooter.load(false);
		//pdpLogger.disable();
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
		leftTrans.setHighGear(true, false);
		rightTrans.setHighGear(true, false);
		shooter.usePID(false);
		leftTrans.setBrakeMode(true);
		rightTrans.setBrakeMode(true);
	}
	
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		if(shifter.get() == Constants.highGear) {
			SmartDashboard.putString("HighGear" , "+++++++++");
		}
		else { SmartDashboard.putString("HighGear" , ""); }
		
		//DRIVING
		robotDrive.arcadeDrive(driveController.getRawAxisWithDeadzone(RobovikingStick.xBoxLeftStickY) , 
				driveController.getRawAxisWithDeadzone(RobovikingStick.xBoxRightStickX));
		shifter.set(!driveController.getToggleButton(RobovikingStick.xBoxButtonLeftStick));
		leftTrans.setHighGear(!driveController.getToggleButton(RobovikingStick.xBoxButtonLeftStick), false);
		rightTrans.setHighGear(!driveController.getToggleButton(RobovikingStick.xBoxButtonLeftStick), false);
		
		//CLIMBER
		if(opController.getRawButton(RobovikingStick.xBoxButtonB)) {
			climber.stop();
		}
		else {
			if(Math.abs(opController.getRawAxis(RobovikingStick.xBoxLeftStickY)) > 0.2 ) {
				climber.run(opController.getRawAxis(RobovikingStick.xBoxLeftStickY));
			} else{
				climber.stop();
			}
		}
		
		//GEARS
		gearHandler.setDoors(driveController.getRawButton(RobovikingStick.xBoxButtonA));
		gearHandler.setRamp(!opController.getRawButton(RobovikingStick.xBoxButtonY));
		gearHandler.setPickup(opController.getRawButton(RobovikingStick.xBoxButtonA));
		
		if(opController.getRawButton(RobovikingStick.xBoxRightBumper)) gearHandler.setRollers(1.0);
		else if(opController.getRawButton(RobovikingStick.xBoxLeftBumper)) gearHandler.setRollers(-0.25);
		else gearHandler.setRollers(0.0);
		
		//SHOOTER
		
		switch(opController.getPOV(0)) {
		case 0:
			shooterSpeed = 1.0;
			break;
		case 90:
			shooterSpeed = 0.95;
			break;
		case 180:
			shooterSpeed = 0.9;
			break;
		case 270:
			shooterSpeed = 0.85;
			break;
		} 
		if(opController.getTriggerPressed(RobovikingStick.xBoxRightTrigger)) shooter.set(shooterSpeed);
		else shooter.set(0.0);
		shooter.load(opController.getRawButton(RobovikingStick.xBoxButtonX));
		shooter.useTargetLight(opController.getToggleButton(RobovikingStick.xBoxButtonRightStick));
	}
	
	public void setupAutonConfig() {
		gearHandler.setDoors(Constants.gearClosed);
		shifter.set(Constants.lowGear);
		leftTrans.setHighGear(false, true);
		rightTrans.setHighGear(false, true);
	}
	
	public double calcTurn(double degToTurn) {
		//long timeoutMilli = 3000;
		//long startTime = System.currentTimeMillis();
		
//		double kP = 0.0006; //0.053 //0.0657
		double kP = 0.053;
		double maxTurn = 0.7;
		double tolerance = 0.5;
				
		double error = degToTurn - gyro.getYaw();
		//System.out.println("calcTurn error: " + error);
			
		double calcTurn = kP * error;
			if (error <= 0){
				calcTurn = Math.max(-maxTurn, calcTurn - .25);
			} else {
				calcTurn = Math.min(maxTurn, calcTurn + .25);
			}
			
		if (gyro.getYaw() > (degToTurn - tolerance) && gyro.getYaw() < (degToTurn + tolerance)){
			return 0.0;
		} else {
			//System.out.println("CommandedVoltage: " + calcTurn + " error: " + error);
			return calcTurn;
		}
	}

	public void rotateDeg(double target) {
		long startTime = System.currentTimeMillis();
		long deltaTime;
		//FOR USE IN AUTONOMOUS MODES
		int idek = 0;
		boolean keepZeroing = true;
		gyro.reset();
		try { Thread.sleep(100); 
		while(keepZeroing) {
			deltaTime = System.currentTimeMillis() - startTime;
			double turningSpeed = calcTurn(target);
			robotDrive.arcadeDrive(0.0, turningSpeed);
			if(turningSpeed == 0.0) idek++;
			if(idek > 20) keepZeroing = false;
			else if(deltaTime >= 3200){ 
				System.out.println("TIMED OUT: rotation could not be completed"); 
				keepZeroing = false;
				robotDrive.arcadeDrive(0.0, 0.0);
			}
		 Thread.sleep(15);  
		}
		} catch (Exception e) { keepZeroing = false; robotDrive.arcadeDrive(0.0,0.0); }
	}
	public void rotateDegAlt(double target) {
		long startTime = System.currentTimeMillis();
		long deltaTime;
		//FOR USE IN AUTONOMOUS MODES
		int idek = 0;
		boolean keepZeroing = true;
		gyro.reset();
		try { Thread.sleep(100); 
		while(keepZeroing) {
			deltaTime = System.currentTimeMillis() - startTime;
			double turningSpeed = calcTurn(target);
			robotDrive.arcadeDrive(0.0, turningSpeed);
			if(turningSpeed == 0.0) idek++;
			if(idek > 20) keepZeroing = false;
			else if(deltaTime >= 3200){ 
				System.out.println("TIMED OUT: rotation could not be completed"); 
				keepZeroing = false;
				robotDrive.arcadeDrive(0.0, 0.0);
			}
		 Thread.sleep(15);  
		}
		} catch (Exception e) { keepZeroing = false; robotDrive.arcadeDrive(0.0,0.0); }
	}
}
		
		//SHOOTER
		/*
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
		*/
		
		/*if(opController.getRawButton(RobovikingStick.xBoxButtonA)) shooter.set(3800.0);
		else if(opController.getRawButton(RobovikingStick.xBoxButtonY)) shooter.set(4100.0);
		else if(opController.getRawButton(RobovikingStick.xBoxButtonX)) shooter.set(4000.0);
		else shooter.set(0.0);
		
		shooter.load(opController.getTriggerPressed(RobovikingStick.xBoxRightTrigger));
		
		//TURRET
		turret.set(opController.getRawAxisWithDeadzone(RobovikingStick.xBoxRightStickX));
		if(opController.getRawButton(RobovikingStick.xBoxButtonStart)) turret.resetEnc();
		//System.out.println(turret.getInfo());
		//System.out.println(shooter.getInfo());
		System.out.println(leftTrans.getRate() + " , " + rightTrans.getRate());
	}*/