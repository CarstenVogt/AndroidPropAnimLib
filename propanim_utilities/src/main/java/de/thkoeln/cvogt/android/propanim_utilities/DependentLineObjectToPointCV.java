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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by vogt on 30.01.2018.
 */

/** This class is used to specify lines that shall be drawn between two objects of class
 * AnimatedGuiObjectCV. An object of class DependendGuiObjectCV can be registered with exactly
 * two AnimatedGuiObjectCV objects. After this registration, the endpoints of the line
 * will always coincide with the centers of the two objects, i.e. the line will always
 * graphically connect the objects.
 */

public class DependentLineObjectToPointCV extends DependentGuiObjectCV {

    /** Animated object that specifies one endpoint of the line */

    private AnimatedGuiObjectCV guiobj;

    /** Fixed point that specifies the other endpoint of the line */

    private Point point;

    /** Paint for drawing the line */

    private Paint paint;

    /** Constructor to initialize the attributes and to register the newly created object
     * with the AnimatedGuiObjectCV object that is one endpoint of the new line */

    public DependentLineObjectToPointCV(AnimatedGuiObjectCV guiobj, Point point, Paint paint) {
        this.guiobj = guiobj;
        this.point = point;
        this.paint = paint;
        guiobj.addDependentGuiObject(this);
        background = true;
    }

    /** Method to draw a line between the current center of guiobj and the fixed point.
     * This method will be called automatically by the draw() methods of all AnimatedGuiObjectsCV
     * where this object is registered.
     *
     * @param canvas Canvas to draw on
     */

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(guiobj.getCenterX(),guiobj.getCenterY(),point.x,point.y,paint);
    }

    /** Method to deregister the object from guiobj.
     */

    @Override
    public void deregister() {
        if (guiobj!=null) {
            AnimatedGuiObjectCV g = guiobj;
            guiobj = null;
            g.removeDependentGuiObject(this);
        }
    }

    /** Method called from the removeDependentGuiObject() of the AnimatedGuiObjectCV
     * where this object was registered. Will do nothing because there is no other
     * object of class AnimatedGuiObjectCV involved.
     * @param guiobj The object from where this method is called.
     */

    @Override
    public void onDeregisteredFromGuiObject(AnimatedGuiObjectCV guiobj) {
    }

}