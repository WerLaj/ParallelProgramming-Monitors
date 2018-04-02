
public class Program {
	public static int cupsInBottle = 10;
	
	public static void main(String[] args)
    {
        int numOfKnights = 6;
        int iterations = 10;
        
        Monitor monitor = new Monitor(numOfKnights);
        Knight[] knights = new Knight[numOfKnights];
        
        System.out.println("Let us begin");
        System.out.println("");
        
        for (int i = 0; i < numOfKnights; i++)
        {
        	knights[i] = new Knight(i, monitor/*, iterations*/);
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
