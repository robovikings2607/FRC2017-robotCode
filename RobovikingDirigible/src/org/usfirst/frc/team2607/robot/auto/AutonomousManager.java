package org.usfirst.frc.team2607.robot.auto;

import java.util.ArrayList;

import org.usfirst.frc.team2607.robot.Constants;
import org.usfirst.frc.team2607.robot.Robot;
import org.usfirst.frc.team2607.robot.RobovikingDriveTrainProfileDriver;

import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.PathGenerator;
import com.team254.lib.trajectory.TrajectoryGenerator;
import com.team254.lib.trajectory.WaypointSequence;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Cerora
 *
 */
public class AutonomousManager {
	  
	Robot robot;
	public ArrayList<AutonomousMode> modes = new ArrayList<AutonomousMode>();

	AutonomousManager(Robot robot){
		this.robot = robot;
		
		modes.add(new DoNothingFailsafe());
		modes.add(new DoNothing());
		modes.add(new CrossBaseline(robot));
		modes.add(new CenterPeg(robot));
		modes.add(new LeftPeg(robot));
		modes.add(new RightPeg(robot));
		modes.add(new Voltage(robot));
	}
	
	public AutonomousMode getModeByName (String name){
		for (AutonomousMode m : modes){
			if (m.getName().equals(name))
				return m;
		}
		
		try {
			throw new Exception();
		} catch (Exception e) {
			System.err.println("Mode not found");
			e.printStackTrace();
			return new DoNothingFailsafe();
		}
	}
	
	public AutonomousMode getModeByIndex (int index){
		try {
			return modes.get(index);
		} catch (IndexOutOfBoundsException e){
			System.err.println("Mode out of array bounds");
			e.printStackTrace();
			return new DoNothingFailsafe();
		}
	}
	
		
	
	/*
	 * BEGIN AUTON MODE DECLARATIONS
	 * 
	 * You must add the mode to the array once you define its class
	 */
	
	public class CrossBaseline extends AutonomousMode {

