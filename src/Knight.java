import java.util.Random;

public class Knight implements Runnable{
	public int id;
	public Monitor monitor;
	public Thread thread;
	public int iterations;
	public boolean isKing;
	
	Knight(int _id, Monitor _m /*, int i*/, boolean _isKing)
	{
		this.id = _id;
		this.monitor = _m;
		this.thread = new Thread(this);
		this.thread.start();
		this.isKing = _isKing;
		//iterations = i;
	}

	@Override
	public void run()
	{
		while(Program.cupsInBottle >= 0)
		{
			sleep();
			
			monitor.takePlateAndCup(id);
			//drink();
			Program.cupsInBottle--;
			monitor.PutDownPlateAndCup(id);
			
			monitor.StartTelling(id);
			tell();
			monitor.StopTelling(id);			
		}
	}
	
	public void sleep()
	{
		Random rnd = new Random();
		int time = rnd.nextInt(5);
		System.out.println("Knight " + id + " sleeps for " + time + " seconds");

		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void tell()
	{
		Random rnd = new Random();
		int time = rnd.nextInt(5);
		System.out.println("Knight " + id + " tells for " + time + " seconds");
		
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**public void drink()
	{
		System.out.println("Knight " + id + " drinks");
	}*/
}
