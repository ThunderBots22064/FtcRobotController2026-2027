package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

// Controls intake
public class Intake {
    private DcMotor intakeWheel;

    private double startTime = -1;

    public Launcher(HardwareMap hardwareMap) {
        intakeWheel = hardwareMap.get(DcMotor.class, "intakeWheel");

        intakeWheel.setDirection(DcMotorSimple.Direction.FORWARD);

        intakeWheel.setPower(0);
    }

    public void reverseLauncher() {
        DcMotorSimple.Direction currentDir = intakeWheel.getDirection();
        DcMotorSimple.Direction newDir = (currentDir == DcMotorSimple.Direction.FORWARD) ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;
        intakeWheel.setDirection(newDir);
    }

    public void launch(double time, double power) {
        if (startTime == -1) {
            startTime = time;

            power = Range.clip(power, 0.0, 1.0);

            intakeWheel.setPower(power);
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
        } 
    }

    public void stop() {
        intakeWheel.setPower(0);
    }
}
