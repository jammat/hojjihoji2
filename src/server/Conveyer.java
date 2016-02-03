package server;

public class Conveyer extends Thread{
	
	protected boolean running;
	protected final int transferAmount = 2;
	protected final int waitTime = 10;
	
	public Conveyer(){
		
	}
	
	public void setRunning (boolean r){
		running=r;
	}
	public boolean isRunning(){
		return running;
	}
	public void setLimit(int l){
		
	}
}
