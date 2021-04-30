package de.thkoeln.cvogt.android.propanim_utilities;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by vogt on 30.01.2018.
 */

/** This class is used to specify lines that shall be drawn between two objects of class
 * AnimatedGuiObjectCV. An object of class DependendLineGuiObjectCV will be registered
 * with these two objects. After this registration, the endpoints of the line will always
 * coincide with the centers of the objects, i.e. the line will always graphically connect
 * the two objects.
 */

public class DependentLineObjectToObjectCV extends DependentGuiObjectCV {

    /** Object that specifies one endpoint of the line */

    private AnimatedGuiObjectCV guiobj1;

    /** Object that specifies the other endpoint of the line */

    private AnimatedGuiObjectCV guiobj2;

    /** Specifies if the line shall always be drawn between the centers of the endpoint objects
     * or to some points on their borders that are calculated from the relative positions of the objects. */

    private boolean lineEndsAreCenters;

    /** Threshold values for the border distances below which the endpoint objects are considered to lie closely together.
     * Become effective if lineEndsAreCenters==false: If the endpoint objects' borders lie farther apart the line will end
     * in the middle of the borders, if they lie closer together their endpoints will lie somewhere between the centers
     * of the borders and the centers of the objects.
     */

    private int thresholdDistanceX, thresholdDistanceY;

    /** Paint for drawing the line */

    private Paint paint;

    /** Constructor to initialize the attributes and to register the newly created object
     * with the two AnimatedGuiObjectCV objects that are the endpoints of the new line.
     * The attribute lineEndsAreCenters is set to true. */

    public DependentLineObjectToObjectCV(AnimatedGuiObjectCV guiobj1, AnimatedGuiObjectCV guiobj2, Paint paint) {
        this(guiobj1,guiobj2,paint,true);
    }

    /** Constructor to initialize the attributes and to register the newly created object
     * with the two AnimatedGuiObjectCV objects that are the endpoints of the new line. */

    public DependentLineObjectToObjectCV(AnimatedGuiObjectCV guiobj1, AnimatedGuiObjectCV guiobj2, Paint paint, boolean lineEndsAreCenters) {
        background = true;
        this.guiobj1 = guiobj1;
        this.guiobj2 = guiobj2;
        this.lineEndsAreCenters = lineEndsAreCenters;
        thresholdDistanceX = (guiobj1.getWidth()+guiobj2.getWidth())/2;
        thresholdDistanceY = (guiobj1.getHeight()+guiobj2.getHeight())/2;
        this.paint = paint;
        guiobj1.addDependentGuiObject(this);
        guiobj2.addDependentGuiObject(this);
    }

    /** Returns the current value of lineEndsAreCenters. */

    public boolean isLineEndsAreCenters() {
        return lineEndsAreCenters;
    }

    public void setThresholdDistanceX(int thresholdDistanceX) {
        this.thresholdDistanceX = thresholdDistanceX;
        if (this.thresholdDistanceX<1) this.thresholdDistanceX = 1;
    }

    public void setThresholdDistanceY(int thresholdDistanceY) {
        this.thresholdDistanceY = thresholdDistanceY;
        if (this.thresholdDistanceY<1) this.thresholdDistanceY = 1;
    }

    public int getThresholdDistanceX() {
        return thresholdDistanceX;
    }

    public int getThresholdDistanceY() {
        return thresholdDistanceY;
    }

    /** Returns the two AnimatedGuiObjectsCV that are the end points of the line. */

    public AnimatedGuiObjectCV[] getEndpoints() {
        AnimatedGuiObjectCV[] result = new AnimatedGuiObjectCV[2];
        result[0] = guiobj1;
        result[1] = guiobj2;
        return result;
    }

    /** Sets the attribute lineTargetIsCenter. */

    public void setLineEndsAreCenters(boolean lineEndsAreCenters) {
        this.lineEndsAreCenters = lineEndsAreCenters;
    }

    /** Method to draw a line between the current centers or borders of guiobj1 and guiobj2.
     * This method will be called automatically by the draw() methods of all AnimatedGuiObjectsCV
     * where this object is registered.
     *
     * @param canvas Canvas to draw on
     */

