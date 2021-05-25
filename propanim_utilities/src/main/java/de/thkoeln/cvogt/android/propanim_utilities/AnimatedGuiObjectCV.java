package de.thkoeln.cvogt.android.propanim_utilities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

// TODO: Movement animation with PATH parameter
// TODO: check "borders around icons" > does it work?
// TODO: support scale gestures
// TODO: text ticker in LayerDrawable
// TODO: more shapes for Drawables - RoundRectShape, ArcShape, VectorDrawable
// TODO: make it embeddable for XML layout files
// (> define attributes in res/values/attrs.xml)

// Bibliothek erzeugen:
// In Android Studio links das Projekt "propanim_utilities" markieren.
// Dann Build > Rebuild Project  (Achtung: Build Project funktioniert nicht)
// > Bibliothek entsteht als "build/outputs/aar/propanim_utilities-debug.aar"
// > vor Weitergabe umbenennen in "propanim_utilities_cv.aar"

// alternativ: Export nach GitHub > Erklärung siehe in Datei GithubExportImport.doc

/** Adapter class for animating objects of class Drawable or class View by applying the property animation techniques of Android.
 * An object of this adapter class wraps an object that can be displayed and animated on the screen by a ViewGroup object of class AnimationViewCV.
 * The generic type parameter specifies the type of the embedded object which can be either Drawable or View.
 * The methods specified by this adapter class work on the embedded object.<BR><BR>
 * NB: Up to now, the methods and concept have been thoroughly tested for Drawables only, whereas the inclusion of Views is
 * still somewhat experimental. So most probably not everything will work correctly.
 * A major problem yet to solved is that the programmatic placement of the Views interferes with the layout generation by the runtime system.<BR><BR>
 * The usual way to work with an object of this class is:<BR>
 * 1.) Create an object by calling one of the static create methods (or by calling directly one of the constructors). This will specify the properties of the embedded Drawable or View object.<BR>
 * 2.) Specify and register animators for the object by calling the addXXXAnimator() methods (or alternatively addAnimator() - see (*) below).<BR>
 * 3.) (Optionally) combine these animators to an animator set by calling playSequentially() and playTogether().<BR>
 * 4.) Add the object to a displayingView of class AnimationViewCV through the displayingView's addAnimatedGuiObject() method.<BR>
 * 5.) Display the displayingView on the screen through setContentView().<BR>
 * 6.) Start the animation (i.e. all registered animators) by calling startAnimations() on the displayingView.
 * <BR><BR>
 * (*) Animators can be registered by calling either addAnimator() or a method like addLinearPathAnimator(), addBezierAnimator() etc.
 * addAnimator() has a parameter of type Animator through which any animator can be passed (which has to be prefabricated by the calling program code).
 * The other methods produce and register animators of a specific type that can be further specified by the method parameters
 * (e.g. to traverse a linear path with some target point, a specific duration and start delay etc.).
 * Note that the addXXXAnimator() methods register AND return the newly created animators such that the calling code can modify them and specify further details.<BR>
 * Note that the animated object still exists even if the animation has reduced its size to zero of if it has left the display.<BR>
 * To delete it, add a listener to the animator: animator.addListener(new AnimationUtilsCV.EndListener_DeleteIfNotVisible(myGuiObject)).
 * Also note: When an animator has been started (by calling startAnimation on guiObject) and has afterwards
 * finished its execution, it will be automatically removed from the object's animator list and therefore can (and will) not be restarted.
 * <P>
 *  A video explanation is given here:
 *  <A HREF="https://youtu.be/RYjBFQDCfPQ"> Overview</A>,
 *  <A HREF="https://youtu.be/HTr16eRqq60"> Code</A>,
 *  <A HREF="https://youtu.be/oHWNgBJRwCg"> Demo</A>
 * <P>
 * The code in the companion package <I>de.thkoeln.cvogt.android.propanimutil_demos</I> demonstrates how to use this class.
 <BR><HR><BR>
 This work is provided by
 Prof. Dr. Carsten Vogt, Technische Hochschule K&ouml;ln, Fakult&auml;t f&uuml;r Informations-, Medien- und Elektrotechnik, Germany
 <P>
 under GPLv3, the GNU General Public License 3,
 <A HREF="http://www.gnu.org/licenses/gpl-3.0.html">http://www.gnu.org/licenses/gpl-3.0.html</A>.
 *
 * @param <T> The class of the object to be animated - either Drawable or View
 * @see AnimationViewCV
 * @see de.thkoeln.cvogt.android.propanimutil_demos
 *
 */

public class AnimatedGuiObjectCV<T> {

    /** Type of the animated object: View */

    public static final int TYPE_VIEW = 1;

    /** Type of the animated object: ShapeDrawable with a RectShape */

    public static final int TYPE_DRAWABLE_RECT = 2;

    /** Type of the animated object: ShapeDrawable with a RectShape that is a square */

    public static final int TYPE_DRAWABLE_SQUARE = 3;

    /** Type of the animated object: ShapeDrawable with an OvalShape */

    public static final int TYPE_DRAWABLE_OVAL = 4;

    /** Type of the animated object: ShapeDrawable with an OvalShape that is a circle */

    public static final int TYPE_DRAWABLE_CIRCLE = 5;

    /** Type of the animated object: Text (= object of class DrawableTextCV) */

    public static final int TYPE_DRAWABLE_TEXT = 6;

    /** Type of the animated object: ShapeDrawable with a PathShape */

    public static final int TYPE_DRAWABLE_PATH = 7;

    /** Type of the animated object: BitmapDrawable */

    public static final int TYPE_DRAWABLE_BITMAP = 8;

    /** Type of the animated object: BitmapDrawable showing a text */

    public static final int TYPE_DRAWABLE_BITMAPTEXT = 9;

    /** Type of the animated object: Any Drawable
     * (for this type of object, currently only the path and rotate animations work) */

    public static final int TYPE_DRAWABLE_ANY = 10;

    /** Drawable or View object to be animated */

    protected T guiObject;

    /** Name of the object */

    private String name;

    /** Type of the object (see constants TYPE_VIEW, TYPE_DRAWABLE_SQUARE etc.) */

    private int type;

    /** Any kind of data associated with the object */

    private Object data;

    /** The attribute specifies whether the object is currently visible, i.e. shall be displayed.
     *  Default value is true.
     */

    private boolean visible = true;

    /** z index specifying the drawing order of objects of class AnimatedGuiObjectCV
     * that are to be displayed in a view of class AnimationViewCV.
     * Objects with higher values will be drawn in front of objects with lower values.
     * Default value is 0.
     */

    private int zindex = 0;

    /** View that currently displays guiObject */

    private AnimationViewCV displayingView;

    /** Animations to be applied to guiObject.
     * NB: When an animator has been started (by calling startAnimation() on guiObject) and has
     * afterwards finished its execution, it will be automatically removed
     * from the object's animator list and therefore can (and will) not be restarted.
     * If an animator anim is started directly (i.e. by calling anim.start()) it will not be removed
     * and hence be started again when startAnimation() is called a second time.
     */

    private ArrayList<Animator> animators;

    /** Objects whose visual display depends on this object */

    private ArrayList<DependentGuiObjectCV> dependentGuiObjects;

    /** Listener to react upon changes of property values */

    private ArrayList<OnPropertyChangedListener> propertyChangedListeners;

    /** Constructor for embedded View objects.
     * Instead of this constructor, the corresponding static method createFromView can be used.
     * @see #createFromView(View, String, int, int)
     * @param view View object to be embedded
     * @param name Name of the View object
     * @param left X coordinate of the left top corner
     * @param top Y coordinate of the left top corner
     */

    // public AnimatedGuiObjectCV(View view, String name, int centerX, int centerY) {
    public AnimatedGuiObjectCV(View view, String name, int left, int top) {
        this.guiObject = (T) view;
        view.setX(left);
        view.setY(top);
        // view.setX(centerX-view.getWidth()/2);
        // view.setY(centerY-view.getHeight()/2);
        // Log.v("DEMO",view.getWidth()+ " "+view.getHeight());
        this.name = new String(name);
        this.type = TYPE_VIEW;
        animators = new ArrayList<Animator>();
        propertyChangedListeners = new ArrayList<OnPropertyChangedListener>();
        dependentGuiObjects = new ArrayList<DependentGuiObjectCV>();
        data = null;
    }

    /** Static method to create an AnimatedGuiObjectCV whose embedded object is a View.
     * @param view View object to be embedded
     * @param name Name of the View object
     * @param left X coordinate of the left top corner
     * @param top Y coordinate of the left top corner
     * @return The created object.
     */

    public static AnimatedGuiObjectCV createFromView(View view, String name, int left, int top) {
        return new AnimatedGuiObjectCV(view,name,left,top);
    }

    /** Constructor for embedded Drawable objects that can be rectangles/squares or ovals/circles.
     * NB: Objects generated by this constructor cannnot be rotated. Rotatable objects are generated by the corresponding constructor with 'rotAngle' as last parameter.
     * Instead of this constructor, one of the corresponding static create methods can be used.
     * @see #AnimatedGuiObjectCV(int, String, int, int, int, int, int, double)
     * @see #createRect(String, int, int, int, int, int)
     * @see #createOval(String, int, int, int, int, int)
     * @see #createSquare(String, int, int, int, int)
     * @see #createCircle(String, int, int, int, int)
     * @param type Type of the Drawable object (see constants TYPE_DRAWABLE_OVAL, ...).<BR>
     *             For TYPE_DRAWABLE_SQUARE and TYPE_DRAWABLE_CIRCLE, the 'height' parameter is ignored, i.e. it is assumed that 'height' == 'width'.<BR>
     *             In case of undefined values or for TYPE_DRAWABLE_BITMAP, a rectangle will be generated
     *             (for TYPE_DRAWABLE_BITMAP, use the constructor with a Bitmap parameter):
     * @see #TYPE_DRAWABLE_RECT
     * @see #TYPE_DRAWABLE_SQUARE
     * @see #TYPE_DRAWABLE_OVAL
     * @see #TYPE_DRAWABLE_CIRCLE
     * @param name Name of the object
     * @param color Color of the Drawable object
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Width of the Drawable object
     * @param height Height of the Drawable object
     */

    public AnimatedGuiObjectCV(int type, String name, int color, int centerX, int centerY, int width, int height) {
        this.type = type;
        switch (type) {
            case TYPE_DRAWABLE_RECT:
            case TYPE_DRAWABLE_SQUARE: guiObject = (T) new ShapeDrawable(new RectShape()); break;
            case TYPE_DRAWABLE_OVAL:
            case TYPE_DRAWABLE_CIRCLE: guiObject = (T) new ShapeDrawable(new OvalShape()); break;
            default: guiObject = (T) new ShapeDrawable(new RectShape()); break;
        }
        this.name = new String(name);
        if (this.guiObject instanceof ShapeDrawable)
            ((ShapeDrawable)this.guiObject).getPaint().setColor(color);
        if (type==TYPE_DRAWABLE_SQUARE||type==TYPE_DRAWABLE_CIRCLE)
            ((Drawable)this.guiObject).setBounds(centerX-width/2,centerY-width/2,centerX+width/2,centerY+width/2);
        else
            ((Drawable)this.guiObject).setBounds(centerX-width/2,centerY-height/2,centerX+width/2,centerY+height/2);
        animators = new ArrayList<Animator>();
        propertyChangedListeners = new ArrayList<OnPropertyChangedListener>();
        dependentGuiObjects = new ArrayList<DependentGuiObjectCV>();
        data = null;
    }

    /** Constructor for embedded Drawable objects that can be rectangles/squares or ovals/circles.<BR>
     * Instead of this constructor, one of the corresponding static create methods can be used.
     * @see #createRect(String, int, int, int, int, int)
     * @see #createOval(String, int, int, int, int, int)
     * @see #createSquare(String, int, int, int, int)
     * @see #createCircle(String, int, int, int, int)
     * @param type Type of the Drawable object (see constants TYPE_DRAWABLE_OVAL, ...).<BR>
     *             For TYPE_DRAWABLE_SQUARE and TYPE_DRAWABLE_CIRCLE, the 'height' parameter is ignored, i.e. it is assumed that 'height' == 'width'.<BR>
     *             In case of undefined values or for TYPE_DRAWABLE_BITMAP, a rectangle will be generated
     *             (for TYPE_DRAWABLE_BITMAP, use the constructor with a Bitmap parameter):
     * @see #TYPE_DRAWABLE_RECT
     * @see #TYPE_DRAWABLE_SQUARE
     * @see #TYPE_DRAWABLE_OVAL
     * @see #TYPE_DRAWABLE_CIRCLE
     * @param name Name of the object
     * @param color Color of the Drawable object
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Width of the Drawable object
     * @param height Height of the Drawable object
     * @param rotAngle Rotation angle (in radians)
     */

    public AnimatedGuiObjectCV(int type, String name, int color, final int centerX, final int centerY, final int width, final int height, double rotAngle) {
        this(type,name,color,centerX,centerY,width,height);
        guiObject = (T) new RotatableDrawableCV((Drawable)guiObject,(float)rotAngle);
    }

    /** Static method to create an AnimatedGuiObjectCV whose embedded Drawable object is a rectangle.
     * @param name Name of the object
     * @param color Color of the Drawable object
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Width of the Drawable object
     * @param height Height of the Drawable object
     * @return The created object.
     */

    public static AnimatedGuiObjectCV createRect(String name, int color, int centerX, int centerY, int width, int height) {
        return new AnimatedGuiObjectCV(TYPE_DRAWABLE_RECT,name,color,centerX,centerY,width,height,0);
    }

    /** Static method to create an AnimatedGuiObjectCV whose embedded Drawable object is an oval.
     * @param name Name of the object
     * @param color Color of the Drawable object
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Width of the Drawable object
     * @param height Height of the Drawable object
     * @return The created object.
     */

    public static AnimatedGuiObjectCV createOval(String name, int color, int centerX, int centerY, int width, int height) {
        return new AnimatedGuiObjectCV(TYPE_DRAWABLE_OVAL,name,color,centerX,centerY,width,height,0);
    }

    /** Constructor for embedded Drawable objects that can be squares or circles.
     * NB: Objects generated by this constructor cannnot be rotated. Rotatable objects are generated by the corresponding constructor with 'rotAngle' as last parameter.
     * Instead of this constructor, one of the corresponding static create methods can be used.
     * @see #AnimatedGuiObjectCV(int, String, int, int, int, int, double)
     * @see #createSquare(String, int, int, int, int)
     * @see #createCircle(String, int, int, int, int)
     * @param type Type of the Drawable object (see constants TYPE_DRAWABLE_OVAL, ...).<BR>
     *             For TYPE_DRAWABLE_SQUARE and TYPE_DRAWABLE_RECT, a square with upper left corner (left,top) and side length 'width' will be generated.<BR>
     *             For TYPE_DRAWABLE_CIRCLE and TYPE_DRAWABLE_OVAL, a circle with upper left corner (left,top) and diameter 'width' will be generated.<BR>
     *             In case of undefined values or for TYPE_DRAWABLE_BITMAP, a rectangle will be generated
     *             (for TYPE_DRAWABLE_BITMAP, use the constructor with a Bitmap parameter):
     * @see #TYPE_DRAWABLE_RECT
     * @see #TYPE_DRAWABLE_SQUARE
     * @see #TYPE_DRAWABLE_OVAL
     * @see #TYPE_DRAWABLE_CIRCLE
     * @param name Name of the object
     * @param color Color of the Drawable object
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Side length (for TYPE_DRAWABLE_SQUARE) or diameter (for TYPE_DRAWABLE_CIRCLE) of the Drawable object
     */

    public AnimatedGuiObjectCV(int type, String name, int color, int centerX, int centerY, int width) {
        this(type,name,color,centerX,centerY,width,width);
    }

