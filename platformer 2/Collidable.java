import java.awt.geom.Rectangle2D;

public interface Collidable
{
	public default boolean isColliding(Collidable other)
	{
		return getBounds().intersects(other.getBounds());
	}
	
	public Rectangle2D getBounds();
}