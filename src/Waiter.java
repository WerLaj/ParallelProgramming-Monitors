import java.util.Random;

public class Waiter implements Runnable {
	
	public Thread thread;
	public Monitor monitor;
	public boolean forCucumbers;
	
	Waiter(Monitor m, boolean c)
	{
		this.thread = new Thread(this);
		this.thread.start();
		this.monitor = m;
		forCucumbers = c;
	}

	@Override
	public void run() {
		while(true)
		{
			Random rnd = new Random();
			int time = rnd.nextInt(10);
			
			try {
				Thread.sleep(time * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(forCucumbers == false)
			{
				monitor.fillBottle();
			}
			else
			{
				monitor.fillPlate();
			}
		}
	}
}