    public void draw(Canvas canvas) {
        int lineend1X, lineend1Y, lineend2X, lineend2Y;
        if (lineEndsAreCenters) {
            lineend1X = guiobj1.getCenterX();
            lineend1Y = guiobj1.getCenterY();
            lineend2X = guiobj2.getCenterX();
            lineend2Y = guiobj2.getCenterY();
        } else {  // draw the line between the borders that face each others
            // The following two factors determine where the lines will end:
            // factorX = 0, factorY = 1 or -1: In the middle of the upper or lower bounds of the guiobjects
            // factorX = 1 or -1, factorY = 0: In the middle of the left or right bounds of the guiobjects
            // Other values: something in between
            // The factors will be modified continuously (i.e. without disruptions) while the objects are moving.
            double factorX, factorY;
            // The following values will be needed in the calculation of the positions:
            // X and Y positions of the object centers
            int center1X = guiobj1.getCenterX(),
                center1Y = guiobj1.getCenterY(),
                center2X = guiobj2.getCenterX(),
                center2Y = guiobj2.getCenterY(),
            // horizontal and vertical distances of the object borders
                bordDistX = Math.abs(center1X-center2X)-(guiobj1.getWidth()+guiobj2.getWidth())/2,
                bordDistY = Math.abs(center1Y-center2Y)-(guiobj1.getHeight()+guiobj2.getHeight())/2,
            // factors indicating whether guiobj1 lies left from / above of guiobj2
                signX = (int) Math.signum(center2X-center1X),
                signY = (int) Math.signum(center2Y-center1Y);
            // threshold distances: line ends will be moved when border distances fall below these thresholds
            if (bordDistX>thresholdDistanceX) {    // the objects lie horizontally far apart
                factorX = signX;    // -> let the line end in the middle of the left or right borders of the objects
                factorY = 0;
            }
            else if (bordDistX>0) { // the objects lie horizontally closer together but do not overlap
                factorX = signX*(double)bordDistX/thresholdDistanceX;   // -> move the line end gradually towards the middle of the lower or upper bound
                if (bordDistY>thresholdDistanceY)
                    factorY = signY*(1- Math.abs(factorX));
                else if (bordDistY>0) {
                    double a = signY*(double)bordDistY/thresholdDistanceY;
                    double b = signY*(1- Math.abs(factorX));
                    factorY = (Math.abs(a)< Math.abs(b)) ? a : b;
                }
                else
                    factorY = 0;
            }
            else {    // the objects overlap horizontally
                factorX = 0;
                if (Math.abs(bordDistY)>thresholdDistanceY)
                    factorY = signY;
                else
                    factorY = signY*(double)bordDistY/thresholdDistanceY;
            }
            lineend1X = (int) (center1X + factorX * guiobj1.getWidth() / 2);
            lineend2X = (int) (center2X - factorX * guiobj2.getWidth() / 2);
            lineend1Y = (int) (center1Y + factorY * guiobj1.getHeight() / 2);
            lineend2Y = (int) (center2Y - factorY * guiobj2.getHeight() / 2);
        }
        canvas.drawLine(lineend1X,lineend1Y,lineend2X,lineend2Y,paint);
    }

