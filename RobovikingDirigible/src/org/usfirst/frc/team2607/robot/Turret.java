package org.usfirst.frc.team2607.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

public class Turret {
	
	CANTalon turret;
	PIDLogger logger;
	double targetPosition = 0.0;
	
	public Turret() {
		turret = new CANTalon(Constants.turretMotor);
		
		turret.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		turret.reverseSensor(false);
		turret.configNominalOutputVoltage(0.0, 0.0);
		turret.configPeakOutputVoltage(6.0, -6.0);
		turret.setProfile(0);
		
		turret.setF(0.0);
		turret.setP(0.0);
		turret.setI(0.0);
		turret.setD(0.0);
		
		turret.setMotionMagicCruiseVelocity(0.0);
		turret.setMotionMagicAcceleration(0.0);
	}
	
	public void useMagic(boolean useMagic) {
		if(useMagic) turret.changeControlMode(TalonControlMode.MotionMagic);
		else turret.changeControlMode(TalonControlMode.PercentVbus);
	}
	
	public void set(double in) {
		turret.set(in);
	}
	
	public String getInfo() {
		return "TURRET: " + targetPosition
				+ " encPos: " + turret.getEncPosition()
				+ " rpmSpeed: " + turret.getSpeed()
				+ " encSpeed: " + turret.getEncVelocity()
				+ " voltageOut: " + turret.getOutputVoltage()
				+ " encError: " + turret.getClosedLoopError();
	}
}
