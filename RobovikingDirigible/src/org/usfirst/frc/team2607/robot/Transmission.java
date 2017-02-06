package org.usfirst.frc.team2607.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class Transmission implements SpeedController{
	
	CANTalon motor1 , motor2;


 public Encoder enc;
 
public Transmission( int[]deviceID, int sourceA, int sourceB){

motor1 = new CANTalon(deviceID[1]);
motor2 = new CANTalon(deviceID[2]);
enc = new Encoder(sourceA, sourceB);
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

@Override
public void set(double speed) {
	// TODO Auto-generated method stub
		 motor1.set (speed);
	motor2.set(speed);
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
}