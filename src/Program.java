import java.util.Random;

public class Program {
	public static int cupsInBottle = 3;
	public static int cucumbersOnPlate = 3;
	public static Monitor monitor;
    public static Knight[] knights;
    public static Waiter cucumberWaiter;
    public static Waiter wineWaiter;
	
	public static void main(String[] args)
    {
        int numOfKnights = 4;
        //int iterations = 10;
        
        Random rnd = new Random();
		int king = rnd.nextInt(4);
        
        monitor = new Monitor(numOfKnights);
        knights = new Knight[numOfKnights];
        
        System.out.println("Let us begin");
        System.out.println("");
        
        wineWaiter = new Waiter(monitor, false);
        cucumberWaiter = new Waiter(monitor, true);
        
        for (int i = 0; i < numOfKnights; i++)
        {
        	if(i == king)
        		knights[i] = new Knight(i, monitor/*, iterations*/, true);
        	else
        		knights[i] = new Knight(i, monitor/*, iterations*/, false);
        }
        
        for (int i = 0; i < numOfKnights; i++)
        {
        	try
        	{
        		knights[i].thread.join();
        	}
        	catch (InterruptedException e)
        	{
        		e.printStackTrace();
        	}
        }
        
        System.out.println("");
		System.out.println("Dinner is over!");
    }
}
