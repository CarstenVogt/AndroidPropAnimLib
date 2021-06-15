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

/**
 * Created by vogt on 30.01.2018.
 */

/** This class is used to specify borders that shall be drawn around object of class
 * AnimatedGuiObjectCV. Works currently only for circles, squares, and rectangles that do not rotate.
 */

public class DependentBorderCV extends DependentGuiObjectCV {

    /** Object around which the border shall be drawn */

    private AnimatedGuiObjectCV guiobj;

    /** Width of the border, i.e. number of pixels between the outer limit of the object
     * and the outer limit of the border. */

    private int borderwidth;

    /** Paint specifying the color of the border */

    private Paint paint;

    /** Constructor to initialize the attributes and to register the newly created object
     * with the associated AnimatedGuiObjectCV object */

    public DependentBorderCV(AnimatedGuiObjectCV guiobj, int borderwidth, int color) {
        this.guiobj = guiobj;
        this.paint = new Paint();
        paint.setColor(color);
        this.borderwidth = borderwidth;
        guiobj.addDependentGuiObject(this);
        background = false;
    }

    /** Method to draw a border around guiobj.
     * This method will be called automatically by the draw() methods of all AnimatedGuiObjectsCV
     * where this object is registered.<BR>
     * Currently, only borders around objects of type TYPE_DRAWABLE_CIRCLE, TYPE_DRAWABLE_SQUARE or TYPE_DRAWABLE_RECT will be drawn.
     *
     * @param canvas Canvas to draw on
     */

    @Override
    public void draw(Canvas canvas) {
        switch (guiobj.getType()) {
            case AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE:
            case AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT:
                int left = guiobj.getLeftBound()- borderwidth;
                int top = guiobj.getTopBound()- borderwidth;
                int right = guiobj.getLeftBound()+guiobj.getWidth()+ borderwidth;
                int bottom = guiobj.getTopBound()+guiobj.getHeight()+ borderwidth;
                canvas.drawRoundRect(left,top,right,bottom, borderwidth /2, borderwidth /2,paint);
                break;
            case AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE:
                canvas.drawCircle(guiobj.getCenterX(),guiobj.getCenterY(),guiobj.getWidth()/2+ borderwidth,paint);
                break;
        }
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