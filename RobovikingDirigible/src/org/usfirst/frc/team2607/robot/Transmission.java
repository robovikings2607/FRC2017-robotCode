package org.usfirst.frc.team2607.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.SpeedController;

public class Transmission implements SpeedController{
	
	CANTalon motor1 , motor2;
	PIDLogger logger;
	private String name;
	boolean pidEnabled;
 
	public Transmission(int channelA , int channelB , String name){
		motor1 = new CANTalon(channelA);
		motor2 = new CANTalon(channelB);
		this.name = name;
		
		motor2.changeControlMode(CANTalon.TalonControlMode.Follower);
		motor2.set(motor1.getDeviceID());
		motor2.enableBrakeMode(true);
		
		motor1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		motor1.configEncoderCodesPerRev(1024);
		motor1.reverseSensor(false);
		motor1.configNominalOutputVoltage(0.0, 0.0);
		motor1.configPeakOutputVoltage(12.0, -12.0);
		
		motor1.enableBrakeMode(true);
		
		/*
		 * 1024 nativeClicks / 1 encoderRotations
		 * 20 encoderRotations / 9 wheelRotations
		 * 4" wheel Diameter
		 * (1024/1) * (20/9) = 2275.556 nativeClicks / 1 wheelRotations
		 * 2275.556 nativeClicks / 12.566 inches (or 1.047 feet)
		 * 2173 nativeClicks / 1 foot
		 */
		// profile 0 is low gear PID gains
		motor1.setProfile(0);
		if(name.equalsIgnoreCase("Right Transmission")) {
			double Kp = 13.1 / 80.0; //10.1
			motor1.setF((1023.00 / 2900.00) * 1.08); // set to (1023 / nativeVelocity)
			motor1.setP(Kp);
			motor1.setI(0);
			motor1.setD(Kp * 5.0);
		} else {	
			double Kp = 55.8 / 140.0;  //44.8
			motor1.setF((1023.00 / 2863.00) * 1.16); // set to (1023 / nativeVelocity)
			motor1.setP(Kp);					// start with 10% of error (native units)
			motor1.setI(0);
			motor1.setD(Kp * 8.5);
		}
		
		// profile 1 is high gear PID gains
		motor1.setProfile(1);
		if(name.equalsIgnoreCase("Right Transmission")) {
			double Kp = 15.0 / 200.0; 
			motor1.setF((1023.0 / 6102.0) * 1.04); // set to (1023 / nativeVelocity) // 6102
			motor1.setP(Kp);
			motor1.setI(0);
			motor1.setD(Kp * 22.0);
		} else {	
			double Kp = 15.0 / 100.0;  
			motor1.setF((1023.0 / 5766.0) * 1.06); // set to (1023 / nativeVelocity) // 5766
			motor1.setP(Kp);					// start with 10% of error (native units)
			motor1.setI(0);
			motor1.setD(Kp * 22.0);
		}
		
		
		
		logger = new PIDLogger(motor1, name);
		logger.start();
		
		pidEnabled = false;
	}
	
	@Override
	public void pidWrite(double output) {
		
	}
	
	@Override
	public double get() {
		return motor1.get();
	}
	
	public void enablePID() {
		motor1.setProfile(1);
		motor1.changeControlMode(TalonControlMode.Speed);
		logger.enableLogging(true);
	}
	
	public void enableVoltage() {
		motor1.changeControlMode(TalonControlMode.Voltage);
		logger.enableLogging(true);
	}
	
	public void disablePID() {
		motor1.changeControlMode(TalonControlMode.PercentVbus);
		logger.enableLogging(false);
	}
	
	public void resetEncoder() {
		motor1.reset();
	}
	public double getDistance() {
		return motor1.getEncPosition();
	}
	
	public double getRate() {
		return motor1.getEncVelocity();
	}
	
	@Override
	public void set(double speed) {
		// TODO Auto-generated method stub
		motor1.set (speed);
		logger.updSetpoint(speed);
	}
	
	@Override
	public void setInverted(boolean isInverted) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean getInverted() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void stopMotor() {
		// TODO Auto-generated method stub
		
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}