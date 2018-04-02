import java.util.Random;

public class Knight implements Runnable{
	public int id;
	public Monitor monitor;
	public Thread thread;
	public int iterations;
	
	Knight(int _id, Monitor _m /*, int i*/)
	{
		this.id = _id;
		this.monitor = _m;
		thread = new Thread(this);
		thread.start();
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
		}
	}
	
	public void sleep()
	{
		Random rnd = new Random();
		int time = rnd.nextInt(10);
		System.out.println("Knight " + id + " sleeps for " + time + " seconds");

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