    /** Constructor for embedded Drawable objects that can be squares or circles.
     * Instead of this constructor, one of the corresponding static create methods can be used.
     * @see #createSquare(String, int, int, int, int)
     * @see #createCircle(String, int, int, int, int)
     * @param type Type of the Drawable object (see constants TYPE_DRAWABLE_OVAL, ...).<BR>
     *             For TYPE_DRAWABLE_SQUARE and TYPE_DRAWABLE_RECT, a square with upper left corner (left,top) and side length 'width' will be generated.<BR>
     *             For TYPE_DRAWABLE_CIRCLE and TYPE_DRAWABLE_OVAL, a circle with upper left corner (left,top) and diameter 'width' will be generated.<BR>
     *             In case of undefined values or for TYPE_DRAWABLE_BITMAP, a rectangle will be generated
     *             (for TYPE_DRAWABLE_BITMAP, use the constructor with a Bitmap parameter):
     * @see #TYPE_DRAWABLE_RECT
     * @see #TYPE_DRAWABLE_SQUARE
     * @see #TYPE_DRAWABLE_OVAL
     * @see #TYPE_DRAWABLE_CIRCLE
     * @param name Name of the object
     * @param color Color of the Drawable object
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Side length (for TYPE_DRAWABLE_SQUARE) or diameter (for TYPE_DRAWABLE_CIRCLE) of the Drawable object
     */

    public AnimatedGuiObjectCV(int type, String name, int color, int centerX, int centerY, int width, double rotAngle) {
        this(type,name,color,centerX,centerY,width,width,rotAngle);
    }

    /** Static method to create an AnimatedGuiObjectCV whose embedded Drawable object is an square.
     * @param name Name of the object
     * @param color Color of the Drawable object
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Width of the Drawable object
     * @return The created object.
     */

    public static AnimatedGuiObjectCV createSquare(String name, int color, int centerX, int centerY, int width) {
        return new AnimatedGuiObjectCV(TYPE_DRAWABLE_SQUARE,name,color,centerX,centerY,width,0);
    }

    /** Static method to create an AnimatedGuiObjectCV whose embedded Drawable object is an circle.
     * @param name Name of the object
     * @param color Color of the Drawable object
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param diameter Diameter of the Drawable object
     * @return The created object.
     */

    public static AnimatedGuiObjectCV createCircle(String name, int color, int centerX, int centerY, int diameter) {
        return new AnimatedGuiObjectCV(TYPE_DRAWABLE_CIRCLE,name,color,centerX,centerY,diameter,0);
    }

    /** Constructor for embedded Drawable objects with a PathShape.
     * NB: Objects generated by this constructor cannnot be rotated. Rotatable objects are generated by the corresponding constructor with 'rotAngle' as last parameter.
     * Instead of this constructor, the static method createFromPath can be used.
     * @see #AnimatedGuiObjectCV(Path, String, int, int, int, int, int, double)
     * @see #createFromPath(Path, String, int, int, int, int, int)
     * @param path Path defining the PathShape
     * @param name Name of the object
     * @param color Color of the Drawable object
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Width of the Drawable object
     * @param height Height of the Drawable object
     */

    public AnimatedGuiObjectCV(Path path, String name, int color, int centerX, int centerY, int width, int height) {
        this.type = TYPE_DRAWABLE_PATH;
        this.name = name;
        ShapeDrawable shd = new ShapeDrawable(new PathShape(path,width,height));
        shd.getPaint().setStyle(Paint.Style.FILL);
        shd.getPaint().setColor(color);
        shd.setIntrinsicWidth(width);
        shd.setIntrinsicHeight(height);
        this.guiObject = (T) shd;
        ((Drawable)this.guiObject).setBounds(centerX-width/2,centerY-height/2,centerX+width/2,centerY+height/2);
        animators = new ArrayList<Animator>();
        propertyChangedListeners = new ArrayList<OnPropertyChangedListener>();
        dependentGuiObjects = new ArrayList<DependentGuiObjectCV>();
        data = null;
    }

    /** Constructor for embedded Drawable objects with a PathShape.
     * @param path Path defining the PathShape
     * @param name Name of the object
     * @param color Color of the Drawable object
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Width of the Drawable object
     * @param height Height of the Drawable object
     * @param rotAngle Rotation angle (in radians)
     */

    public AnimatedGuiObjectCV(Path path, String name, int color, int centerX, int centerY, int width, int height, double rotAngle) {
        this(path,name,color,centerX,centerY,width,height);
        guiObject = (T) new RotatableDrawableCV((Drawable)guiObject,(float)rotAngle);
    }

    /** Static method to create an AnimatedGuiObjectCV with a PathShape.
     * @param path Path defining the PathShape
     * @param name Name of the object
     * @param color Color of the Drawable object
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Width of the Drawable object
     * @param height Height of the Drawable object
     * @return The created object
     */

    public static AnimatedGuiObjectCV createFromPath(Path path, String name, int color, int centerX, int centerY, int width, int height) {
        return new AnimatedGuiObjectCV(path, name, color, centerX, centerY, width, height,0);
    }

    /**
     * Method to create a star-shaped object, i.e. an object of class AnimatedGuiObjectCV of type TYPE_DRAWABLE_PATH.
     * @param name Name of the object
     * @param noOfPoints Number of points of the star
     * @param centerX center of the star - X coordinate
     * @param centerY center of the star - Y coordinate
     * @param outerRadius Radius of the circle that goes through the points of the star
     * @param innerRadius Radius of the circle that goes through the indentations of the star
     * @param color Color of the star
     */

    static public AnimatedGuiObjectCV createStar(String name, int noOfPoints, int centerX, int centerY, int outerRadius, int innerRadius, int color) {
        return createStar(name,noOfPoints,centerX,centerY,outerRadius,innerRadius,color,0);
    }

    /**
     * Method to create a star-shaped object, i.e. an object of class AnimatedGuiObjectCV of type TYPE_DRAWABLE_PATH.
     * @param name Name of the object
     * @param noOfPoints Number of points of the star
     * @param centerX center of the star - X coordinate
     * @param centerY center of the star - Y coordinate
     * @param outerRadius Radius of the circle that goes through the points of the star
     * @param innerRadius Radius of the circle that goes through the indentations of the star
     * @param color Color of the star
     * @param rotAngle Rotation angle of the star (in radians). 0 = One point of the star points straightly upwards
     */

    static public AnimatedGuiObjectCV createStar(String name, int noOfPoints, int centerX, int centerY, int outerRadius, int innerRadius, int color, double rotAngle) {
        Point outerPoints[] = GraphicsUtilsCV.pointsOnCircle(outerRadius,outerRadius,outerRadius,2*noOfPoints);
        Point innerPoints[] = GraphicsUtilsCV.pointsOnCircle(outerRadius,outerRadius,innerRadius,2*noOfPoints);
        Path starPath = new Path();
        starPath.moveTo(outerPoints[0].x,outerPoints[0].y);
        starPath.lineTo(innerPoints[1].x,innerPoints[1].y);
        for (int i=2;i<2*noOfPoints;i+=2) {
            starPath.lineTo(outerPoints[i].x,outerPoints[i].y);
            starPath.lineTo(innerPoints[i+1].x,innerPoints[i+1].y);
        }
        starPath.lineTo(outerPoints[0].x,outerPoints[0].y);
        AnimatedGuiObjectCV<Drawable> star = new AnimatedGuiObjectCV<Drawable>(starPath,name, color, centerX,centerY,2*outerRadius,2*outerRadius,rotAngle);
        return star;
    }

    /** Constructor for embedded Bitmap Drawables.
     * NB: Objects generated by this constructor cannnot be rotated. Rotatable objects are generated by the corresponding constructor with 'rotAngle' as last parameter.
     * Instead of this constructor, the corresponding static method createFromBitmap can be used.
     * @see #AnimatedGuiObjectCV(Context, String, Bitmap, int, int, int, int, double)
     * @see #createFromBitmap(Context, String, Bitmap, int, int, int, int)
     * @param context Context
     * @param name Name of the object
     * @param bitmap Bitmap to be displayed
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Width of the Drawable object
     * @param height Height of the Drawable object
     */

    public AnimatedGuiObjectCV(Context context, String name, Bitmap bitmap, int centerX, int centerY, int width, int height) {
        guiObject = (T)new BitmapDrawable(context.getResources(),bitmap);
        this.name = new String(name);
        this.type = TYPE_DRAWABLE_BITMAP;
        ((Drawable)this.guiObject).setBounds(centerX-width/2,centerY-height/2,centerX+width/2,centerY+height/2);
        animators = new ArrayList<Animator>();
        propertyChangedListeners = new ArrayList<OnPropertyChangedListener>();
        dependentGuiObjects = new ArrayList<DependentGuiObjectCV>();
        data = null;
    }

    /** Constructor for embedded Bitmap Drawables.
     * @param context Context
     * @param name Name of the object
     * @param bitmap Bitmap to be displayed
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Width of the Drawable object
     * @param height Height of the Drawable object
     * @param rotAngle Rotation angle (in radians)
     */

    public AnimatedGuiObjectCV(Context context, String name, Bitmap bitmap, int centerX, int centerY, int width, int height, double rotAngle) {
        this(context,name,bitmap,centerX,centerY,width,height);
        guiObject = (T) new RotatableDrawableCV((Drawable)guiObject,rotAngle);
    }

    /** Static method to create an AnimatedGuiObjectCV whose embedded Drawable object is a BitmapDrawable.
     * @param context Context
     * @param name Name of the object
     * @param bitmap Bitmap to be displayed
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param width Width of the Drawable object
     * @param height Height of the Drawable object
     * @return The created object
     */

    public static AnimatedGuiObjectCV createFromBitmap(Context context, String name, Bitmap bitmap, int centerX, int centerY, int width, int height) {
        return new AnimatedGuiObjectCV(context, name, bitmap, centerX, centerY, width, height,0);
    }

    /** Constructor for Drawables showing texts.
     * NB: The objects generated cannot be rotated. For rotable objects, use the constructor with a 'rotAngle' parameter.
     * Instead of this constructor, one of the corresponding static create methods can be used.
     * @see #AnimatedGuiObjectCV(int, Context, String, String, int, int, int, double)
     * @see #createText(String, String, int, int, int)
     * @see #createBitmapText(Context, String, String, int, int, int)
     * @param type type of the embedded object (either TYPE_DRAWABLE_TEXT = object shall be of class DrawableTextCV or TYPE_DRAWABLE_BITMAPTEXT = object shall be a BitmapDrawable showing the text).
     *             TYPE_DRAWABLE_TEXT: Constructor is very fast, but size animations can be jerky.
     *             TYPE_DRAWABLE_BITMAPTEXT: Constructor is somewhat slow, but animations (especially of size) are smooth.
     * @param context Context
     * @param name Name of the object
     * @param text Text to be displayed
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param textHeight Height of the text (in pixels)
     */

    public AnimatedGuiObjectCV(int type, Context context, String name, String text, int centerX, int centerY, int textHeight) {
        this.type = type;
        this.name = new String(name);
        if (type==TYPE_DRAWABLE_TEXT) {
            this.guiObject = (T) new DrawableTextCV(text,textHeight);
            int width = ((DrawableTextCV) guiObject).getWidth();
            int height = ((DrawableTextCV) guiObject).getHeight();
            ((Drawable)this.guiObject).setBounds(centerX-width/2,centerY-height/2,centerX+width/2,centerY+height/2);
        }
        if (type==TYPE_DRAWABLE_BITMAPTEXT) {
            Bitmap bitmap = GraphicsUtilsCV.textToBitmap(text, textHeight, Color.BLACK, Color.WHITE);
            bitmap = GraphicsUtilsCV.makeBitmapTransparent(bitmap, Color.WHITE);
            this.guiObject = (T) new BitmapDrawable(context.getResources(), bitmap);
            ((Drawable)this.guiObject).setBounds(centerX-bitmap.getWidth()/2,centerY-bitmap.getHeight()/2,centerX+bitmap.getWidth()/2,centerY+bitmap.getHeight()/2);
        }
        animators = new ArrayList<Animator>();
        propertyChangedListeners = new ArrayList<OnPropertyChangedListener>();
        dependentGuiObjects = new ArrayList<DependentGuiObjectCV>();
        data = null;
    }

    /** Constructor for Drawables showing texts.
     * Instead of this constructor, one of the corresponding static create methods can be used.
     * @see #createText(String, String, int, int, int)
     * @see #createBitmapText(Context, String, String, int, int, int)
     * @param type type of the embedded object (either TYPE_DRAWABLE_TEXT = object shall be of class DrawableTextCV or TYPE_DRAWABLE_BITMAPTEXT = object shall be a BitmapDrawable showing the text)
     *             TYPE_DRAWABLE_TEXT: Constructor is very fast, but size animations can be jerky.
     *             TYPE_DRAWABLE_BITMAPTEXT: Constructor is somewhat slow, but animations (especially of size) are smooth.
     * @param context Context
     * @param name Name of the object
     * @param text Text to be displayed
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param textHeight Height of the text (in pixels)
     * @param rotAngle Rotation angle (in radians)
     */

    public AnimatedGuiObjectCV(int type, Context context, String name, String text, int centerX, int centerY, int textHeight, double rotAngle) {
        this(type,context,name,text,centerX,centerY,textHeight);
        guiObject = (T) new RotatableDrawableCV((Drawable)guiObject,rotAngle);
    }

    /** Static method to create an AnimatedGuiObjectCV with a text.
     * The constructor called by this method is faster than that called by createBitmapText, but size animations can be jerky.
     * @see #createBitmapText(Context, String, String, int, int, int)
     * @param name Name of the object
     * @param text Text to be displayed
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param textHeight Height of the text (in pixels)
     * @return The created object
     */

    public static AnimatedGuiObjectCV createText(String name, String text, int centerX, int centerY, int textHeight) {
        return new AnimatedGuiObjectCV(TYPE_DRAWABLE_TEXT, null, name, text, centerX, centerY, textHeight, 0);
    }

    /** Static method to create an AnimatedGuiObjectCV with a Bitmap showing a text.
     * The constructor called by this method is slower than that called by createBitmapText, but animations will be smoother.
     * @see #createText(String, String, int, int, int)
     * @param context Context
     * @param name Name of the object
     * @param text Text to be displayed
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param textHeight Height of the text (in pixels)
     * @return The created object
     */

    public static AnimatedGuiObjectCV createBitmapText(Context context, String name, String text, int centerX, int centerY, int textHeight) {
        return new AnimatedGuiObjectCV(TYPE_DRAWABLE_BITMAPTEXT, context, name, text, centerX, centerY, textHeight, 0);
    }

    /** Constructor for embedded Drawables of any type, especially LayerDrawables
     * (for this type of object, currently only the path and color animations work).
     * NB: The objects generated cannot be rotated. For rotable objects, use the constructor with a 'rotAngle' parameter.
     * Instead of this constructor, the static method createFromDrawable() can be used.
     * @see #AnimatedGuiObjectCV(String, Drawable, int, int, double)
     * @see #createFromDrawable(String, Drawable, int, int)
     * @param name Name of the object
     * @param drawable The Drawable object to embed
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     */

    public AnimatedGuiObjectCV(String name, Drawable drawable, int centerX, int centerY) {
        this.guiObject = (T) drawable;
        this.name = new String(name);
        this.type = TYPE_DRAWABLE_ANY;
        int width, height;
        /* Incorrect code:
        if (drawable instanceof LayerDrawable) {
            width = ((LayerDrawable)drawable).getLayerWidth(0);
            height = ((LayerDrawable)drawable).getLayerHeight(0);
        } else {
            width = drawable.getIntrinsicWidth();
            height = drawable.getIntrinsicHeight();
        // } */
        width = drawable.getBounds().right - drawable.getBounds().left;
        height = drawable.getBounds().bottom - drawable.getBounds().top;
        ((Drawable)this.guiObject).setBounds(centerX-width/2,centerY-height/2,centerX+width/2,centerY+height/2);
        animators = new ArrayList<Animator>();
        propertyChangedListeners = new ArrayList<OnPropertyChangedListener>();
        dependentGuiObjects = new ArrayList<DependentGuiObjectCV>();
        data = null;
    }