    /* A less compact implementation */
    public void draw2(Canvas canvas) {
        int lineend1X, lineend1Y, lineend2X, lineend2Y;
        if (lineEndsAreCenters) {
            lineend1X = guiobj1.getCenterX();
            lineend1Y = guiobj1.getCenterY();
            lineend2X = guiobj2.getCenterX();
            lineend2Y = guiobj2.getCenterY();
        } else {  // draw the line between the borders that face each others
            // The following two factors determine where the lines will end:
            // factorX = 0, factorY = 1: In the center of the upper or lower bound of the guiobject
            // factorX = 1, factorY = 0: In the center of the left or right bound of the guiobject
            // Other values: something in between
            // The factors will be modified continuously (i.e. without disruptions) while the objects are moving.
            double factorX, factorY;
            AnimatedGuiObjectCV oLeft, oRight;
            if (guiobj1.getCenterX()<guiobj2.getCenterX()) {
                oLeft = guiobj1; oRight = guiobj2; }
            else {
                oLeft = guiobj2; oRight = guiobj1; }
            boolean leftIsAboveRight = oLeft.getCenterY()<oRight.getCenterY();
            if (oLeft.getRightBound()+thresholdDistanceX<oRight.getLeftBound()) {
                factorX = 1;
                factorY = 0;
            }
            else if (oLeft.getRightBound()<oRight.getLeftBound()) {
                factorX = (double) (oRight.getLeftBound()-oLeft.getRightBound())/thresholdDistanceX;
                if (leftIsAboveRight && (oLeft.getBottomBound()+thresholdDistanceY<oRight.getTopBound()) ||
                        !leftIsAboveRight && (oLeft.getTopBound()-thresholdDistanceY>oRight.getBottomBound()))
                    factorY = 1-factorX;
                else if (leftIsAboveRight && (oLeft.getBottomBound()<oRight.getTopBound()) ||
                        !leftIsAboveRight && (oLeft.getTopBound()>oRight.getBottomBound())) {
                    double a;
                    if (leftIsAboveRight)
                        a = (double)(oRight.getTopBound()-oLeft.getBottomBound())/thresholdDistanceY;
                    else
                        a = (double)(oLeft.getTopBound()-oRight.getBottomBound())/thresholdDistanceY;
                    double b = 1-factorX;
                    factorY = (a<b) ? a : b;
                }
                else {
                    // factorX = 1;
                    factorY = 0;
                }
            }
            else {
                factorX = 0;
                if (leftIsAboveRight && oLeft.getBottomBound()+thresholdDistanceY<oRight.getTopBound() ||
                        !leftIsAboveRight && oLeft.getTopBound()-thresholdDistanceY>oRight.getBottomBound())
                    factorY = 1;
                else
                if (leftIsAboveRight)
                    factorY = (double)(oRight.getTopBound()-oLeft.getBottomBound())/thresholdDistanceY;
                else
                    factorY = (double)(oLeft.getTopBound()-oRight.getBottomBound())/thresholdDistanceY;
            }
            if (leftIsAboveRight) {
                lineend1X = (int) (oLeft.getCenterX() + factorX * (oLeft.getRightBound() - oLeft.getCenterX()));
                lineend2X = (int) (oRight.getCenterX() - factorX * (oRight.getCenterX() - oRight.getLeftBound()));
                lineend1Y = (int) (oLeft.getCenterY() + factorY * (oLeft.getBottomBound() - oLeft.getCenterY()));
                lineend2Y = (int) (oRight.getCenterY() - factorY * (oRight.getCenterY() - oRight.getTopBound()));
            }
            else {
                lineend1X = (int) (oLeft.getCenterX() + factorX * (oLeft.getRightBound() - oLeft.getCenterX()));
                lineend2X = (int) (oRight.getCenterX() - factorX * (oRight.getCenterX() - oRight.getLeftBound()));
                lineend1Y = (int) (oLeft.getCenterY() - factorY * (oLeft.getCenterY()-oLeft.getTopBound()));
                lineend2Y = (int) (oRight.getCenterY() + factorY * (oRight.getBottomBound()-oRight.getCenterY()));
            }
        }
        canvas.drawLine(lineend1X,lineend1Y,lineend2X,lineend2Y,paint);
    } /**/

    /** Method to deregister the object from guiobj1 and guiobj2. */

    @Override
    public void deregister() {
        if (guiobj1!=null) {
            AnimatedGuiObjectCV g = guiobj1;
            guiobj1 = null;
            g.removeDependentGuiObject(this);
        }
        if (guiobj2!=null) {
            AnimatedGuiObjectCV g = guiobj2;
            guiobj2 = null;
            g.removeDependentGuiObject(this);
        }
    }

    /** Method called from the removeDependentGuiObject() of an AnimatedGuiObjectCV
     * where this object was registered. Will deregister this object from the other
     * AnimatedGuiObjectCV object as well.
     * @param guiobj The object from where this method is called.
     */

    @Override
    public void onDeregisteredFromGuiObject(AnimatedGuiObjectCV guiobj) {
        if (guiobj == guiobj1 && guiobj2 != null) {
            AnimatedGuiObjectCV g = guiobj2;
            guiobj2 = null;
            g.removeDependentGuiObject(this);
            return;
        }
        if (guiobj == guiobj2 && guiobj1 != null) {
            AnimatedGuiObjectCV g = guiobj1;
            guiobj1 = null;
            g.removeDependentGuiObject(this);
            return;
        }
    }

    /** Method to test if both endpoint objects are visible. Called by methods that shall draw the line.
     * They will not draw the line if one of the endpoints is not visible.
     * @return true if both endpoints are visible, false otherwise.
     */

    public boolean bothEndpointsVisible() {
        return guiobj1.isVisible() && guiobj2.isVisible();
    }

}