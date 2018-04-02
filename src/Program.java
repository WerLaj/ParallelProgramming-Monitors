
public class Program {
	public static int cupsInBottle = 5;
	public static Monitor monitor;
    public static Knight[] knights;
	
	public static void main(String[] args)
    {
        int numOfKnights = 4;
        //int iterations = 10;
        
        monitor = new Monitor(numOfKnights);
        knights = new Knight[numOfKnights];
        
        System.out.println("Let us begin");
        System.out.println("");
        
        for (int i = 0; i < numOfKnights; i++)
        {
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
