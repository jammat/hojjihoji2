package server;

public class Pump extends Thread{
	
	protected boolean running;
	
	public Pump(){
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