    /** Constructor for embedded Drawables of any type, especially LayerDrawables
     * (for this type of object, currently only the path and rotate animations work).
     * @param name Name of the object
     * @param drawable The Drawable object to embed
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @param rotAngle Rotation angle (in radians)
     */

    public AnimatedGuiObjectCV(String name, Drawable drawable, int centerX, int centerY, double rotAngle) {
        this(name,drawable,centerX,centerY);
        if (drawable instanceof RotatableDrawableCV)
            guiObject = (T) drawable;
        else
            guiObject = (T) new RotatableDrawableCV((Drawable)guiObject,rotAngle);
    }

    /** Static method to create an AnimatedGuiObjectCV with an embedded Drawable, especially a LayerDrawable.
     * (for this type of object, currently only the path, rotate, and color animations work)
     * @param name Name of the object
     * @param drawable The Drawable object to embed
     * @param centerX X coordinate of the center of the Drawable object
     * @param centerY Y coordinate of the center of the Drawable object
     * @return The created object
     */

    public static AnimatedGuiObjectCV createFromDrawable(String name, Drawable drawable, int centerX, int centerY) {
        return new AnimatedGuiObjectCV(name, drawable, centerX, centerY, 0);
    }

    /** Method to delete the object. It will deregister the object from its view, remove all dependent gui objects
     * and all property change listeners and clear the animator list.
     */

    public void delete() {
        displayingView.removeAnimatedGuiObject(this);
        removeAllPropertyChangedListeners();
        removeAllDependentGuiObjects();
    }

    /** Set the left bound of the embedded Drawable or View object = the X coordinate of the top left corner of its enclosing rectangle.
     * NB: For a rotated object, this refers to the unrotated enclosing rectangle.
     *
     * @param left Left bound = X coordinate of the top left corner
     */

    public void setLeftBound(int left) {
        setLeftTop(left, getTopBound());
    }

    /** Set the top bound of the embedded Drawable or View object = the Y coordinate of the top left corner of its enclosing rectangle.
     * NB: For a rotated object, this refers to the unrotated enclosing rectangle.
     *
     * @param top Top bound = Y coordinate of the top left corner
     */

    public void setTopBound(int top) {
        setLeftTop(getLeftBound(),top);
    }

    /** Set the left and the top bound of the embedded Drawable or View object = set the coordinates of the top left corner of its enclosing rectangle.
     * NB: For a rotated object, this refers to the unrotated enclosing rectangle.
     *
     * @param left Left bound = X coordinate of the top left corner
     * @param top Top bound = Y coordinate of the top left corner
     */

    public void setLeftTop(int left, int top) {
        if (guiObject instanceof View) {
            View v = (View) guiObject;
            v.setX(left);
            v.setY(top);
        } else {
            int width = ((Drawable)getGuiObject()).getBounds().width();
            int height = ((Drawable)getGuiObject()).getBounds().height();
            // Log.v("DEMO",">>> setLeftTop: gL="+getLeftBound()+" l="+left+" gT="+getTopBound()+" t="+top);
            ((Drawable)getGuiObject()).setBounds(left, top, left + width, top + height);
        }
        for (Iterator<OnPropertyChangedListener> it = propertyChangedListeners.iterator(); it.hasNext();)
            it.next().onPropertyChanged(this,"LeftTop");
    }

    /** Set the left and the top bound of the embedded Drawable or View object = the coordinates of the top left corner of its enclosing rectangle.
     * NB: For a rotated object, this refers to the unrotated enclosing rectangle.
     *
     * @param leftTop coordinates of the top left corner
     */

    public void setLeftTop(Point leftTop) {
        setLeftTop(leftTop.x,leftTop.y);
//        int width = ((Drawable)guiObject).getBounds().width();
//        int height = ((Drawable)guiObject).getBounds().height();
//        ((Drawable)guiObject).setBounds(leftTop.x,leftTop.y,leftTop.x+width,leftTop.y+height);
    }

    /** Set the X coordinate of the center of the embedded Drawable or View object
     *
     * @param centerX X coordinate of the center
     */

    public void setCenterX(int centerX) {
        setCenter(centerX,getCenterY());
    }

    /** Set the Y coordinate of the center of the embedded Drawable or View object
     *
     * @param centerY Y coordinate of the center
     */

    public void setCenterY(int centerY) {
        setCenter(getCenterX(),centerY);
    }

    /** Set the center of the embedded Drawable or View object
     *
     * @param centerX X coordinate of the center of the object
     * @param centerY Y coordinate of the center of the object
     */

