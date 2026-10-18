package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Subsystems.*;
import org.firstinspires.ftc.teamcode.Utils.*;

@TeleOp(name="Main",group="Decode")
public class Main extends OpMode {
    Drivetrain drivetrain;

    Launcher launcher;
    Imu imu;
    OnPress slowMode = new OnPress();
    OnPress wheelCorrectionUp = new OnPress();
    OnPress wheelCorrectionDown = new OnPress();
    OnPress launchInitiate = new OnPress();
    OnPress altLaunchInitiate = new OnPress();

    double sideCorrection = 0.15;

    double power = 1.00;
    OnPress wheelFlip = new OnPress();
    OnPress launcherFlip = new OnPress();
    OnPress kickerFlip = new OnPress();
    boolean launcherFlipped = false;
    boolean kickerFlipped = false;

    boolean fieldOriented = false;
    boolean slowModeToggled = false;

    public void init() {
        drivetrain = new Drivetrain(hardwareMap, 0.35);
        imu = new Imu(hardwareMap);
        launcher = new Launcher(hardwareMap);

    }

    public void loop() {
        double time = getRuntime();

        // Toggle field oriented modes only when both bumpers are pressed (Without repeatedly toggling

//        Calculate the value from the controller to give to the drivetrain
        double xComponent = -deadzone(gamepad1.right_stick_x, 0.20);
        // Note that the y-value on joysticks is negative for forward values so it's reversed here
        double yComponent = deadzone(-gamepad1.left_stick_y, 0.20);
        double magnitude = Math.hypot(xComponent, yComponent);
        double angle = Math.atan2(yComponent, xComponent);
        double turn = deadzone(gamepad1.right_trigger, 0.1) - deadzone(gamepad1.left_trigger, 0.1);

        telemetry.addData("Raw Angle (DEG)", Math.toDegrees(angle));

        // Use the IMU to calculate the desired angle in field-oriented mode
        if (fieldOriented) {
            angle -= imu.getRawHeading();
        }

        telemetry.addData("Raw Heading (DEG)", Math.toDegrees(imu.getRawHeading()));
        telemetry.addData("Adjusted Angle (DEG)", Math.toDegrees(angle));


        if (slowMode.pressed(gamepad1.y)) {
            slowModeToggled = !slowModeToggled;
        }

//        if(wheelFlip.pressed(gamepad1.x)) {
//            drivetrain.wheelFlip(0);
//        }

        if (kickerFlip.pressed(gamepad2.dpad_left)) {
            launcher.reverseKicker();
            kickerFlipped = !kickerFlipped;
        }
        if (launcherFlip.pressed(gamepad2.dpad_right)) {
            launcher.reverseLauncher();
            launcherFlipped = !launcherFlipped;
        }

        telemetry.addData("MAIN LAUNCH WHEEL FLIPPED?: ", launcherFlipped);
        telemetry.addData("LAUNCH KICKER SERVO FLIPPED?: ", kickerFlipped);

        if (wheelCorrectionUp.pressed(gamepad2.dpad_up)) {
            power += 0.05;
        } else if (wheelCorrectionDown.pressed(gamepad2.dpad_down)) {
            power -= 0.05;
        }
        power = Range.clip(power, 0, 1.0);

//        sideCorrection = Math.min(Math.max(sideCorrection, 0.0), 1.00);
        telemetry.addData("Launcher Power", power);

        if (launchInitiate.pressed(gamepad2.x) || altLaunchInitiate.pressed(gamepad1.x)) {
            launcher.launch(time, power);
        }

        launcher.handleLaunch(time);
        drivetrain.drive(angle, magnitude, turn, slowModeToggled, 1.0);
        //        telemetry.add("Slide target: ", slide.getTarget());
    }

    /**
     * Creates a deadzone around a value, usually used to prevent tiny error in inputs from causing outputs
     * @param val The base value
     * @param deadzone The minimum value that the value must be larger than to not fall to 0
     * @return A value that is either greater than deadzone or equal to 0
     */
    private double deadzone(double val, double deadzone) {
        if (Math.abs(val) <= deadzone) {
            return 0.0;
        }
        return val;
    }
}
