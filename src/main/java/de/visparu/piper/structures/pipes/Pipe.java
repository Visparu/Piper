package de.visparu.piper.structures.pipes;

import de.visparu.piper.structures.boards.Field;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

public abstract class Pipe
{
	
	public static final Color	COLOR_PIPE_EDGE	= Color.DARK_GRAY;
	public static final Color	COLOR_PIPE		= Color.GRAY;
	public static final Color	COLOR_FILL		= Color.GREEN;
	
	public static final int		PIPE_WIDTH				= 10;
	public static final int		SIZE_SMALL				= Pipe.PIPE_WIDTH;
	public static final int		SIZE_MEDIUM				= (int) (Field.SIZE / 2.0F - Pipe.PIPE_WIDTH / 2.0F);
	public static final int		SIZE_LARGE				= (int) (Field.SIZE / 2.0F + Pipe.PIPE_WIDTH / 2.0F);
	public static final float	MAX_PROGRESS_SEGMENT	= Pipe.MAX_PROGRESS / 3;
	
	public static final float	MIN_PROGRESS	= 0.0F;
	public static final float	MAX_PROGRESS	= 100.0F;
	
	public enum Direction
	{
		NORTH, EAST, SOUTH, WEST
	}
	
	public enum Rotation
	{
		CLOCKWISE, COUNTER_CLOCKWISE
	}
	
	private Direction		direction;
	private Set<Direction>	entryPoints	= new HashSet<>();
	private float			progress;
	
	public Pipe()
	{
		this(Direction.NORTH);
	}
	
	public Pipe(Direction direction)
	{
		this(direction, 0.0F);
	}
	
	public Pipe(Direction direction, float progress)
	{
		this.direction = direction;
		this.progress = progress;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb1 = new StringBuilder();
		for (Direction d : this.getOpeningDirections())
		{
			sb1.append(", ");
			sb1.append(d);
		}
		String openings = "";
		if (sb1.length() >= 2)
		{
			openings = sb1.substring(2);
		}
		
		StringBuilder sb2 = new StringBuilder();
		for (Direction d : this.entryPoints)
		{
			sb2.append(", ");
			sb2.append(d);
		}
		String entryPoints = "";
		if (sb2.length() >= 2)
		{
			entryPoints = sb2.substring(2);
		}
		
		return String.format("%s: {Direction: %s, Progress: %s, Openings: {%s}, Entrypoints: {%s}",
				this.getClass().getSimpleName(), this.direction, this.progress, openings, entryPoints);
	}
	
	public void render(Graphics2D g2d)
	{
		this.render_background(g2d);
		this.render_fill(g2d);
		this.render_edges(g2d);
	}
	
	private void render_background(Graphics2D g2d)
	{
		Set<Direction> directions = this.getOpeningDirections();
		for (Direction d : Direction.values())
		{
			if (directions.contains(d))
			{
				int x, y, w, h;
				switch (d)
				{
					case EAST:
					{
						x = Pipe.SIZE_LARGE;
						y = Pipe.SIZE_MEDIUM;
						w = Pipe.SIZE_MEDIUM;
						h = Pipe.SIZE_SMALL;
						break;
					}
					case NORTH:
					{
						x = Pipe.SIZE_MEDIUM;
						y = 0;
						w = Pipe.SIZE_SMALL;
						h = Pipe.SIZE_MEDIUM;
						break;
					}
					case SOUTH:
					{
						x = Pipe.SIZE_MEDIUM;
						y = Pipe.SIZE_LARGE;
						w = Pipe.SIZE_SMALL;
						h = Pipe.SIZE_MEDIUM;
						break;
					}
					case WEST:
					{
						x = 0;
						y = Pipe.SIZE_MEDIUM;
						w = Pipe.SIZE_MEDIUM;
						h = Pipe.SIZE_SMALL;
						break;
					}
					default:
					{
						throw new IllegalStateException();
					}
				}
				
				g2d.setColor(Pipe.COLOR_PIPE);
				g2d.fillRect(x, y, w, h);
			}
		}
		int x = Pipe.SIZE_MEDIUM;
		int y = Pipe.SIZE_MEDIUM;
		int w = Pipe.SIZE_SMALL;
		int h = Pipe.SIZE_SMALL;
		g2d.setColor(Pipe.COLOR_PIPE);
		g2d.fillRect(x, y, w, h);
	}
	
	private void render_fill(Graphics2D g2d)
	{
		this.render_fill_segment1(g2d);
		this.render_fill_segment2(g2d);
		this.render_fill_segment3(g2d);
	}
	
