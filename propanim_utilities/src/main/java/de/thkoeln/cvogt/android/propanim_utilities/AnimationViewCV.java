package de.thkoeln.cvogt.android.propanim_utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by vogt on 5.1.2018.
 */

/** ViewGroup class to display animated objects of class AnimatedGuiObjectCV (i.e. animated Drawable and View objects) with.<BR>
 * For more comments on how to proceed, see the documentation of the class AnimatedGuiObjectCV.<BR><BR>
 * N.B.: Views of this class can currently not be embedded in a layout together with other views.
 * Neither can this class be referenced in an XML layout file.
 * You must therefore generate an object of this class in the Java code of an activity by calling the constructor
 * specified below and then display the object fullscreen by passing it as a parameter to setContentView().
 * <BR><BR>
 * Author: Prof. Dr. Carsten Vogt, Technische Hochschule Koeln / University of Applied Sciences Cologne, Germany, carsten.vogt@th-koeln.de
 * Version: 1.0
 */

public class AnimationViewCV extends RelativeLayout {

    /** Objects to be displayed (where their embedded objects are of class Drawable or View).
     * The objects are sorted by their z index values, objects with lower values first.
     * This means that objects with higher values will be drawn later that objects with
     * lower values and therefore will appear in the foreground. */

    private ArrayList<AnimatedGuiObjectCV> guiObjects;

    /** Listener that shall be called when a touch event occurs. */

    private OnTouchListener onTouchListener;

    /** Gesture detector that shall be notified by the OnTouchListener. */

    private GestureDetector gestureDetector;

    /** Constructor: Generates an empty list for 'guiObjects'
     * and sets the OnTouchListener, the GestureDetector, and the GestureListener to null.
     */

    public AnimationViewCV(final Context context) {
        super(context);
        setBackgroundColor(0xFFFFFFFF);
        guiObjects = new ArrayList<AnimatedGuiObjectCV>();
        onTouchListener = null;
        gestureDetector = null;
    }

