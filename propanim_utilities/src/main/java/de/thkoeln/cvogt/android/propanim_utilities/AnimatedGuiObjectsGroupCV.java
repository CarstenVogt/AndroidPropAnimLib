package de.thkoeln.cvogt.android.propanim_utilities;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

// TODO: Bei allen addXXXAnimator-Methoden eine HashMap mit den neuen Animatoren zurückgeben.

/*

 * @return A HashMap mapping the objects in the group to the animators added here.

public HashMap<AnimatedGuiObjectCV,Animator> ...
        HashMap<AnimatedGuiObjectCV,Animator> map = new HashMap();
        ...
        AnimatedGuiObjectCV obj = it.next();
        Animator anim = obj.add...;
        map.put(obj,anim);
        }
        return map;
        }

*/

/**
 * Created by vogt on 27.1.2019.
 */

/**
 * Class for groups of objects of class AnimatedGuiObjectCV that can be animated together.
 * <BR><BR>
 * Note: Playing multiple animators for an object group sequentially might lead to unexpected results.
 * For example, expanding a group by a scaling factor f and then contracting it by the factor 1/f will NOT return to the original
 * placement of the objects but will finally result in a contraction of the original group by the factor 1/f.
 * The reason for this behavior is that an animator is based on the state of the group at the time of the definition of this animator
 * rather than on the group state at its starting time.
 * It is therefore recommended to define only animators that are started simultaneously at once
 * and to define other animators that shall start later not before that first set of animators has finished playing.
 * This could be done by a separate thread that sleeps while the first set of animators is playing.
 * <BR><BR>
 * Author: Prof. Dr. Carsten Vogt, Technische Hochschule Koeln / University of Applied Sciences Cologne, Germany, carsten.vogt@th-koeln.de
 * Version: 1.0
 *
 */

public class AnimatedGuiObjectsGroupCV extends ArrayList<AnimatedGuiObjectCV> {

    /** The name of the group
     */

    public String name;

    /** A cache to map the names of the objects in the group to the corresponding object references.
     * Note that the mapping is not necessarily up to date - e.g. because objects have left or joined the group or have changed their names.
     * Therefore, this mapping should be used only through the findObjectByName() method.
     */

    private HashMap<String,AnimatedGuiObjectCV> cacheObjNamesRefs;

    /** Constructor
     */

