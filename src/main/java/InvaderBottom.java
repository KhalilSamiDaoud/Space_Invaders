import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * InvaderBottom is the instance of the abstract Invader for the lowest value
 * Invader
 * 
 * Implements all aspects of Invader
 * 
 * @author Kenneth Sheppard
 * @version 12-2-19
 */
public class InvaderBottom extends Invader {

    private Image imageA = getImage("img_invaderbottomA.gif");
    private Image imageB = getImage("img_invaderbottomB.gif");
    private boolean alternate = false;

    /**
     * Create a new InvaderBottom object.
     * 
     * @param x
     *            the X position of the Ship
     * @param y
     *            the Y position of the Ship
     */
    public InvaderBottom(int x, int y) {
        super(x, y);
        pointValue = 10;
        setWidth(imageA.getWidth(null));
        setHeight(imageA.getHeight(null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (!isHit) {
            if (alternate) {
                g2.drawImage(imageB, getX(), getY(), null);
            }
            else {
                g2.drawImage(imageA, getX(), getY(), null);
            }
        }
        else {
            setWidth(0);
            setHeight(0);
            g2.drawImage(hitImage, getX(), getY(), null);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setAlteration() {
        if (!alternate)
            alternate = true;
        else
            alternate = false;
    }

}
