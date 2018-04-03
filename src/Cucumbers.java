
public class Cucumbers {
	public int cucumbersAvailable;
	public int maxNumberOfCucumbers;
	
	public Cucumbers(int num)
	{
		cucumbersAvailable = num;
		maxNumberOfCucumbers = num;
	}
	
	public void eatCucumber()
	{
		cucumbersAvailable--;
	}
	
	public void fillPlate()
	{
		cucumbersAvailable = maxNumberOfCucumbers;
	}
	
	public boolean areAvailable()
	{
		if(cucumbersAvailable > 0)
			return true;
		else
			return false;
	}
}
