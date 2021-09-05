package de.visparu.piper.structures.pipes;

import java.util.HashSet;
import java.util.Set;

import de.visparu.piper.structures.Pipe;

public class CrossPipe extends Pipe
{
	
	@Override
	public Set<Direction> getOpeningDirections()
	{
		Set<Direction> result = new HashSet<>();
		result.add(Direction.NORTH);
		result.add(Direction.EAST);
		result.add(Direction.SOUTH);
		result.add(Direction.WEST);
		return result;
	}
	
}
