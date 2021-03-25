import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.sound.sampled.Clip;

/**
 * The mystery ship is an invader that crosses the top of the screen and never
 * descends.
 * 
 * It travels either from left to right or from right to left, and could be
 * worth 50, 100, 150, or 300 points. The direction and points it is worth
 * should be randomly selected when each ship is created. Differently from other
 * invaders, this ship does not have an alternating image, and should
 * continuously play a sound when traveling across the screen. The image and
 * audio clip for the mystery ship are "img_mystery.gif" and "aud_mystery.wav",
 * respectively. Note that if a mystery ship is flying across the screen and the
 * game is paused then its sound should stop and should resume when (and if) the
 * game resumes. No more than one mystery ship can exist at the same time.
 * 
 * @author Kenneth Sheppard
 * @version 12-2-19
 */
public class Mystery extends Invader {

    private Image image = getImage("img_mystery.gif");
    private Clip clip = getAudioClip("aud_mystery.wav");
    private int clipPosition;

    /**
     * Create a new Mystery object.
     * 
     * @param x
     *            the X position of the Ship
     * @param y
     *            the Y position of the Ship
     */
    public Mystery(int x, int y) {
        super(x, y);
        clipPosition = 0;
        setWidth(image.getWidth(null));
        setHeight(image.getHeight(null));

        switch ((int) (Math.random() * 4)) {
            case 0:
                pointValue = 50;
                break;
            case 1:
                pointValue = 100;
                break;
            case 2:
                pointValue = 150;
                break;
            case 3:
                pointValue = 300;
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (isHit) {
            setWidth(0);
            setHeight(0);
            g2.drawImage(hitImage, getX(), getY(), null);
        }
        else {
            g2.drawImage(image, getX(), getY(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAlteration() {
        // will do nothing here
    }

    /**
     * controls audio clips.
     * 
     * @param a
     *            the action to be performed
     */
    public void clipActions(String a) {
        switch (a) {
            case "play":
                clip.setFramePosition(clipPosition);
                clip.start();
                break;
            case "pause":
                clipPosition = clip.getFramePosition();
                clip.stop();
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
