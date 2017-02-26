package org.usfirst.frc.team2607.robot.auto;

import java.util.ArrayList;


import org.usfirst.frc.team2607.robot.Robot;
import org.usfirst.frc.team2607.robot.RobovikingDriveTrainProfileDriver;

import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.PathGenerator;
import com.team254.lib.trajectory.TrajectoryGenerator;
import com.team254.lib.trajectory.WaypointSequence;

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
		modes.add(new CenterPeg(robot));
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
	
	public class CenterPeg extends AutonomousMode {
		
		CenterPeg(Robot r) {
			super(r);
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
			
			Path path = this.getPathFromFile("/home/lvuser/centerPeg.txt");
			
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path);
			driver.followPathBACKWARDS();
			try {
				while (!driver.isDone()) { 
					Thread.sleep(20); 
				}
				robot.gearHandler.set(true);
				Thread.sleep(10);
				robot.leftTrans.set(-100);
				robot.rightTrans.set(100);
				Thread.sleep(500);
				robot.leftTrans.set(0);
				robot.rightTrans.set(0);
				robot.gearHandler.set(false);
			} catch (Exception e) {}
		}

		@Override
		public String getName() {
			return "CenterPeg";
		}
		
	}
	
	public class LeftPeg extends AutonomousMode {
		
		LeftPeg(Robot r) {
			super(r);
		}

		@Override
		public void run() {
			TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
	        config.dt = .05;
	        config.max_acc = 10.0;
	        config.max_jerk = 30.0;
	        config.max_vel = 12.0;
	        final double kWheelbaseWidth = 29.872 / 12.0;
	        
	        WaypointSequence p = new WaypointSequence(10);
	        p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));

	        p.addWaypoint(new WaypointSequence.Waypoint(7.5 , -1.75 , 5.6));

	        Path path = PathGenerator.makePath(p, config,
	            kWheelbaseWidth, "Cotton Candy");
	        
	        //Path path = this.getPathFromFile("/home/lvuser/rightPeg.txt");
			
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path);
			driver.followPathBACKWARDS();
			
			try {
				while (!driver.isDone()) { 
					Thread.sleep(20); 
				}
				robot.gearHandler.set(true);
				Thread.sleep(10);
				robot.leftTrans.set(-100);
				robot.rightTrans.set(100);
				Thread.sleep(500);
				robot.leftTrans.set(0);
				robot.rightTrans.set(0);
				robot.gearHandler.set(false);
			} catch (Exception e) {}
		}

		@Override
		public String getName() {
			return "LeftPeg";
		}
	}
	
	public class RightPeg extends AutonomousMode {
		
		RightPeg(Robot r) {
			super(r);
		}

		@Override
		public void run() {
			
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