	private void render_fill_segment1(Graphics2D g2d)
	{
		float segmentProgress = Math.max(Math.min(this.progress / Pipe.MAX_PROGRESS_SEGMENT, 1.0F), 0.0F);
		for (Direction d : this.entryPoints)
		{
			int x, y, w, h;
			switch (d)
			{
				case EAST:
				{
					int xs = Pipe.SIZE_LARGE;
					int ys = Pipe.SIZE_MEDIUM;
					int ws = Pipe.SIZE_MEDIUM;
					int hs = Pipe.SIZE_SMALL;
					
					w = (int) (ws * segmentProgress);
					h = hs;
					x = xs + ws - w;
					y = ys;
					break;
				}
				case NORTH:
				{
					int xs = Pipe.SIZE_MEDIUM;
					int ys = 0;
					int ws = Pipe.SIZE_SMALL;
					int hs = Pipe.SIZE_MEDIUM;
					
					w = ws;
					h = (int) (hs * segmentProgress);
					x = xs;
					y = ys;
					break;
				}
				case SOUTH:
				{
					int xs = Pipe.SIZE_MEDIUM;
					int ys = Pipe.SIZE_LARGE;
					int ws = Pipe.SIZE_SMALL;
					int hs = Pipe.SIZE_MEDIUM;
					
					w = ws;
					h = (int) (hs * segmentProgress);
					x = xs;
					y = ys + hs - h;
					break;
				}
				case WEST:
				{
					int xs = 0;
					int ys = Pipe.SIZE_MEDIUM;
					int ws = Pipe.SIZE_MEDIUM;
					int hs = Pipe.SIZE_SMALL;
					
					w = (int) (ws * segmentProgress);
					h = hs;
					x = xs;
					y = ys;
					break;
				}
				default:
				{
					throw new IllegalStateException();
				}
			}
			
			g2d.setColor(Pipe.COLOR_FILL);
			g2d.fillRect(x, y, w, h);
		}
	}
	
	private void render_fill_segment2(Graphics2D g2d)
	{
		float segmentProgress = Math
				.max(Math.min((this.progress - Pipe.MAX_PROGRESS_SEGMENT) / Pipe.MAX_PROGRESS_SEGMENT, 1.0F), 0.0F);
		int xs = Pipe.SIZE_MEDIUM;
		int ys = Pipe.SIZE_MEDIUM;
		int ws = Pipe.SIZE_SMALL;
		int hs = Pipe.SIZE_SMALL;
		for (Direction d : this.entryPoints)
		{
			int x, y, w, h;
			switch (d)
			{
				case EAST:
				{
					w = (int) (ws * segmentProgress);
					h = hs;
					x = xs + ws - w;
					y = ys;
					break;
				}
				case NORTH:
				{
					w = ws;
					h = (int) (hs * segmentProgress);
					x = xs;
					y = ys;
					break;
				}
				case SOUTH:
				{
					w = ws;
					h = (int) (hs * segmentProgress);
					x = xs;
					y = ys + hs - h;
					break;
				}
				case WEST:
				{
					w = (int) (ws * segmentProgress);
					h = hs;
					x = xs;
					y = ys;
					break;
				}
				default:
				{
					throw new IllegalStateException();
				}
			}
			
			g2d.setColor(Pipe.COLOR_FILL);
			g2d.fillRect(x, y, w, h);
		}
	}
	
	private void render_fill_segment3(Graphics2D g2d)
	{
		int x, y, w, h;
		float segmentProgress = Math
				.max(Math.min((this.progress - 2 * Pipe.MAX_PROGRESS_SEGMENT) / Pipe.MAX_PROGRESS_SEGMENT, 1.0F), 0.0F);
		for (Direction d : this.getOpeningDirections())
		{
			if (this.entryPoints.contains(d))
			{
				continue;
			}
			switch (d)
			{
				case EAST:
				{
					int xs = Pipe.SIZE_LARGE;
					int ys = Pipe.SIZE_MEDIUM;
					int ws = Pipe.SIZE_MEDIUM;
					int hs = Pipe.SIZE_SMALL;
					
					w = (int) (ws * segmentProgress);
					h = hs;
					x = xs;
					y = ys;
					break;
				}
				case NORTH:
				{
					int xs = Pipe.SIZE_MEDIUM;
					int ys = 0;
					int ws = Pipe.SIZE_SMALL;
					int hs = Pipe.SIZE_MEDIUM;
					
					w = ws;
					h = (int) (hs * segmentProgress);
					x = xs;
					y = ys + hs - h;
					break;
				}
				case SOUTH:
				{
					int xs = Pipe.SIZE_MEDIUM;
					int ys = Pipe.SIZE_LARGE;
					int ws = Pipe.SIZE_SMALL;
					int hs = Pipe.SIZE_MEDIUM;
					
					w = ws;
					h = (int) (hs * segmentProgress);
					x = xs;
					y = ys;
					break;
				}
				case WEST:
				{
					int xs = 0;
					int ys = Pipe.SIZE_MEDIUM;
					int ws = Pipe.SIZE_MEDIUM;
					int hs = Pipe.SIZE_SMALL;
					
					w = (int) (ws * segmentProgress);
					h = hs;
					x = xs + ws - w;
					y = ys;
					break;
				}
				default:
				{
					throw new IllegalStateException();
				}
			}
			
			g2d.setColor(Pipe.COLOR_FILL);
			g2d.fillRect(x, y, w, h);
		}
	}
	
