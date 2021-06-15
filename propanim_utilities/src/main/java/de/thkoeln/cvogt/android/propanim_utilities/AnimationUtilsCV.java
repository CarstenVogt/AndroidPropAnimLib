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

 */package de.thkoeln.cvogt.android.propanim_utilities;

/**
 * Created by vogt on 16.01.2018.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/** This class provides some utility classes and methods for animation programming
 *  with the classes AnimationViewCV and AnimatedGuiObjectCV
 */

public class AnimationUtilsCV {

    /**
     * Listener to remove an object of class AnimatedGuiObjectCV from the
     * view of class AnimationViewCV where it is registered.
     * The listener is registered with an animation by calling
     * the Animator.addListener() method.
     */

    public static class EndListener_Delete extends AnimatorListenerAdapter {

        /**
         * The guiobj that is animated by the animation
         * where this listener is registered.
         */
        private AnimatedGuiObjectCV guiobj;

        /** To avoid recursive calls that will occur when the gui object is deleted and hence all its animators are cancelled */

        private boolean alreadyCalled;

        public EndListener_Delete(AnimatedGuiObjectCV guiobj) {
            this.guiobj = guiobj;
            this.alreadyCalled = false;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (alreadyCalled) return;
            alreadyCalled = true;
            guiobj.delete();
        }

    }

    /**
     * Listener to remove an object of class AnimatedGuiObjectCV from the
     * view of class AnimationViewCV where it is registered.
     * The object is removed only if it is no more visible after the
     * animation ends - either because the object size is zero
     * or because the object lies now outside of the display.
     * The listener is registered with an animation by calling
     * the Animator.addListener() method.
     */

    public static class EndListener_DeleteIfNotVisible extends AnimatorListenerAdapter {

        /**
         * The guiobj that is animated by the animation
         * where this listener is registered.
         */

        private AnimatedGuiObjectCV guiobj;

        public EndListener_DeleteIfNotVisible(AnimatedGuiObjectCV guiobj) {
            this.guiobj = guiobj;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            boolean visible = (guiobj.getHeight() > 0 && guiobj.getWidth() > 0);
            try {
                int screenheight = Resources.getSystem().getDisplayMetrics().heightPixels;
                int screenwidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                if ((guiobj.getTopBound()+guiobj.getHeight()<=0) ||
                        (guiobj.getLeftBound()+guiobj.getWidth()<=0) ||
                        (guiobj.getTopBound()>screenheight) ||
                        (guiobj.getLeftBound()>screenwidth))
                    visible = false;
            } catch (NullPointerException e) {}
            if (visible) return;
            animator.removeListener(this);
            guiobj.delete();
        }

    }

    /** Listener to react upon collisions between objects. */

    public static class PropertyChangedListener_Collision implements AnimatedGuiObjectCV.OnPropertyChangedListener {

        /** Constant to specify the desired reaction upon a collision:<BR>
         * Let the colliding objects explode.
         */
        public static final int REACTTYPE_EXPLODE = 0;

        /** Constant to specify the desired reaction upon a collision:<BR>
         * Stop all animations of the colliding objects.
         */
        public static final int REACTTYPE_STOP = 1;

        /** Attribute to specify the desired reaction (see constants). */

        private int desiredReaction;

        /** Bitmap picture showing an explosion. */

        Bitmap explosionBitmap;

        /** Constructor: Initializes the attributes */

        public PropertyChangedListener_Collision(Context context, int desiredReaction) {
            this.desiredReaction = desiredReaction;
            explosionBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
            explosionBitmap = GraphicsUtilsCV.makeBitmapTransparent(explosionBitmap, Color.WHITE);
        }

        @Override
        public void onPropertyChanged(AnimatedGuiObjectCV obj, String propertyName) {
            if (obj.getName().equals("EXPLOSION")) return; // An explosion object cannot collide with other objects
            // Check if obj has collided with another object
            AnimatedGuiObjectCV collidingObj = findCollidingObject(obj);
            if (collidingObj==null||collidingObj.getName().equals("EXPLOSION"))
                return;
            obj.removePropertyChangedListener(this);
            collidingObj.removePropertyChangedListener(this);
            switch (desiredReaction) {
                case REACTTYPE_EXPLODE:
                    AnimationViewCV view = obj.getDisplayingView();
                    view.removeAnimatedGuiObject(obj);
                    view.removeAnimatedGuiObject(collidingObj);
                    int posX = (obj.getCenterX()+collidingObj.getCenterX())/2;
                    int posY = (obj.getCenterY()+collidingObj.getCenterY())/2;
                    int duration = 2000;
                    AnimatedGuiObjectCV<Drawable> explosionGuiObject =
                            new AnimatedGuiObjectCV(obj.getDisplayingView().getContext(),"EXPLOSION",explosionBitmap,
                                    posX,posY,2*obj.getWidth(),2*obj.getWidth());
                    explosionGuiObject.addSizeAnimator(0,0,duration)
                            .addListener(new EndListener_DeleteIfNotVisible(explosionGuiObject));
                    view.addAnimatedGuiObject(explosionGuiObject);
                    explosionGuiObject.startAnimation();
                    break;
                case REACTTYPE_STOP:
                    obj.clearAnimatorList();
                    collidingObj.clearAnimatorList();
                    break;
            }
        }

