package server;

public class pumppu extends Thread{
	
	protected boolean running;
	
	public pumppu(){
		running=false;
	}
	
	public void runPump(){
		running=true;
	}
	public void stopPump(){
		running=false;
	}
	
	public boolean isRunning(){
		return running;
	}
}