	private void render_edges(Graphics2D g2d)
	{
		Set<Direction> directions = this.getOpeningDirections();
		for (Direction d : Direction.values())
		{
			if (directions.contains(d))
			{
				int x, y, w, h;
				switch (d)
				{
					case EAST:
					{
						x = Pipe.SIZE_LARGE;
						y = Pipe.SIZE_MEDIUM;
						w = Pipe.SIZE_MEDIUM;
						h = Pipe.SIZE_SMALL;
						break;
					}
					case NORTH:
					{
						x = Pipe.SIZE_MEDIUM;
						y = 0;
						w = Pipe.SIZE_SMALL;
						h = Pipe.SIZE_MEDIUM;
						break;
					}
					case SOUTH:
					{
						x = Pipe.SIZE_MEDIUM;
						y = Pipe.SIZE_LARGE;
						w = Pipe.SIZE_SMALL;
						h = Pipe.SIZE_MEDIUM;
						break;
					}
					case WEST:
					{
						x = 0;
						y = Pipe.SIZE_MEDIUM;
						w = Pipe.SIZE_MEDIUM;
						h = Pipe.SIZE_SMALL;
						break;
					}
					default:
					{
						throw new IllegalStateException();
					}
				}
				
				g2d.setColor(Pipe.COLOR_PIPE_EDGE);
				g2d.drawRect(x, y, w, h);
			}
		}
		int x = Pipe.SIZE_MEDIUM;
		int y = Pipe.SIZE_MEDIUM;
		int w = Pipe.SIZE_SMALL;
		int h = Pipe.SIZE_SMALL;
		g2d.setColor(Pipe.COLOR_PIPE_EDGE);
		g2d.drawRect(x, y, w, h);
	}
	
	public Direction rotate(Rotation rotation)
	{
		switch (rotation)
		{
			case CLOCKWISE:
			{
				switch (this.direction)
				{
					case EAST:
					{
						this.direction = Direction.SOUTH;
						break;
					}
					case NORTH:
					{
						this.direction = Direction.EAST;
						break;
					}
					case SOUTH:
					{
						this.direction = Direction.WEST;
						break;
					}
					case WEST:
					{
						this.direction = Direction.NORTH;
						break;
					}
					default:
					{
						throw new IllegalStateException();
					}
				}
				break;
			}
			case COUNTER_CLOCKWISE:
			{
				switch (this.direction)
				{
					case EAST:
					{
						this.direction = Direction.NORTH;
						break;
					}
					case NORTH:
					{
						this.direction = Direction.WEST;
						break;
					}
					case SOUTH:
					{
						this.direction = Direction.EAST;
						break;
					}
					case WEST:
					{
						this.direction = Direction.SOUTH;
						break;
					}
					default:
					{
						throw new IllegalStateException();
					}
				}
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}
		return this.direction;
	}
	
	public float increaseProgress(float progress)
	{
		this.progress += progress;
		if (this.progress > Pipe.MAX_PROGRESS)
		{
			float overflow = this.progress - Pipe.MAX_PROGRESS;
			this.progress = Pipe.MAX_PROGRESS;
			return overflow;
		} else
		{
			return 0.0F;
		}
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}
	
	public Set<Direction> getEntryPoints()
	{
		return this.entryPoints;
	}
	
	public void addEntryPoint(Direction entryPoint)
	{
		if (!this.getOpeningDirections().contains(entryPoint))
		{
			throw new IllegalArgumentException();
		}
		this.entryPoints.add(entryPoint);
	}
	
	public float getProgress()
	{
		return progress;
	}
	
	public abstract Set<Direction> getOpeningDirections();
	
}
