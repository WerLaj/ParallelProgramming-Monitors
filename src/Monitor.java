import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Monitor {
	private int numOfKnights;
	private enum States { sleeping, drinking, telling};
	private States [] knightsStates;
	final Condition [] conditions;
	final Lock lock;
	private Cucumbers [] cucumbers;
	
	Monitor(int num)
	{
		this.numOfKnights = num;
		knightsStates = new States[numOfKnights];
		conditions = new Condition[numOfKnights];
		lock = new ReentrantLock();	
		cucumbers = new Cucumbers[numOfKnights / 2];
		for ( int i = 0; i < numOfKnights; i++)
		{
			knightsStates[i] = States.sleeping;
			conditions[i] = lock.newCondition();
			if(i < numOfKnights/2)
				cucumbers[i] = new Cucumbers(10);
		}
	}
	
	public void takePlateAndCup(int i)
	{
		lock.lock();
		try 
		{
			if((knightsStates[(i - 1 + numOfKnights) % numOfKnights]  != States.drinking)
					&& (knightsStates[(i + 1) % numOfKnights] != States.drinking)
					&& cucumbers[i/2].areAvailable())
			{
				System.out.println("Knight " + i + " drinks");
				cucumbers[i/2].eatCucumber();
				System.out.println(cucumbers[i/2].cucumbersAvailable + " cucumbers left");
				knightsStates[i] = States.drinking;
			}
			else
			{
				try
				{
					System.out.println("Knight " + i + " waits because at least one neightbour is drinking or there are no cucumbers");
					conditions[i].await();
					System.out.println("Knight " + i + " drinks");
					cucumbers[i/2].eatCucumber();
					System.out.println(cucumbers[i/2].cucumbersAvailable + " cucumbers left");
					knightsStates[i] = States.drinking;					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		finally {
			lock.unlock();
		}
	}
	
	public void PutDownPlateAndCup(int i)
	{
		lock.lock();
		try
		{
			System.out.println("Knight "+i+" puts down plate and cup");
			knightsStates[i] = States.telling; 
			int left = (i - 1 + numOfKnights) % numOfKnights;
			int leftleft = (i - 2 + numOfKnights) % numOfKnights;
			if(knightsStates[left] == States.sleeping 
					&& knightsStates[leftleft] != States.drinking)
			{
				conditions[left].signal();
			}
			
			if( (knightsStates[(i+1)%numOfKnights] == States.sleeping) 
					&& (knightsStates[(i+2)%numOfKnights] != States.drinking) )
			{
				conditions[(i + 1) % numOfKnights].signal();
			}
		}
		finally {
			lock.unlock();
		}
	}

}
