/**
 * Invader is the abstract superclass of all invaders Stores file
 * img_invaderhit.gif as the hit image for the invaders. Defines a field noting
 * how many points an invader is worth.
 * 
 * @author Kenneth Sheppard
 * @version 12-2-19
 */
public abstract class Invader extends Ship {
    /** value is the point value of the given invader. */
    protected int pointValue;

    /**
     * Create a new Invader object.
     * 
     * @param x
     *            the X position of the Ship
     * @param y
     *            the Y position of the Ship
     */
    public Invader(int x, int y) {
        super(x, y);
        hitImage = getImage("img_invaderhit.gif");
    }
    
    /**
     * Sets the alteration boolean every time its called.
     */
    public abstract void setAlteration();
    
}