    public void setCenter(int centerX, int centerY) {
        int width = getWidth();
        int height = getHeight();
        if (guiObject instanceof View) {
            ((View) guiObject).setLeft(centerX-width/2);
            ((View) guiObject).setTop(centerY-height/2);
            ((View) guiObject).setRight(centerX+width/2);
            ((View) guiObject).setBottom(centerY+height/2);
            // Log.v("DEMO","centerX="+centerX+" centerY="+centerY+" width="+width+" height="+height);
        } else {
            ((Drawable)getGuiObject()).setBounds(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
        }
        // Log.v("DEMO",getName()+": setCenter "+centerX+" "+centerY);
        // Log.v("DEMO",getName()+": left "+getLeftBound()+" top "+getTopBound()+" right "+getRightBound()+" bottom "+getBottomBound());
        for (Iterator<OnPropertyChangedListener> it = propertyChangedListeners.iterator(); it.hasNext();)
            it.next().onPropertyChanged(this,"Center");
    }

    /** Set the center of the embedded Drawable or View object
     *
     * @param center center of the object
     */

    public void setCenter(Point center) {
        setCenter(center.x,center.y);
//        int width = ((Drawable)guiObject).getBounds().width();
//        int height = ((Drawable)guiObject).getBounds().height();
//        ((Drawable)guiObject).setBounds(center.x-width/2,center.y-height/2,center.x+width/2,center.y+height/2);
    }

    /** Set the width of the embedded Drawable or View object, i.e. the width of the enclosing rectangle.
     * NB: For rotated objects, this refers to the width of the unrotated enclosing rectangle.

     *
     * @param width Width
     */

    public void setWidth(int width) {
        setSize(width, getHeight());
    }

    /** Set the height of the embedded Drawable or View object, i.e. the height of the enclosing rectangle.
     * NB: For rotated objects, this refers to the height of the unrotated enclosing rectangle.
     *
     * @param height Height
     */

    public void setHeight(int height) {
        setSize(getWidth(),height);
    }

    /** Set the size of the embedded Drawable or View object, i.e. the width and height of the enclosing rectangle.
     * NB: For rotated objects, this refers to the width and height of the unrotated enclosing rectangle.
     *
     * @param width Width
     * @param height Height
     */

    public void setSize(int width, int height) {
        if (guiObject instanceof View) {
            int left = getLeftBound();
            int top = getTopBound();
           //  ((View) guiObject).setLeft(left);
           // ((View) guiObject).setTop(top);
           // ((View) guiObject).setRight(left+width);
           // ((View) guiObject).setBottom(top+height);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ((View) guiObject).getLayoutParams();
            lp.height = height; lp.width = width;
            ((View) guiObject).setLayoutParams(lp);
        } else if (!(guiObject instanceof LayerDrawable)) {
            int centerX = getCenterX();
            int centerY = getCenterY();
            /*
            T guiObject = getGuiObject();
            if (guiObject instanceof LayerDrawable) {
                for (int i=0;i<((LayerDrawable)guiObject).getNumberOfLayers();i++)
                    ((LayerDrawable)guiObject).setLayerSize(i,width,height);
            }
            */
            ((Drawable)getGuiObject()).setBounds(centerX-width/2, centerY-height/2, centerX+width/2, centerY+height/2);
        } else {  // LayerDrawable
            // Log.v("DEMO",getName()+": "+width+" "+height);
            // for (int i=0;i<((LayerDrawable) guiObject).getNumberOfLayers();i++)
            // ((LayerDrawable) guiObject).setLayerSize(i,width/2,height/2);
            int centerX = getCenterX();
            int centerY = getCenterY();
            ((LayerDrawable) guiObject).setBounds(centerX-width/2, centerY-height/2, centerX+width/2, centerY+height/2);
        }
        for (Iterator<OnPropertyChangedListener> it = propertyChangedListeners.iterator(); it.hasNext();)
            it.next().onPropertyChanged(this,"Size");
    }

    /** Set the rotation angle of the embedded Drawable or View object
     * (for an embedded Drawable object only effective if it is of class RotatableDrawableCV,
     *  i.e. has been generated by the corresponding constructor that specifies an initial rotation angle)
     *
     * @param rotationAngle Rotation angle
     */

    public void setRotationAngle(float rotationAngle) {
        if (guiObject instanceof RotatableDrawableCV)
            ((RotatableDrawableCV) guiObject).setRotationAngle(rotationAngle);
        if (guiObject instanceof View)
            ((View) guiObject).setRotation((float)(rotationAngle*360/(2* Math.PI)));
        for (Iterator<OnPropertyChangedListener> it = propertyChangedListeners.iterator(); it.hasNext();)
            it.next().onPropertyChanged(this,"RotationAngle");
    }

    /** Set the embedded Drawable or View object.
     * Only accessible from the same package.
     */

    protected void setGuiObject(T guiObject) {
        this.guiObject = guiObject;
        if (displayingView!=null)
            displayingView.invalidate();
    }

    /** Get the embedded Drawable or View object
     *
     * @return  The Drawable or View object
     */

    public T getGuiObject() {
        if (guiObject instanceof RotatableDrawableCV)
            return (T) ((RotatableDrawableCV) guiObject).getDrawable();
        else return (T) guiObject;
    }

    /** Get the left bound = the X coordinate of the top and bottom left corner of the embedded Drawable or View object, i.e. of its enclosing rectangle.
     * NB: For rotated objects, this refers to the unrotated rectangle. If you want to have the the value for the rotated rectangle, use getMinX() instead!
     *
     * @return  X coordinate of the top and bottom left corner
     */

    public int getLeftBound() {
        if (guiObject instanceof View) {
            // return ((View) guiObject).getLeft();
            return (int)((View) guiObject).getX();
        }
        else
            return ((Drawable)getGuiObject()).getBounds().left;
    }

    /** For an unrotated object, the method returns the same value as getLeftBound().
     * For a rotated object, it returns the X coordinate of the leftmost corner
     * of the enclosing rotated rectangle (whereas getLeftBound() does not take the
     * rotation into account).
     * NB: Does not yet work correctly for rotated embedded View objects.
     *
     * @return  The minimum X coordinate
     */

    public int getMinX() {
        Point[] corners = getCorners();
        int result = Integer.MAX_VALUE;
        for (int i=0;i<4;i++)
            if (corners[i].x<result)
                result = corners[i].x;
        return result;
    }

    /** Get the right bound = the X coordinate of the bottom right corner of the embedded Drawable or View object, i.e. of its enclosing rectangle.
     * NB: For rotated objects, this refers to the unrotated rectangle. If you want to have the value for the rotated rectangle, use getMaxX() instead!
     *
     * @return  X coordinate of the top and bottom right corner
     */

    public int getRightBound() {
        if (guiObject instanceof View) {
            // return ((View) guiObject).getLeft();
            return (int)((View) guiObject).getX()+getWidth();
        }
        else
            return ((Drawable)getGuiObject()).getBounds().left+getWidth();
    }

    /** For an unrotated object, the method returns the same value as getRightBound().
     * For a rotated object, it returns the X coordinate of the rightmost corner
     * of the enclosing rotated rectangle (whereas getRightBound() does not take the
     * rotation into account).
     *
     * @return  The maximum X coordinate
     */

    public int getMaxX() {
        Point[] corners = getCorners();
        int result = Integer.MIN_VALUE;
        for (int i=0;i<4;i++)
            if (corners[i].x>result)
                result = corners[i].x;
        return result;
    }

    /** Get the top bound = the Y coordinate of the top left and right corner of the embedded Drawable or View object, i.e. of its enclosing rectangle.
     * NB: For rotated objects, this refers to the unrotated rectangle. If you want to have the value for the rotated rectangle, use getMinY() instead!
     *
     * @return  Y coordinate of the top left and right corner
     */

    public int getTopBound() {
        if (guiObject instanceof View)
//            return ((View)guiObject).getTop();
            return (int)((View)guiObject).getY();
        else
            return ((Drawable)getGuiObject()).getBounds().top;
    }

    /** For an unrotated object, the method returns the same value as getTopBound().
     * For a rotated object, it returns the Y coordinate of the topmost corner
     * of the enclosing rotated rectangle (whereas getTopBound() does not take the
     * rotation into account).
     * NB: Does not yet work correctly for rotated embedded View objects.
     *
     * @return  The minimum Y coordinate
     */

    public int getMinY() {
        Point[] corners = getCorners();
        int result = Integer.MAX_VALUE;
        for (int i=0;i<4;i++)
            if (corners[i].y<result)
                result = corners[i].y;
        return result;
    }

    /** Get the bottom bound = the Y coordinate of the bottom left and right corner of the embedded Drawable or View object, i.e. of its enclosing rectangle.
     * NB: For rotated objects, this refers to the unrotated rectangle. If you want to have the value for the rotated rectangle, use getMaxY() instead!
     *
     * @return  Y coordinate of the bottom left and right corner
     */

    public int getBottomBound() {
        if (guiObject instanceof View)
//            return ((View)guiObject).getTop();
            return (int)((View)guiObject).getY()+getHeight();
        else
            return ((Drawable)getGuiObject()).getBounds().top+getHeight();
    }

    /** For an unrotated object, the method returns the same value as getBottomBound().
     * For a rotated object, it returns the Y coordinate of the lowermost corner
     * of the enclosing rotated rectangle (whereas getBottomBound() does not take the
     * rotation into account).
     * NB: Does not yet work correctly for rotated embedded View objects.
     *
     * @return  The maximum Y coordinate
     */

    public int getMaxY() {
        Point[] corners = getCorners();
        int result = Integer.MIN_VALUE;
        for (int i=0;i<4;i++)
            if (corners[i].y>result)
                result = corners[i].y;
        return result;
    }

    /** Get the X coordinate of the center of the embedded Drawable or View object
     *
     * @return  X coordinate of the center
     */

    public int getCenterX() {
        if (guiObject instanceof View) {
            View v = (View) guiObject;
            return v.getLeft()+v.getWidth()/2;
        } else {
            return ((Drawable)getGuiObject()).getBounds().left + getWidth() / 2;
        }
    }

    /** Get the Y coordinate of the center of the embedded Drawable or View object
     *
     * @return  Y coordinate of the center
     */

    public int getCenterY() {
        if (guiObject instanceof View) {
            View v = (View) guiObject;
            return v.getTop()+v.getHeight()/2;
        } else {
            return ((Drawable)getGuiObject()).getBounds().top + getHeight() / 2;
        }
    }

    /** Get the center of the embedded Drawable or View object
     *
     * @return  center of the object
     */

    public Point getCenter() {
        return new Point(getCenterX(),getCenterY());
    }

    /** Get the width of the embedded Drawable or View object, i.e. of its enclosing rectangle.
     * NB: For rotated objects, this refers to the unrotated rectangle. If you want to have a value based for the rotated object, use getCorners() instead!
     *
     * @return  width
     */

    public int getWidth() {
        if (guiObject instanceof View) {
            return ((View) guiObject).getWidth();
        }
        else {
            return ((Drawable) getGuiObject()).getBounds().width();
        }
    }

    /** Get the height of the embedded Drawable or View object, i.e. of its enclosing rectangle.
     * NB: For rotated objects, this refers to the unrotated rectangle. If you want to have a value based for the rotated object, use getCorners() instead!
     *
     * @return  height
     */

    public int getHeight() {
        if (guiObject instanceof View) {
            return ((View) guiObject).getHeight();
        }
        else {
            return ((Drawable)getGuiObject()).getBounds().height();
        }
    }

    /** Set the color of the embedded Drawable or View object
     * (for Drawable objects only effective if is of type ShapeDrawable, DrawableTextCV, or DrawableTextWithIconCV)
     */

    public void setColor(int color) {
        if (guiObject instanceof View)
            ((View) guiObject).setBackgroundColor(color);
        if (guiObject instanceof ShapeDrawable)
            ((ShapeDrawable) guiObject).getPaint().setColor(color);
        if (guiObject instanceof DrawableTextCV) {
            DrawableTextCV gobj = (DrawableTextCV) guiObject;
            int alpha = gobj.getPaint().getAlpha();
            gobj.getPaint().setColor(color);
            gobj.getPaint().setAlpha(alpha);
        }
        if (guiObject instanceof RotatableDrawableCV) {
            Drawable drw = ((RotatableDrawableCV) guiObject).getDrawable();
            if (drw instanceof ShapeDrawable)
                ((ShapeDrawable) drw).getPaint().setColor(color);
            if (drw instanceof DrawableTextWithIconCV)
                ((DrawableTextWithIconCV) drw).setColor(color);
            if (drw instanceof DrawableTextCV)
                // int alpha = ((DrawableTextCV)drw).getPaint().getAlpha();
                ((DrawableTextCV)drw).getPaint().setColor(color);
                // ((DrawableTextCV)drw).getPaint().setAlpha(alpha);
            }
        for (Iterator<OnPropertyChangedListener> it = propertyChangedListeners.iterator(); it.hasNext();)
            it.next().onPropertyChanged(this,"Color");
    }

    /** Set the border color of the embedded DrawabletextWithIconCV object
     * (only effective for objects of this type)
     */

    public void setColorBorder(int color) {
        if (!(guiObject instanceof RotatableDrawableCV)) return;
        Drawable drw = ((RotatableDrawableCV) guiObject).getDrawable();
        if (!(drw instanceof DrawableTextWithIconCV)) return;
        ((DrawableTextWithIconCV) drw).setColorBorder(color);
        for (Iterator<OnPropertyChangedListener> it = propertyChangedListeners.iterator(); it.hasNext();)
            it.next().onPropertyChanged(this,"ColorBorder");
    }

    /** Set the border stroke width of the embedded DrawabletextWithIconCV object
     * (only effective for objects of this type)
     */

    public void setStrokewidthBorder(int strokewidthBorder) {
        if (!(guiObject instanceof RotatableDrawableCV)) return;
        Drawable drw = ((RotatableDrawableCV) guiObject).getDrawable();
        if (!(drw instanceof DrawableTextWithIconCV)) return;
        ((DrawableTextWithIconCV) drw).setStrokewidthBorder(strokewidthBorder);
        for (Iterator<OnPropertyChangedListener> it = propertyChangedListeners.iterator(); it.hasNext();)
            it.next().onPropertyChanged(this,"ColorBorder");
    }

    /** Get the color of the embedded Drawable or View object
     *
     * @return  color (if type ShapeDrawable or DrawableTextWithIconCV or View), 0 (otherwise)
     */

    public int getColor() {
        if (guiObject instanceof View)
            return ((View) guiObject).getSolidColor();
        if (guiObject instanceof ShapeDrawable)
            return ((ShapeDrawable) guiObject).getPaint().getColor();
        if (guiObject instanceof DrawableTextCV)
            return ((DrawableTextCV) guiObject).getPaint().getColor();
        if (guiObject instanceof DrawableTextWithIconCV)
            return ((DrawableTextWithIconCV) guiObject).getColor();
        if (guiObject instanceof RotatableDrawableCV) {
            Drawable drw = ((RotatableDrawableCV) guiObject).getDrawable();
            if (drw instanceof ShapeDrawable)
                return ((ShapeDrawable) drw).getPaint().getColor();
            if (drw instanceof DrawableTextCV)
                return ((DrawableTextCV) drw).getPaint().getColor();
            if (drw instanceof DrawableTextWithIconCV)
                ((DrawableTextWithIconCV) drw).getColor();
        }
        return 0;
    }

    /** Set the transparency of the embedded Drawable or View object
     * (for a Drawable object only effective if is of type ShapeDrawable ot DrawableTextWithIconCV).
     *
     * @param transparency  The new transparency of the object (= transparency byte of the color attribute).
     *                      If transparency<0 the transparency byte will be set to 0x==.
     *                      If transparency>255 the transparency byte will be set to 0xFF.
     */

    public void setTransparency(int transparency) {
        int trsp = transparency;
        if (trsp<0) trsp = 0;
        if (trsp>255) trsp = 255;
        trsp <<= 24;
        if (guiObject instanceof View)
            ((View) guiObject).setBackgroundColor((getColor()&0x00FFFFFF)|trsp);
        if (guiObject instanceof ShapeDrawable)
            ((ShapeDrawable) guiObject).getPaint().setColor((getColor()&0x00FFFFFF)|trsp);
        if (guiObject instanceof RotatableDrawableCV) {
            Drawable drw = ((RotatableDrawableCV) guiObject).getDrawable();
            if (drw instanceof ShapeDrawable)
                ((ShapeDrawable) drw).getPaint().setColor((getColor()&0x00FFFFFF)|trsp);
            if (drw instanceof DrawableTextWithIconCV) {
                ((DrawableTextWithIconCV) drw).setTransparency(transparency);
            }
        }
        if (guiObject instanceof DrawableTextWithIconCV) {
            ((DrawableTextWithIconCV) guiObject).setTransparency(transparency);
        }

        for (Iterator<OnPropertyChangedListener> it = propertyChangedListeners.iterator(); it.hasNext();)
            it.next().onPropertyChanged(this,"Transparency");
    }

    /** Get the transparency value of the embedded Drawable or View object
     *
     * @return  transparency (if type ShapeDrawable or DrawableTextWithIconCV or View), 0 (otherwise)
     */

    public int getTransparency() {
        return (getColor()>>24)&0xFF;
    }

    /** Get the type of the embedded object
     *
     * @return type The type of the object (TYPE_VIEW, TYPE_DRAWABLE_RECT, ...)
     */

    public int getType() {
        return type;
    }

    /** Get the rotation angle of the embedded Drawable or View object
     * (if a Drawable object is not rotatable, i.e. not of class RotatableDrawableCV,
     * the return value will be 0)
     *
     * @return Current rotation angle
     */

    public float getRotationAngle() {
        if (guiObject instanceof RotatableDrawableCV)
            return ((RotatableDrawableCV) guiObject).getRotationAngle();
        if (guiObject instanceof View)
            ((View) guiObject).getRotation();
        return 0;
    }

    /** Get the name of the object
     *
     * @return The name of the object
     */

    public String getName() {
        if (name==null)
            return new String("");
        return new String(name);
    }

    /** Set the name of the object
     *
     * @param name
     */

    public void setName(String name) {
        this.name = new String(name);
    }

    /** Get the data stored with the object.
     * @return The data stored with the object.
     */

    public Object getData() {
        return data;
    }

    /** Set the data stored with the object
     *
     * @param data
     */

    public void setData(Object data) {
        this.data = data;
    }

    /** Get the view displaying the object.
     * @return The AnimationViewCV object displaying this GUI object.
     */

    public AnimationViewCV getDisplayingView() {
        return displayingView;
    }

    /** Set the displayingView that shall display the object. Will be called in the addAnimatedGuiObject() method of a displayingView of class AnimatedViewCV.
     *
     * @param displayingView
     */

    public void setDisplayingView(AnimationViewCV displayingView) {
        this.displayingView = displayingView;
    }

    /** Gets the visibility
     * @return The current value of the visible attribute (the return type being 'Boolean' instead of 'boolean' because this method is called by the evaluator of an animator)
     */

    public boolean getVisible() {
        return this.visible;
    }

    /** Sets the visibility
     * @param visible The new value for the visible attribute (the parameter type being 'Boolean' instead of 'boolean' because this method is called by the evaluator of an animator)
     */

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    /** Checks if the object is currently visible
     * @return Current value of the visible attribute
     */

    public Boolean isVisible() {
        return visible;
    }

    /** Set the z index
     * @param zindex The new value for the zindex attribute
     */

    public void setZindex(int zindex) {
        this.zindex = zindex;
        if (displayingView!=null)
            displayingView.sortAnimatedGuiObjectsByZindex();
    }

    /** Get the current value of the z index attribute
     * @return The z index value
     */

    public int getZindex() {
        return this.zindex;
    }

    /** Modify the z index attribute
     * @param valueToAdd The value that shall be added to the z index (can be negative)
     * @return The new z index value
     */

    public int addToZindex(int valueToAdd) {
        setZindex(getZindex()+valueToAdd);
        return zindex;
    }

    /** Check if the object lies on top of all objects that are currently displayed at its position.
     * @return true if it lies on top, false otherwise
     */

    public boolean liesOnTop() {
        ArrayList<AnimatedGuiObjectCV> objectsAtPos = displayingView.getAnimatedGuiObjectsAtPoint(getCenterX(),getCenterY());
        return objectsAtPos.get(objectsAtPos.size()-1)==this;
    }

    /** Get the four corners of the embedded Drawable or View object (i.e. of its enclosing rectangle).
     * If the object is rotated the corners of the rotated enclosing rectangle are returned (in contrast to getTopBound() etc.).
     * NB: Does not yet work correctly for rotated embedded View objects.
     *
     * @return Array with exactly four elements of class Point, specifying the four corners
     * (in clockwise order, starting with the left top corner
     * - for rotated objects, the notion "left top" etc. refers to the position of the corner of the unrotated enclosing rectangle)
     */

    public Point[] getCorners() {
        if (guiObject instanceof RotatableDrawableCV)
            return ((RotatableDrawableCV) guiObject).getCorners();
        Point[] result = new Point[4];
        if (guiObject instanceof Drawable) {
            Rect enclRect = ((Drawable) guiObject).getBounds();
            result[0] = new Point(enclRect.left, enclRect.top);
            result[1] = new Point(enclRect.right, enclRect.top);
            result[2] = new Point(enclRect.right, enclRect.bottom);
            result[3] = new Point(enclRect.left, enclRect.bottom);
            return result;
        }
        else {  // embedded object is of class View
            View gv = ((View) guiObject);
            result[0] = new Point((int)gv.getX(),(int)gv.getY());
            result[1] = new Point((int)(gv.getX()+gv.getWidth()),(int)gv.getY());
            result[2] = new Point((int)(gv.getX()+gv.getWidth()),(int)(gv.getY()+gv.getHeight()));
            result[3] = new Point((int)gv.getX(),(int)(gv.getY()+gv.getHeight()));
            return result;
        }
        /* Old version:
                if (!(guiObject instanceof RotatableDrawableCV)) {
            Point[] result = new Point[4];
            result[0] = new Point(getLeftBound(), getTopBound());
            result[1] = new Point(getLeftBound()+getWidth(), getTopBound());
            result[2] = new Point(getLeftBound()+getWidth(), getTopBound()+getHeight());
            result[3] = new Point(getLeftBound(), getTopBound()+getHeight());
            return result;
        }
        else
            return ((RotatableDrawableCV) guiObject).getCorners();

         */
    }

    /** Get the enclosing rectangle of the embedded Drawable or View object
     * (in case of a rotated object, the rectangle for the object without rotation is returned)
     *
     * @return The enclosing rectangle
     */

    public Rect getArea() {
        return new Rect(getLeftBound(),getTopBound(),getLeftBound()+getWidth(),getTopBound()+getHeight());
    }

    /** Draw the guiObject and all its dependent objects of class DependentGuiObjectCV
     * on a canvas.
     *
     * @param canvas Canvas to draw on
     */

    public void draw(Canvas canvas) {
//        draw(canvas,true,true);
//    }
        if (!getVisible()) return;
        if (dependentGuiObjects!=null)
            for (Iterator<DependentGuiObjectCV> it = dependentGuiObjects.iterator(); it.hasNext();) {
                DependentGuiObjectCV dobj = it.next();
                if (!dobj.background)
                    dobj.draw(canvas);
            }
        if (guiObject instanceof Drawable)
            ((Drawable)guiObject).draw(canvas);
    }

    /** Draw the guiObject and/or all its dependent objects of class DependentGuiObjectCV
     * on a canvas. If all dependent objects shall appear in the background (e.g. lines connecting
     * GUI objects), call draw() first with drawDependentObjects==true and then again
     * with drawGuiObject==true. This it done so by the draw() method of AnimationViewCV.
     *
     * @param canvas Canvas to draw on
     * @param drawGuiObject Draw the guiObject itself
     * @param drawDependentObjects Draw the dependent objects
     */

    public void draw(Canvas canvas, boolean drawGuiObject, boolean drawDependentObjects) {
        if (!getVisible()) return;
        if (dependentGuiObjects!=null && drawDependentObjects)
            for (Iterator<DependentGuiObjectCV> it = dependentGuiObjects.iterator(); it.hasNext();) {
                DependentGuiObjectCV dobj = it.next();
                if (dobj instanceof DependentLineObjectToObjectCV && !((DependentLineObjectToObjectCV) dobj).bothEndpointsVisible())
                    continue;
                dobj.draw(canvas);
            }
        if (guiObject instanceof Drawable && drawGuiObject)
            ((Drawable)guiObject).draw(canvas);
    }

    /** Draw all objects of class DependentGuiObjectCV that depend on this AnimatedGuiObjectCV object
     * and shall appear in the background.
     *
     * @param canvas Canvas to draw on
     */

    public void drawDependentsInBackground(Canvas canvas) {
        if (!getVisible()) return;
        if (dependentGuiObjects!=null)
            for (Iterator<DependentGuiObjectCV> it = dependentGuiObjects.iterator(); it.hasNext();) {
                DependentGuiObjectCV dobj = it.next();
                if (dobj instanceof DependentLineObjectToObjectCV && !((DependentLineObjectToObjectCV) dobj).bothEndpointsVisible())
                    continue;
                if (dobj.background)
                    dobj.draw(canvas);
            }
    }

    /** Check if a given point is within the enclosing rectangle of the Drawable or View object
     *
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @return The test result
     */

    public boolean contains(int x, int y) {
//        if (getName().equals("Laurel"))
//        Log.v("DEMO",getArea().left+" "+getArea().top+" "+getArea().right+" "+getArea().bottom+"   "+x+" "+y);
//        if (guiObject instanceof View)
//            return getLeftBound()<=x && x<=getLeftBound()+getWidth() && getTopBound()<=y && y<= getTopBound()+getHeight();
        if (!(guiObject instanceof RotatableDrawableCV))
            return getArea().contains(x,y);
        else return ((RotatableDrawableCV)guiObject).contains(x,y);
    }

    /** Check if a given point is within the enclosing rectangle of the Drawable or View object
     *
     * @param point The point
     * @return The test result
     */

    public boolean contains(Point point) {
        // Log.v("DEMO","   contains "+getName()+" "+point.x+" "+point.y);
        return contains(point.x,point.y);
    }

    /** Check if another object overlaps with this object.
     *
     * @param obj The object for which overlapping shall be checked
     * @return true if obj overlaps with this object, false otherwise
     */

    public boolean overlapsWith(AnimatedGuiObjectCV obj) {
        if (getType()==AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE&&obj.getType()==AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE) {
            if (GraphicsUtilsCV.distance(getCenter(),obj.getCenter()) < (getWidth() + obj.getWidth()) / 2)
                return true; }
        else {
            Point[] corners = getCorners();
            for (int i = 0; i <= 3; i++)
                if (obj.contains(corners[i]))
                    return true;
            corners = obj.getCorners();
            for (int i = 0; i <= 3; i++)
                if (this.contains(corners[i]))
                    return true;
        }
        return false;
    }

    /** Checks if the object lies (partly or completely) within a given rectangle.
     * @param left left bound of the rectangle
     * @param top upper bound of the rectangle
     * @param right right bound of the rectangle
     * @param bottom bottom bound of the rectangle
     * @return true if the object overlaps with the rectangle, false otherwise
     * */

    public boolean liesInArea(int left, int top, int right, int bottom) {
        Point[] corners = getCorners();
        for (int i=0;i<corners.length;i++)
            if (corners[i].x>=left&&corners[i].x<=right&&corners[i].y>=top&&corners[i].y<=bottom)
                return true;
        return false;
    }

    /** Move the object by a vector (i.e. for a specific distance into a specific direction)
     *
     * @param dirX X coordinate of the vector
     * @param dirY Y coordinate of the vector
     */

    public void move(int dirX, int dirY) {
        setCenter(getCenterX()+dirX,getCenterY()+dirY);
    }

    /** Register an object of class DependentGuiObjectCV
     * to be drawn together with this object.
     *
     * @param obj The object to be registered
     */

    public void addDependentGuiObject(DependentGuiObjectCV obj) {
        dependentGuiObjects.add(obj);
    }

    /** Get all registered dependent objects of class DependentGuiObjectCV.
     *
     * @return ArrayList that contains all registered objects of class DependentGuiObjectCV
     */

    public ArrayList<DependentGuiObjectCV> getDependentGuiObjects() {
        if (dependentGuiObjects==null)
            return new ArrayList<DependentGuiObjectCV>();
        return new ArrayList<DependentGuiObjectCV>(dependentGuiObjects);
    }

    /** Deregister an object of class DependentGuiObjectCV.
     *
     * @param obj The object to be deregistered
     */

    public void removeDependentGuiObject(DependentGuiObjectCV obj) {
        dependentGuiObjects.remove(obj);
        obj.onDeregisteredFromGuiObject(this);
    }

    /** Deregister all objects of class DependentGuiObjectCV.
     */

    public void removeAllDependentGuiObjects() {
        for (Iterator<DependentGuiObjectCV> it = dependentGuiObjects.iterator(); it.hasNext();)
            it.next().onDeregisteredFromGuiObject(this);
        dependentGuiObjects = new ArrayList<DependentGuiObjectCV>();
    }

    /** Start all registered animators simultaneously and immediately.
     * Once an animator has finished its execution, it will be automatically removed
     * from the object's animator list and therefore can (and will) not be restarted.
     */

    public void startAnimation() {
        for (Iterator<Animator> it = animators.iterator(); it.hasNext();) {
            Animator anim = it.next();
            if (anim.isStarted()) continue;
            anim.addListener(new EndListener_RemoveAnimator(this));
            anim.start();
        }
    }

    /**
     * Listener to remove the animator from the animator list of the
     * AnimatedGuiObjectCV when the animation ends.
     * Beware - this might not be reliable (https://stackoverflow.com/questions/5474923/onanimationend-is-not-getting-called-onanimationstart-works-fine).
     */
    private class EndListener_RemoveAnimator extends AnimatorListenerAdapter {
        private AnimatedGuiObjectCV guiobj;
        public EndListener_RemoveAnimator(AnimatedGuiObjectCV guiobj) {
            this.guiobj = guiobj;
        }
        @Override
        public void onAnimationEnd(Animator animator) {
            animator.removeAllListeners();
            guiobj.removeAnimator(animator);
        }
    }

    /** Add a set of animators that shall be played one after the other.
     * The method generates a corresponding AnimatorSet object and adds it to the object's 'animators' attribute.
     * The individual animators are removed from the 'animators' attribute (if present there).
     * The usual way to specify a combination of animators is first to generate the individual animators
     * and then to combine them by calling playSequentially() and playTogether().
     * The animators can be generated either by using the addXXXAnimator() methods or by "stand-alone methods" like ObjectAnimator.ofFloat().
     * They can be registered with the object (as the addXXXAnimator() methods do) but such an advance registration is not mandatory.
     * It is possible to nest these playXXX() calls, e.g. myObject.playSequentially(myObject.playTogether(anim1,anim2),myObject.playTogether(anim3,anim3)).
     *
     * @param anims Animators to be played
     * @return Animator set containing the animators
     */

    public AnimatorSet playSequentially(Animator... anims) {
        ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                AnimatedGuiObjectCV.this.displayingView.invalidate();
            }
        };
        for (Animator anim: anims) {
            if (anim instanceof ValueAnimator) {
                ((ValueAnimator) anim).addUpdateListener(updateListener);
            }
            if (animators.contains(anim))
                animators.remove(anim);
        }
        AnimatorSet animset = new AnimatorSet();
        animset.playSequentially(anims);
        animators.add(animset);
        return animset;
    }

    /** Add a set of animators that shall be played simultaneously.
     * The method generates a corresponding AnimatorSet object and adds it to the object's 'animators' attribute.
     * The individual animators are removed from the 'animators' attribute (if present there).
     * The usual way to specify a combination of animators is first to generate the individual animators
     * and then to combine them by calling playSequentially() and playTogether().
     * The animators can be generated either by using the addXXXAnimator() methods or by "stand-alone methods" like ObjectAnimator.ofFloat().
     * They can be registered with the object (as the addXXXAnimator() methods do) but such an advance registration is not mandatory.
     * It is possible to nest these playXXX() calls, e.g. myObject.playSequentially(myObject.playTogether(anim1,anim2),myObject.playTogether(anim3,anim3)).
     *
     * @param anims Animators to be played
     * @return Animator set containing the animators
     */

    public AnimatorSet playTogether(Animator... anims) {
        ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                AnimatedGuiObjectCV.this.displayingView.invalidate();
            }
        };
        for (Animator anim: anims) {
            if (anim instanceof ValueAnimator) {
                ((ValueAnimator) anim).addUpdateListener(updateListener);
            }
            if (animators.contains(anim))
                animators.remove(anim);
        }
        AnimatorSet animset = new AnimatorSet();
        animset.playTogether(anims);
        animators.add(animset);
        return animset;
    }

    /** Add an animator to drive the animation of the embedded Drawable or View object.
     * The animator will be played simultaneously to the other animators registered by this method.
     * To specify temporal relationships, use the methods playTogether() and playSequentially().
     *
     * @param anim Animator to be added
     */

    public void addAnimator(Animator anim) {
        ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                AnimatedGuiObjectCV.this.displayingView.invalidate();
            }
        };
        AnimatorListenerAdapter animatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                // CAN BE EXTENDED BY INSTRUCTIONS TO BE EXECUTED WHEN THE ANIMATION ENDS
                // BEWARE - STACKOVERFLOW SAYS THAT THIS IS NOT RELIABLE: https://stackoverflow.com/questions/5474923/onanimationend-is-not-getting-called-onanimationstart-works-fine
            }
        };
        if (anim instanceof ValueAnimator) {
            ((ValueAnimator) anim).addUpdateListener(updateListener);
            anim.addListener(animatorListener);
        }
        animators.add(anim);
    }

    /** Returns the animators currently registered with the object,
     * i.e. a copy of the ArrayList containing the references to the animators.
     * @return ArrayList with the animators
     */

    public ArrayList<Animator> getAnimators() {
        return (ArrayList<Animator>) animators.clone();
    }

    /** Removes an animator from the animator list of the animated object.
     * @param animator The animator to be removed
     */

    public void removeAnimator(Animator animator) {
        animator.cancel();
        animators.remove(animator);
    }

    /** Removes all registered animators. */

    public void clearAnimatorList() {
        for (Iterator<Animator> it = animators.iterator(); it.hasNext();) {
            Animator anim = it.next();
            anim.removeAllListeners();
            anim.cancel();
        }
        animators = new ArrayList<Animator>();
    }

    /** Add an animator to move the embedded Drawable or View object in a direct line to some specified target coordinates.
     * The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     * The start delay is zero, i.e. the animation starts immediately.
     *
     * @param targetX X coordinate of the target = horizontal center of the object at its final position
     * @param targetY Y coordinate of the target = vertical center of the object at its final position
     * @param duration Duration of the animation (in ms)
     * @return The new animator (= set with two animators for the X and the Y property, respectively)
     */

    public AnimatorSet addLinearPathAnimator(int targetX, int targetY, int duration) {
        return addLinearPathAnimator(targetX,targetY,new LinearInterpolator(),duration,0);
    }

    /** Add an animator to move the embedded Drawable or View object in a direct line to some specified target coordinates.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *
     * @param targetX X coordinate of the target = horizontal center of the object at its final position
     * @param targetY Y coordinate of the target = vertical center of the object at its final position
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator (= set with two animators for the X and the Y property, respectively)
     */

    public AnimatorSet addLinearPathAnimator(int targetX, int targetY, int duration, int startDelay) {
        return addLinearPathAnimator(targetX,targetY,new LinearInterpolator(),duration,startDelay);
    }

    /** Add an animator to move the embedded Drawable or View object in a direct line to some specified target coordinates.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param targetX X coordinate of the target = horizontal center of the object at its final position
     * @param targetY Y coordinate of the target = vertical center of the object at its final position
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @return The new animator (= set with two animators for the X and the Y property, respectively)
     */

    public AnimatorSet addLinearPathAnimator(int targetX, int targetY, TimeInterpolator timeInterpolator, int duration) {
        return addLinearPathAnimator(targetX,targetY,timeInterpolator,duration,0);
    }

    /** Add an animator to move the embedded Drawable or View object in a direct line to some specified target coordinates.
     *
     * @param targetX X coordinate of the target = horizontal center of the object at its final position
     * @param targetY Y coordinate of the target = vertical center of the object at its final position
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator (= set with two animators for the X and the Y property, respectively)
     */

    public AnimatorSet addLinearPathAnimator(int targetX, int targetY, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        ObjectAnimator animX = ObjectAnimator.ofInt(this, "centerX", targetX);
        animX.setInterpolator(timeInterpolator);
        addAnimator(animX);
        ObjectAnimator animY = ObjectAnimator.ofInt(this, "centerY", targetY);
        animY.setInterpolator(timeInterpolator);
        addAnimator(animY);
        AnimatorSet animSet = playTogether(animX, animY);
        animSet.setStartDelay(startDelay);
        animSet.setDuration(duration);
        return animSet;
    }

    /** Add an animator to move the embedded Drawable or View object on a direct line towards some specified target coordinates or away from these coordinates.
     * The distance of the object at its final position from these coordinates is the original distance multiplied by an attraction factor.
     * If this factor is small than 1, the object will move closer to the target (i.e. will be attracted by the target).
     * If this factor is larger than 1, the object will move farther away from the target (i.e. will be repulsed from the target).
     * If this factor equals 0, the object will be moved onto the target coordinates.
     * If this factor is smaller than 0, the object will be moved beyond the target coordinates.
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param attractionFactor Factor as explained above
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addAttractOrRepulseAnimator(int targetX, int targetY, double attractionFactor, int duration) {
        return addAttractOrRepulseAnimator(targetX,targetY,attractionFactor,duration,0);
    }

    /** Add an animator to move the embedded Drawable or View object on a direct line towards some specified target coordinates or away from these coordinates.
     * The distance of the object at its final position from these coordinates is the original distance multiplied by an attraction factor.
     * If this factor is small than 1, the object will move closer to the target (i.e. will be attracted by the target).
     * If this factor is larger than 1, the object will move farther away from the target (i.e. will be repulsed from the target).
     * If this factor equals 0, the object will be moved onto the target coordinates.
     * If this factor is smaller than 0, the object will be moved beyond the target coordinates.
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param attractionFactor Factor as explained above
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addAttractOrRepulseAnimator(int targetX, int targetY, double attractionFactor, int duration, int startDelay) {
        return addAttractOrRepulseAnimator(targetX,targetY,attractionFactor,new LinearInterpolator(),duration,startDelay);
    }

    /** Add an animator to move the embedded Drawable or View object on a direct line towards some specified target coordinates or away from these coordinates.
     * The distance of the object at its final position from these coordinates is the original distance multiplied by an attraction factor.
     * If this factor is small than 1, the object will move closer to the target (i.e. will be attracted by the target).
     * If this factor is larger than 1, the object will move farther away from the target (i.e. will be repulsed from the target).
     * If this factor equals 0, the object will be moved onto the target coordinates.
     * If this factor is smaller than 0, the object will be moved beyond the target coordinates.
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param attractionFactor Factor as explained above
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addAttractOrRepulseAnimator(int targetX, int targetY, double attractionFactor, TimeInterpolator timeInterpolator, int duration) {
        return addAttractOrRepulseAnimator(targetX,targetY,attractionFactor,timeInterpolator,duration,0);
    }

    /** Add an animator to move the embedded Drawable or View object on a direct line towards some specified target coordinates or away from these coordinates.
     * The distance of the object at its final position from these coordinates is the original distance multiplied by an attraction factor.
     * If this factor is small than 1, the object will move closer to the target (i.e. will be attracted by the target).
     * If this factor is larger than 1, the object will move farther away from the target (i.e. will be repulsed from the target).
     * If this factor equals 0, the object will be moved onto the target coordinates.
     * If this factor is smaller than 0, the object will be moved beyond the target coordinates.
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param attractionFactor Factor as explained above
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addAttractOrRepulseAnimator(int targetX, int targetY, double attractionFactor, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        return addLinearPathAnimator((int)(targetX+attractionFactor*(getCenterX()-targetX)),(int)(targetY+attractionFactor*(getCenterY()-targetY)),timeInterpolator,duration,startDelay);
    }

    /** Add an animator to move the embedded Drawable or View object along some path consisting of multiple linear segments.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param path Path of the animation = vertical / horizontal centers of the object along its way
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addLinearSegmentsPathAnimator(ArrayList<Point> path, int duration) {
        return addLinearSegmentsPathAnimator(path,new LinearInterpolator(),duration,0);
    }

    /** Add an animator to move the embedded Drawable or View object along some path consisting of multiple linear segments.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param path Path of the animation = vertical / horizontal centers of the object along its way
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addLinearSegmentsPathAnimator(ArrayList<Point> path, TimeInterpolator timeInterpolator, int duration) {
        return addLinearSegmentsPathAnimator(path,timeInterpolator,duration,0);
    }

    /** Add an animator to move the embedded Drawable or View object along some path consisting of multiple linear segments.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *
     * @param path Path of the animation = vertical / horizontal centers of the object along its way
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addLinearSegmentsPathAnimator(ArrayList<Point> path, int duration, int startDelay) {
        return addLinearSegmentsPathAnimator(path,new LinearInterpolator(),duration,startDelay);
    }

    /** Add an animator to move the embedded Drawable or View object along some path consisting of multiple linear segments.
    *
    * @param path Path of the animation = vertical / horizontal centers of the object along its way
    * @param timeInterpolator TimeInterpolator to control the timing of the animation
    * @param duration Duration of the animation (in ms)
    * @param startDelay Start delay of the animation (in ms)
    * @return The new animator
    */

    public Animator addLinearSegmentsPathAnimator(ArrayList<Point> path, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        int x[] = new int[path.size()];
        int i = 0;
        for (Iterator<Point> it = path.iterator(); it.hasNext(); i++)
            x[i] = it.next().x;
        ObjectAnimator animX = ObjectAnimator.ofInt(this,"centerX",x);
        animX.setInterpolator(timeInterpolator);
        addAnimator(animX);
        int y[] = new int[path.size()];
        i = 0;
        for (Iterator<Point> it = path.iterator(); it.hasNext(); i++)
            y[i] = it.next().y;
        ObjectAnimator animY = ObjectAnimator.ofInt(this,"centerY",y);
        animY.setInterpolator(timeInterpolator);
        addAnimator(animY);
        AnimatorSet animSet = playTogether(animX,animY);
        animSet.setStartDelay(startDelay);
        animSet.setDuration(duration);
        return animSet;
    }

    /** Add an animator to move the object along a quadratic Bezier curve.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param controlX X coordinate of the control point
     * @param controlY Y coordinate of the control point
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addBezierPathAnimator(int controlX, int controlY, int targetX, int targetY, int duration) {
        return addBezierPathAnimator(controlX,controlY,targetX,targetY,new LinearInterpolator(),duration,0);
    }

    /** Add an animator to move the object along a quadratic Bezier curve.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param controlX X coordinate of the control point
     * @param controlY Y coordinate of the control point
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addBezierPathAnimator(int controlX, int controlY, int targetX, int targetY, TimeInterpolator timeInterpolator, int duration) {
        return addBezierPathAnimator(controlX,controlY,targetX,targetY,timeInterpolator,duration,0);
    }

    /** Add an animator to move the object along a quadratic Bezier curve.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *
     * @param controlX X coordinate of the control point
     * @param controlY Y coordinate of the control point
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addBezierPathAnimator(int controlX, int controlY, int targetX, int targetY, int duration, int startDelay) {
        return addBezierPathAnimator(controlX,controlY,targetX,targetY,new LinearInterpolator(),duration,startDelay);
    }

    /** Add an animator to move the object along a quadratic Bezier curve.
     *
     * @param controlX X coordinate of the control point
     * @param controlY Y coordinate of the control point
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addBezierPathAnimator(int controlX, int controlY, int targetX, int targetY, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        BezierEvaluator eval = new BezierEvaluator(new Point(controlX,controlY));
        ObjectAnimator anim = ObjectAnimator.ofObject(this,"center",eval,getCenter(),new Point(targetX,targetY));
        anim.setInterpolator(timeInterpolator);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to move the object along a cubic Bezier curve.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param control1X X coordinate of the first control point
     * @param control1Y Y coordinate of the first control point
     * @param control2X X coordinate of the second control point
     * @param control2Y Y coordinate of the second control point
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addBezierPathAnimator(int control1X, int control1Y, int control2X, int control2Y, int targetX, int targetY, int duration) {
        return addBezierPathAnimator(control1X, control1Y, control2X, control2Y, targetX, targetY, new LinearInterpolator(), duration, 0);
    }

    /** Add an animator to move the object along a cubic Bezier curve.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param control1X X coordinate of the first control point
     * @param control1Y Y coordinate of the first control point
     * @param control2X X coordinate of the second control point
     * @param control2Y Y coordinate of the second control point
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addBezierPathAnimator(int control1X, int control1Y, int control2X, int control2Y, int targetX, int targetY, TimeInterpolator timeInterpolator, int duration) {
        return addBezierPathAnimator(control1X, control1Y, control2X, control2Y, targetX, targetY, timeInterpolator, duration, 0);
    }

    /** Add an animator to move the object along a cubic Bezier curve.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *
     * @param control1X X coordinate of the first control point
     * @param control1Y Y coordinate of the first control point
     * @param control2X X coordinate of the second control point
     * @param control2Y Y coordinate of the second control point
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addBezierPathAnimator(int control1X, int control1Y, int control2X, int control2Y, int targetX, int targetY, int duration, int startDelay) {
        return addBezierPathAnimator(control1X, control1Y, control2X, control2Y, targetX, targetY, new LinearInterpolator(), duration, startDelay);
    }

    /** Add an animator to move the object along a cubic Bezier curve.
     *
     * @param control1X X coordinate of the first control point
     * @param control1Y Y coordinate of the first control point
     * @param control2X X coordinate of the second control point
     * @param control2Y Y coordinate of the second control point
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addBezierPathAnimator(int control1X, int control1Y, int control2X, int control2Y, int targetX, int targetY, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        BezierEvaluator eval = new BezierEvaluator(new Point(control1X,control1Y),new Point(control2X,control2Y));
        ObjectAnimator anim = ObjectAnimator.ofObject(this,"center",eval,getCenter(),new Point(targetX,targetY));
        anim.setInterpolator(timeInterpolator);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to move the object along an arc of a circle around a specified center.<BR>
     *  The circle is assumed to go through the current center of the embedded object, as returned by getCenterX() and getCenterY().
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param centerCircleX X coordinate of the circle center
     * @param centerCircleY Y coordinate of the circle center
     * @param angle Angle of the arc in radians (positive = clockwise movement, negative = counterclockwise movement)
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addArcPathAnimator(int centerCircleX, int centerCircleY, double angle, int duration) {
        return addArcPathAnimator(centerCircleX,centerCircleY,angle,new LinearInterpolator(),duration,0);
    }

    /** Add an animator to move the object along an arc of a circle around a specified center.<BR>
     *  The circle is assumed to go through the current center of the embedded object, as returned by getCenterX() and getCenterY().
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param centerCircleX X coordinate of the circle center
     * @param centerCircleY Y coordinate of the circle center
     * @param angle Angle of the arc in radians (positive = clockwise movement, negative = counterclockwise movement)
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addArcPathAnimator(int centerCircleX, int centerCircleY, double angle, TimeInterpolator timeInterpolator, int duration) {
        return addArcPathAnimator(centerCircleX,centerCircleY,angle,timeInterpolator,duration,0);
    }

    /** Add an animator to move the object along an arc of a circle around a specified center.<BR>
     *  The circle is assumed to go through the current center of the embedded object, as returned by getCenterX() and getCenterY().
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *
     * @param centerCircleX X coordinate of the circle center
     * @param centerCircleY Y coordinate of the circle center
     * @param angle Angle of the arc in radians (positive = clockwise movement, negative = counterclockwise movement)
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addArcPathAnimator(int centerCircleX, int centerCircleY, double angle, int duration, int startDelay) {
        return addArcPathAnimator(centerCircleX,centerCircleY,angle,new LinearInterpolator(),duration,startDelay);
    }

    /** Add an animator to move the object along an arc of a circle around a specified center.<BR>
     *  The circle is assumed to go through the current center of the embedded object, as returned by getCenterX() and getCenterY().
     *
     * @param centerCircleX X coordinate of the circle center
     * @param centerCircleY Y coordinate of the circle center
     * @param angle Angle of the arc in radians (positive = clockwise movement, negative = counterclockwise movement)
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addArcPathAnimator(int centerCircleX, int centerCircleY, double angle, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        ArcEvaluator eval = new ArcEvaluator(new Point(centerCircleX,centerCircleY),angle);
        Point dummy = new Point(0,0);
        ObjectAnimator anim = ObjectAnimator.ofObject(this,"center",eval,dummy,dummy);
        anim.setInterpolator(timeInterpolator);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to move the object along an arc of a circle around a specified center.<BR>
     *  The circle is assumed to go through the current center of the embedded object, as returned by getCenterX() and getCenterY().
     *  (Use this method if you want to specify the arc by the target point on the circle (and not by the angle).
     *
     * @param centerCircleX X coordinate of the circle center
     * @param centerCircleY Y coordinate of the circle center
     * @param targetX X coordinate of the target point on the circle (note: the target point must lie on the circle)
     * @param targetY Y coordinate of the target point on the circle (note: the target point must lie on the circle)
     * @param clockwise Movement clockwise or anticlockwise
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addArcPathAnimator(int centerCircleX, int centerCircleY, int targetX, int targetY, boolean clockwise, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        Point center = new Point(centerCircleX,centerCircleY);
        Point start = new Point(getCenterX(),getCenterY());
        Point target = new Point(targetX,targetY);
        double angle = 2* Math.asin((GraphicsUtilsCV.distance(start,target)/2)/ GraphicsUtilsCV.distance(start,center));
        if (!clockwise) angle = -angle;
        return addArcPathAnimator(centerCircleX,centerCircleY,angle,timeInterpolator,duration,startDelay);
    }

    /** Add an animator to move the object along a circle around a specified center.<BR>
     *  The circle is assumed to go through the current center of the embedded object, as returned by getCenterX() and getCenterY().<BR>
     *  Note: The object will start from its initial position and rotate infinitely. Therefore, this animation cannot be combined with other animations that modify the location of the object.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param centerCircleX X coordinate of the circle center
     * @param centerCircleY Y coordinate of the circle center
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addCirclePathAnimator(int centerCircleX, int centerCircleY, int duration) {
        return addCirclePathAnimator(centerCircleX,centerCircleY,new LinearInterpolator(),duration,0);
    }

    /** Add an animator to move the object along a circle around a specified center.<BR>
     *  The circle is assumed to go through the current center of the embedded object, as returned by getCenterX() and getCenterY().<BR>
     *  Note: The object will start from its initial position and rotate infinitely. Therefore, this animation cannot be combined with other animations that modify the location of the object.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param centerCircleX X coordinate of the circle center
     * @param centerCircleY Y coordinate of the circle center
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addCirclePathAnimator(int centerCircleX, int centerCircleY, TimeInterpolator timeInterpolator, int duration) {
        return addCirclePathAnimator(centerCircleX,centerCircleY,timeInterpolator,duration,0);
    }

    /** Add an animator to move the object along a circle around a specified center.<BR>
     *  The circle is assumed to go through the current center of the embedded object, as returned by getCenterX() and getCenterY().<BR>
     *  Note: The object will start from its initial position and rotate infinitely. Therefore, this animation cannot be combined with other animations that modify the location of the object.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *
     * @param centerCircleX X coordinate of the circle center
     * @param centerCircleY Y coordinate of the circle center
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addCirclePathAnimator(int centerCircleX, int centerCircleY, int duration, int startDelay) {
        return addCirclePathAnimator(centerCircleX,centerCircleY,new LinearInterpolator(),duration,startDelay);
    }

    /** Add an animator to move the object along a circle around a specified center.<BR>
     *  The circle is assumed to go through the current center of the embedded object, as returned by getCenterX() and getCenterY().
     *  Note: The object will start from its initial position and rotate clockwise infinitely. Therefore, this animation cannot be combined with other animations that modify the location of the object.
     *
     * @param centerCircleX X coordinate of the circle center
     * @param centerCircleY Y coordinate of the circle center
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addCirclePathAnimator(int centerCircleX, int centerCircleY, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        return addCirclePathAnimator(centerCircleX,centerCircleY,true,timeInterpolator,duration,startDelay);
    }

    /** Add an animator to move the object along a circle around a specified center.<BR>
     *  The circle is assumed to go through the current center of the embedded object, as returned by getCenterX() and getCenterY().
     *  Note: The object will start from its initial position and rotate infinitely. Therefore, this animation cannot be combined with other animations that modify the location of the object.
     *
     * @param centerCircleX X coordinate of the circle center
     * @param centerCircleY Y coordinate of the circle center
     * @param clockwise Movement clockwise or anticlockwise?
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addCirclePathAnimator(int centerCircleX, int centerCircleY, boolean clockwise, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        double angle = 2* Math.PI;
        if (!clockwise) angle = -angle;
        ArcEvaluator eval = new ArcEvaluator(new Point(centerCircleX,centerCircleY),angle);
        ObjectAnimator anim = ObjectAnimator.ofObject(this,"center",eval,getCenter(),new Point(0,0));
//        CircleEvaluator eval = new CircleEvaluator(getCenter(),new Point(centerCircleX,centerCircleY));
//        ObjectAnimator anim = ObjectAnimator.ofObject(this,"center",eval,new Point(0,0),new Point(0,0));
        anim.setInterpolator(timeInterpolator);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to move the object infinitely along an ellipse around a specified center.<BR>
     * The ellipse is implemented by a circle that is compressed vertically by a specified compression factor
     * and then rotated by a specified angle around its center.<BR>
     * The object is assumed to lie initially on this ellipse. Use the method GraphicsUtils.pointsOnEllipse()
     * to calculate such points.
     *
     * @param centerEllipseX X coordinate of the center of the ellipse
     * @param centerEllipseY Y coordinate of the center of the ellipse
     * @param radius;   // Radius of the circle
     * @param comprFactor; // Vertical compression of the circle (0<=comprFactor<=1), yielding the ellipse
     * @param rotAngleEllipse; // Rotation angle of the resulting ellipse (in radians)
     * @param clockwise Movement clockwise or anticlockwise?
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of one round of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addEllipseAnimator(int centerEllipseX, int centerEllipseY, int radius, double comprFactor, double rotAngleEllipse, boolean clockwise, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        return addEllipseAnimator(centerEllipseX, centerEllipseY, radius, comprFactor, rotAngleEllipse, clockwise, ValueAnimator.INFINITE, timeInterpolator, duration, startDelay);
    }

    /** Add an animator to move the object along an ellipse around a specified center.<BR>
     * The ellipse is implemented by a circle that is compressed vertically by a specified compression factor
     * and then rotated by a specified angle around its center.<BR>
     * The object is assumed to lie initially on this ellipse. Use the method GraphicsUtils.pointsOnEllipse()
     * to calculate such points.
     *
     * @param centerEllipseX X coordinate of the center of the ellipse
     * @param centerEllipseY Y coordinate of the center of the ellipse
     * @param radius; Radius of the circle
     * @param comprFactor; Vertical compression of the circle (0<=comprFactor<=1), yielding the ellipse
     * @param rotAngleEllipse; Rotation angle of the resulting ellipse (in radians)
     * @param clockwise Movement clockwise or anticlockwise?
     * @param repeatCount Number of rotations
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of one round of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addEllipseAnimator(int centerEllipseX, int centerEllipseY, int radius, double comprFactor, double rotAngleEllipse, boolean clockwise, int repeatCount, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        double startAngle = 0;
        ArcEllipseEvaluator eval = new ArcEllipseEvaluator(new Point(centerEllipseX,centerEllipseY), radius, comprFactor, rotAngleEllipse, 2* Math.PI, clockwise);
        ObjectAnimator anim = ObjectAnimator.ofObject(this,"center",eval,getCenter(),new Point(0,0));
        anim.setInterpolator(timeInterpolator);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        anim.setRepeatCount(repeatCount);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to move the object along an archimedian spiral,
     * starting from the current center of the object and moving outwards.
     *
     * @param numberRotations The number of rotations around the center to be performed (fractions are allowed)
     * @param targetDistance The target distance of the object to the center
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addSpiralPathOutwardAnimator(double numberRotations, int targetDistance, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        SpiralOutwardEvaluator eval = new SpiralOutwardEvaluator(new Point(getCenterX(),getCenterY()),numberRotations,targetDistance);
        Point dummy = new Point(0,0);
        ObjectAnimator anim = ObjectAnimator.ofObject(this,"center",eval,dummy,dummy);
        anim.setInterpolator(timeInterpolator);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to move the object along an archimedian spiral,
     * starting from the current position of the object inwards towards the center of the spiral.
     *
     * @param centerSpiralX X coordinate of the spiral center
     * @param centerSpiralY Y coordinate of the spiral center
     * @param numberRotations The number of rotations around the center to be performed
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addSpiralPathInwardAnimator(int centerSpiralX, int centerSpiralY, int numberRotations, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        SpiralInwardEvaluator eval = new SpiralInwardEvaluator(new Point(centerSpiralX,centerSpiralY),numberRotations);
        Point dummy = new Point(0,0);
        ObjectAnimator anim = ObjectAnimator.ofObject(this,"center",eval,dummy,dummy);
        anim.setInterpolator(timeInterpolator);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to modify the size the embedded Drawable or View object.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *  The start delay is zero, i.e. the animation starts immediately.
     *  N.B.: For View objects, the size animator might not work properly due to interference with the embedding RelativeLayout
     *
     * @param targetWidth Target width
     * @param targetHeight Target height
     * @param duration Duration of the animation (in ms)
     * @return The new animator (= set with two animators for the width and the height, respectively)
     */

    public AnimatorSet addSizeAnimator(int targetWidth, int targetHeight, int duration) {
        return addSizeAnimator(targetWidth,targetHeight,new LinearInterpolator(),duration,0);
    }

    /** Add an animator to modify the size the embedded Drawable or View object.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *  N.B.: For View objects, the size animator might not work properly due to interference with the embedding RelativeLayout
     *
     * @param targetWidth Target width
     * @param targetHeight Target height
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator (= set with two animators for the width and the height, respectively)
     */

    public AnimatorSet addSizeAnimator(int targetWidth, int targetHeight, int duration, int startDelay) {
        return addSizeAnimator(targetWidth,targetHeight,new LinearInterpolator(),duration,startDelay);

    }

    /** Add an animator to modify the size the embedded Drawable or View object.
     *  The start delay is zero, i.e. the animation starts immediately.
     *  N.B.: For View objects, the size animator might not work properly due to interference with the embedding RelativeLayout
     *
     * @param targetWidth Target width
     * @param targetHeight Target height
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @return The new animator (= set with two animators for the width and the height, respectively)
     */

    public AnimatorSet addSizeAnimator(int targetWidth, int targetHeight, TimeInterpolator timeInterpolator, int duration) {
        return addSizeAnimator(targetWidth,targetHeight,timeInterpolator,duration,0);
    }

    /** Add an animator to modify the size the embedded Drawable or View object.
     *  N.B.: For View objects, the size animator might not work properly due to interference with the embedding RelativeLayout
     *
     * @param targetWidth Target width
     * @param targetHeight Target height
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator (= set with two animators for the width and the height, respectively)
     */

    public AnimatorSet addSizeAnimator(int targetWidth, int targetHeight, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        ObjectAnimator animWidth = ObjectAnimator.ofInt(this,"width",targetWidth);
        animWidth.setInterpolator(timeInterpolator);
        addAnimator(animWidth);
        ObjectAnimator animHeight = ObjectAnimator.ofInt(this,"height",targetHeight);
        animHeight.setInterpolator(timeInterpolator);
        addAnimator(animHeight);
        AnimatorSet animSet = playTogether(animWidth,animHeight);
        animSet.setStartDelay(startDelay);
        animSet.setDuration(duration);
        return animSet;
    }

    /** Add an animator to modify the size of the embedded Drawable or View object.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *  The start delay is zero, i.e. the animation starts immediately.
     *  N.B.: For View objects, the size animator might not work properly due to interference with the embedding RelativeLayout
     *
     * @param sizes Sequence of the sizes (as pairs of width and height)
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addSizeAnimator(ArrayList<Point> sizes, int duration) {
        return addSizeAnimator(sizes,new LinearInterpolator(),duration,0);
    }

    /** Add an animator to modify the size of the embedded Drawable or View object.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *  N.B.: For View objects, the size animator might not work properly due to interference with the embedding RelativeLayout
     *
     * @param sizes Sequence of the sizes (as pairs of width and height)
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addSizeAnimator(ArrayList<Point> sizes, int duration, int startDelay) {
        return addSizeAnimator(sizes,new LinearInterpolator(),duration,startDelay);
    }

    /** Add an animator to modify the size of the embedded Drawable or View object.
     *  The start delay is zero, i.e. the animation starts immediately.
     *  N.B.: For View objects, the size animator might not work properly due to interference with the embedding RelativeLayout
     *
     * @param sizes Sequence of the sizes (as pairs of width and height)
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addSizeAnimator(ArrayList<Point> sizes, TimeInterpolator timeInterpolator, int duration) {
        return addSizeAnimator(sizes,timeInterpolator,duration,0);
    }

    /** Add an animator to modify the size of the embedded Drawable or View object.
     *  N.B.: For View objects, the size animator might not work properly due to interference with the embedding RelativeLayout
     *
     * @param sizes Sequence of the sizes (as pairs of width and height)
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addSizeAnimator(ArrayList<Point> sizes, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        int widths[] = new int[sizes.size()];
        int i = 0;
        for (Iterator<Point> it = sizes.iterator(); it.hasNext(); i++)
            widths[i] = it.next().x;
        ObjectAnimator animWidth = ObjectAnimator.ofInt(this,"width",widths);
        animWidth.setInterpolator(timeInterpolator);
        addAnimator(animWidth);
        int heights[] = new int[sizes.size()];
        i = 0;
        for (Iterator<Point> it = sizes.iterator(); it.hasNext(); i++)
            heights[i] = it.next().y;
        ObjectAnimator animHeight = ObjectAnimator.ofInt(this,"height",heights);
        animHeight.setInterpolator(timeInterpolator);
        addAnimator(animHeight);
        AnimatorSet animSet = playTogether(animWidth,animHeight);
        animSet.setStartDelay(startDelay);
        animSet.setDuration(duration);
        return animSet;
    }

    /** Add an animator to zoom the object.
     *  N.B.: For View objects, the size animator might not work properly due to interference with the embedding RelativeLayout
     *
     * @param zoomFactor Factor by which the size of the object shall be modified
     * @param duration Duration of the animation (in ms)
     * @return The new animator (= set with two animators for the width and the height, respectively)
     */

    public AnimatorSet addZoomAnimator(double zoomFactor, int duration) {
        return addZoomAnimator(zoomFactor,new LinearInterpolator(),duration);
    }

    /** Add an animator to zoom the object.
     *  N.B.: For View objects, the size animator might not work properly due to interference with the embedding RelativeLayout
     *
     * @param zoomFactor Factor by which the size of the object shall be modified
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator (= set with two animators for the width and the height, respectively)
     */

    public AnimatorSet addZoomAnimator(double zoomFactor, int duration, int startDelay) {
        return addZoomAnimator(zoomFactor,new LinearInterpolator(),duration,startDelay);
    }

    /** Add an animator to zoom the object.
     *  N.B.: For View objects, the size animator might not work properly due to interference with the embedding RelativeLayout
     *
     * @param zoomFactor Factor by which the size of the object shall be modified
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @return The new animator (= set with two animators for the width and the height, respectively)
     */

    public AnimatorSet addZoomAnimator(double zoomFactor, TimeInterpolator timeInterpolator, int duration) {
        return addZoomAnimator(zoomFactor,timeInterpolator,duration,0);
    }

    /** Add an animator to zoom the object.
     *  N.B.: For View objects, the size animator might not work properly due to interference with the embedding RelativeLayout
     *
     * @param zoomFactor Factor by which the size of the object shall be modified
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator (= set with two animators for the width and the height, respectively)
     */

    public AnimatorSet addZoomAnimator(double zoomFactor, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        TypeEvaluator evalWidth = new ScaleWidthEvaluator(zoomFactor);
        ObjectAnimator animWidth = ObjectAnimator.ofObject(this,"width",evalWidth,0,0);
        animWidth.setInterpolator(timeInterpolator);
        addAnimator(animWidth);
        TypeEvaluator evalHeight = new ScaleHeightEvaluator(zoomFactor);
        ObjectAnimator animHeight = ObjectAnimator.ofObject(this,"height",evalHeight,0,0);
        animHeight.setInterpolator(timeInterpolator);
        addAnimator(animHeight);
        AnimatorSet animSet = playTogether(animWidth,animHeight);
        animSet.setStartDelay(startDelay);
        animSet.setDuration(duration);
        return animSet;
    }

    /** Add an animator to modify the rotation angle of the embedded Drawable or View object
     * (for embedded Drawable objects only effective if of subclass RotatableDrawableCV).
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param targetAngle Target angle of the object in radians (= the absolute angle reached in the end, i.e. not the rotation angle)
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addRotationAnimator(float targetAngle, int duration) {
      return addRotationAnimator(targetAngle, new LinearInterpolator(), duration, 0);
    }

    /** Add an animator to modify the rotation angle of the embedded Drawable or View object
     * (for embedded Drawable objects only effective if of subclass RotatableDrawableCV).
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *
     * @param targetAngle Target angle of the object in radians (= the absolute angle reached in the end, i.e. not the rotation angle)
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addRotationAnimator(float targetAngle, int duration, int startDelay) {
        return addRotationAnimator(targetAngle, new LinearInterpolator(), duration, startDelay);
    }

    /** Add an animator to modify the rotation angle of the embedded Drawable or View object
     * (for embedded Drawable objects only effective if of subclass RotatableDrawableCV).
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param targetAngle Target angle of the object in radians (= the absolute angle reached in the end, i.e. not the rotation angle)
     * @param duration Duration of the animation (in ms)
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @return The new animator
     */

    public Animator addRotationAnimator(float targetAngle, TimeInterpolator timeInterpolator, int duration) {
        return addRotationAnimator(targetAngle, timeInterpolator, duration, 0);
    }

    /** Add an animator to modify the rotation angle of the embedded Drawable or View object
     * (for embedded Drawable objects only effective if of subclass RotatableDrawableCV).
     *
     * @param targetAngle Target angle of the object in radians (= the absolute angle reached in the end, i.e. not the rotation angle)
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addRotationAnimator(float targetAngle, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        if (!(guiObject instanceof RotatableDrawableCV || guiObject instanceof View)) return null;
        ObjectAnimator animRotation = ObjectAnimator.ofFloat(this,"rotationAngle",targetAngle);
        animRotation.setInterpolator(timeInterpolator);
        animRotation.setDuration(duration);
        animRotation.setStartDelay(startDelay);
        addAnimator(animRotation);
        return animRotation;
    }

    /** Add an animator to rotate the embedded Drawable or View object infinitely
     * (for embedded Drawable objects only effective if of subclass RotatableDrawableCV).
     *
     * @param roundDuration Duration of the one rotation (in ms)
     * @return The new animator
     */

    public Animator addInfiniteRotationAnimator(int roundDuration) {
        if (!(guiObject instanceof RotatableDrawableCV || guiObject instanceof View)) return null;
        ObjectAnimator animRotation = ObjectAnimator.ofFloat(this,"rotationAngle",(float)(2* Math.PI));
        animRotation.setInterpolator(new LinearInterpolator());
        animRotation.setDuration(roundDuration);
        animRotation.setRepeatCount(ValueAnimator.INFINITE);
        addAnimator(animRotation);
        return animRotation;
    }

    /** Add an animator to right-left shake the embedded Drawable or View object for a number of times
     * (for embedded Drawable objects only effective if of subclass RotatableDrawableCV).
     *
     * @param targetAngle Shaking angle
     * @param durationShakeRound Duration of one complete right-left shake (in ms)
     * @param noShakes Number of shakes to be performed
     * @return The new animator
     */

    public Animator addShakeAnimator(float targetAngle, int durationShakeRound, int noShakes) {
        if (!(guiObject instanceof RotatableDrawableCV || guiObject instanceof View)) return null;
        Animator[] animators = new Animator[noShakes*3];
        for (int i=0;i<noShakes;i++) {
            ObjectAnimator anim = ObjectAnimator.ofFloat(this, "rotationAngle", targetAngle);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(durationShakeRound/4);
            animators[3*i] = anim;
            anim = ObjectAnimator.ofFloat(this, "rotationAngle", -targetAngle);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(durationShakeRound/2);
            animators[3*i+1] = anim;
            anim = ObjectAnimator.ofFloat(this, "rotationAngle", 0);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(durationShakeRound/4);
            animators[3*i+2] = anim;
        }
        Animator anim = playSequentially(animators);
        return anim;
    }

    /** Add an animator to modify the color of the embedded Drawable or View object.
     *  The start delay is zero, i.e. the animation starts immediately.
     *  For a Drawable object, the color can be animated only if it is of type ShapeDrawable, DrawableTextCV, or DrawableTextWithIconCV.
     *
     * @param targetColor Target color
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addColorAnimator(int targetColor, int duration) {
        return addColorAnimator(targetColor,duration,0);
    }

    /** Add an animator to modify the color of the embedded Drawable or View object.
     *  For a Drawable object, the color can be animated only if it is of type ShapeDrawable, DrawableTextCV, or DrawableTextWithIconCV.
     *
     * @param targetColor Target color
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addColorAnimator(int targetColor, int duration, int startDelay) {
        ObjectAnimator anim = ObjectAnimator.ofArgb(this,"color",targetColor);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to modify the border color of the embedded object of class DrawableTextWithIconCV
     *  (has an effect only for objects of this type)
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param targetColor Target color
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addColorBorderAnimator(int targetColor, int duration) {
        return addColorBorderAnimator(targetColor,duration,0);
    }

    /** Add an animator to modify the border color of the embedded object of class DrawableTextWithIconCV
     *  (has an effect only for objects of this type)
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param targetColor Target color
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addColorBorderAnimator(int targetColor, int duration, int startDelay) {
        if (!(guiObject instanceof RotatableDrawableCV)) return null;
        Drawable drw = ((RotatableDrawableCV) guiObject).getDrawable();
        if (!(drw instanceof DrawableTextWithIconCV)) return null;
        ObjectAnimator anim = ObjectAnimator.ofArgb(this,"colorBorder",((DrawableTextWithIconCV) drw).getColorBorder(),targetColor);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to modify the border stroke width of the embedded object of class DrawableTextWithIconCV
     *  (has an effect only for objects of this type)
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param targetStrokewidth Target stroke width
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addStrokewidthBorderAnimator(int targetStrokewidth, int duration) {
        return addStrokewidthBorderAnimator(targetStrokewidth,duration,0);
    }

    /** Add an animator to modify the border stroke width of the embedded object of class DrawableTextWithIconCV
     *  (has an effect only for objects of this type)
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param targetStrokewidth Target border stroke width
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addStrokewidthBorderAnimator(int targetStrokewidth, int duration, int startDelay) {
        if (!(guiObject instanceof RotatableDrawableCV)) return null;
        Drawable drw = ((RotatableDrawableCV) guiObject).getDrawable();
        if (!(drw instanceof DrawableTextWithIconCV)) return null;
        ObjectAnimator anim = ObjectAnimator.ofInt(this,"strokewidthBorder",((DrawableTextWithIconCV) drw).getStrokewidthBorder(), targetStrokewidth);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to let the Drawable object blink.
     *  The LinearInterpolator() is used to control the timing of the animation, i.e. the animation runs at constant speed.
     *
     * @param durInterval Duration of a blink interval (in ms)
     * @return The new animator
     */

    public Animator addBlinkAnimator(int durInterval) {
        return addBlinkAnimator(new LinearInterpolator(),durInterval);
    }

    /** Add an animator to let the Drawable object blink.
     *
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param durInterval Duration of a blink interval (in ms)
     * @return The new animator
     */

    public Animator addBlinkAnimator(TimeInterpolator timeInterpolator, int durInterval) {
        int currentColor = getColor();
        ObjectAnimator anim = ObjectAnimator.ofArgb(this,"color",currentColor,currentColor&0x00FFFFFF);
        anim.setInterpolator(timeInterpolator);
        anim.setDuration(durInterval);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to let the object appear on the display after a specified delay.
     *
     * @param delay Duration of time after which the object will appear (in ms)
     * @return The new animator
     */

    public Animator addAppearanceAnimator(int delay) {
        TrueOnEndEvaluator eval = new TrueOnEndEvaluator();
        ObjectAnimator anim = ObjectAnimator.ofObject(this,"visible",eval,false,true);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(delay);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to let the object fade in.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addFadeInAnimator(int duration) {
        return addFadeInAnimator(duration,0);
    }

    /** Add an animator to let an object fade in.
     *
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addFadeInAnimator(int duration, int startDelay) {
        // int objectColor = getColor();
        // ObjectAnimator anim = ObjectAnimator.ofArgb(this,"color",objectColor&0x00FFFFFF,objectColor);
        ObjectAnimator anim = ObjectAnimator.ofInt(this,"transparency",0,0xFF);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to let the object fade out.
     *  The start delay is zero, i.e. the animation starts immediately.
     *
     * @param duration Duration of the animation (in ms)
     * @return The new animator
     */

    public Animator addFadeOutAnimator(int duration) {
        return addFadeOutAnimator(duration,0);
    }

    /** Add an animator to let an object fade out.
     *
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return The new animator
     */

    public Animator addFadeOutAnimator(int duration, int startDelay) {
        ObjectAnimator anim = ObjectAnimator.ofInt(this,"transparency",255,0);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        addAnimator(anim);
        return anim;
    }

    /** Add an animator to modify the z value of an object in two phases.
     *
     * @param zValue1 The z value the object shall have in the first phase
     * @param zValue2 The z value the object shall have in the second phase
     * @param durPhase1 The duration of the first phase (in ms)
     * @param durPhase2 The duration of the second phase (in ms)
     * @param startDelay Initial delay before the animation starts (in ms)
     * @return The new animator
     */

    public Animator addZAnimator(int zValue1, int zValue2, int durPhase1, int durPhase2, int startDelay) {
        TwoIntsEvaluator eval = new TwoIntsEvaluator(durPhase1,durPhase2);
        ObjectAnimator anim = ObjectAnimator.ofObject(this,"zindex",eval,zValue1,zValue2);
        anim.setInterpolator(new LinearInterpolator());
        anim.setStartDelay(startDelay);
        anim.setDuration(durPhase1+durPhase2);
        addAnimator(anim);
        return anim;
    }

    /** Class that defines a TypeEvaluator which will return true when the animation period has elapsed.
     */

    private class TrueOnEndEvaluator implements TypeEvaluator<Boolean> {
        public Boolean evaluate(float f, Boolean dummy1, Boolean dummy2) {
            if (f>=0.999999) return true;
            else return false;
        }
    }

    /** Class that defines a TypeEvaluator which will scale the width of an object,
     * i.e. start with the original width and end with a width that equals the original width multiplied with a scaling factor.
     */

    private class ScaleWidthEvaluator implements TypeEvaluator<Integer> {
        double scalingfactor;
        int initWidth;
        boolean attributeIsValid;
        // The initial width of the object is stored when the animation starts.
        // 'attributeIsValid' specifies if this has been done already.
        ScaleWidthEvaluator(double scalingfactor) {
            this.scalingfactor = scalingfactor;
            this.attributeIsValid = false;
        }
        public Integer evaluate(float f, Integer dummy1, Integer dummy2) {
            if (!attributeIsValid) {
                initWidth = AnimatedGuiObjectCV.this.getWidth();
                attributeIsValid = true;
            }
            return (int)((1-f)*initWidth+f*scalingfactor*initWidth);
        }
    }

    /** Class that defines a TypeEvaluator which will scale the height of an object,
     * i.e. start with the original height and end with a height that equals the original height multiplied with a scaling factor.
     */

    private class ScaleHeightEvaluator implements TypeEvaluator<Integer> {
        double scalingfactor;
        int initHeight;
        boolean attributeIsValid;
        // The initial height of the object is stored when the animation starts.
        // 'attributeIsValid' specifies if this has been done already.
        ScaleHeightEvaluator(double scalingfactor) {
            this.scalingfactor = scalingfactor;
            this.attributeIsValid = false;
        }
        public Integer evaluate(float f, Integer dummy1, Integer dummy2) {
            if (!attributeIsValid) {
                initHeight = AnimatedGuiObjectCV.this.getHeight();
                attributeIsValid = true;
            }
            return (int)((1-f)*initHeight+f*scalingfactor*initHeight);
        }
    }

    /** Class that defines a TypeEvaluator which will return one of two integer values
     * - the first integer in the first phase of the animation and the second integer in the second phase.<BR>
     * Can be used, e.g., to modify the z value of an object, thus showing it before and behind another object.
     */

    private class TwoIntsEvaluator implements TypeEvaluator<Integer> {
        private double relDur1;
        public TwoIntsEvaluator(int durPhase1, int durPhase2) {
            this.relDur1 = (double)durPhase1/(durPhase1+durPhase2);
        }
        public Integer evaluate(float f, Integer i1, Integer i2) {
            if (f<=relDur1) return i1;
            else return i2;
        }
    }

    /** Class that defines a TypeEvaluator for a movement along an arc of a circle.<BR>
     * The evaluator assumes that the center of the object shall be moving along a circle with given center that goes through the current center of the object.
     */

    private class ArcEvaluator implements TypeEvaluator<Point> {
        Point center; // center of the circle
        double angle; // angle of the arc to traverse
        boolean attributeIsValid;
        // The start position of the rotating object is stored when the animation starts.
        // 'attributeIsValid' specifies if this has been done already.
        Point start;
        ArcEvaluator(Point center, double angle) {
            this.center = new Point(center);
            this.angle = angle;
            this.attributeIsValid = false;
        }
        public Point evaluate(float f, Point dummy1, Point dummy2) {
            if (!attributeIsValid) {
                start = AnimatedGuiObjectCV.this.getCenter();
                attributeIsValid = true;
            }
            return GraphicsUtilsCV.rotate(start,center,f*angle);
        }
    }

    /** Class that defines a TypeEvaluator for a movement along an arc of a ellipse.
     * The ellipse is implemented by a circle that is compressed vertically by a specified compression factor
     * and then rotated by a specified angle around its center.
     */

    private class ArcEllipseEvaluator implements TypeEvaluator<Point> {
        Point centerEllipse; // center of the circle (= center of the ellipse)
        int radius;   // radius of the circle
        double comprFactor; // vertical compression of the circle (0<=comprFactor<=1), yielding the ellipse
        double rotAngleEllipse; // rotation angle of the resulting ellipse (in radians)
        double angleToTraverse;  // angle of the arc that the point shall traverse (in radians)
        boolean clockwise;  // clockwise or counter-clockwise rotation?
        double startAngle; // angle that specifies the start position of the moving point, referring to the circle, i.e. the uncompressed, unrotated ellipse (in radians)
        boolean startAngleIsValid; // the startAngle attribute is initialized in the first evaluator call; until then, startAngleIs Valid is false
        ArcEllipseEvaluator(Point centerEllipse, int radius, double comprFactor, double rotAngleEllipse, double angleToTraverse, boolean clockwise) {
            this.centerEllipse = new Point(centerEllipse);
            this.radius = radius;
            this.comprFactor = comprFactor;
            this.rotAngleEllipse = rotAngleEllipse;
            this.angleToTraverse = angleToTraverse;
            this.clockwise = clockwise;
            this.startAngleIsValid = false;
        }
        public Point evaluate(float f, Point dummy1, Point dummy2) {
            if (!startAngleIsValid) {
                Point startPoint = GraphicsUtilsCV.rotate(getCenter(),centerEllipse,-rotAngleEllipse);
                startPoint = new Point(startPoint.x,(int)(centerEllipse.y+(startPoint.y-centerEllipse.y)/comprFactor));
                startAngle = Math.acos((double)(startPoint.x-centerEllipse.x)/radius);
                if (startPoint.y > centerEllipse.y)
                    startAngle = 2 * Math.PI - startAngle;
                startAngleIsValid = true;
            }
            Point ptOnCircle;
            if (clockwise)
                ptOnCircle = GraphicsUtilsCV.rotate(new Point(centerEllipse.x+radius,centerEllipse.y),centerEllipse,startAngle-f*angleToTraverse);
            else
                ptOnCircle = GraphicsUtilsCV.rotate(new Point(centerEllipse.x+radius,centerEllipse.y),centerEllipse,startAngle+f*angleToTraverse);
            int compressedY = centerEllipse.y + (int)(comprFactor*(centerEllipse.y-ptOnCircle.y));
            Point ptOnEllipseUnrotated = new Point(ptOnCircle.x,compressedY);
            Point ptOnEllipse = GraphicsUtilsCV.rotate(ptOnEllipseUnrotated,centerEllipse,rotAngleEllipse);
            return ptOnEllipse;
        }
    }

    /** Class that defines a TypeEvaluator for an outward movement along a spiral.<BR>
     * The evaluator assumes that the center of the object shall be moving along a spiral curve starting at its current location
     * for a number of rotations to a target distance.
     */

    private class SpiralOutwardEvaluator implements TypeEvaluator<Point> {
        Point centerSpiral; // center of the spiral
        double numberRotations; // number of rotations to be performed
        double targetDistance; // target distance to the center
        double targetAngle; // target angle of the object
        SpiralOutwardEvaluator(Point centerSpiral, double numberRotations, int targetDistance) {
            this.centerSpiral = new Point(centerSpiral);
            this.numberRotations = numberRotations;
            this.targetDistance = targetDistance;
            this.targetAngle = 2* Math.PI*numberRotations;
        }
        public Point evaluate(float f, Point dummy1, Point dummy2) {
            int x = (int) (Math.cos(f * targetAngle) * f * targetDistance + centerSpiral.x);
            int y = (int) (Math.sin(f * targetAngle) * f * targetDistance + centerSpiral.y);
            return new Point(x, y);
        }
    }

    /** Class that defines a TypeEvaluator for an inward movement along a spiral.<BR>
     * The evaluator assumes that the center of the object shall be moving along a spiral curve starting at its current location
     * for a number of rotations to a specified spiral center.
     */

    private class SpiralInwardEvaluator implements TypeEvaluator<Point> {
        Point centerSpiral; // center of the spiral
        int numberRotations; // number of rotations to be performed
        double startAngle; // angle of the object at the beginning of the animation
        double startDistance; // distance of the object to the center at the beginning of the animation
        boolean attributesValid;
        SpiralInwardEvaluator(Point centerSpiral, int numberRotations) {
            this.centerSpiral = new Point(centerSpiral);
            this.numberRotations = numberRotations;
            this.startAngle = 2* Math.PI*numberRotations;
            this.attributesValid = false;
        }
        public Point evaluate(float f, Point dummy1, Point dummy2) {
            if (!attributesValid) {
                this.startDistance = GraphicsUtilsCV.distance(centerSpiral,getCenter());
                double currentObjectAngle = Math.acos((getCenterX()-centerSpiral.x)/startDistance);
                if (getCenterY() >= centerSpiral.y)
                    currentObjectAngle = 2 * Math.PI - currentObjectAngle;
                this.startAngle += currentObjectAngle;
                attributesValid = true;
            }
            int x = (int) (Math.cos(-(1-f) * startAngle) * (1-f) * startDistance + centerSpiral.x);
            int y = (int) (Math.sin(-(1-f) * startAngle) * (1-f) * startDistance + centerSpiral.y);
            return new Point(x,y);
        }
    }

    /** Class that defines a TypeEvaluator for a movement along a Bezier curve of order 2 or 3. */

    private class BezierEvaluator implements TypeEvaluator<Point> {

        /** order of the Bezier curve: 2 = quadratic, 3 = cubic; all other values are undefined */

        private int order;

        /** first control point */

        private Point contr1;

        /** second control point */

        private Point contr2;

        /** position of the object when the animation starts */

        Point start;

        /** Constructor for a quadratic Bezier curve
         *
         * @param contr1 Control point of the curve
         */

        BezierEvaluator(Point contr1) {
            this.start = null;
            this.contr1 = new Point(contr1);
            this.order = 2;
        }

        /** Constructor for a cubic Bezier curve
         *
         * @param contr1 First control point of the curve
         * @param contr2 Second control point of the curve
         */

        BezierEvaluator(Point contr1, Point contr2) {
            this.start = null;
            this.contr1 = new Point(contr1);
            this.contr2 = new Point(contr2);
            this.order = 3; }

        /** Evaluator */

        public Point evaluate(float f, Point dummy, Point target) {
            int x=0, y=0;
            if (start==null)
                start = getCenter();
            if (order==2) {
                x = (int) (((start.x - 2 * contr1.x + target.x) * f * f + (-2 * start.x + 2 * contr1.x) * f + start.x));
                y = (int) ((start.y-2*contr1.y+target.y)*f*f+(-2*start.y+2*contr1.y)*f+start.y);
            }
            if (order==3) {
                x = (int) ((1 - f) * (1 - f) * (1 - f) * start.x + 3 * (1 - f) * (1 - f) * f * contr1.x + 3 * (1 - f) * f * f * contr2.x + f * f * f * target.x);
                y = (int) ((1-f)*(1-f)*(1-f)*start.y+3*(1-f)*(1-f)*f*contr1.y+3*(1-f)*f*f*contr2.y+f*f*f*target.y);
            }
            return new Point(x,y);
        }

    }

    /** Register a listener to react upon property changes.
     *
     * @param listener The listener to be registered
     */

    public void addPropertyChangedListener(OnPropertyChangedListener listener) {
        propertyChangedListeners.add(listener);
    }

    /** Deregister a listener to react upon property changes.
     *
     * @param listener The listener to be deregistered
     */

    public void removePropertyChangedListener(OnPropertyChangedListener listener) {
        propertyChangedListeners.remove(listener);
    }

    /** Deregister all listeners to react upon property changes.
     */

    public void removeAllPropertyChangedListeners() {
       propertyChangedListeners = new ArrayList<OnPropertyChangedListener>();
    }

    /** Interface for classes to react on property changes on an object of class AnimatedGuiObjectCV,
     * e.g. to detect collisions with other objects.
     * Objects implementing this interface can be registered with objects of class AnimatedGuiObjectCV.
     */

    public interface OnPropertyChangedListener {

        /** Called automatically from a setXXX() method of the object where the listener is registered.
         *
         * @param obj The object whose property value has changed.
         * @param propertyName The name of the property whose value has changed ("LeftTop", "Center", "Size", "RotationAngle", "Color")
         */
        public void onPropertyChanged(AnimatedGuiObjectCV obj, String propertyName);

    }

    /** Class to store and later to restore member values of an object of class AnimatedGuiObjectCV
     * - mainly those values that pertain to the presentation of the object on the display.
     * Additional values of any meaning can be stored in a HashMap<String,String> component,
     * i.e. in a kind of bundle (the class Bundle itself cannot be not used here because it is not serializable).
     */

    static public class ObjectProperties implements Serializable {

        /** The name of the object */
        public String name;
        public int type;
        public int centerX;
        public int centerY;
        public int width;
        public int height;
        public float rotationAngle;
        public int zindex;
        public int color;
        public int transparency;
        public boolean visible;
        /** Any data that shall also be stored */
        public HashMap<String, String> data;

        public ObjectProperties(AnimatedGuiObjectCV obj) {
            name = obj.getName();
            type = obj.getType();
            centerX = obj.getCenterX();
            centerY = obj.getCenterY();
            width = obj.getWidth();
            height = obj.getHeight();
            rotationAngle = obj.getRotationAngle();
            zindex = obj.getZindex();
            color = obj.getColor();
            transparency = obj.getTransparency();
            visible = obj.getVisible();
            data = new HashMap<String, String>();
        }

    }

    /** Class to store and later to restore member values of an object of class AnimatedGuiObjectCV
     * - mainly those values that pertain to the presentation of the object on the display.
     */

    /** Method to get the properties of the object as defined by class ObjectProperties.
     *
     * @retrun  The object properties.
     */

    public ObjectProperties getProperties() {
      return new ObjectProperties(this);
    }

    /** Method to assign values to members of the object. To make the changes visible on the display,
     * call the invalidate() method of the displaying view.
     *
     * @param values  Object of class ObjectProperties that stores the values to be assigned.
     *                The name and the type of the object will remain unchanged.
     */

    public void setProperties(ObjectProperties values) {
        setCenterX(values.centerX);
        setCenterY(values.centerY);
        setWidth(values.width);
        setHeight(values.height);
        setZindex(values.zindex);
        setRotationAngle(values.rotationAngle);
        setColor(values.color);
        setTransparency(values.transparency);
        setVisible(values.visible);
    }

    /** Method to assign values to members of the object. The changes will be animated immediately on the display.
     *
     * @param values  Object of class ObjectProperties that stores the values to be assigned.
     *                The name and the type of the object will remain unchanged.
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     */

    public void setPropertiesAnimated(ObjectProperties values, int duration, int startDelay) {
        if (values.centerX!=getCenterX()||values.centerY!=getCenterY())
            addLinearPathAnimator(values.centerX,values.centerY,duration,startDelay);
        if (values.width!=getWidth()||values.height!=getHeight())
            addSizeAnimator(values.width,values.height,duration,startDelay);
        setZindex(values.zindex);
        if (values.rotationAngle!=getRotationAngle())
            addRotationAnimator(values.rotationAngle,duration,startDelay);
        if (values.color!=getColor())
            addColorAnimator(values.color,duration,startDelay);
        setTransparency(values.transparency);
        setVisible(values.visible);
        startAnimation();
    }

}