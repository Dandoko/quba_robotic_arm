#include <Wire.h>
#include <AccelStepper.h>
#include <Adafruit_MotorShield.h>
#include <Servo.h>

Adafruit_MotorShield shieldBottom(0x60);
Adafruit_MotorShield shieldTop(0x61);

Adafruit_StepperMotor *sm1 = shieldBottom.getStepper(200, 1);
Adafruit_StepperMotor *sm2 = shieldBottom.getStepper(200, 2);
Adafruit_StepperMotor *sm3 = shieldTop.getStepper(200, 1);

Servo servo;

int pos;

void forwardstep1() {  
  sm1->onestep(FORWARD, MICROSTEP);
}
void backwardstep1() {  
  sm1->onestep(BACKWARD, MICROSTEP);
}

void forwardstep2() {  
  sm2->onestep(FORWARD, MICROSTEP);
}
void backwardstep2() {  
  sm2->onestep(BACKWARD, MICROSTEP);
}

void forwardstep3() {  
  sm3->onestep(FORWARD, MICROSTEP);
}
void backwardstep3() {  
  sm3->onestep(BACKWARD, MICROSTEP);
}

AccelStepper ctr1(forwardstep1, backwardstep1);
AccelStepper ctr2(forwardstep2, backwardstep2);
AccelStepper ctr3(forwardstep3, backwardstep3);

void setup()
{  
  shieldBottom.begin();
  shieldTop.begin();
   
  ctr1.setMaxSpeed(300.0);
  ctr1.setAcceleration(150.0);
    
  ctr2.setMaxSpeed(300.0);
  ctr2.setAcceleration(150.0);

  ctr3.setMaxSpeed(300.0);
  ctr3.setAcceleration(100.0);

  servo.attach(10);

  Serial.setTimeout(5000);
  Serial.begin(9600);
  while (!Serial) {
    ;
  }
}

void loop()
{
  String input = "";
  input = Serial.readStringUntil('\n');

  if (input.length() != 0) {
    ctr1.moveTo(input.substring(0, 4).toInt());
    ctr2.moveTo(input.substring(4, 8).toInt());
    ctr3.moveTo(input.substring(8, 12).toInt());
  }

  while (ctr1.isRunning() || ctr2.isRunning() || ctr3.isRunning()) {
    ctr1.run();
    ctr2.run();
    ctr3.run();
  }

  if (input.charAt(12) == '1') {
    for (pos = 0; pos <= 30; pos += 1) {
      servo.write(pos);
      delay(15);
    }
  } else if (input.charAt(12) == '2') {
    for (pos = 30; pos >= 0; pos -= 1) {
      servo.write(pos);
      delay(15);
    }
  }
}
