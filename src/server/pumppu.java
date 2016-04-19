package server;

public class pumppu extends Thread{
	
	protected boolean running;
	
	public pumppu(){
		running=false;
	}
	
	public void runPump(){  // kaynnistetaan pumppu
		running=true;
	}
	public void stopPump(){  // pysaytetaan pumppu
		running=false;
	}
	
	public boolean isRunning(){  // onko kaynnissa
		return running;
	}
}
