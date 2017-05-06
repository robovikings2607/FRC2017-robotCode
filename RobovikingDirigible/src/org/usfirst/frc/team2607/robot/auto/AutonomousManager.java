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
		//modes.add(new LeftPeg(robot));
		//modes.add(new RightPeg(robot));
		//modes.add(new TestTurn(robot));
		modes.add(new RightShotCenterPeg(robot));
		modes.add(new LeftShotCenterPeg(robot));
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
	 * You must add the mode to the array once you define its class
	 */
	public class LeftPeg extends AutonomousMode {
		private Path path_0 , path_1;
		
		LeftPeg(Robot r) {
			super(r);
			
			TrajectoryGenerator.Config config =new TrajectoryGenerator.Config();
			config.dt = 0.05;
			config.max_acc = 4.0;
			config.max_jerk= 25.0;
			config.max_vel = 5.0;
			
			TrajectoryGenerator.Config config_alt =new TrajectoryGenerator.Config();
			config_alt.dt = 0.05;
			config_alt.max_acc = 6.0;
			config_alt.max_jerk= 25.0;
			config_alt.max_vel = 10.0;
			
			WaypointSequence waypoints_0 = new WaypointSequence(10);
			waypoints_0.addWaypoint(new WaypointSequence.Waypoint(0.0 , 0.0 , 0.0));
			waypoints_0.addWaypoint(new WaypointSequence.Waypoint(6.6 , 0.0 , 0.0));
			path_0 = PathGenerator.makePath(waypoints_0, config, Constants.kWheelbaseWidth, "LeftPeg_0");
			
			WaypointSequence waypoints_1 = new WaypointSequence(10);
			waypoints_1.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			waypoints_1.addWaypoint(new WaypointSequence.Waypoint(6.0, 0.0, 0.0));
			path_1 = PathGenerator.makePath(waypoints_1, config, Constants.kWheelbaseWidth, "LeftPeg_1");
			
			/*DRIVE ACROSS FIELD (1 of 2)
			WaypointSequence waypoints_2 = new WaypointSequence(10);
			waypoints_2.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			waypoints_2.addWaypoint(new WaypointSequence.Waypoint(17.5, 0.0, 0.0));
			path_2 = PathGenerator.makePath(waypoints_2, config_alt, Constants.kWheelbaseWidth, "LeftPeg_2");
			*/
		}

		@Override
		public void run() {
			robot.setupAutonConfig();
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_0);
			try {
				Thread.sleep(250); //WAITING FOR SHIFTERS
				driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_0);
				driver.followPathBACKWARDS();
				while(!driver.isDone()) Thread.sleep(20);
				
				robot.rotateDeg(62.5);
				Thread.sleep(30);
				driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_1);
				driver.followPath();
				while(!driver.isDone()) Thread.sleep(20);
				
				System.out.println("RELEASE GEAR NOW!");
				//TODO Release Gear
				robot.gearHandler.setDoors(Constants.gearOpen);
				driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_1);
				driver.followPath();
				while(!driver.isDone()) Thread.sleep(20);
				
				/* DRIVE ACROSS FIELD (2 of 2)
				robot.rotateDeg(-62.5);
				Thread.sleep(30);
				driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_2);
				driver.followPathBACKWARDS();
				while(!driver.isDone()) Thread.sleep(20);
				*/
				
			} catch (Exception e) { 
				System.out.println("ERROR: stopping autonomous"); 
				driver.interruptProfile();
			}
		}
		
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "LeftPeg";
		}
	}
	
	public class RightPeg extends AutonomousMode {
		private Path path_0 , path_1;
		
		RightPeg(Robot r) {
			super(r);
			
			TrajectoryGenerator.Config config =new TrajectoryGenerator.Config();
			config.dt = 0.05;
			config.max_acc = 4.0;
			config.max_jerk= 25.0;
			config.max_vel = 5.0;
			
			TrajectoryGenerator.Config config_alt =new TrajectoryGenerator.Config();
			config_alt.dt = 0.05;
			config_alt.max_acc = 6.0;
			config_alt.max_jerk= 25.0;
			config_alt.max_vel = 10.0;
			
			WaypointSequence waypoints_0 = new WaypointSequence(10);
			waypoints_0.addWaypoint(new WaypointSequence.Waypoint(0.0 , 0.0 , 0.0));
			waypoints_0.addWaypoint(new WaypointSequence.Waypoint(6.5 , 0.0 , 0.0));
			path_0 = PathGenerator.makePath(waypoints_0, config, Constants.kWheelbaseWidth, "RightPeg_0");
			
			WaypointSequence waypoints_1 = new WaypointSequence(10);
			waypoints_1.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			waypoints_1.addWaypoint(new WaypointSequence.Waypoint(6.0, 0.0, 0.0));
			path_1 = PathGenerator.makePath(waypoints_1, config, Constants.kWheelbaseWidth, "RightPeg_1");
			
			/* DRIVE ACROSS (1 of 2)
			WaypointSequence waypoints_2 = new WaypointSequence(10);
			waypoints_2.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			waypoints_2.addWaypoint(new WaypointSequence.Waypoint(20.0, 0.0, 0.0));
			path_2 = PathGenerator.makePath(waypoints_2, config_alt, Constants.kWheelbaseWidth, "RightPeg_2");
			*/
		}
		
		@Override
		public void run() {
			robot.setupAutonConfig();
			try {
				Thread.sleep(250); //WAITING FOR SHIFTERS
				RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_0);
				driver.followPathBACKWARDS();
				while(!driver.isDone()) Thread.sleep(20);
				
				robot.rotateDeg(-58.3);
				Thread.sleep(30);
				driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_1);
				driver.followPathBACKWARDS();
				while(!driver.isDone()) Thread.sleep(20);
				
				System.out.println("RELEASE GEAR NOW!");
				//TODO Release Gear
				robot.gearHandler.setDoors(Constants.gearOpen);
				driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_1);
				driver.followPath();
				while(!driver.isDone()) Thread.sleep(20);
				
				/*
				robot.rotateDeg(58.3);
				Thread.sleep(30);
				driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_2);
				driver.followPathBACKWARDS();
				*/
			} catch (Exception e) { 
				System.out.println("ERROR: stopping autonomous"); 
			}
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "04-RightPeg";
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
			
			robot.shifter.set(Constants.lowGear);
			robot.leftTrans.setHighGear(false, true);
			robot.rightTrans.setHighGear(false, true);
			
			try{ Thread.sleep(250);} catch(Exception e){System.out.println("Error waiting for shifters to shift...");}
			
			//Path path = this.getPathFromFile("/home/lvuser/centerPeg.txt");
			
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path);
			driver.followPathBACKWARDS();
			try {
				while (!driver.isDone()) { 
					System.out.println("   Finnegan is a dunce!! BIG THINGS!!!!!    ;)");
					Thread.sleep(20); 
				}
				robot.gearHandler.setDoors(Constants.gearOpen);
				Thread.sleep(750);
				robot.leftTrans.set(-60);
				robot.rightTrans.set(60);
				Thread.sleep(400);
				robot.leftTrans.set(0);
				robot.rightTrans.set(0);
				robot.gearHandler.setDoors(Constants.gearClosed);
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
	
	public class RightShotCenterPeg extends AutonomousMode {
		//AKA RED ALLIANCE
		Path path_0, path_1;
		
		RightShotCenterPeg(Robot r) {
			super(r);
			TrajectoryGenerator.Config config =new TrajectoryGenerator.Config();
			config.dt = 0.05;
			config.max_acc = 4.5;
			config.max_jerk= 25.0;
			config.max_vel = 7.0;
			
			WaypointSequence waypoint_0 = new WaypointSequence(10);
			waypoint_0.addWaypoint(new WaypointSequence.Waypoint(0.0 , 0.0 , 0.0));
			waypoint_0.addWaypoint(new WaypointSequence.Waypoint(7.3 , 0.0 , 0.0));
			path_0 = PathGenerator.makePath(waypoint_0, config, Constants.kWheelbaseWidth, "CenterPeg_0");
			
			WaypointSequence waypoint_1 = new WaypointSequence(10);
			waypoint_1.addWaypoint(new WaypointSequence.Waypoint(0.0 , 0.0 , 0.0));
			waypoint_1.addWaypoint(new WaypointSequence.Waypoint(3.7 , 0.0 , 0.0));
			path_1 = PathGenerator.makePath(waypoint_1, config, Constants.kWheelbaseWidth, "CenterPeg_1");
		}
		
		@Override
		public void run() {
			robot.setupAutonConfig();
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_0);
			try{ 
				Thread.sleep(250);
				driver.followPathBACKWARDS();
				while (!driver.isDone()) Thread.sleep(20);
				
				robot.gearHandler.setDoors(true);
				Thread.sleep(550);
				
				driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_1);
				driver.followPath();
				while(!driver.isDone()) Thread.sleep(20);
				
				robot.gearHandler.setDoors(false);
				Thread.sleep(30);
				
				robot.rotateDegAlt(-164.4); //change if robot needs to turn more //-22deg
				Thread.sleep(30);
				robot.shooter.useTargetLight(true);
				
				robot.shooter.set(0.958); //change if missing short or long 0.0 to 1.0 (%)
				Thread.sleep(320);
				robot.shooter.load(true);
				Thread.sleep(5000);
				robot.shooter.load(false);
				robot.shooter.set(0.0);
				robot.shooter.useTargetLight(false);
				
			} catch (Exception e) {
				driver.interruptProfile();
				robot.shooter.set(0.0);
				robot.shooter.load(false);
				robot.shooter.useTargetLight(false);
			}
		}
		@Override
		public String getName() {
			return "RightShotCenterPeg";
		}
	}
	
	public class LeftShotCenterPeg extends AutonomousMode {
		//AKA Blue Alliance NOT TESTED
		Path path_0, path_1;
		
		LeftShotCenterPeg(Robot r) {
			super(r);
			TrajectoryGenerator.Config config =new TrajectoryGenerator.Config();
			config.dt = 0.05;
			config.max_acc = 3.0;
			config.max_jerk= 25.0;
			config.max_vel = 4.0;
			
			WaypointSequence waypoint_0 = new WaypointSequence(10);
			waypoint_0.addWaypoint(new WaypointSequence.Waypoint(0.0 , 0.0 , 0.0));
			waypoint_0.addWaypoint(new WaypointSequence.Waypoint(7.3 , 0.0 , 0.0));
			path_0 = PathGenerator.makePath(waypoint_0, config, Constants.kWheelbaseWidth, "CenterPeg_0");
			
			WaypointSequence waypoint_1 = new WaypointSequence(10);
			waypoint_1.addWaypoint(new WaypointSequence.Waypoint(0.0 , 0.0 , 0.0));
			waypoint_1.addWaypoint(new WaypointSequence.Waypoint(3.7 , 0.0 , 0.0));
			path_1 = PathGenerator.makePath(waypoint_1, config, Constants.kWheelbaseWidth, "CenterPeg_1");
		}
		
		@Override
		public void run() {
			robot.setupAutonConfig();
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_0);
			try{ 
				Thread.sleep(250);
				driver.followPathBACKWARDS();
				while (!driver.isDone()) Thread.sleep(20);
				
				robot.gearHandler.setDoors(true);
				Thread.sleep(550);
				
				driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path_1);
				driver.followPath();
				while(!driver.isDone()) Thread.sleep(20);
				
				robot.gearHandler.setDoors(false);
				Thread.sleep(30);
				
				robot.rotateDeg(-22.6); //increase magnitude to increase turn
				Thread.sleep(30);
				robot.shooter.useTargetLight(true);
				robot.shooter.set(0.953); //change if ball is shooting to short or far
				Thread.sleep(700);
				robot.shooter.load(true);
				Thread.sleep(5000);
				robot.shooter.useTargetLight(false);
				robot.shooter.load(false);
				robot.shooter.set(0.0);
				
			} catch (Exception e) {
				driver.interruptProfile();
				robot.shooter.useTargetLight(false);
				robot.shooter.load(false);
				robot.shooter.set(0.0);
			}
		}
		@Override
		public String getName() {
			return "LeftShotCenterPeg";
		}
	}
	
	//*************************************************************************//*************************************************************************8
	//*************************************************************************//*************************************************************************8
	//*************************************************************************//*************************************************************************8
	
	/*
	public class RedLeftPeg extends AutonomousMode {
		//Mirrors BlueRightPeg
		Path path;
		RedLeftPeg(Robot r) {
			super(r);
			TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
	        config.dt = .05;
	        config.max_acc = 3.0;//5.0;
	        config.max_jerk = 30.0;
	        config.max_vel = 4.0;//7.0;
	        
	        double kDist = 7.0;
	        WaypointSequence p = new WaypointSequence(10);
			p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			//p.addWaypoint(new WaypointSequence.Waypoint(6.45, 0.0, 0.0));
			//p.addWaypoint(new WaypointSequence.Waypoint(8.95 , -1.9 , 5.1));
			//p.addWaypoint(new WaypointSequence.Waypoint(4.9, 0.0, 0.0));
			//p.addWaypoint(new WaypointSequence.Waypoint(8.6 , -4.5 , 4.8));
			p.addWaypoint(new WaypointSequence.Waypoint( kDist - 3.5, 0.0, 0.0));
			p.addWaypoint(new WaypointSequence.Waypoint(kDist , -6.2 , 4.8));
			path = PathGenerator.makePath(p, config,
		            Constants.kWheelbaseWidth, "Cotton Candy");
		}

		@Override
		public void run() {
	        /*
	        WaypointSequence p = new WaypointSequence(10);
	        p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));

	        p.addWaypoint(new WaypointSequence.Waypoint(7.5 , -1.75 , 5.6));
			
			robot.shifter.set(Constants.lowGear);
			robot.leftTrans.setHighGear(false, true);
			robot.rightTrans.setHighGear(false, true);
			robot.gearHandler.setDoors(Constants.gearClosed);
	        
	        //Path path = this.getPathFromFile("/home/lvuser/rightPeg.txt");
			
			System.out.println("running LeftPeg auton...");
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path);
			driver.followPathBACKWARDS();
			
			try {
				while (!driver.isDone()) { 
					System.out.println("   Finnegan is a dunce!! BIG THINGS!!    ;)");
					Thread.sleep(20); 
				}
				robot.gearHandler.setDoors(Constants.gearOpen);
				Thread.sleep(750);
				robot.leftTrans.set(-60);
				robot.rightTrans.set(60);
				Thread.sleep(150);
				robot.leftTrans.set(0);
				robot.rightTrans.set(0);
				Thread.sleep(200);
				robot.gearHandler.setDoors(Constants.gearClosed);
				robot.shifter.set(Constants.highGear);
				robot.leftTrans.setHighGear(true, false);
				robot.rightTrans.setHighGear(true, false);
			} catch (Exception e) {System.out.println("Interrupted...");}
			
			System.out.println("done Leftpeg");
		}

		@Override
		public String getName() {
			return "RedLeftPeg";
		}
	}
	
	public class BlueRightPeg extends AutonomousMode {
		//Mirrors RedLeftPeg
		Path path;
		BlueRightPeg(Robot r) {
			super(r);
			TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
	        config.dt = .05;
	        config.max_acc = 3.0;//5.0;
	        config.max_jerk = 30.0;
	        config.max_vel = 4.0;//7.0;
	        
	        double kDist = 7.0;
	        WaypointSequence p = new WaypointSequence(10);
			p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			//p.addWaypoint(new WaypointSequence.Waypoint(6.45, 0.0, 0.0));
			//p.addWaypoint(new WaypointSequence.Waypoint(8.95 , -1.9 , 5.1));
			p.addWaypoint(new WaypointSequence.Waypoint( kDist - 3.5, 0.0, 0.0));
			p.addWaypoint(new WaypointSequence.Waypoint(kDist , 6.2 , 1.5)); //opposite of 4.8 rads //4.4 y works, just drive straight further
			path = PathGenerator.makePath(p, config,
		            Constants.kWheelbaseWidth, "Cherry Pie");
		}

		@Override
		public void run() {
	        /*
	        WaypointSequence p = new WaypointSequence(10);
	        p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));

	        p.addWaypoint(new WaypointSequence.Waypoint(7.5 , -1.75 , 5.6));
			
			robot.shifter.set(Constants.lowGear);
			robot.leftTrans.setHighGear(false, true);
			robot.rightTrans.setHighGear(false, true);
			robot.gearHandler.setDoors(Constants.gearClosed);
	        
	        //Path path = this.getPathFromFile("/home/lvuser/rightPeg.txt");
			
			System.out.println("running RightPeg auton...");
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans , robot.rightTrans , path);
			driver.followPathBACKWARDS();
			
			try {
				while (!driver.isDone()) {
					System.out.println("   Finnegan is a dunce!! BIG THINGS!!    ;)");
					Thread.sleep(20); 
				}
				/*
				robot.leftTrans.set(20);
				robot.rightTrans.set(-20);
				Thread.sleep(400);
				robot.leftTrans.set(0);
				robot.rightTrans.set(0);
				
				robot.gearHandler.setDoors(Constants.gearOpen);
				Thread.sleep(1000);
				robot.leftTrans.set(-10);
				robot.rightTrans.set(10);
				Thread.sleep(150);
				robot.leftTrans.set(0);
				robot.rightTrans.set(0);
				Thread.sleep(200);
				robot.gearHandler.setDoors(Constants.gearClosed);
				robot.shifter.set(Constants.highGear);
				robot.leftTrans.setHighGear(true, false);
				robot.rightTrans.setHighGear(true, false);
			} catch (Exception e) {System.out.println("Interrupted...");}
			
			System.out.println("done Rightpeg");
		}

		@Override
		public String getName() {
			return "BlueRightPeg";
		}
	}
	
	public class RedRightPeg extends AutonomousMode {
		//Mirrors BlueLeftPeg //GOOD AT LEHIGH
		Path path;
		
		RedRightPeg(Robot r) {
			super(r);
			TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
			config.dt = .05;
			config.max_acc = 4.0;
			config.max_jerk = 30.0;
			config.max_vel = 5.0;
			
			double kDist = 7.1;
			WaypointSequence p = new WaypointSequence(10);
			p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			p.addWaypoint(new WaypointSequence.Waypoint(kDist - 2.7, 0.0, 0.0));
			p.addWaypoint(new WaypointSequence.Waypoint(kDist , 4.3 , 1.5)); //change rads
			//p.addWaypoint(new WaypointSequence.Waypoint(7.5, -1.75, 5.6));  // heading 5.6 turned left instead of right since we follow backwards
			//p.addWaypoint(new WaypointSequence.Waypoint(13.7, 2.3, 1.5));
			
			path = PathGenerator.makePath(p, config, Constants.kWheelbaseWidth, "rightPeg");
			
		}

		@Override
		public void run() {
			
			robot.gearHandler.setDoors(Constants.gearClosed);
			robot.shifter.set(Constants.lowGear);
			robot.leftTrans.setHighGear(false, true);
			robot.rightTrans.setHighGear(false, true);
			
			System.out.println("running RightPeg auton....");
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans, robot.rightTrans, path);
			driver.followPathBACKWARDS();
			
			try {
				while (!driver.isDone()) {
				System.out.println("   Finnegan is a dunce!! BIG THINGS!!    ;)");
					Thread.sleep(20);
				}
				
				/*
				robot.leftTrans.set(60);
				robot.rightTrans.set(-60);
				Thread.sleep(20);
				robot.leftTrans.set(0);
				robot.rightTrans.set(0);
				
				robot.gearHandler.setDoors(Constants.gearOpen);
				Thread.sleep(701);
				robot.leftTrans.set(-20);
				robot.rightTrans.set(20);
				Thread.sleep(400);
				robot.leftTrans.set(0);
				robot.rightTrans.set(0);
				robot.gearHandler.setDoors(Constants.gearOpen);
				robot.shifter.set(Constants.highGear);
				robot.leftTrans.setHighGear(true, false);
				robot.rightTrans.setHighGear(true, false);
			} catch (Exception e) {
				System.out.println("....RightPeg path interrupted");
			}
			
			
			System.out.println("done RightPeg");
		}

		@Override
		public String getName() {
			return "RedRightPeg";
		}
	}
	
	public class BlueLeftPeg extends AutonomousMode {
		//Mirrors RedRightPeg
		Path path;
		
		BlueLeftPeg(Robot r) {
			super(r);
			TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
			config.dt = .05;
			config.max_acc = 4.0;
			config.max_jerk = 30.0;
			config.max_vel = 5.0;
			
			double kDist = 7.9;
			WaypointSequence p = new WaypointSequence(10);
			p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			p.addWaypoint(new WaypointSequence.Waypoint(kDist - 2.7, 0.0, 0.0));
			p.addWaypoint(new WaypointSequence.Waypoint(kDist , -4.3 , 4.8));
			//p.addWaypoint(new WaypointSequence.Waypoint(7.5, -1.75, 5.6));  // heading 5.6 turned left instead of right since we follow backwards
			//p.addWaypoint(new WaypointSequence.Waypoint(13.7, 2.3, 1.5));
			
			path = PathGenerator.makePath(p, config, Constants.kWheelbaseWidth, "leftPeg");
			
		}

		@Override
		public void run() {
			
			robot.shifter.set(Constants.lowGear);
			robot.gearHandler.setDoors(Constants.gearClosed);
			robot.leftTrans.setHighGear(false, true);
			robot.rightTrans.setHighGear(false, true);
			
			System.out.println("running LeftPeg auton....");
			RobovikingDriveTrainProfileDriver driver = new RobovikingDriveTrainProfileDriver(robot.leftTrans, robot.rightTrans, path);
			driver.followPathBACKWARDS();
			
			try {
				while (!driver.isDone()) {
				System.out.println("   Finnegan is a dunce!! BIG THINGS!!    ;)");
					Thread.sleep(20);
				}
				
				robot.gearHandler.setDoors(Constants.gearOpen);
				Thread.sleep(750);
				robot.leftTrans.set(-20);
				robot.rightTrans.set(20);
				Thread.sleep(400);
				robot.leftTrans.set(0);
				robot.rightTrans.set(0);
				robot.gearHandler.setDoors(Constants.gearOpen);
				robot.shifter.set(Constants.highGear);
				robot.leftTrans.setHighGear(true, false);
				robot.rightTrans.setHighGear(true, false);
			} catch (Exception e) {
				System.out.println("....RightPeg path interrupted");
			}
			
			
			System.out.println("done LeftPeg");
		}

		@Override
		public String getName() {
			return "BlueLeftPeg";
		}
	}
	*/
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
	
	public class CrossBaseline extends AutonomousMode {

		Path path;
		CrossBaseline(Robot r) {
			super(r);
			TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
			config.dt = .05;
			config.max_acc = 9.0;//4.5; //prev 5.0
			config.max_jerk = 25.0;
			config.max_vel = 10.0;//5.0;
			
			WaypointSequence p = new WaypointSequence(10);
			p.addWaypoint(new WaypointSequence.Waypoint(0.0, 0.0, 0.0));
			p.addWaypoint(new WaypointSequence.Waypoint(14.0, 0.0, 0.0));
			
			path = PathGenerator.makePath(p, config, Constants.kWheelbaseWidth, "CrossBaseline");
		}
		@Override
		public void run() {
			robot.shifter.set(Constants.highGear);
			robot.leftTrans.setHighGear(true, true);
			robot.rightTrans.setHighGear(true, true);
			
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
	
	public class TestTurn extends AutonomousMode {

		TestTurn(Robot r) {
			super(r);
			
		}
		@Override
		public void run() {
			robot.setupAutonConfig();
			try { Thread.sleep(100);
					robot.rotateDeg(90.0);
			} catch (Exception e) {}
			
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "turn-test";
		}
		
	}
	
}