		Path path;
		CrossBaseline(Robot r) {
			super(r);
			TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
			config.dt = .05;
			config.max_acc = 4.5; //prev 5.0
			config.max_jerk = 25.0;
			config.max_vel = 7.0;
			
			WaypointSequence p = new WaypointSequence(10);
			p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			p.addWaypoint(new WaypointSequence.Waypoint(14.0, 0.0, 0.0));
			
			path = PathGenerator.makePath(p, config, Constants.kWheelbaseWidth, "CrossBaseline");
		}
		@Override
		public void run() {
			robot.shifter.set(Constants.lowGear);
			robot.leftTrans.setHighGear(false, true);
			robot.rightTrans.setHighGear(false, true);
			
			try{ Thread.sleep(250);} catch(Exception e){System.out.println("Error waiting for shifters to shift...");}
			
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path);
			driver.followPathBACKWARDS();
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "CrossBaseline";
		}
		
	}
	
	public class CenterPeg extends AutonomousMode {
		
		Path path;
		CenterPeg(Robot r) {
			super(r);
			TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
			config.dt = .05;
			config.max_acc = 4.5; //prev 5.0
			config.max_jerk = 25.0;
			config.max_vel = 7.0;
			
			WaypointSequence p = new WaypointSequence(10);
			p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			p.addWaypoint(new WaypointSequence.Waypoint(7.3, 0.0, 0.0));
			
			path = PathGenerator.makePath(p, config, Constants.kWheelbaseWidth, "CenterPeg");
		}

		@Override
		public void run() {
			
			/*
	    	TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
	        config.dt = .05;
	        config.max_acc = 14.0;
	        config.max_jerk = 30.0;
	        config.max_vel = 8.0;
	        final double kWheelbaseWidth = 29.872 / 12.0;
	        final double fudgex = 1.0;
	        
	        WaypointSequence p = new WaypointSequence(10);
	        p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));

	        p.addWaypoint(new WaypointSequence.Waypoint(12.0 * fudgex , 0.0 , 0.0));

	        Path path = PathGenerator.makePath(p, config,
	            kWheelbaseWidth, "Corn Dogs");
	        */
			
			robot.shifter.set(Constants.lowGear);
			robot.leftTrans.setHighGear(false, true);
			robot.rightTrans.setHighGear(false, true);
			
			try{ Thread.sleep(250);} catch(Exception e){System.out.println("Error waiting for shifters to shift...");}
			
			//Path path = this.getPathFromFile("/home/lvuser/centerPeg.txt");
			
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path);
			driver.followPathBACKWARDS();
			try {
				while (!driver.isDone()) { 
					Thread.sleep(20); 
				}
				robot.gearHandler.openDoors(Constants.gearOpen);
				Thread.sleep(500);
				robot.leftTrans.set(-100);
				robot.rightTrans.set(100);
				Thread.sleep(300);
				robot.leftTrans.set(0);
				robot.rightTrans.set(0);
				robot.gearHandler.openDoors(Constants.gearClosed);
				robot.shifter.set(Constants.highGear);
				robot.leftTrans.setHighGear(true, false);
				robot.rightTrans.setHighGear(true, false);
			} catch (Exception e) {}
		}

		@Override
		public String getName() {
			return "CenterPeg";
		}
		
	}
	
	public class LeftPeg extends AutonomousMode {
		
		Path path;
		LeftPeg(Robot r) {
			super(r);
			TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
	        config.dt = .05;
	        config.max_acc = 4.0;//5.0;
	        config.max_jerk = 30.0;
	        config.max_vel = 5.0;//7.0;
	        
	        WaypointSequence p = new WaypointSequence(10);
			p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			//p.addWaypoint(new WaypointSequence.Waypoint(6.45, 0.0, 0.0));
			//p.addWaypoint(new WaypointSequence.Waypoint(8.95 , -1.9 , 5.1));
			p.addWaypoint(new WaypointSequence.Waypoint(7.15, 0.0, 0.0));
			p.addWaypoint(new WaypointSequence.Waypoint(9.8 , -2.4 , 5.1));
			path = PathGenerator.makePath(p, config,
		            Constants.kWheelbaseWidth, "Cotton Candy");
		}

		@Override
		public void run() {
	        /*
	        WaypointSequence p = new WaypointSequence(10);
	        p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));

	        p.addWaypoint(new WaypointSequence.Waypoint(7.5 , -1.75 , 5.6));
			*/
			robot.shifter.set(Constants.lowGear);
			robot.leftTrans.setHighGear(false, true);
			robot.rightTrans.setHighGear(false, true);
			robot.gearHandler.openDoors(Constants.gearClosed);
	        
	        //Path path = this.getPathFromFile("/home/lvuser/rightPeg.txt");
			
			System.out.println("running LeftPeg auton...");
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path);
			driver.followPathBACKWARDS();
			/*
			try {
				while (!driver.isDone()) { 
					Thread.sleep(20); 
				}
				robot.gearHandler.set(Constants.gearOpen);
				Thread.sleep(499);
				robot.leftTrans.set(-60);
				robot.rightTrans.set(60);
				Thread.sleep(350);
				robot.leftTrans.set(0);
				robot.rightTrans.set(0);
				robot.gearHandler.set(Constants.gearClosed);
				robot.shifter.set(Constants.highGear);
				robot.leftTrans.setHighGear(true, false);
				robot.rightTrans.setHighGear(true, false);
			} catch (Exception e) {System.out.println("Interrupted...");}
			*/
			System.out.println("done Leftpeg");
		}

		@Override
		public String getName() {
			return "RedLeftPeg";
		}
	}
	
	public class RightPeg extends AutonomousMode {
		

		Path path;
		
		RightPeg(Robot r) {
			super(r);
			TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
			config.dt = .05;
			config.max_acc = 5.0;
			config.max_jerk = 30.0;
			config.max_vel = 7.0;
			
			WaypointSequence p = new WaypointSequence(10);
			p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			p.addWaypoint(new WaypointSequence.Waypoint(4.6, 0.0, 0.0));
			p.addWaypoint(new WaypointSequence.Waypoint(7.2 , 3.7 , 1.55));
			//p.addWaypoint(new WaypointSequence.Waypoint(7.5, -1.75, 5.6));  // heading 5.6 turned left instead of right since we follow backwards
			//p.addWaypoint(new WaypointSequence.Waypoint(13.7, 2.3, 1.5));
			
			path = PathGenerator.makePath(p, config, Constants.kWheelbaseWidth, "rightPeg");
			
		}

		@Override
		public void run() {
			
			robot.shifter.set(Constants.lowGear);
			robot.leftTrans.setHighGear(false, true);
			robot.rightTrans.setHighGear(false, true);
			
			System.out.println("running RightPeg auton....");
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans, robot.rightTrans, path);
			driver.followPathBACKWARDS();
			/*
			try {
				while (!driver.isDone()) {
					Thread.sleep(20);
				}
				
				robot.gearHandler.set(Constants.gearOpen);
				Thread.sleep(10);
				robot.leftTrans.set(-100);
				robot.rightTrans.set(100);
				Thread.sleep(500);
				robot.leftTrans.set(0);
				robot.rightTrans.set(0);
				robot.gearHandler.set(Constants.gearOpen);
				robot.shifter.set(Constants.highGear);
				robot.leftTrans.setHighGear(true, false);
				robot.rightTrans.setHighGear(true, false);
			} catch (Exception e) {
				System.out.println("....RightPeg path interrupted");
			}
			*/
			
			System.out.println("done RightPeg");
		}

		@Override
		public String getName() {
			return "RightPeg";
		}
	}
	
	public class DoNothing extends AutonomousMode {
		
		DoNothing(){
			
		}

		@Override
		public void run() {
			System.out.println("Explicitly told not to move");
		}

		@Override
		public String getName() {
			return "DoNothing";
		}
		
	}
	
	public class Voltage extends AutonomousMode {
		
		Voltage(Robot r){
			super(r);
		}

		@Override
		public void run() {
			System.out.println("running voltage test");
			robot.leftTrans.enableVoltage(false);
			robot.rightTrans.enableVoltage(false);
			try {
				while (true && !Thread.interrupted()) {
					robot.leftTrans.set(SmartDashboard.getNumber("leftVoltage", 0.0));
					robot.rightTrans.set(SmartDashboard.getNumber("rightVoltage",0.0));
					Thread.sleep(20);
				}
			} catch (Exception e) {}
		}

		@Override
		public String getName() {
			return "Voltage";
		}
		
	}

	
	
	public class DoNothingFailsafe extends AutonomousMode {
		
		DoNothingFailsafe(){
			
		}

		@Override
		public void run() {
			System.out.println("This shouldn't be running - Mode 0 selected for some reason");
		}

		@Override
		public String getName() {
			return "DoNothingFailsafe";
		}
		
	}
}