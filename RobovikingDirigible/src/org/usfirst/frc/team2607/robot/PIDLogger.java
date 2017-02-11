package org.usfirst.frc.team2607.robot;

import java.io.File;
import java.io.PrintWriter;

import com.ctre.CANTalon;

public class PIDLogger extends Thread {
	private PrintWriter logFile = null;
	private boolean loggingEnabled = false;
	String deviceName;
	private double SP;
	
	CANTalon srx;
	
	@Override
	public void run(){
		while (true){
			logEntry();
			try {Thread.sleep(10); } catch (Exception e) {}
		}
		
	}
	
	public PIDLogger(CANTalon talonSRX , String dn){
		srx = talonSRX;
		deviceName = dn;
	}
	
	public void updSetpoint(double newSP) {
		SP = newSP;
	}
	
	public void enableLogging(boolean enable) {
	    	boolean okToEnable = enable;
			if (enable && !loggingEnabled) {
	    		if (logFile != null) {
	    			logFile.close();
	    			logFile = null;
	    		}
	    		try {
	    			String s = "/home/lvuser/" + deviceName + "." + System.currentTimeMillis() + ".csv";
	    			logFile = new PrintWriter(new File(s));
	    			logFile.println("Time,SP,RPM,Native,Err,VIn,VOut,AmpOut,P,I,D,F");
	    		} catch (Exception e) {
	    			okToEnable = false;
	    		}
	    	} 
	    	
	    	if (!enable && loggingEnabled) {
	    		if (logFile != null) {
	    			logFile.close();
	    			logFile = null;
	    		}
	    	}
	    	
	    	loggingEnabled = okToEnable;
	    }
	 
	 public void logEntry() {
	        if (loggingEnabled) {
	        	logFile.println(System.currentTimeMillis() + "," +
	        					SP + "," +
	        					srx.getSpeed() + "," + 
	        					srx.getEncVelocity() + "," +
	        				    srx.getClosedLoopError() + "," + 
	        					srx.getBusVoltage()+ "," + 
	        				    srx.getOutputVoltage() + "," +
	        				    srx.getOutputCurrent() + "," +
	        				    srx.getP() + "," +
	        				    srx.getI() + "," +
	        				    srx.getD() + "," +
	        				    srx.getF() + ","	);
	        	logFile.flush();
	        }
	    }

}
