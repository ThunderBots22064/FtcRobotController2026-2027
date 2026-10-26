package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

// Controls Launcher
public class Intake {
    private DcMotor launchWheel;
    private Servo kicker;

    private double startTime = -1;

    public Intake(HardwareMap hardwareMap) {
        launchWheel = hardwareMap.get(DcMotor.class, "launcherWheel");
        kicker = hardwareMap.get(Servo.class, "kicker");

        launchWheel.setDirection(DcMotorSimple.Direction.FORWARD);
        kicker.setDirection(Servo.Direction.FORWARD);

        launchWheel.setPower(0);
        kicker.setPosition(0.5);
    }

    public void reverseKicker() {
        Servo.Direction currentDir = kicker.getDirection();
        Servo.Direction newDir = (currentDir == Servo.Direction.FORWARD) ? Servo.Direction.REVERSE : Servo.Direction.FORWARD;
        kicker.setDirection(newDir);
    }

    public void reverseLauncher() {
        DcMotorSimple.Direction currentDir = launchWheel.getDirection();
        DcMotorSimple.Direction newDir = (currentDir == DcMotorSimple.Direction.FORWARD) ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;
        launchWheel.setDirection(newDir);
    }

    public void launch(double time, double power) {
        if (startTime == -1) {
            startTime = time;

            power = Range.clip(power, 0.0, 1.0);

            launchWheel.setPower(power);
        }
    }

    public void handleLaunch(double time) {
        if (startTime == -1) {
            return;
        }

        double timeElapsed = time - startTime;

//        Motor up to speed so we push the ball in
        if (timeElapsed >= 3.5 + 3) {
            startTime = -1;
            stop();
        } else if (timeElapsed >= 3.5) {
            kicker.setPosition(1.0);
        }
    }

    public void stop() {
        launchWheel.setPower(0);
        kicker.setPosition(0.5);
    }
}
