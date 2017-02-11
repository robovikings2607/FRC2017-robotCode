package org.usfirst.frc.team2607.robot;

import edu.wpi.first.wpilibj.Talon;

public class Climber {
	//TODO define a Talon that will control the climber motor
	
	//TODO make a 'Climber' constructor that takes in parameters for the Talon's PWM channel
	//TODO instantiate the Talon using the parameter that's passed in
	
	//TODO make a method that spins the motor when called
	
	//TODO make a method that spins the motor in reverse when called

	Talon talonMotor;
	public Climber(int pwmChannel) {
		talonMotor = new Talon(pwmChannel);
	 }
	 
	public void runForward(){
	 talonMotor.set (0.5);
	 }
	
	public void runBackwards(){
		talonMotor.set(-0.5);
	}
	
	public void stop(){
		talonMotor.set(0.0);
	}
}