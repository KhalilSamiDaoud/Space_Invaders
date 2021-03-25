import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.sound.sampled.Clip;

/**
 * Base is the class for the user controlled Ship at the base of the screen. Has
 * defined draw method and stores the images for being hit.
 * 
 * @author Kenneth Sheppard
 * @version 12-1-19
 */
public class Base extends Ship {

    private Image notHitImage = getImage("img_base.gif");
    private Image hitImage = getImage("img_basehit.gif");
    private Clip fireAudio = getAudioClip("aud_basefire.wav");
    private int clipPosition;

    /**
     * Create a new Base object.
     * 
     * @param x
     *            the X-position of the Base
     * @param y
     *            the Y position of the Base
     */
    public Base(int x, int y) {
        super(x, y);
        setWidth(notHitImage.getWidth(null));
        setHeight(notHitImage.getHeight(null));
        clipPosition = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
        Graphics2D g2 = (Graphics2D) g;
        if (isHit) {
            g2.drawImage(hitImage, getX(), getY(), null);
        }
        else {
            g2.drawImage(notHitImage, getX(), getY(), null);
        }
    }

    /**
     * controls audio clips.
     * 
     * @param a
     *            the action to be performed
     */
    public void clipActions(String a) {
        switch(a) {
            case "play":
                fireAudio.setFramePosition(clipPosition);
                fireAudio.start();
                break;
            case "pause":
                clipPosition = fireAudio.getFramePosition();
                fireAudio.stop();
                break;
            case "reset":
                clipPosition = 0;
                break;
        }
    }
    
    /**
     * Returns the current position of the clip.
     * 
     * @return clipPosition - the current position of the clip.
     */
    public int getClipPosition() {
        return clipPosition;
    }
}
