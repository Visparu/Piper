package de.visparu.piper.structures.boards;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import de.visparu.piper.root.Piper;
import de.visparu.piper.structures.pipes.Pipe;
import de.visparu.piper.structures.pipes.Pipe.Direction;

public class Field
{
	
	public static final Color	COLOR_BACKGROUND	= Color.LIGHT_GRAY;
	public static final Color	COLOR_ENTRY_EXIT	= Color.YELLOW;
	public static final Color	COLOR_FIXED			= Color.BLACK;
	public static final Color	COLOR_EDGE			= Color.BLACK;
	public static final Color	COLOR_WON			= new Color(200, 255, 200);
	public static final Color	COLOR_LOSS			= Color.RED;
	
	public static final int SIZE = 50;
	
	private Pipe pipe;
	
	private boolean	entry		= false;
	private boolean	exit		= false;
	private boolean	fixed		= false;
	private boolean	lossField	= false;
	
	@Override
	public String toString()
	{
		Point p = Piper.getBoard().getFieldPosition(this);
		int x = (p == null) ? -1 : p.x;
		int y = (p == null) ? -1 : p.y;
		return String.format("Field: {x: %d, y: %d, Lossfield: %b, %s}", x, y, this.lossField, this.pipe);
	}
	
	public void render(Graphics2D g2d)
	{
		if (Piper.getBoard().hasWon())
		{
			g2d.setColor(Field.COLOR_WON);
		} else if (this.lossField)
		{
			g2d.setColor(Field.COLOR_LOSS);
		} else if (this.entry || this.exit)
		{
			g2d.setColor(Field.COLOR_ENTRY_EXIT);
		} else
		{
			g2d.setColor(Field.COLOR_BACKGROUND);
		}
		if (this.fixed)
		{
			g2d.setColor(g2d.getColor().darker());
		}
		g2d.fillRect(0, 0, Field.SIZE, Field.SIZE);
		if (this.pipe != null)
		{
			this.pipe.render(g2d);
		}
		g2d.setColor(Field.COLOR_EDGE);
		g2d.drawRect(0, 0, Field.SIZE - 1, Field.SIZE - 1);
	}
	
	public void rotatePipe()
	{
		if (this.fixed || this.pipe == null || !this.pipe.getEntryPoints().isEmpty())
		{
			return;
		}
		switch (this.pipe.getDirection())
		{
			case EAST:
			{
				this.pipe.setDirection(Direction.SOUTH);
				break;
			}
			case NORTH:
			{
				this.pipe.setDirection(Direction.EAST);
				break;
			}
			case SOUTH:
			{
				this.pipe.setDirection(Direction.WEST);
				break;
			}
			case WEST:
			{
				this.pipe.setDirection(Direction.NORTH);
				break;
			}
			default:
			{
				throw new IllegalStateException();
			}
		}
	}
	
	public Pipe getPipe()
	{
		return this.pipe;
	}
	
	public void setPipe(Pipe pipe)
	{
		this.pipe = pipe;
	}
	
	public boolean isEntry()
	{
		return this.entry;
	}
	
	public void setEntry(boolean entry)
	{
		this.entry = entry;
	}
	
	public boolean isExit()
	{
		return this.exit;
	}
	
	public void setExit(boolean exit)
	{
		this.exit = exit;
	}
	
	public boolean isFixed()
	{
		return this.fixed;
	}
	
	public void setFixed(boolean fixed)
	{
		this.fixed = fixed;
	}
	
	public boolean isLossField()
	{
		return this.lossField;
	}
	
	public void setLossField(boolean lossField)
	{
		this.lossField = lossField;
	}
	
}
