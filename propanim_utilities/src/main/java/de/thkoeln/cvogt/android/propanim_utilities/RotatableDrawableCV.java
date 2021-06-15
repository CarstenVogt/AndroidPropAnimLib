/* This work is provided under GPLv3, the GNU General Public License 3
   http://www.gnu.org/licenses/gpl-3.0.html */

/**
 * Reference: https://github.com/CarstenVogt/AndroidPropAnimLib
 *
 * The app demonstrates the functionality and the usage of the
 * package de.thkoeln.cvogt.android.propanim_utilities
 * which provides utility classes for the Android property animation technique.
 * For details, please watch the introductory videos (see reference)
 * and read the comments for the classes AnimatedGuiObjectCV and AnimationViewCV.

 Prof. Dr. Carsten Vogt
 Technische Hochschule Köln, Germany
 Fakultät für Informations-, Medien- und Elektrotechnik
 carsten.vogt@th-koeln.de
 15.6.2021

 */

package de.thkoeln.cvogt.android.propanim_utilities;

/**
 * Created by vogt on 04.01.2018.
 */

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

/** Class for Drawable objects that can be rotated */

public class RotatableDrawableCV extends LayerDrawable {

    /** Current rotation angle (in radians) */

    private float rotationAngle;

    /** Drawable object to be rotated */

    private Drawable drawable;

    /** Set the rotation angle (in radians) */

    public void setRotationAngle(float rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    /** Get the rotation angle (in radians) */

    public float getRotationAngle() {
        return rotationAngle;
    }

    /** Get the Drawable object */

    public Drawable getDrawable() {
        return drawable;
    }

    /** Constructor
     *
     * @param drawable Drawable object to be rotated
     * @param rotationAngle Rotation angle (in radians)
     */

    public RotatableDrawableCV(Drawable drawable, double rotationAngle) {
        super(wrapDrawable(drawable));
        this.drawable = drawable;
        this.rotationAngle = (float) rotationAngle;
    }

    /** Check if a given point is within the enclosing rotated rectangle of the Drawable or View object
     *
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @return The test result
     */

    public boolean contains(int x, int y) {
        Rect enclosingRect = drawable.getBounds();
        if (rotationAngle==0)
            return enclosingRect.contains(x,y);
        Point pt = rotateAroundCenter(new Point(x,y),false);
        return enclosingRect.contains(pt.x,pt.y);
    }

    /** Get the four corners of the embedded Drawable or View object (i.e. of its enclosing rectangle),
     * taking into account the rotation.
     *
     * @return Array with exactly four elements of class Point, specifying the four corners
     * (in clockwise order, starting with the left top corner)
     */

    public Point[] getCorners() {
        Rect enclosingRect = drawable.getBounds();
        Point[] result = new Point[4];
        result[0] = rotateAroundCenter(new Point(enclosingRect.left,enclosingRect.top),true);
        result[1] = rotateAroundCenter(new Point(enclosingRect.right,enclosingRect.top),true);
        result[2] = rotateAroundCenter(new Point(enclosingRect.right,enclosingRect.bottom),true);
        result[3] = rotateAroundCenter(new Point(enclosingRect.left,enclosingRect.bottom),true);
        return result;
    }

    /** Auxiliary method to wrap a Drawable object in an array, as needed by the constructor of superclass LayerDrawable */

    private static Drawable[] wrapDrawable(Drawable drawable) {
        Drawable[] array = { drawable };
        return array;
    }

    /** Auxiliary method to rotate a Point around the center of the drawable object by the rotationAngle
     *
     * @param p The point to rotate
     * @param inAngleDirection If set to false, the rotation will be by the amount of the rotationAngle but in the opposite direction
     * */

    public Point rotateAroundCenter(Point p, boolean inAngleDirection) {
        float angle;
        if (inAngleDirection)
            angle = rotationAngle;
        else angle = -rotationAngle;
        Rect enclosingRect = drawable.getBounds();
        return GraphicsUtilsCV.rotate(p.x,p.y,enclosingRect.centerX(),enclosingRect.centerY(),angle);
    }

    /** Draw the Drawable object with the specified rotation angle */

    @Override
    public void draw(final Canvas canvas) {
            canvas.save();
            Rect r = drawable.getBounds();
            canvas.rotate((float)(rotationAngle*180/ Math.PI), r.centerX(), r.centerY());
            drawable.draw(canvas);
            canvas.restore();
    }

}
