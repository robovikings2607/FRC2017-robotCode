package org.usfirst.frc.team2607.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class Climber {
	//TODO define a Talon that will control the climber motor
	
	//TODO make a 'Climber' constructor that takes in parameters for the Talon's PWM channel
	//TODO instantiate the Talon using the parameter that's passed in
	
	//TODO make a method that spins the motor when called
	
	//TODO make a method that spins the motor in reverse when called

	Talon talonMotor;
	Solenoid climberBrake;
	public Climber(int pwmChannel) {
		talonMotor = new Talon(pwmChannel);
		climberBrake = new Solenoid(Constants.pcmDeviceID, Constants.brakeSolenoid);
	 }
	 
	public void runForward(){
	 talonMotor.set(1.0);
	 }
	
	public void runBackwards(){
		talonMotor.set(-1.0);
	}
	
	public void stop(){
		talonMotor.set(0.0);
	}
	
	public void lockInPlace(boolean on){
		climberBrake.set(on);
	}
}