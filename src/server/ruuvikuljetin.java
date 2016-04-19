package server;

public class ruuvikuljetin extends Thread{
	
	protected boolean running;
	protected final int transferAmount = 2;
	protected final int waitTime = 10;
	
	public ruuvikuljetin(){
		
	}
	
	public void setRunning (boolean r){  // asetetaan kaynnistymaan
		running=r;
	}
	public boolean isRunning(){  // onko kaynnissa
		return running;
	}
	public void setLimit(int l){  // asetetaan raja.
		
	}
}
