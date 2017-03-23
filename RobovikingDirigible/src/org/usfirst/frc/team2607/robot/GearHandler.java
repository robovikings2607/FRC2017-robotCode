package org.usfirst.frc.team2607.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class GearHandler {
	
	//TODO add a Solenoid (pneumatic control switch) object
	Solenoid door , ramp;
	
	public GearHandler (){
		//TODO set the Solenoid to a "port number" parameter
		door = new Solenoid(Constants.pcmDeviceID , Constants.gearDoorSolenoid);
		ramp = new Solenoid(Constants.pcmDeviceID , Constants.gearRampSolenoid);
		
	}
	public void openDoors(boolean t) {
		door.set(t);
	}
	public boolean get (){
		return door.get();
	}
	
	public void openRamp( boolean wamp ) {
		ramp.set(wamp);
	}
}