        /** Try to find an object with which the object given by the parameter has collided (= with which it overlaps).
         *
         * @param obj The object for which a colliding object shall be found.
         * @return The object with which obj has collided (if any) or null (if there is no collision)
         */

        static public AnimatedGuiObjectCV findCollidingObject(AnimatedGuiObjectCV obj) {
            ArrayList<AnimatedGuiObjectCV> list = obj.getDisplayingView().getAnimatedGuiObjects();
            for (Iterator<AnimatedGuiObjectCV> it = list.iterator(); it.hasNext();) {
                AnimatedGuiObjectCV obj2 = it.next();
                if (obj2==obj) continue;
                if (obj.overlapsWith(obj2))
                    return obj2;
                /*
                if (obj.getType()==AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE&&obj2.getType()==AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE) {
                    int xAbst = Math.abs(obj.getCenterX() - obj2.getCenterX());
                    int yAbst = Math.abs(obj.getCenterY() - obj2.getCenterY());
                    if (Math.sqrt(xAbst * xAbst + yAbst * yAbst) < (obj.getWidth() + obj2.getWidth()) / 2)
                        return obj2; }
                else {
                    Point[] corners = obj.getCorners();
                    for (int i=0;i<=3;i++)
                        if (obj2.contains(corners[i]))
                            return obj2;
                    corners = obj2.getCorners();
                    for (int i=0;i<=3;i++)
                        if (obj.contains(corners[i]))
                            return obj2;
                }
                */
            }
            return null;
        }

    }

    /** Listeners that relocate an object in accordance with the moving finger of the user. */

