package de.visparu.piper.structures.pipes;

import java.util.HashSet;
import java.util.Set;

import de.visparu.piper.structures.Pipe;

public class DeadEndPipe extends Pipe
{
	
	@Override
	public Set<Direction> getOpeningDirections()
	{
		Set<Direction> result = new HashSet<>();
		result.add(super.getDirection());
		return result;
	}
	
}
