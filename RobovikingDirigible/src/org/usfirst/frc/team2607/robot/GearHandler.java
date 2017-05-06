package org.usfirst.frc.team2607.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class GearHandler {
	
	//TODO add a Solenoid (pneumatic control switch) object
	Solenoid door , ramp, pickUp;
	Talon rollers;
	
	public GearHandler (){
		//TODO set the Solenoid to a "port number" parameter
		door = new Solenoid(Constants.pcmDeviceID , Constants.gearDoorSolenoid);
		ramp = new Solenoid(Constants.pcmDeviceID , Constants.gearRampSolenoid);
		pickUp = new Solenoid(Constants.pcmDeviceID, Constants.gearPickUpSolenoid);
		rollers = new Talon(Constants.gearPickupMotor);
	}
	public void setDoors(boolean t) {
		door.set(t);
	}
	
	public void setRamp( boolean wamp ) {
		ramp.set(wamp);
	}
	
	public void setPickup( boolean plarg ){
		pickUp.set(plarg);
	}
	
	public void setRollers(double umLikeSeven){
		rollers.set(umLikeSeven);
	}
}

