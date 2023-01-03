package com.agaram.eln.primary.timers;

import java.util.TimerTask;

public class ValidateUserLogout  extends TimerTask{
	@SuppressWarnings("unused")
	private final int serial;


	public ValidateUserLogout ( int serial )
    {
      this.serial = serial;
    }

    public void run() {
      //Do stuff
    	System.out.println("reach timer");
    }
}
