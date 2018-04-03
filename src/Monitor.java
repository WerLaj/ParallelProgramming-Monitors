import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Monitor {
	private int numOfKnights;
	private enum States { sleeping, drinking, telling};
	private States [] knightsStates;
	final Condition [] drinkConditions;
	final Condition [] tellConditions;
	final Condition emptyBottle;
	final Condition [] noCucumbers;
	final Lock lock;
	private Cucumbers [] cucumbers;
	private boolean isKingTelling;
	
	Monitor(int num)
	{
		this.numOfKnights = num;
		knightsStates = new States[numOfKnights];
		lock = new ReentrantLock();
		drinkConditions = new Condition[numOfKnights];
		tellConditions = new Condition[numOfKnights];
		emptyBottle = lock.newCondition();	
		noCucumbers = new Condition[numOfKnights/2];
		cucumbers = new Cucumbers[numOfKnights / 2];
		isKingTelling = false;
		for ( int i = 0; i < numOfKnights; i++)
		{
			knightsStates[i] = States.sleeping;
			drinkConditions[i] = lock.newCondition();
			tellConditions[i] = lock.newCondition();
			if(i < numOfKnights/2)
			{
				cucumbers[i] = new Cucumbers(Program.cucumbersOnPlate);
				noCucumbers[i] = lock.newCondition();
			}
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
				if(Program.cupsInBottle == 0)
				{
					System.out.println("Knight " + i + " can't drink - bottle empty");
					try {
						emptyBottle.await();
					} catch (InterruptedException e) {e.printStackTrace();}
				}
				System.out.println("Knight " + i + " drinks");
				Program.knights[i].cupsOfWine++;
				cucumbers[i/2].eatCucumber();
				System.out.println(cucumbers[i/2].cucumbersAvailable + " cucumbers left");
				knightsStates[i] = States.drinking;
			}
			else 
			{
				System.out.println("Knight " + i + " waits because at least one neightbour is drinking or there are no cucumbers");
				//knightsStates[i] = States.drinking;//??????????
				try
				{
					drinkConditions[i].await();			
				} catch (InterruptedException e) {e.printStackTrace();}
				if(cucumbers[i/2].areAvailable())
				{
					System.out.println("Knight " + i + " can't drink - no cucumbers");
					try {
						noCucumbers[i/2].await();
					} catch (InterruptedException e) {e.printStackTrace();}
				}
				if(Program.cupsInBottle == 0)
				{
					System.out.println("Knight " + i + " can't drink - bottle empty");
					try {
						emptyBottle.await();
					} catch (InterruptedException e) {e.printStackTrace();}
				}
				System.out.println("Knight " + i + " drinks");
				Program.knights[i].cupsOfWine++;
				knightsStates[i] = States.drinking;
				cucumbers[i/2].eatCucumber();
				System.out.println(cucumbers[i/2].cucumbersAvailable + " cucumbers left");		
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
			//knightsStates[i] = States.telling; //????????????
			int left = (i - 1 + numOfKnights) % numOfKnights;
			int leftleft = (i - 2 + numOfKnights) % numOfKnights;
			if(knightsStates[left] == States.sleeping 
					&& knightsStates[leftleft] != States.drinking)
			{
				drinkConditions[left].signal();
			}
			
			if( (knightsStates[(i+1)%numOfKnights] == States.sleeping) 
					&& (knightsStates[(i+2)%numOfKnights] != States.drinking) )
			{
				drinkConditions[(i + 1) % numOfKnights].signal();
			}
		}
		finally {
			lock.unlock();
		}
	}
	
	public void fillBottle()
	{		
		lock.lock();
		try
		{
			System.out.println("---Waiter fills the bottle");
			Program.cupsInBottle = 3;
			emptyBottle.signal();
		}
		finally 
		{
			lock.unlock();
		}
	}
	
	public void fillPlate()
	{		
		lock.lock();
		try
		{
			System.out.println("---Waiter fills all plates");
			for (int i = 0; i < numOfKnights/2; i++)
			{
				cucumbers[i].fillPlate();
				noCucumbers[i].signal();
			}
		}
		finally 
		{
			lock.unlock();
		}
	}
	
	public void StartTelling(int i) 
	{
		lock.lock();
		try
		{
			if(Program.knights[i].isKing == true)
			{
				System.out.println("KING " + i + " tells");
				knightsStates[i] = States.telling;
				isKingTelling = true;
			}
			else if(knightsStates[(i - 1 + numOfKnights) % numOfKnights] != States.telling 
					&& knightsStates[(i+1)%numOfKnights] != States.telling
					&& isKingTelling == false)
			{
				System.out.println("Knight " + i + " tells");
				knightsStates[i] = States.telling;
			}
			else
			{
				System.out.println("Knight " + i + " waits because at least one neightbour is telling");
				//knightsStates[i] = States.telling;//??????????
				try {
					tellConditions[i].await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Knight " + i + " tells");;
				knightsStates[i] = States.telling;	
			}
		}
		finally 
		{
			lock.unlock();
		}
	}
	
	public void StopTelling(int i)
	{
		lock.lock();
		try
		{
			System.out.println("Knight " + i + " stops telling");
			knightsStates[i] = States.sleeping; 
			int left = (i - 1 + numOfKnights) % numOfKnights;
			int leftleft = (i - 2 + numOfKnights) % numOfKnights;
			if(knightsStates[left] == States.drinking 
					&& knightsStates[leftleft] != States.telling)
			{
				tellConditions[left].signal();
			}
			
			if( (knightsStates[(i+1)%numOfKnights] == States.drinking) 
					&& (knightsStates[(i+2)%numOfKnights] != States.telling) )
			{
				tellConditions[(i + 1) % numOfKnights].signal();
			}
		}
		finally 
		{
			lock.unlock();
		}
	}

}