    public AnimatedGuiObjectsGroupCV(String name) {
        super();
        this.name = name;
        cacheObjNamesRefs = new HashMap<>();
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            if (obj.getName()!=null&&obj.getName().length()>0)
               cacheObjNamesRefs.put(obj.getName(),obj);
        }
    }

    /** Find an object with a given name in the group.
     * @param name The name of the object
     * @return A reference to the object with this name (or to the first object in the group if the group contains more than one such object)
     * or null if the group contains no object with this name.
     */

    public AnimatedGuiObjectCV findObjectByName(String name) {
        AnimatedGuiObjectCV obj = cacheObjNamesRefs.get(name);
        if (obj!=null && this.contains(obj) && obj.getName().equals(name))
            return obj;
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
            obj = it.next();
            if (obj.getName()!=null&&obj.getName().equals(name)) {
                cacheObjNamesRefs.put(name, obj);
                return obj;
            }
        }
        return null;
    }

    /** Creates a HashMap that maps for all objects in the group their names to the corresponding objects of class AnimatedGuiObjectCV.ObjectProperties.
     * Objects that have no name will not be included. If there are multiple objects with the same name, the HashMap will contain the last of these objects only.
     *
     * @return The HashMap
     */

    public HashMap<String,AnimatedGuiObjectCV.ObjectProperties> getObjectProperties() {
        HashMap<String,AnimatedGuiObjectCV.ObjectProperties> result = new HashMap<>();
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            if (obj.getName()!=null && obj.getName().length()>1)
                result.put(obj.getName(),new AnimatedGuiObjectCV.ObjectProperties(obj));
        }
        return result;
    }

    /** Sets the properties of the objects in the group according to the values stored in a HashMap.
     * Objects that are not covered by the HashMap will remain unchanged.
     *
     * @param propertiesMap The HashMap mapping object names to property values.
     */

    public void setObjectProperties(HashMap<String,AnimatedGuiObjectCV.ObjectProperties> propertiesMap) {
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            AnimatedGuiObjectCV.ObjectProperties properties = propertiesMap.get(obj.getName());
            if (properties!=null)
                obj.setProperties(properties);
        }
    }

    /** Sets the properties of the objects in the group according to the values stored in a HashMap.
     * The changes will be animated immediately on the display.
     * Objects that are not covered by the HashMap will remain unchanged.
     *
     * @param propertiesMap The HashMap mapping object names to property values.
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     */

    public void setObjectPropertiesAnimated(HashMap<String,AnimatedGuiObjectCV.ObjectProperties> propertiesMap, int duration, int startDelay) {
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            AnimatedGuiObjectCV.ObjectProperties properties = propertiesMap.get(obj.getName());
            if (properties!=null)
                obj.setPropertiesAnimated(properties,duration,startDelay);
        }
    }

    /** Get the smallest rectangle that encloses the objects of the group.
     * @return The minimal rectangle that encloses all objects (null if the group is empty)
     */

    public Rect getEnclosingRect() {
        return GraphicsUtilsCV.getEnclosingRect(this);
    }


    /**
     * Method to start the animation of all objects of the group.
     */

    public void startAnimations() {
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();)
            it.next().startAnimation();
    }

    /**
     * Method to clear the animator lists of all objects of the group.
     */

    public void clearAnimatorLists() {
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();)
            it.next().clearAnimatorList();
    }

    /** Method to create animators that will fade the objects of the group in.
     * The animators will be associated with the objects, but not started yet.
     * @param duration Duration of the animations (in ms)
     */

    public void addFadeInAnimator(int duration) {
        addFadeInAnimator(duration,0);
    }

    /** Method to create animators that will fade the objects of the group in.
     * The animators will be associated with the objects, but not started yet.
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     */

    public void addFadeInAnimator(int duration, int startDelay) {
        for (Iterator<AnimatedGuiObjectCV> itObj = this.iterator(); itObj.hasNext();) {
            AnimatedGuiObjectCV obj = itObj.next();
            obj.addFadeInAnimator(duration,startDelay);
        }
    }

    /** Method to create animators that will fade the objects of the group out.
     * The animators will be associated with the objects, but not started yet.
     * @param duration Duration of the animations (in ms)
     */

    public void addFadeOutAnimator(int duration) {
        addFadeOutAnimator(duration,0);
    }

    /** Method to create animators that will fade the objects of the group out.
     * The animators will be associated with the objects, but not started yet.
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     */

    public void addFadeOutAnimator(int duration, int startDelay) {
        for (Iterator<AnimatedGuiObjectCV> itObj = this.iterator(); itObj.hasNext();) {
            AnimatedGuiObjectCV obj = itObj.next();
            obj.addFadeOutAnimator(duration,startDelay);
        }
    }

    /** Method to create animators that will move the objects of the group to specified positions on direct linear paths.
     * The animators will be associated with the objects, but not started yet.
     * @param targetPositions Target positions of the animations (x and y coordinates of the centers of the objects).
     *                        The first position will be applied to the first object in the group, the second to the second etc.
     *                        If this list has less entries than there are objects, no animation at all will be assigned.
     * @param duration Duration of the animations (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addLinearPathsAnimator(ArrayList<Point> targetPositions, int duration) {
        return addLinearPathsAnimator(targetPositions,duration,0);
    }

    /** Method to create animators that will move the objects of the group to specified positions on direct linear paths.
     * The animators will be associated with the objects, but not started yet.
     * @param targetPositions Target positions of the animations (x and y coordinates of the centers of the objects).
     *                        The first position will be applied to the first object in the group, the second to the second etc.
     *                        If this list has less entries than there are objects, no animation at all will be assigned.
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addLinearPathsAnimator(ArrayList<Point> targetPositions, int duration, int startDelay) {
        return addLinearPathsAnimator(targetPositions,new LinearInterpolator(),duration,startDelay);
    }

    /** Method to create animators that will move the objects of the group to specified positions on direct linear paths.
     * The animators will be associated with the objects, but not started yet.
     * @param targetPositions Target positions of the animations (x and y coordinates of the centers of the objects).
     *                        The first position will be applied to the first object in the group, the second to the second etc.
     *                        If this list has less entries than there are objects, no animation at all will be assigned.
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animations (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addLinearPathsAnimator(ArrayList<Point> targetPositions, TimeInterpolator timeInterpolator, int duration) {
        return addLinearPathsAnimator(targetPositions,timeInterpolator,duration,0);
    }

    /** Method to create animators that will move the objects of the group to specified positions on direct linear paths.
     * The animators will be associated with the objects, but not started yet.
     * @param targetPositions Target positions of the animations (x and y coordinates of the centers of the objects).
     *                        The first position will be applied to the first object in the group, the second to the second etc.
     *                        If this list has less entries than there are objects, no animation at all will be assigned.
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here (empty if the size of tragetPositions does not match - see above).
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addLinearPathsAnimator(ArrayList<Point> targetPositions, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        HashMap<AnimatedGuiObjectCV,Animator> map = new HashMap();
        if (targetPositions.size()<this.size()) return map;
        Iterator<Point> itTargets = targetPositions.iterator();
        for (Iterator<AnimatedGuiObjectCV> itObj = this.iterator(); itObj.hasNext();) {
            AnimatedGuiObjectCV obj = itObj.next();
            Point target = itTargets.next();
            Animator anim = obj.addLinearPathAnimator(target.x,target.y,timeInterpolator,duration,startDelay);
            map.put(obj,anim);
        }
        return map;
    }

    /** Method to create animators that will move the objects of the group to specified positions on direct linear paths.
     * All objects will be moved with the same speed, i.e. the object with the longest distance to traverse will arrive last.
     * The animators will be associated with the objects, but not started yet.
     * @param targetPositions Target positions of the animations (x and y coordinates of the centers of the objects).
     *                        The first position will be applied to the first object in the group, the second to the second etc.
     *                        If this list has less entries than there are objects, no animation at all will be assigned.
     * @param timeInterpolator TimeInterpolator to control the timing of the animation
     * @param duration Duration of the animation (in ms), i.e. time to move the object with the longest distance to its target. The other objects will arrive earlier.
     * @param startDelay Start delay of the animations (in ms)
     */

    public void addLinearPathsConstSpeedAnimator(ArrayList<Point> targetPositions, TimeInterpolator timeInterpolator, int duration, int startDelay) {
        if (targetPositions.size()<this.size()) return;
        double maxDistance = 0;
        HashMap<AnimatedGuiObjectCV, Double> distances = new HashMap<>();
        Iterator<Point> itTargets = targetPositions.iterator();
        for (Iterator<AnimatedGuiObjectCV> itObj = this.iterator(); itObj.hasNext();) {
            AnimatedGuiObjectCV obj = itObj.next();
            double distanceToTarget = GraphicsUtilsCV.distance(obj.getCenter(),itTargets.next());
            distances.put(obj,distanceToTarget);
            if (distanceToTarget>maxDistance)
                maxDistance = distanceToTarget;
        }
        itTargets = targetPositions.iterator();
        for (Iterator<AnimatedGuiObjectCV> itObj = this.iterator(); itObj.hasNext();) {
            AnimatedGuiObjectCV obj = itObj.next();
            Point target = itTargets.next();
            obj.addLinearPathAnimator(target.x,target.y,timeInterpolator,(int)(duration*distances.get(obj)/maxDistance),startDelay);
        }
    }

    /** Method to create animators that will translate the objects of the group,
     * i.e. move them all for the same distance into the same direction on a direct linear path.
     * The animators will be associated with the objects, but not started yet.
     * @param transX Horizontal translation (in px)
     * @param transY Vertical translation (in px)
     * @param duration Duration of the animations (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addTranslationAnimator(int transX, int transY, int duration) {
        return addTranslationAnimator(transX,transY,duration,0);
    }

    /** Method to create animators that will translate the objects of the group,
     * i.e. move them all for the same distance into the same direction on a direct linear path.
     * The animators will be associated with the objects, but not started yet.
     * @param transX Horizontal translation (in px)
     * @param transY Vertical translation (in px)
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addTranslationAnimator(int transX, int transY, int duration, int startDelay) {
        HashMap<AnimatedGuiObjectCV,Animator> map = new HashMap();
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            int targetX = obj.getCenterX()+transX,
                    targetY = obj.getCenterY()+transY;
            Animator anim = obj.addLinearPathAnimator(targetX,targetY,duration,startDelay);
            map.put(obj,anim);
        }
        return map;
    }

    /** Method to create animators that will move the objects of the group to new positions.
     * At their new positions, the objects are stacked, i.e. they are ordered above or below, right or left to
     * each others equidistantly in the horizontal and in the vertical directions.
     * Objects may overlap. In this case, the last object in the list will be placed behind all other objects
     * (by setting its Z value to 1), the second to last object will be placed in front of it (by setting its Z value to 2)
     * and so on.
     * The animators will be associated with the objects, but not started yet.
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
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addStackingAnimator(int targetX, int targetY, int distX, int distY, int duration) {
        return addStackingAnimator(targetX,targetY,distX,distY,duration,0);
    }

    /** Method to create animators that will move the objects of the group to new positions.
     * At their new positions, the objects are stacked, i.e. they are ordered above or below, right or left to
     * each others equidistantly in the horizontal and in the vertical directions.
     * Objects may overlap. In this case, the last object in the list will be placed behind all other objects
     * (by setting its Z value to 1), the second to last object will be placed in front of it (by setting its Z value to 2)
     * and so on.
     * The animators will be associated with the objects, but not started yet.
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
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addStackingAnimator(int targetX, int targetY, int distX, int distY, int duration, int startDelay) {
        HashMap<AnimatedGuiObjectCV,Animator> map = new HashMap();
        for (int i=0;i<this.size();i++) {
            AnimatedGuiObjectCV obj = this.get(i);
            int tX = targetX + i*distX;
            if (distX>=0)
                tX += (obj.getWidth()-this.get(0).getWidth())/2;
            if (distX<0)
                tX += (this.get(0).getWidth()-obj.getWidth())/2;
            int tY = targetY + i*distY;
            if (distY>=0)
                tY += (obj.getHeight()-this.get(0).getHeight())/2;
            if (distY<0)
                tY += (this.get(0).getHeight()-obj.getHeight())/2;
            Animator anim = obj.addLinearPathAnimator(tX,tY,duration,startDelay);
            obj.setZindex(this.size()-i);
            map.put(obj,anim);
        }
        return map;
    }

    /** Method to create animators that will move the objects of the group to new positions.
     * At their new positions, the objects are stacked, i.e. they are ordered above or below, right or left to each others.
     * At the same times the objects are zoomed such that objects will get smaller and and smaller (or larger and larger)
     * the higher their position in the object group is.
     * Objects may overlap. In this case, the last object in the list will be placed behind all other objects
     * (by setting its Z value to 1), the second to last object will be placed in front of it (by setting its Z value to 2)
     * and so on.
     * The animators will be associated with the objects, but not started yet.
     * @param targetX The target position (X coordinate) of the first object (i.e. of its center) in the guiObjects list
     * @param targetY The target position (Y coordinate) of the first object (i.e. of its center) in the guiObjects list
     * @param distX The distance between the first two objects in the horizontal direction (in px):<BR>
     *              If distX > 0, the left border of the first object is taken as the starting point and the
     *              following objects are placed such that distX is the distance between the left borders
     *              of any two neighboring objects.
     *              If distX < 0, the right border of the first object is taken as the starting point and the
     *              following objects are placed such that distX is the distance between the right borders
     *              of any two neighboring objects.
     *              If distX == 0, the X coordinates of the centers of all objects will be set to the X coordinate
     *              of the center of the first object.
     *              For the following objects in the group, the distances will be adapted according to their zoom factors.
     * @param distY The distance between the first two objects in the vertical direction (in px):<BR>
     *              If distY > 0, the top border of the first object is taken as the starting point and the
     *              following objects are placed such that distY is the distance between the top borders
     *              of any two neighboring objects.
     *              If distY < 0, the bottom border of the first object is taken as the starting point and the
     *              following objects are placed such that distY is the distance between the bottom borders
     *              of any two neighboring objects.
     *              If distY == 0, the Y coordinates of the centers of all objects will be set to the Y coordinate
     *              of the center of the first object.
     *              For the following objects in the group, the distances will be adapted according to their zoom factors.
     * @param zoomFactor The zoom factor. The size first object in the group will remain unchanged,
     *                   the size of the second object will be zoomed by this factor, the size of the third by zoomFactor*zoomFactor,
     *                   the size of the forth by zoomFactor*zoomFactor*zoomFactor and so on.
     * @param duration Duration of the animations (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addStackingAnimatorWithZoom(int targetX, int targetY, int distX, int distY, double zoomFactor, int duration) {
         return addStackingAnimatorWithZoom(targetX,targetY,distX,distY,zoomFactor,duration,0);
    }

    /** Method to create animators that will move the objects of the group to new positions.
     * At their new positions, the objects are stacked, i.e. they are ordered above or below, right or left to each others.
     * At the same times the objects are zoomed such that objects will get smaller and and smaller (or larger and larger)
     * the higher their position in the object group is.
     * Objects may overlap. In this case, the last object in the list will be placed behind all other objects
     * (by setting its Z value to 1), the second to last object will be placed in front of it (by setting its Z value to 2)
     * and so on.
     * The animators will be associated with the objects, but not started yet.
     * @param targetX The target position (X coordinate) of the first object (i.e. of its center) in the guiObjects list
     * @param targetY The target position (Y coordinate) of the first object (i.e. of its center) in the guiObjects list
     * @param distX The distance between the first two objects in the horizontal direction (in px):<BR>
     *              If distX > 0, the left border of the first object is taken as the starting point and the
     *              following objects are placed such that distX is the distance between the left borders
     *              of any two neighboring objects.
     *              If distX < 0, the right border of the first object is taken as the starting point and the
     *              following objects are placed such that distX is the distance between the right borders
     *              of any two neighboring objects.
     *              If distX == 0, the X coordinates of the centers of all objects will be set to the X coordinate
     *              of the center of the first object.
     *              For the following objects in the group, the distances will be adapted according to their zoom factors.
     * @param distY The distance between the first two objects in the vertical direction (in px):<BR>
     *              If distY > 0, the top border of the first object is taken as the starting point and the
     *              following objects are placed such that distY is the distance between the top borders
     *              of any two neighboring objects.
     *              If distY < 0, the bottom border of the first object is taken as the starting point and the
     *              following objects are placed such that distY is the distance between the bottom borders
     *              of any two neighboring objects.
     *              If distY == 0, the Y coordinates of the centers of all objects will be set to the Y coordinate
     *              of the center of the first object.
     *              For the following objects in the group, the distances will be adapted according to their zoom factors.
     * @param zoomFactor The zoom factor. The size first object in the group will remain unchanged,
     *                   the size of the second object will be zoomed by this factor, the size of the third by zoomFactor*zoomFactor,
     *                   the size of the forth by zoomFactor*zoomFactor*zoomFactor and so on.
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addStackingAnimatorWithZoom(int targetX, int targetY, int distX, int distY, double zoomFactor, int duration, int startDelay) {
        HashMap<AnimatedGuiObjectCV,Animator> map = new HashMap();
        double zFact = 1;
        int tX = targetX, tY = targetY;
        for (int i=0;i<this.size();i++) {
            AnimatedGuiObjectCV obj = this.get(i);
            if (distX>=0)
                tX += zFact*(obj.getWidth()-this.get(0).getWidth())/2;
            if (distX<0)
                tX += zFact*(this.get(0).getWidth()-obj.getWidth())/2;
            if (distY>=0)
                tY += zFact*(obj.getHeight()-this.get(0).getHeight())/2;
            if (distY<0)
                tY += zFact*(this.get(0).getHeight()-obj.getHeight())/2;
            obj.addLinearPathAnimator(tX,tY,duration,startDelay);
            Animator anim = obj.addZoomAnimator(zFact,duration,startDelay);
            obj.setZindex(this.size()-i);
            zFact *= zoomFactor;
            tX += distX*zFact;
            tY += distY*zFact;
            map.put(obj,anim);
        }
        return map;
    }

    /** Add animators to move the object group on an arc, i.e. on a path on a circle around the center of the group.
     * The individual objects will not be rotated.
     * The animators will be associated with the objects, but not started yet.
     * @param rotAngle The rotation angle of the arc
     * @param duration Duration of the animation (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addArcPathAnimator(float rotAngle, int duration) {
        return addArcPathAnimator(rotAngle,duration,0);
    }

    /** Add animators to move the object group on an arc, i.e. on a path on a circle around the center of the group.
     * The individual objects will not be rotated.
     * The animators will be associated with the objects, but not started yet.
     * @param rotAngle The rotation angle of the arc
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addArcPathAnimator(float rotAngle, int duration, int startDelay) {
        Rect enclRect = getEnclosingRect();
        return addArcPathAnimator(enclRect.centerX(),enclRect.centerY(),rotAngle,duration,startDelay);
    }

    /** Add animators to move the object group on an arc, i.e. on a path on a circle around a center.
     * The individual objects will not be rotated.
     * The animators will be associated with the objects, but not started yet.
     * @param rotCenterX X coordinate of the center of the circle path
     * @param rotCenterY Y coordinate of the center of the circle path
     * @param rotAngle The rotation angle of the arc
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addArcPathAnimator(int rotCenterX, int rotCenterY, float rotAngle, int duration, int startDelay) {
        HashMap<AnimatedGuiObjectCV,Animator> map = new HashMap();
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            Animator anim = obj.addArcPathAnimator(rotCenterX, rotCenterY, rotAngle, duration, startDelay);
            map.put(obj,anim);
        }
        return map;
    }

    /** Add animators to rotate the object group around the center of the object group.
     * The objects will be moved (and at the same time rotated) on an arc around the center.
     * The animators will be associated with the objects, but not started yet.
     * @param rotAngle The rotation angle of the arc
     * @param duration Duration of the animation (in ms)
     */

    public void addRotationAnimator(float rotAngle, int duration) {
        addRotationAnimator(rotAngle,duration,0);
    }

    /** Add animators to rotate the object group around the center of the object group.
     * The objects will be moved (and at the same time rotated) on an arc around the center.
     * The animators will be associated with the objects, but not started yet.
     * @param rotAngle The rotation angle of the arc
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     */

    public void addRotationAnimator(float rotAngle, int duration, int startDelay) {
        Rect enclRect = getEnclosingRect();
        addRotationAnimator(enclRect.centerX(),enclRect.centerY(),rotAngle,duration,startDelay);
    }

    /** Add animators to rotate the object group around a center.
     * The objects will be moved (and at the same time rotated) on an arc, i.e. on a path on a circle around a center.
     * The animators will be associated with the objects, but not started yet.
     * @param rotCenterX X coordinate of the center of the circle path
     * @param rotCenterY Y coordinate of the center of the circle path
     * @param rotAngle The rotation angle of the arc
     * @param duration Duration of the animation (in ms)
     */

    public void addRotationAnimator(int rotCenterX, int rotCenterY, float rotAngle, int duration) {
        addRotationAnimator(rotCenterX,rotCenterY,rotAngle,duration,0);
    }

    /** Add animators to rotate the object group around a center.
     * The objects will be moved (and at the same time rotated) on an arc, i.e. on a path on a circle around a center.
     * The animators will be associated with the objects, but not started yet.
     * @param rotCenterX X coordinate of the center of the circle path
     * @param rotCenterY Y coordinate of the center of the circle path
     * @param rotAngle The rotation angle of the arc
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     */

    public void addRotationAnimator(int rotCenterX, int rotCenterY, float rotAngle, int duration, int startDelay) {
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            obj.addArcPathAnimator(rotCenterX, rotCenterY, rotAngle, duration, startDelay);
            obj.addRotationAnimator(rotAngle,duration,startDelay);
        }
    }

    /** Method to create animators that will place the objects on a circle.
     * The animators will be associated with the objects, but not started yet.
     * @param circleCenterX The x coordinate of the circle center.
     * @param circleCenterY The y coordinate of the circle center.
     * @param circleRadius The y coordinate of the circle center.
     * @param duration Duration of the animations (in ms)
     */

    public void addPlaceOnCircleAnimator(int circleCenterX, int circleCenterY, int circleRadius, int duration) {
        addPlaceOnCircleAnimator(circleCenterX, circleCenterY, circleRadius, duration, 0);
    }

    /** Method to create animators that will place the objects on a circle.
     * The animators will be associated with the objects, but not started yet.
     * @param circleCenterX The x coordinate of the circle center.
     * @param circleCenterY The y coordinate of the circle center.
     * @param circleRadius The y coordinate of the circle center.
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addPlaceOnCircleAnimator(int circleCenterX, int circleCenterY, int circleRadius, int duration, int startDelay) {
        ArrayList<Point> targets = new ArrayList<>();
        Point[] pointsOnCircle = GraphicsUtilsCV.pointsOnCircle(circleCenterX,circleCenterY,circleRadius,this.size());
        for (int i=0;i<pointsOnCircle.length;i++)
            targets.add(pointsOnCircle[i]);
        return addLinearPathsAnimator(targets,duration,startDelay);
    }

    /** Method to create animators that will place the objects on an ellipse.
     * The ellipse is implemented by a circle that is compressed vertically by a specified compression factor
     * and then rotated by a specified angle around its center.
     * The animators will be associated with the objects, but not started yet.
     * @param ellCenterX Center of the ellipse - X coordinate
     * @param ellCenterY Center of the ellipse - Y coordinate
     * @param ellRadius Radius of the ellipse
     * @param ellComprFactor Vertical compression of the circle (0<=comprFactor<=1), yielding the ellipse
     * @param ellRotAngle Rotation angle of the resulting ellipse (in radians)
     * @param duration Duration of the animations (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addPlaceOnEllipseAnimator(int ellCenterX, int ellCenterY, int ellRadius, double ellComprFactor, double ellRotAngle, int duration) {
        return addPlaceOnEllipseAnimator(ellCenterX, ellCenterY, ellRadius, ellComprFactor, ellRotAngle, duration, 0);
    }

    /** Method to create animators that will place the objects on an ellipse.
     * The ellipse is implemented by a circle that is compressed vertically by a specified compression factor
     * and then rotated by a specified angle around its center.
     * The animators will be associated with the objects, but not started yet.
     * @param ellCenterX Center of the ellipse - X coordinate
     * @param ellCenterY Center of the ellipse - Y coordinate
     * @param ellRadius Radius of the ellipse
     * @param ellComprFactor Vertical compression of the circle (0<=comprFactor<=1), yielding the ellipse
     * @param ellRotAngle Rotation angle of the resulting ellipse (in radians)
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     * @return A HashMap mapping the objects in the group to the animators added here.
     */

    public HashMap<AnimatedGuiObjectCV,Animator> addPlaceOnEllipseAnimator(int ellCenterX, int ellCenterY, int ellRadius, double ellComprFactor, double ellRotAngle, int duration, int startDelay) {
        ArrayList<Point> targets = new ArrayList<>();
        Point[] pointsOnEllipse = GraphicsUtilsCV.pointsOnEllipse(ellCenterX, ellCenterY, ellRadius, ellComprFactor, ellRotAngle,this.size());
        for (int i=0;i<pointsOnEllipse.length;i++)
            targets.add(pointsOnEllipse[i]);
        return addLinearPathsAnimator(targets,duration,startDelay);
    }

    /** Method to create animators that will place the objects on an ellipse.
     * The ellipse is implemented by a circle that is compressed vertically by a specified compression factor
     * and then rotated by a specified angle around its center.
     * Moreover, objects will be zoomed to different sizes and will be placed in front and behind each others
     * such that a perspective is created:
     * The object with the highest Y position in the ellipse (i.e. that will be the bottommost of the objects)
     * will keep its size and be placed in front of all objects.
     * Its two neighboring objects in the group will be zoomed by the factor 'zoomFactor' and be placed behind the first object.
     * The following two objects will be zoomed by 'zoomFactor*zoomFactor' and be placed behind the aforementioned objects etc.
     * The animators will be associated with the objects, but not started yet.
     * @param ellCenterX Center of the ellipse - X coordinate
     * @param ellCenterY Center of the ellipse - Y coordinate
     * @param ellRadius Radius of the ellipse
     * @param ellComprFactor Vertical compression of the circle (0<=comprFactor<=1), yielding the ellipse
     * @param ellRotAngle Rotation angle of the resulting ellipse (in radians)
     * @param zoomFactor Zoom factor as described above
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     */

    public void addPlaceOnEllipseAndZoomAnimator(int ellCenterX, int ellCenterY, int ellRadius, double ellComprFactor, double ellRotAngle, double zoomFactor, int duration, int startDelay) {
        ArrayList<Point> targets = new ArrayList<>();
        Point[] pointsOnEllipse = GraphicsUtilsCV.pointsOnEllipse(ellCenterX, ellCenterY, ellRadius, ellComprFactor, ellRotAngle,this.size());
        for (int i=0;i<pointsOnEllipse.length;i++)
            targets.add(pointsOnEllipse[i]);
        addLinearPathsAnimator(targets,duration,startDelay);
        if (this.size()==1) return;
        int maxY = 0, objindexMaxY = 0;
        for (int i=0;i<targets.size();i++)
            if (targets.get(i).y>maxY) {
                maxY = targets.get(i).y;
                objindexMaxY = i;
            }
        int zIndex = this.size()/2+1;
        int indexLeft = (this.size() + objindexMaxY - 1) % this.size();
        int indexRight = (objindexMaxY + 1);
        this.get(objindexMaxY).setZindex(zIndex--);
        double zFact = zoomFactor;
        while (true) {
            this.get(indexLeft).setZindex(zIndex);
            this.get(indexLeft).addZoomAnimator(zFact,duration,startDelay);
            if (indexLeft == indexRight) break;
            indexLeft = (this.size() + indexLeft - 1) % this.size();
            this.get(indexRight).setZindex(zIndex);
            this.get(indexRight).addZoomAnimator(zFact,duration,startDelay);
            if (indexLeft == indexRight) break;
            indexRight = (indexRight + 1) % this.size();
            zFact *= zoomFactor;
            zIndex--;
        }
    }

    /** Operation ID for addDistrAndAlignAnimators(): to align the tops of the objects */

    public static final int ALIGN_TOP = 1;

    /** Operation ID for addDistrAndAlignAnimators(): to align the vertical centers of the objects */

    public static final int ALIGN_VERT_CENTERS = 2;

    /** Operation ID for addDistrAndAlignAnimators(): to align the bottoms of the objects */

    public static final int ALIGN_BOTTOM = 3;

    /** Operation ID for addDistrAndAlignAnimators(): to align the left borders of the objects */

    public static final int ALIGN_LEFT = 4;

    /** Operation ID for addDistrAndAlignAnimators(): to align the horizontal centers of the objects */

    public static final int ALIGN_HORIZ_CENTERS = 5;

    /** Operation ID for addDistrAndAlignAnimators(): to align the bottoms of the objects */

    public static final int ALIGN_RIGHT = 6;

    /** Operation ID for addDistrAndAlignAnimators(): to distribute the objects horizontally with equal distances between their bounds */

    public static final int DISTR_HORIZ_BOUNDS = 11;

    /** Operation ID for addDistrAndAlignAnimators(): to distribute the objects horizontally with equal distances between their centers */

    public static final int DISTR_HORIZ_CENTERS = 12;

    /** Operation ID for addDistrAndAlignAnimators(): to distribute the objects vertically with equal distances between their bounds */

    public static final int DISTR_VERT_BOUNDS = 21;

    /** Operation ID for addDistrAndAlignAnimators(): to distribute the objects vertically with equal distances between their centers */

    public static final int DISTR_VERT_CENTERS = 22;

    /** Method to create animators that will move the objects on linear paths to new positions.
     * At these new positions, the objects will be horizontally or vertically aligned and/or be horizontally and/or vertically distributed with equal distances.
     * The operations to be performed are specified by an ALIGN_XXX constant, a DISTR_HORIZ_XXX constant and/or a DISTR_VERT_XXX constant.
     * ALIGN_TOP / ALIGN_VERT_CENTERS / ALIGN_BOTTOM and DISTR_VERT_XXX must not be used together.
     * ALIGN_LEFT / ALIGN_HORIZ_CENTERS / ALIGN_RIGHT and DISTR_HORIZ_XXX must not be used together.
     * If these conditions are violated no animators at all will be created.
     *
     * @param alignOp The alignment to be performed: ALIGN_XXX constant (as described above) or 0 for no alignment.
     * @param alignCoord The X or the Y coordinate where to align the objects.
     *                   If Integer.MIN_VALUE, the objects will be aligned with respect to the leftmost object / horizontal center / rightmost object / top object / vertical center / bottom object of the group.
     * @param horizDistrOp The horizontal distribution to be performed: DISTR_HORIZ_XXX constant(as described above) or 0 for no horizontal distribution.
     * @param horizDist The requested horizontal distances between the borders or the centers of the objects.
     *                   If Integer.MIN_VALUE, the objects will be distributed equidistantly between the leftmost and the rightmost object.
     * @param vertDistrOp The vertical distribution to be performed: DISTR_VERT_XXX constant(as described above) or 0 for no vertical distribution.
     * @param vertDist The requested vertical distances between the borders or the centers of the objects.
     *                   If Integer.MIN_VALUE, the objects will be distributed equidistantly between the leftmost and the rightmost object.
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     */

    public void addAlignAndDistrAnimator(int alignOp, int alignCoord, int horizDistrOp, int horizDist, int vertDistrOp, int vertDist, int duration, int startDelay) {
        if (((alignOp == ALIGN_LEFT ||alignOp == ALIGN_HORIZ_CENTERS || alignOp == ALIGN_RIGHT)&&(horizDistrOp!=0) ||
                ((alignOp == ALIGN_TOP ||alignOp == ALIGN_VERT_CENTERS || alignOp == ALIGN_BOTTOM)&&(vertDistrOp!=0))))
            return;
        ArrayList<Point> targetPositions = new ArrayList<>();
        int targetX, targetY;
        // Alignment
        switch (alignOp) {
            case ALIGN_LEFT:
                if (alignCoord == Integer.MIN_VALUE)
                    targetX = GraphicsUtilsCV.objectWithLeftmostBound(this).getLeftBound();
                 else targetX = alignCoord;
                for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
                    AnimatedGuiObjectCV nextObj = it.next();
                    targetPositions.add(new Point(targetX+nextObj.getWidth()/2, nextObj.getCenterY()));
                }
                break;
            case ALIGN_HORIZ_CENTERS:
                if (alignCoord == Integer.MIN_VALUE) {
                    int left = GraphicsUtilsCV.objectWithLeftmostBound(this).getLeftBound(),
                            right = GraphicsUtilsCV.objectWithRightmostBound(this).getRightBound();
                    targetX = left + (right - left) / 2;
                }
                 else targetX = alignCoord;
                for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
                    AnimatedGuiObjectCV nextObj = it.next();
                    targetPositions.add(new Point(targetX, nextObj.getCenterY()));
                }
                break;
            case ALIGN_RIGHT:
                if (alignCoord == Integer.MIN_VALUE)
                      targetX = GraphicsUtilsCV.objectWithRightmostBound(this).getRightBound();
                 else targetX = alignCoord;
                for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
                    AnimatedGuiObjectCV nextObj = it.next();
                    targetPositions.add(new Point(targetX-nextObj.getWidth()/2, nextObj.getCenterY()));
                }
                break;
            case ALIGN_TOP:
                if (alignCoord == Integer.MIN_VALUE)
                      targetY = GraphicsUtilsCV.objectWithTopmostBound(this).getTopBound();
                 else targetY = alignCoord;
                for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
                    AnimatedGuiObjectCV nextObj = it.next();
                    targetPositions.add(new Point(nextObj.getCenterX(),targetY+nextObj.getHeight()/2));
                }
                break;
            case ALIGN_VERT_CENTERS:
                if (alignCoord == Integer.MIN_VALUE) {
                    int top = GraphicsUtilsCV.objectWithTopmostBound(this).getTopBound(),
                            bottom = GraphicsUtilsCV.objectWithBottommostBound(this).getBottomBound();
                    targetY = top + (bottom - top) / 2;
                 }
                 else targetY = alignCoord;
                for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
                    AnimatedGuiObjectCV nextObj = it.next();
                    targetPositions.add(new Point(nextObj.getCenterX(),targetY));
                }
                break;
            case ALIGN_BOTTOM:
                if (alignCoord == Integer.MIN_VALUE)
                      targetY = GraphicsUtilsCV.objectWithBottommostBound(this).getBottomBound();
                 else targetY = alignCoord;
                for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
                    AnimatedGuiObjectCV nextObj = it.next();
                    targetPositions.add(new Point(nextObj.getCenterX(),targetY-nextObj.getHeight()/2));
                }
                break;
            default:
                for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
                    AnimatedGuiObjectCV nextObj = it.next();
                    targetPositions.add(new Point(nextObj.getCenterX(),nextObj.getCenterY()));
                }
        }
        // Map the objects to the points stored in target positions - as a basis for adding the distribute operations
        HashMap<AnimatedGuiObjectCV, Point> objectsToPoints = new HashMap<>();
        Iterator<Point> itPt =  targetPositions.iterator();
        for (Iterator<AnimatedGuiObjectCV> itObj = this.iterator(); itObj.hasNext();)
            objectsToPoints.put(itObj.next(),itPt.next());
        // Horizontal distribution
        if (horizDistrOp==DISTR_HORIZ_CENTERS) {
            ArrayList<AnimatedGuiObjectCV> objectsSorted = GraphicsUtilsCV.sortObjectsHorizCenters(this);
            int distance;
            if (horizDist> Integer.MIN_VALUE) distance = horizDist;
            else distance = (objectsSorted.get(objectsSorted.size()-1).getCenterX()-objectsSorted.get(0).getCenterX())/(this.size()-1);
            int xPos = objectsSorted.get(0).getCenterX();
            for (Iterator<AnimatedGuiObjectCV> it = objectsSorted.iterator(); it.hasNext();) {
                objectsToPoints.get(it.next()).x = xPos;
                xPos += distance;
            }
        }
        if (horizDistrOp==DISTR_HORIZ_BOUNDS) {
            ArrayList<AnimatedGuiObjectCV> objectsSorted = GraphicsUtilsCV.sortObjectsHorizCenters(this);
            int distance;
            if (horizDist> Integer.MIN_VALUE) distance = horizDist;
            else {
                int availableWidth = GraphicsUtilsCV.objectWithRightmostBound(this).getRightBound() - GraphicsUtilsCV.objectWithLeftmostBound(this).getLeftBound();
                for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();)
                    availableWidth -= it.next().getWidth();
                distance = availableWidth / (this.size()-1);
            };
            int xPos = objectsSorted.get(0).getCenterX();
            for (int i=0;i<objectsSorted.size();i++) {
                objectsToPoints.get(objectsSorted.get(i)).x = xPos;
                if (i==objectsSorted.size()-1) break;
                xPos += distance + objectsSorted.get(i).getWidth()/2 + objectsSorted.get(i+1).getWidth()/2;
            }
        }
        // Vertical distribution
        if (vertDistrOp==DISTR_VERT_CENTERS) {
            ArrayList<AnimatedGuiObjectCV> objectsSorted = GraphicsUtilsCV.sortObjectsVertCenters(this);
            int distance;
            if (vertDist> Integer.MIN_VALUE) distance = vertDist;
            else distance = (objectsSorted.get(objectsSorted.size()-1).getCenterY()-objectsSorted.get(0).getCenterY())/(this.size()-1);
            int yPos = objectsSorted.get(0).getCenterY();
            for (Iterator<AnimatedGuiObjectCV> it = objectsSorted.iterator(); it.hasNext();) {
                objectsToPoints.get(it.next()).y = yPos;
                yPos += distance;
            }
        }
        if (vertDistrOp==DISTR_VERT_BOUNDS) {
            ArrayList<AnimatedGuiObjectCV> objectsSorted = GraphicsUtilsCV.sortObjectsVertCenters(this);
            int distance;
            if (vertDist> Integer.MIN_VALUE) distance = vertDist;
            else {
                int availableHeight = GraphicsUtilsCV.objectWithBottommostBound(this).getBottomBound() - GraphicsUtilsCV.objectWithTopmostBound(this).getTopBound();
                for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();)
                    availableHeight -= it.next().getHeight();
                distance = availableHeight / (this.size()-1);
            };
            int yPos = objectsSorted.get(0).getCenterY();
            for (int i=0;i<objectsSorted.size();i++) {
                objectsToPoints.get(objectsSorted.get(i)).y = yPos;
                if (i==objectsSorted.size()-1) break;
                yPos += distance + objectsSorted.get(i).getHeight()/2 + objectsSorted.get(i+1).getHeight()/2;
            }
        }
        addLinearPathsAnimator(targetPositions,duration,startDelay);
    }

    /** Method to create animators that will align the left borders of the objects of the group while keeping their vertical positions (= y-values).
     * The object will be aligned to the left border of the leftmost object (left border as to the time of the creation of the animators, not the time of their start).
     * The animators will be associated with the objects, but not started yet.
     * NB: The animators might show unexpected results if not started first, i.e. if started after other animators defined for the same objects.
     * @param duration Duration of the animations (in ms)
     */

    public void addAlignLeftAnimator(int duration) {
        addAlignLeftAnimator(duration,0);
    }

    /** Method to create animators that will align the left borders of the objects of the group while keeping their vertical positions (= y-values).
     * The object will be aligned to the left border of the leftmost object (left border as to the time of the creation of the animators, not the time of their start).
     * The animators will be associated with the objects, but not started yet.
     * NB: The animators might show unexpected results if not started first, i.e. if started after other animators defined for the same objects.
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     */

    public void addAlignLeftAnimator(int duration, int startDelay) {
        int targetX = GraphicsUtilsCV.objectWithLeftmostBound(this).getLeftBound();
        addAlignLeftToXAnimator(targetX,duration,startDelay);
    }

    /** Method to create animators that will align the left borders of the objects of the group while keeping their vertical positions (= y-values).
     * The animators will be associated with the objects, but not started yet.
     * @param targetX The x coordinate where to align the objects.
     * @param duration Duration of the animations (in ms)
     */

    public void addAlignLeftToXAnimator(int targetX, int duration) {
        addAlignLeftToXAnimator(targetX,duration,0);
    }

    /** Method to create animators that will align the left borders of the objects of the group while keeping their vertical positions (= y-values).
     * The animators will be associated with the objects, but not started yet.
     * @param targetX The x coordinate where to align the objects.
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     */

    public void addAlignLeftToXAnimator(int targetX, int duration, int startDelay) {
        ArrayList<Point> targets = new ArrayList<>();
        for (Iterator<AnimatedGuiObjectCV> itObj = this.iterator(); itObj.hasNext();) {
            AnimatedGuiObjectCV obj = itObj.next();
            targets.add(new Point(targetX+obj.getWidth()/2, obj.getCenterY()));
        }
        addLinearPathsAnimator(targets,duration,startDelay);
    }

    /** Method to create animators that will align the right borders of the objects of the group while keeping their vertical positions (= y-values).
     * The object will be aligned to the right border of the rightmost object (right border as to the time of the creation of the animators, not the time of their start).
     * The animators will be associated with the objects, but not started yet.
     * NB: The animators might show unexpected results if not started first, i.e. if started after other animators defined for the same objects.
     * @param duration Duration of the animations (in ms)
     */

    public void addAlignRightAnimator(int duration) {
        addAlignRightAnimator(duration,0);
    }

    /** Method to create animators that will align the right borders of the objects of the group while keeping their vertical positions (= y-values).
     * The object will be aligned to the right border of the rightmost object (right border as to the time of the creation of the animators, not the time of their start).
     * The animators will be associated with the objects, but not started yet.
     * NB: The animators might show unexpected results if not started first, i.e. if started after other animators defined for the same objects.
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     */

    public void addAlignRightAnimator(int duration, int startDelay) {
        int targetX = GraphicsUtilsCV.objectWithRightmostBound(this).getRightBound();
        addAlignRightToXAnimator(targetX,duration,startDelay);
    }

    /** Method to create animators that will align the right borders of the objects of the group while keeping their vertical positions (= y-values).
     * The animators will be associated with the objects, but not started yet.
     * @param targetX The x coordinate where to align the objects.
     * @param duration Duration of the animations (in ms)
     */

    public void addAlignRightToXAnimator(int targetX, int duration) {
        addAlignRightToXAnimator(targetX,duration,0);
    }

    /** Method to create animators that will align the right borders of the objects of the group while keeping their vertical positions (= y-values).
     * The animators will be associated with the objects, but not started yet.
     * @param targetX The x coordinate where to align the objects.
     * @param duration Duration of the animations (in ms)
     * @param startDelay Start delay of the animations (in ms)
     */

    public void addAlignRightToXAnimator(int targetX, int duration, int startDelay) {
        ArrayList<Point> targets = new ArrayList<>();
        for (Iterator<AnimatedGuiObjectCV> itObj = this.iterator(); itObj.hasNext();) {
            AnimatedGuiObjectCV obj = itObj.next();
            targets.add(new Point(targetX-obj.getWidth()/2, obj.getCenterY()));
        }
        addLinearPathsAnimator(targets,duration,startDelay);
    }

    /** Add animators to move the objects on direct lines to positions within a given rectangle.
     * The relative distances of the objects in the X and Y dimensions will be maintained.
     * The animators will be associated with the objects, but not started yet.
     * @param targetLeft Left bound of the target rectangle
     * @param targetTop Upper bound of the target rectangle
     * @param targetRight Right bound of the target rectangle
     * @param targetBottom Lower bound of the target rectangle
     * @param duration Duration of the animation (in ms)
     */

    public void addMoveToAreaAnimator(int targetLeft, int targetTop, int targetRight, int targetBottom, int duration) {
        addMoveToAreaAnimator(targetLeft,targetTop,targetRight,targetBottom,duration,0);
    }

    /** Add animators to move the objects on direct lines to positions within a given rectangle.
     * The relative positions of the objects will be maintained.
     * The animators will be associated with the objects, but not started yet.
     * @param targetLeft Left bound of the target rectangle
     * @param targetTop Upper bound of the target rectangle
     * @param targetRight Right bound of the target rectangle
     * @param targetBottom Lower bound of the target rectangle
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     */

    public void addMoveToAreaAnimator(int targetLeft, int targetTop, int targetRight, int targetBottom, int duration, int startDelay) {
        Rect enclRect = getEnclosingRect();
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            int targetX, targetY;
            // place the object in the X dimension such that its distances to the left and right borders of the target rectangle will be proportionally the same as in the original enclosing rectangle of the group
            // and in the Y dimension accordingly
            if (obj.getWidth()>targetRight-targetLeft)
                targetX = targetLeft+(targetRight-targetLeft)/2;
            else {
                int origXMargins = enclRect.right - enclRect.left - obj.getWidth();
                double proportionLeftMargin = (double) (obj.getLeftBound() - enclRect.left) / origXMargins;
                targetX = targetLeft + (int) (proportionLeftMargin * (targetRight - targetLeft - obj.getWidth())) + obj.getWidth() / 2;
            }
            if (obj.getHeight()>targetBottom-targetTop)
                targetY = targetTop+(targetBottom-targetTop)/2;
            else {
                int origYMargins = enclRect.bottom - enclRect.top - obj.getHeight();
                double proportionTopMargin = (double) (obj.getTopBound() - enclRect.top) / origYMargins;
                targetY = targetTop + (int) (proportionTopMargin * (targetBottom - targetTop - obj.getHeight())) + obj.getHeight() / 2;
            }
            obj.addLinearPathAnimator(targetX,targetY,duration,startDelay);
        }
    }

    /** Add animators to move the objects on direct lines towards the center of the object group or away from it.
     * The distance of an object at its final position from the center is its original distance multiplied by an attraction factor.
     * If this factor is small than 1, the object will move closer to the center (i.e. will be attracted by the center).
     * If this factor is larger than 1, the object will move farther away from the center (i.e. will be repulsed from the center).
     * The animators will be associated with the objects, but not started yet.
     * @param attractionFactor Factor as explained above
     * @param duration Duration of the animation (in ms)
     */

    public void addContractOrExpandAnimator(double attractionFactor, int duration) {
        addContractOrExpandAnimator(attractionFactor,duration,0);
    }

    /** Add animators to move the objects on direct lines towards the center of the object group or away from it.
     * The distance of an object at its final position from the center is its original distance multiplied by an attraction factor.
     * If this factor is small than 1, the object will move closer to the center (i.e. will be attracted by the center).
     * If this factor is larger than 1, the object will move farther away from the center (i.e. will be repulsed from the center).
     * The animators will be associated with the objects, but not started yet.
     * @param attractionFactor Factor as explained above
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     */

    public void addContractOrExpandAnimator(double attractionFactor, int duration, int startDelay) {
        Rect enclRect = getEnclosingRect();
        addContractOrExpandAnimator(enclRect.centerX(),enclRect.centerY(),attractionFactor,duration,startDelay);
    }

    /** Add animators to move the objects on direct lines towards some specified target coordinates or away from these coordinates.
     * The distance of an object at its final position from these coordinates is its original distance multiplied by an attraction factor.
     * If this factor is small than 1, the object will move closer to the target (i.e. will be attracted by the target).
     * If this factor is larger than 1, the object will move farther away from the target (i.e. will be repulsed from the target).
     * The animators will be associated with the objects, but not started yet.
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param attractionFactor Factor as explained above
     * @param duration Duration of the animation (in ms)
     */

    public void addContractOrExpandAnimator(int targetX, int targetY, double attractionFactor, int duration) {
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();)
            it.next().addAttractOrRepulseAnimator(targetX,targetY,attractionFactor,duration,0);
    }

    /** Add animators to move the objects on direct lines towards some specified target coordinates or away from these coordinates.
     * The distance of an object at its final position from these coordinates is its original distance multiplied by an attraction factor.
     * If this factor is small than 1, the object will move closer to the target (i.e. will be attracted by the target).
     * If this factor is larger than 1, the object will move farther away from the target (i.e. will be repulsed from the target).
     * The animators will be associated with the objects, but not started yet.
     * @param targetX X coordinate of the target point
     * @param targetY Y coordinate of the target point
     * @param attractionFactor Factor as explained above
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     */

    public void addContractOrExpandAnimator(int targetX, int targetY, double attractionFactor, int duration, int startDelay) {
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();)
            it.next().addAttractOrRepulseAnimator(targetX,targetY,attractionFactor,duration,startDelay);
    }

    /** Add animators to zoom into or out of the object group.
     * The center of the object group is used as the center of the zoom operation.
     * The animators will be associated with the objects, but not started yet.
     * @param zoomFactor The zoom factor (e.g. 0.5 = 50%, 2 = 200% etc.)
     * @param duration Duration of the animation (in ms)
     */

    public void addZoomAnimator(double zoomFactor, int duration) {
        addZoomAnimator(zoomFactor,duration,0);
    }

    /** Add animators to zoom into or out of the object group.
     * The center of the object group is used as the center of the zoom operation.
     * The animators will be associated with the objects, but not started yet.
     * @param zoomFactor The zoom factor (e.g. 0.5 = 50%, 2 = 200%)
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     */

    public void addZoomAnimator(double zoomFactor, int duration, int startDelay) {
        Rect enclRect = getEnclosingRect();
        addZoomAnimator(enclRect.centerX(),enclRect.centerY(),zoomFactor,duration,startDelay);
    }

    /** Add animators to zoom into or out of the object group.
     * The animators will be associated with the objects, but not started yet.
     * @param zoomCenterX X coordinate of the center of the zoom operation
     * @param zoomCenterY Y coordinate of the center of the zoom operation
     * @param zoomFactor The zoom factor (e.g. 0.5 = 50%, 2 = 200% etc.)
     * @param duration Duration of the animation (in ms)
     */

    public void addZoomAnimator(int zoomCenterX, int zoomCenterY, double zoomFactor, int duration) {
        addZoomAnimator(zoomCenterX,zoomCenterY,zoomFactor,duration,0);
    }

    /** Add animators to zoom into or out of the object group.
     * The animators will be associated with the objects, but not started yet.
     * @param zoomCenterX X coordinate of the center of the zoom operation
     * @param zoomCenterY Y coordinate of the center of the zoom operation
     * @param zoomFactor The zoom factor (e.g. 0.5 = 50%, 2 = 200%)
     * @param duration Duration of the animation (in ms)
     * @param startDelay Start delay of the animation (in ms)
     */

    public void addZoomAnimator(int zoomCenterX, int zoomCenterY, double zoomFactor, int duration, int startDelay) {
        addContractOrExpandAnimator(zoomCenterX,zoomCenterY,zoomFactor,duration,startDelay);
        for (Iterator<AnimatedGuiObjectCV> it = this.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            obj.addZoomAnimator(zoomFactor, duration, startDelay);
        }
    }

}