    /** Is called automatically by the runtime system when the view is to be drawn.
     * Calls in turn the draw() methods of all objects in 'guiObjects' that contain Drawable objects.
     * All embedded View objects are drawn automatically (see comment for method addAnimatedGuiObject()). */

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ViewParent p = getParent();
        while (!(p==null || p instanceof ScrollView))
            p = p.getParent();
        boolean isEmbeddedInScrollView = p!=null;
        // First draw all dependent objects that shall appear in the background
        for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV guiobj = it.next();
            if (guiobj.getType()!= AnimatedGuiObjectCV.TYPE_VIEW)
                guiobj.drawDependentsInBackground(canvas);
        }
        // Then draw the GUI objects themselves and their dependent objects that shall appear on the same level
        int dispWidth = GUIUtilitiesCV.getDisplayWidth(),
            dispHeight = GUIUtilitiesCV.getDisplayHeight();
        for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV guiobj = it.next();
            if (guiobj.getType()== AnimatedGuiObjectCV.TYPE_VIEW) continue;
            if (!isEmbeddedInScrollView && !guiobj.liesInArea(0,0,dispWidth,dispHeight)) continue;
            guiobj.draw(canvas);
        }
    }

    /** Returns an ArrayList with all animated objects registered with this view.
     * The objects are sorted by their z values; the object with the highest value
     * (i.e. the object that will be drawn on top) standing at the end of the list.
     * @return A copy of the guiObjects ArrayList
     */

    public ArrayList<AnimatedGuiObjectCV> getAnimatedGuiObjects() {
        return new ArrayList<AnimatedGuiObjectCV>(guiObjects);
    }

    /** Returns the AnimatedGuiObjectCV objects whose enclosing rectangle contains a given point.
     * The objects are ordered by their z values; the object with the highest value
     * (i.e. the object that will be drawn on top) is the last object in this list.
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @return The AnimatedDrawableAndViewsCV objects enclosing the point
     * */

    public ArrayList<AnimatedGuiObjectCV> getAnimatedGuiObjectsAtPoint(int x, int y) {
        ArrayList<AnimatedGuiObjectCV> result = new ArrayList<>();
        for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV guiobj = it.next();
            if (guiobj.contains(x,y))
                result.add(guiobj);
        }
        Collections.sort(result,
                new Comparator<AnimatedGuiObjectCV>() {
                    @Override
                    public int compare(AnimatedGuiObjectCV o1, AnimatedGuiObjectCV o2) {
                        return o1.getZindex()-o2.getZindex();
                    }
                }
        );
        return result;
    }

    /** Returns the AnimatedGuiObjectCV objects that lie (partly or completely) within a given rectangle.
     * The objects are ordered by their z values; the object with the highest value
     * (i.e. the object that will be drawn on top) is the last object in this list.
     * @param left left bound of the rectangle
     * @param top upper bound of the rectangle
     * @param right right bound of the rectangle
     * @param bottom bottom bound of the rectangle
     * @return The AnimatedDrawableAndViewsCV objects within the rectangle
     */

    public ArrayList<AnimatedGuiObjectCV> getAnimatedGuiObjectsInArea(int left, int top, int right, int bottom) {
        ArrayList<AnimatedGuiObjectCV> result = new ArrayList<>();
        for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV guiobj = it.next();
            if (guiobj.liesInArea(left,top,right,bottom))
                result.add(guiobj);
        }
        Collections.sort(result,
                new Comparator<AnimatedGuiObjectCV>() {
                    @Override
                    public int compare(AnimatedGuiObjectCV o1, AnimatedGuiObjectCV o2) {
                        return o1.getZindex()-o2.getZindex();
                    }
                }
        );
        return result;
    }

    /** Adds an animated object to 'guiObjects' (provided it is not already included in 'guiObjects', i.e. there will be no duplicates).
     *  If the embedded object is of class View, it is also added to the layout of this displaying view
     *  such that it will be drawn automatically to the screen. */

    public void addAnimatedGuiObject(AnimatedGuiObjectCV animatedGuiObject) {
        if (guiObjects.contains(animatedGuiObject)) return;
        animatedGuiObject.setDisplayingView(this);
        guiObjects.add(animatedGuiObject);
        sortAnimatedGuiObjectsByZindex();
        if (animatedGuiObject.getType()== AnimatedGuiObjectCV.TYPE_VIEW)
            super.addView((View)animatedGuiObject.getGuiObject());
    }

    /** Adds all animated objects of an ArrayList to 'guiObjects'.
     *  If the embedded object is of class View, it is also added to the layout of this displaying view
     *  such that it will be drawn automatically to the screen. */

    public void addAnimatedGuiObjects(ArrayList<AnimatedGuiObjectCV> animatedGuiObjects) {
        for (AnimatedGuiObjectCV guiObj : animatedGuiObjects)
            addAnimatedGuiObject(guiObj);
    }

    /** Adds all animated objects of a group to 'guiObjects'.
     *  If the embedded object is of class View, it is also added to the layout of this displaying view
     *  such that it will be drawn automatically to the screen. */

    public void addAnimatedGuiObjects(AnimatedGuiObjectsGroupCV animatedGuiObjectsGroup) {
        for (Iterator<AnimatedGuiObjectCV> it = animatedGuiObjectsGroup.iterator(); it.hasNext();)
            addAnimatedGuiObject(it.next());
    }

    /** Removes an animated object from 'guiObjects' and stops all its animations.
     *  If the embedded object is of class View, it is also removed from the layout of this displaying view.
     */

    public void removeAnimatedGuiObject(AnimatedGuiObjectCV animatedGuiObject) {
        guiObjects.remove(animatedGuiObject);
        animatedGuiObject.clearAnimatorList();
        if (animatedGuiObject.getType()== AnimatedGuiObjectCV.TYPE_VIEW)
            super.removeView((View)animatedGuiObject.getGuiObject());
    }

    /** Removes all animated objects of a group from 'guiObjects' and stops all their animations.
     *  If there are embedded objects of class View, they is also removed from the layout of this displaying view.
     */

    public void removeAnimatedGuiObjects(AnimatedGuiObjectsGroupCV animatedGuiObjectsGroup) {
        for (Iterator<AnimatedGuiObjectCV> it = animatedGuiObjectsGroup.iterator(); it.hasNext();)
            removeAnimatedGuiObject(it.next());
    }

    /** Removes all animated objects from 'guiObjects' and stops their animations.
     *  If the embedded object of an animated object is of class View, it is also removed from the layout of this displaying view.
     */

    public void removeAllAnimatedGuiObjects() {
      for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext(); ) {
          AnimatedGuiObjectCV obj = it.next();
          obj.clearAnimatorList();
          if (obj.getType()== AnimatedGuiObjectCV.TYPE_VIEW)
              super.removeView((View)obj.getGuiObject());
      }
      guiObjects.clear();
    }

    /** Sorts an ArrayList with animated objects according to their z index values.
     * Is called automatically by the setZindex() methods of all registered animated objects
     * and by the addAnimatedGuiObject() method of this class.
     * */

    public void sortAnimatedGuiObjectsByZindex() {
        Collections.sort(guiObjects,
                new Comparator<AnimatedGuiObjectCV>() {
                    @Override
                    public int compare(AnimatedGuiObjectCV o1, AnimatedGuiObjectCV o2) {
                        return o1.getZindex()-o2.getZindex();
                    }
                }
        );
    }

    /** Start the animations of all objects in 'guiObjects' immediately. */

    public void startAnimations() {
        for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext();)
            it.next().startAnimation();
    }

    /** Set the OnTouchListener. */

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    /** Remove the OnTouchListener. */

    public void removeOnTouchListener() {
        this.onTouchListener = null;
    }

    /** Set the GestureListener. The corresponding gesture detector will be enabled for long press
     * and double tap gestures. */

    public void setGestureListener(GestureListener gestureListener) {
        gestureListener.setView(this);
        this.gestureDetector = new GestureDetector(getContext(),gestureListener);
        this.gestureDetector.setIsLongpressEnabled(true);
        this.gestureDetector.setOnDoubleTapListener(gestureListener);
    }

    /** Remove the GestureListener. */

    public void removeGestureListener() {
        this.gestureDetector = null;
    }

    /** Is called automatically by the runtime system when a touch event occurs.
     * Calls in turn the onTouchEvent method of the superclass
     * and notifies the registered gesture detector (if any).
     * Calls the onTouch() method of the registered OnTouchListener (if any), passing this view,
     * the GUI object touched and the MotionEvent object that describes the event.
     * If multiple GUI objects are affected by the touch event,
     * onTouch() is called for each of these objects.
     * If there is no GUI object at the position touched,
     * onTouch() is called once with null for the GUI object parameter.
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
      super.onTouchEvent(event);
      if (gestureDetector!=null)
            gestureDetector.onTouchEvent(event);
      if (onTouchListener!=null) {
            ArrayList<AnimatedGuiObjectCV> objects = getAnimatedGuiObjectsAtPoint((int)event.getX(),(int)event.getY());
            if (objects==null||objects.size()==0)
                onTouchListener.onTouch(this,null,event);
             else
                for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();)
                   onTouchListener.onTouch(this, it.next(), event);
            invalidate();
      }
      return true;
    }

    /** Interface for Listeners that can be registered with an AnimationViewCV object
     * and that will be called when a touch event occurs on this object.
     */

    public interface OnTouchListener {

        /** Header of a callback method that is called automatically when a touch event occurs on the view
         * where the OnTouchListener is registered. Implement this method for a specific reaction on the event.
         * NB: If a gesture detector is registered, its onTouchEvent() method will be called by the given onTouchEvent()
         * method of this view. Hence, do not call the gesture detector's method here!
         *
         * @param view
         * @param guiobject Gui object that has been touched (null if no object was touched)
         * @param event The motion event
         * @return true if the event was handled, false otherwise
         */

        boolean onTouch(AnimationViewCV view, AnimatedGuiObjectCV guiobject, MotionEvent event);

    }

    /** Class for gesture listeners that can be registered with an AnimationViewCV object
     * and that will be called by its gesture detector when a gesture is detected.
     * Override the methods of this class for the gestures you wish to handle.
     */

    public static class GestureListener extends GestureDetector.SimpleOnGestureListener {

        /** View for which this listener is registered. This attribute may be accessed from subclasses. */

        protected AnimationViewCV view;

        /** GUI objects on which the gesture occurred (or null if there is no such object).
         * This attribute is set by the onXXX() methods defined here and may be accessed from subclasses.
         * NB: This attribute is useful if you do not want to find out the relevant GUI objects.
         * in your own methods that override the methods defined here. However, your methods must then
         * call the methods defined here by super.onXXX().
         */

        protected ArrayList<AnimatedGuiObjectCV> guiobjects;

        public GestureListener() {
            this.view = null;
        }

        /** Sets the view for which this listener is registered. This method will be called
         * in the setGestureListener() method of the view; do not call it otherwise!
         *
         * @param view View for which the listener is registered.
         */

        void setView(AnimationViewCV view) {
            this.view = view;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            guiobjects = view.getAnimatedGuiObjectsAtPoint((int)e.getX(),(int)e.getY());
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
          guiobjects = view.getAnimatedGuiObjectsAtPoint((int)e.getX(),(int)e.getY());
          return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
          guiobjects = view.getAnimatedGuiObjectsAtPoint((int)e1.getX(),(int)e1.getY());
          return true;
        }

    }

    /** Interface for classes to handle collisions between animated objects.
     * Can be registered with objects of class AnimatedGuiObjectCV.
     */

    public interface CollisionHandler {

        /** Called automatically by the object for which the listener is registered.
         *
         * @param obj1 The first colliding object
         *             (in the current implementation of AnimatedGuiObjectCV:
         *             Always the object that performs the collision detection,
         *             i.e. the object whose animation step is currently being executed)
         * @param obj2 The second colliding object
         *             (in the current implementation of AnimatedGuiObjectCV:
         *             Always the object for which the object performing the collision detection
         *             as described above detects a collision)
         */
        public void handleCollision(AnimatedGuiObjectCV obj1, AnimatedGuiObjectCV obj2);

    }

}
