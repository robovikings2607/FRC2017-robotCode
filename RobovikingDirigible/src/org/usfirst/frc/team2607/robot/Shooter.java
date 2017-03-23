package org.usfirst.frc.team2607.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Talon;

public class Shooter {
	
	CANTalon shooter , shooterFollower;
	Talon loader;
	PIDLogger logger;
	double targetSpeed = 0.0;
	
	final double kF = 1023.0 / 28340.0;
	final double kP = 102.3 / 2040.0;
	
	public Shooter() {
		shooter = new CANTalon(Constants.shooterMotorA);
		shooterFollower = new CANTalon(Constants.shooterMotorB);
		loader = new Talon(Constants.loaderMotor);
		
		shooterFollower.changeControlMode(TalonControlMode.Follower);
		shooterFollower.set(Constants.shooterMotorA);
		
		shooter.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		shooter.reverseSensor(false);
		shooter.configNominalOutputVoltage(0.0, 0.0);
		shooter.configPeakOutputVoltage(12.0 , 0.0);
		shooter.setProfile(0);
		
		shooter.setF(this.kF);
		shooter.setP(this.kP * 1.75);
		shooter.setI(0.0);
		shooter.setD(this.kP * 30.0);
		
		logger = new PIDLogger(shooter , "shooterWheel");
		logger.start();
	}
	
	public void usePID(boolean use) {
		if(use) shooter.changeControlMode(TalonControlMode.Speed);
		else shooter.changeControlMode(TalonControlMode.PercentVbus);
		logger.enableLogging(use);
	}
	
	public void load(boolean switch_) {
		if(switch_) loader.set(0.75);
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
