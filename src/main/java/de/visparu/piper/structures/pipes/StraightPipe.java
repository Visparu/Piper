package de.visparu.piper.structures.pipes;

import java.util.HashSet;
import java.util.Set;

import de.visparu.piper.structures.Pipe;

public class StraightPipe extends Pipe
{
	
	public StraightPipe()
	{
		super();
	}
	
	public StraightPipe(Direction direction)
	{
		super(direction);
	}
	
	public StraightPipe(Direction direction, Direction entryPoint)
	{
		super(direction);
		super.addEntryPoint(entryPoint);
	}
	
	@Override
	public Set<Direction> getOpeningDirections()
	{
		Direction d1 = super.getDirection();
		Direction d2;
		switch (d1)
		{
			case EAST:
				d2 = Direction.WEST;
				break;
			case NORTH:
				d2 = Direction.SOUTH;
				break;
			case SOUTH:
				d2 = Direction.NORTH;
				break;
			case WEST:
				d2 = Direction.EAST;
				break;
			default:
				throw new IllegalStateException();
		}
		
		Set<Direction> result = new HashSet<>();
		result.add(d1);
		result.add(d2);
		return result;
	}
	
}
