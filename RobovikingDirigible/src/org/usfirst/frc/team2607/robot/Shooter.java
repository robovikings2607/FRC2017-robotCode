package org.usfirst.frc.team2607.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Talon;

public class Shooter {
	/*NOTES:
	 * -120 in from boiler wall to turret center : accurate @ 4000rpm to 4100rpm
	 * -85.5 + 26 in from boiler wall to turret center: accurate @ ~3800rpm
	 */
	
	CANTalon shooter , shooterFollower;
	Talon loader;
	Relay light;
	PIDLogger logger;
	double targetSpeed = 0.0;
	
	public Shooter() {
		shooter = new CANTalon(Constants.shooterMotorA);
		shooterFollower = new CANTalon(Constants.shooterMotorB);
		loader = new Talon(Constants.loaderMotor);
		light = new Relay(Constants.lightRelay);
		
		shooterFollower.changeControlMode(TalonControlMode.Follower);
		shooterFollower.set(Constants.shooterMotorA);
		
		shooter.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		shooter.reverseSensor(false);
		shooter.configNominalOutputVoltage(0.0, 0.0);
		shooter.configPeakOutputVoltage(12.0 , 0.0);
		shooter.setProfile(0);
		
		shooter.setF(.0355);
		shooter.setP(.0425);
		shooter.setI(0.0);
		shooter.setD(0.0);
		
		shooter.enableBrakeMode(false);
		shooterFollower.enableBrakeMode(false);
		
		logger = new PIDLogger(shooter , "shooterWheel");
		logger.start();
	}
	
	public void usePID(boolean use) {
		if(use) shooter.changeControlMode(TalonControlMode.Speed);
		else shooter.changeControlMode(TalonControlMode.PercentVbus);
		logger.enableLogging(use);
	}
	
	public void useTargetLight(boolean iCantBelieveItsNotButter) {
		if(iCantBelieveItsNotButter) light.set(Relay.Value.kForward);
		else light.set(Relay.Value.kOff);
	}
	
	public void load(boolean switch_) {
		if(switch_) loader.set(0.42);
		else loader.set(0.0);
	}
	
	public void set(double speed) {
		shooter.set(speed);
		targetSpeed = speed;
		logger.updSetpoint(speed);
	}
	
	public String getInfo() {
		return "SHOOTER: target: " + targetSpeed
				+ " rpmSpeed: " + shooter.getSpeed()
				+ " encSpeed: " + shooter.getEncVelocity()
				+ " voltageOut: " + shooter.getOutputVoltage()
				+ " encError: " + shooter.getClosedLoopError();
	}
}
