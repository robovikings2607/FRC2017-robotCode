package org.usfirst.frc.team2607.robot;

import com.ctre.phoenix.MotorControl.SmartMotorController.TalonControlMode;
import com.ctre.phoenix.MotorControl.CAN.TalonSRX;
import com.ctre.phoenix.*;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.SpeedController;

public class Transmission implements SpeedController{
	
	TalonSRX motor1 , motor2;
	PIDLogger logger;
	private String name;
	boolean highGear = true, speedIsRPM = false; 

	public Transmission(int channelA , int channelB , String name){
		motor1 = new TalonSRX(channelA);
		motor2 = new TalonSRX(channelB);
		this.name = name;
		
		motor2.changeControlMode(TalonSRX.TalonControlMode.Follower);
		motor2.set(motor1.getDeviceID());
		motor2.enableBrakeMode(false);
		
		motor1.setFeedbackDevice(TalonSRX.FeedbackDevice.QuadEncoder);
		motor1.configEncoderCodesPerRev(1024);
		motor1.reverseSensor(false);
		motor1.configNominalOutputVoltage(0.0, 0.0);
		motor1.configPeakOutputVoltage(12.0, -12.0);
		motor1.setVoltageRampRate(0.0);
		
		motor1.enableBrakeMode(false);
		
		/*
		 * 1024 nativeClicks / 1 encoderRotations
		 * 20 encoderRotations / 9 wheelRotations
		 * 4" wheel Diameter
		 * (1024/1) * (20/9) = 2275.556 nativeClicks / 1 wheelRotations
		 * 2275.556 nativeClicks / 12.566 inches (or 1.047 feet)
		 * 2173 nativeClicks / 1 foot
		 */
	
		motor1.setProfile(0);
		setHighGearGains();
		
		// High Gear
		// up to 250 RPM: 
		//		right: FF = (1023.0 / 6102.0) * 1.24, Kp =30.0 / 200.0, Kd = Kp * 22.0
		//		left: FF = (1023.0 / 5766.0) * 1.36, Kp = 15.0 / 100.0, Kd = Kp * 22.0
		// 250 RPM to 450 RPM....also worked down to 250 RPM:
		// 		right: (1023.0 / 6102.0) * 1.14,  60.0 / 200.0, Kp * 22.0
		//      left: (1023.0 / 5766.0) * 1.20, 25.0 / 100.0, Kp * 22.0
		// 500 RPM, also worked down to 200 RPM
		//		right: 1023.0 / 6102.0) * 1.14  60.0 / 200.0, Kp * 22.0
		//		left: 1023.0 / 5766.0) * 1.18  23.0 / 100.0 Kp * 34.0
		// scratch the above, try what's below
				
		logger = new PIDLogger(motor1, name);
		logger.start();
	}

	public void setLowGearGains() {
		if (name.equalsIgnoreCase("Right Transmission")) {
			double Kp = 0.0;
			motor1.setF((1023.00 / 2900.00) * 1.08);
			motor1.setP(Kp);
			motor1.setI(0);
			motor1.setD(0.0);
		} else {
			double Kp = 0.0;
			motor1.setF((1023.00 / 2863.00) * 1.08);
			motor1.setP(0.0);
			motor1.setI(0);
			motor1.setD(0.0);
		}
	}
	
	public void setHighGearGains() {
		if(name.equalsIgnoreCase("Right Transmission")) {
			double Kp = 0.0; 
			motor1.setF((1023.0 / 6102.0) * 1.1);
			motor1.setP(Kp);
			motor1.setI(0);
			motor1.setD(0);
		} else {	
			double Kp = 0.0;  
			motor1.setF((1023.0 / 5766.0) * 1.1);
			motor1.setP(Kp);					
			motor1.setI(0);
			motor1.setD(0);
		}		
	}
	
	public void setMotionProfileLowGearGains() {
		if(name.equalsIgnoreCase("Right Transmission")) {
			double Kp = 50.0 / 80.0; //13.1 / 80.0; //10.1
			motor1.setF((1023.00 / 2900.00) * 1.08); // set to (1023 / nativeVelocity)
			motor1.setP(Kp);
			motor1.setI(0);
			motor1.setD(0.0 * 5.0);
		} else {	
			double Kp = 100.0 / 140.0; //55.8 / 140.0;  //44.8
			motor1.setF((1023.00 / 2863.00) * 1.08); // set to (1023 / nativeVelocity)
			motor1.setP(Kp);					// start with 10% of error (native units)
			motor1.setI(0);
			motor1.setD(0.0 * 8.5);
		}
	}

	public void setMotionProfileHighGearGains() {
		if(name.equalsIgnoreCase("Right Transmission")) {
			double Kp = 102.3 / 300.0; //60.0 / 200.0; 
			motor1.setF((1023.0 / 6102.0) * 1.1);
			motor1.setP(Kp);
			motor1.setI(0);
			motor1.setD(0);
		} else {	
			double Kp = 102.3 / 300.0; //23.0 / 100.0;  
			motor1.setF((1023.0 / 5766.0) * 1.1);
			motor1.setP(Kp);					// start with 10% of error (native units)
			motor1.setI(0);
			motor1.setD(0);
		}
	}
	
	public void setHighGear(boolean hg, boolean following) {
		if (highGear != hg) {
			highGear = hg;
			if (highGear) {
				if (following) {
					setMotionProfileHighGearGains();
				} else {
					setHighGearGains();	
				}
			} else { 
				if (following) {
					setMotionProfileLowGearGains();
				} else {
					setLowGearGains();
				}
			}
		}
	}
	
	@Override
	public void pidWrite(double output) {
		
	}
	
	@Override
	public double get() {
		return motor1.get();
	}
	
	public void enablePID(boolean enableLogging, boolean expectRPM) {
		motor1.changeControlMode(TalonControlMode.Speed);
		speedIsRPM = expectRPM;
		logger.enableLogging(enableLogging);
	}
	
	public void enableVoltage(boolean enableLogging) {
		motor1.changeControlMode(TalonControlMode.Voltage);
		logger.enableLogging(enableLogging);
	}
	
	public void disablePID() {
		motor1.changeControlMode(TalonControlMode.PercentVbus);
		logger.enableLogging(false);
	}
	
	public void enableMotionProfileMode(boolean enableLogging) {
		motor1.changeControlMode(TalonControlMode.MotionProfile);
		logger.enableLogging(enableLogging);
	}
	
	public double getDistance() {
		return motor1.getEncPosition();
	}
	
	public double getRate() {
		return motor1.getEncVelocity();
	}
	
	public void setBrakeMode(boolean brotherbear) {
		motor1.enableBrakeMode(brotherbear);
		motor2.enableBrakeMode(brotherbear);
	}
	double prevAbsSpeed = 0.0;
	int tickCount = 0;
	
	@Override
	public void set(double speed) {
		// TODO Auto-generated method stub

		if (!speedIsRPM) { 
			if (highGear) speed = speed * 820.0;
			else speed = speed * 410.0;			
		}
		motor1.set(speed);
		if (++tickCount >= 50) {
			System.out.println(name + ": " + speed + ((speedIsRPM) ? "RPM" : "%"));
			tickCount = 0;
		}
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

	public TalonSRX getMasterSRX() {
		return motor1;
	}
}