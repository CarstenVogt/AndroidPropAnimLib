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
 * Created by vogt on 30.01.2018.
 */

import android.graphics.Canvas;

/** This class is used to specify objects that can be displayed on the screen
 * such that their visual display depends on other objects that are of class AnimatedGuiObjectCV.
 * This means that an object of class DependentGuiObjectCV cannot be animated on its own
 * but only in relation to the object(s) it depends on. To specify dependencies, an object of
 * class DependentGuiObjectCV can be registered with one or more objects of class AnimatedGuiObjectCV.
 * In an animation, the draw() method of the AnimatedGuiObjectCV object will then call automatically
 * the draw() method of the registered DependentGuiObjectCV object.<BR>
 * A typical example is a line connecting two objects. Here, the endpoints of the line have to be modified
 * in accordance with the actual positions of the objects (see subclasses LineBetweenObjects and
 * LineBetweenObjectAndPoint).<BR>
 * NB: Does currently not work fully for all types of objects, e.g. not fully for rotatable drawable.
 */

public abstract class DependentGuiObjectCV {

    /** Specifies whether the dependent object shall be drawn in the background
     * (e.g. lines connecting the centers of two GUI objects)
     * or on the same z level with the GUI object it is associated with
     * (e.g borders around a GUI object).
     */

    boolean background;

    /** Method to draw the object - to be implemented in subclasses.
     * This method will be called automatically by the draw() methods
     * of objects of class AnimatedGuiObjectCV where this object is registered.
     *
     * @param canvas Canvas to draw on
     */

    abstract public void draw(Canvas canvas);

    /** Method to deregister the object from all AnimatedGuiObjectCV objects
     * where it is currently registered.
     */

    abstract public void deregister();

    /** Method called from the removeDependentGuiObject() of an object of class
     * AnimatedGuiObjectCV. Can e.g. be used to deregister this DependentGuiObjectCV object
     * from other AnimatedGuiObjectCV objects.
     * @param guiobj The object from where this method is called.
     */

    abstract public void onDeregisteredFromGuiObject(AnimatedGuiObjectCV guiobj);

}