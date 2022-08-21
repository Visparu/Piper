package de.visparu.piper.structures.pipes.standard;

import java.util.HashSet;
import java.util.Set;

import de.visparu.piper.structures.Pipe;

public class CurvedPipe extends Pipe
{
	
	@Override
	public Set<Direction> getOpeningDirections()
	{
		Direction d1, d2;
		switch (super.getDirection())
		{
			case EAST:
			{
				d1 = Pipe.Direction.WEST;
				break;
			}
			case NORTH:
			{
				d1 = Pipe.Direction.SOUTH;
				break;
			}
			case SOUTH:
			{
				d1 = Pipe.Direction.NORTH;
				break;
			}
			case WEST:
			{
				d1 = Pipe.Direction.EAST;
				break;
			}
			default:
			{
				throw new IllegalStateException();
			}
		}
		switch (d1)
		{
			case EAST:
			{
				d2 = Direction.SOUTH;
				break;
			}
			case NORTH:
			{
				d2 = Direction.EAST;
				break;
			}
			case SOUTH:
			{
				d2 = Direction.WEST;
				break;
			}
			case WEST:
			{
				d2 = Direction.NORTH;
				break;
			}
			default:
			{
				throw new IllegalStateException();
			}
		}
		
		Set<Direction> result = new HashSet<>();
		result.add(d1);
		result.add(d2);
		return result;
	}
	
}
