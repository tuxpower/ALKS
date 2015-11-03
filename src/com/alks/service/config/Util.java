package com.alks.service.config;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Util {

	
	
	public static void runRotateKeys(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
			    System.out.println("Rotating keys...");
			  }
			}, 2*60*1000, 2*60*1000); // (delay,period) 7*24*60*60*1000 For 7 days
	}
	
	public static void startNewThread(){
		ExecutorService service = Executors.newSingleThreadExecutor();

		try {
		    Runnable r = new Runnable() {
		        @Override
		        public void run() {
		            // Database task
		        }
		    };

		    Future<?> f = service.submit(r);

		    f.get(2, TimeUnit.MINUTES);     // attempt the task for two minutes
		}
		catch (final InterruptedException e) {
		    // The thread was interrupted during sleep, wait or join
		}
		catch (final TimeoutException e) {
		    // Took too long!
		}
		catch (final ExecutionException e) {
		    // An exception from within the Runnable task
		}
		finally {
		    service.shutdown();
		}
	}
	
}
