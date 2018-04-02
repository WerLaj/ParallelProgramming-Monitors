
public class Cucumbers {
	public int cucumbersAvailable;
	
	public Cucumbers(int num)
	{
		cucumbersAvailable = num;
	}
	
	public void eatCucumber()
	{
		cucumbersAvailable--;
	}
	
	public boolean areAvailable()
	{
		if(cucumbersAvailable > 0)
			return true;
		else
			return false;
	}
}
