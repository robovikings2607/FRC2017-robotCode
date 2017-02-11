package org.usfirst.frc.team2607.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.SpeedController;

public class Transmission implements SpeedController{
	
	CANTalon motor1 , motor2;
	private String name;
 
	public Transmission(int channelA , int channelB , String name){
		motor1 = new CANTalon(channelA);
		motor2 = new CANTalon(channelB);
		this.name = name;
		
		motor2.changeControlMode(CANTalon.TalonControlMode.Follower);
		motor2.set(motor1.getDeviceID());
		
		motor1.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		motor1.configEncoderCodesPerRev(4096);
		motor1.reverseSensor(false);
		motor1.configNominalOutputVoltage(0.0, 0.0);
		motor1.configPeakOutputVoltage(0, -12.0);
		motor1.setProfile(0);	
		motor1.setF(0); // set to (1023 / nativeVelocity)
		motor1.setP(0);		// start with 10% of error (native units)
		motor1.setI(0);
		motor1.setD(0);
	}
	
	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public double get() {
		// TODO Auto-generated method stub
		return motor1.get();
	}
	
	public void enablePID() {
		motor1.changeControlMode(TalonControlMode.Speed);
	}
	
	public void disablePID() {
		motor1.changeControlMode(TalonControlMode.PercentVbus);
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