    public static class OnTouchListener_Relocation implements AnimationViewCV.OnTouchListener {
        float mLastTouchX, mLastTouchY;
        AnimatedGuiObjectCV objectToMove = null;
        @Override
        public boolean onTouch(AnimationViewCV view, AnimatedGuiObjectCV guiobject, MotionEvent ev) {
            if (guiobject == null && objectToMove == null) return true;
            disallowAncestorsInterceptTouchEvent(view);
            final int action = ev.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    objectToMove = guiobject;
                    mLastTouchX = ev.getX();
                    mLastTouchY = ev.getY();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (objectToMove == null) {
                        objectToMove = guiobject;
                        mLastTouchX = ev.getX();
                        mLastTouchY = ev.getY();
                    }
                    if (objectToMove == null) return true;
                    final float x = ev.getX();
                    final float y = ev.getY();
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;
                    objectToMove.setCenterX((int) (objectToMove.getCenterX() + dx));
                    objectToMove.setCenterY((int) (objectToMove.getCenterY() + dy));
                    mLastTouchX = x;
                    mLastTouchY = y;
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    objectToMove = null;
                    break;
                }
            }
            return true;
        }

        // If the view is embedded in a ScrollView, calling this method ensures that gesture handling of this
        // ScrollView does not interfere with this OnTouchListener.
        // (cf. http://stackoverflow.com/questions/6194739/overriding-ontouchevent-competing-with-scrollview)

        private void disallowAncestorsInterceptTouchEvent(View view) {
            ViewParent p = view.getParent();
            while (p!=null) {
                p.requestDisallowInterceptTouchEvent(true);
                p = p.getParent();
            }
        }

    }

    //
    // -------------------------------------------------------------------------------------------------------------------
    //
    // UPDATE: Instead of the following methods, better use the AnimatedGuiObjectsGroupCV class with its animation methods
    //

    /** Method to create animators that will move a number of objects of class AnimatedGuiObjectsCV to specified positions on a direct linear path.
     * The animators will be associated with the objects, but not started yet.
     * @param guiObjWithPos HashMap - key set = the objects to move, value set = the corresponding target positions
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     * @return HashMap - key set = the animated objects, value set = the corresponding animators
     */

    public static HashMap<AnimatedGuiObjectCV, Animator> addLinearPathAnimators(HashMap<AnimatedGuiObjectCV, Point> guiObjWithPos, int duration, int startDelay) {
        HashMap<AnimatedGuiObjectCV, Animator> animators = new HashMap<>();
        for (Iterator<AnimatedGuiObjectCV> it = guiObjWithPos.keySet().iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            Point target = guiObjWithPos.get(obj);
            Animator anim = obj.addLinearPathAnimator(target.x,target.y,duration,startDelay);
            animators.put(obj,anim);
        }
        return animators;
    }

    /** Method to create animators that will translate a number of objects of class AnimatedGuiObjectsCV,
     * i.e. move them all for the same distance into the same direction on a direct linear path.
     * The animators will be associated with the objects, but not started yet.
     * After their execution, the animators will be automatically removed from the objects.
     * @param guiObjects The objects to move
     * @param transX Horizontal translation (in px)
     * @param transY Vertical translation (in px)
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     * @return HashMap - key set = the animated objects, value set = the corresponding animators
     */

    public static HashMap<AnimatedGuiObjectCV, Animator> addTranslationAnimators(ArrayList<AnimatedGuiObjectCV> guiObjects, int transX, int transY, int duration, int startDelay) {
        HashMap<AnimatedGuiObjectCV, Animator> animators = new HashMap<>();
        for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            int targetX = obj.getCenterX()+transX,
                    targetY = obj.getCenterY()+transY;
            Animator anim = obj.addLinearPathAnimator(targetX,targetY,duration,startDelay);
            animators.put(obj,anim);
        }
        return animators;
    }

    /** Method to create animators that will move a number of objects to new positions.
     * At their new positions, the objects are stacked, i.e. they are ordered above or below, right or left to
     * each others equidistantly in the horizontal and in the vertical directions.
     * Objects may overlap. In this case, the last object in the list will be placed behind all other objects
     * (by setting its Z value to 1), the second to last object will be placed in front of it (by setting its Z value to 2)
     * and so on.
     * The animators will be associated with the objects, but not started yet.
     * After their execution, the animators will be automatically removed from the objects.
     * @param guiObjects The objects to move
     * @param targetX The target position (X coordinate) of the first object (i.e. of its center) in the guiObjects list
     * @param targetY The target position (Y coordinate) of the first object (i.e. of its center) in the guiObjects list
     * @param distX The distance between any two neighboring objects in the horizontal direction (in px):<BR>
     *              If distX > 0, the left border of the first object is taken as the starting point and the
     *              following objects are placed such that distX is the distance between the left borders
     *              of any two neighboring objects.
     *              If distX < 0, the right border of the first object is taken as the starting point and the
     *              following objects are placed such that distX is the distance between the right borders
     *              of any two neighboring objects.
     *              If distX == 0, the X coordinates of the centers of all objects will be set to the X coordinate
     *              of the center of the first object.
     * @param distY The distance between any two neighboring objects in the vertical direction (in px):<BR>
     *              If distY > 0, the top border of the first object is taken as the starting point and the
     *              following objects are placed such that distY is the distance between the top borders
     *              of any two neighboring objects.
     *              If distY < 0, the bottom border of the first object is taken as the starting point and the
     *              following objects are placed such that distY is the distance between the bottom borders
     *              of any two neighboring objects.
     *              If distY == 0, the Y coordinates of the centers of all objects will be set to the Y coordinate
     *              of the center of the first object.
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     * @return HashMap - key set = the animated objects, value set = the corresponding animators
     */

    public static HashMap<AnimatedGuiObjectCV, Animator> addStackingAnimators(ArrayList<AnimatedGuiObjectCV> guiObjects, int targetX, int targetY, int distX, int distY, int duration, int startDelay) {
        HashMap<AnimatedGuiObjectCV, Animator> animators = new HashMap<>();
        for (int i=0;i<guiObjects.size();i++) {
            AnimatedGuiObjectCV obj = guiObjects.get(i);
            int tX = targetX + i*distX;
            if (distX>=0)
                tX += (obj.getWidth()-guiObjects.get(0).getWidth())/2;
            if (distX<0)
                tX += (guiObjects.get(0).getWidth()-obj.getWidth())/2;
            int tY = targetY + i*distY;
            if (distY>=0)
                tY += (obj.getHeight()-guiObjects.get(0).getHeight())/2;
            if (distY<0)
                tY += (guiObjects.get(0).getHeight()-obj.getHeight())/2;
            Animator anim = obj.addLinearPathAnimator(tX,tY,duration,startDelay);
            animators.put(obj,anim);
            obj.setZindex(guiObjects.size()-i);
        }
        return animators;
    }

}

