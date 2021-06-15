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
 * Created by vogt on 16.01.2018.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/** This class provides some utility methods for graphics programming.
 * For utilities concerning mathematical graphs, see class GraphsUtilsCV.
 */

public class GraphicsUtilsCV {

    /** Method to calculate the distance between two points.
     *
     * @param p1 First point
     * @param p2 Second point
     * @return Distance between the points
     */

    public static double distance(Point p1, Point p2) {
        return distance(p1.x,p1.y,p2.x,p2.y);
    }

    /** Method to calculate the distance between two points.
     *
     * @param p1x First point: x position
     * @param p1y First point: y position
     * @param p2x Second point: x position
     * @param p2y Second point: y position
     * @return Distance between the points
     */

    public static double distance(double p1x, double p1y, double p2x, double p2y) {
        return Math.sqrt((p2x-p1x)*(p2x-p1x)+(p2y-p1y)*(p2y-p1y));
    }

    /** Method to calculate the horizontal distance between two objects of class AnimatedGuiObjectCV.
     * Let oleft be the left object and oright the right object, i.e. the X coordinate of the center of oleft
     * is smaller than the X coordinate of the center of oright.
     * Then the value returned is the difference between the X coordinate of the leftmost corner of the
     * enclosing rectangle of oright (= its left bound in case of an unrotated object) and the
     * X coordinate of the rightmost corner of the enclosing rectangle of oleft (= its right bound
     * in case of an unrotated object).
     *
     * @param obj1 The first object
     * @param obj2 The first object
     * @return Horizontal distance between the objects, as defined above
     */

    public static int distCentersHoriz(AnimatedGuiObjectCV obj1, AnimatedGuiObjectCV obj2) {
        AnimatedGuiObjectCV oLeft = obj1, oRight = obj2;
        if (obj1.getCenterX()>obj2.getCenterX()) {
            oLeft = obj2; oRight = obj1;
        }
        return oRight.getMinX()-oLeft.getMaxX();
    }

    /** Method to calculate the vertical distance between two objects of class AnimatedGuiObjectCV.
     * Let otop be the upper object and obottom the lower object, i.e. the Y coordinate of the center of otop
     * is smaller than the Y coordinate of the center of obottom.
     * Then the value returned is the difference between the Y coordinate of the upmost corner of the
     * enclosing rectangle of obottom (= its top bound in case of an unrotated object) and the
     * Y coordinate of the lowermost corner of the enclosing rectangle of otop (= its lower bound
     * in case of an unrotated object).
     *
     * @param obj1 The first object
     * @param obj2 The first object
     * @return Vertical distance between the objects, as defined above
     */

    public static int distCentersVert(AnimatedGuiObjectCV obj1, AnimatedGuiObjectCV obj2) {
        AnimatedGuiObjectCV oTop = obj1, oBottom = obj2;
        if (obj1.getCenterY()>obj2.getCenterY()) {
            oBottom = obj2; oTop = obj1;
        }
        return oBottom.getMinY()-oTop.getMaxY();
    }

    /** Method to find the object with the leftmost bound in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the leftmost bound
     */

    public static AnimatedGuiObjectCV objectWithLeftmostBound(Collection<AnimatedGuiObjectCV> objects) {
        if (objects==null || objects.isEmpty()) return null;
        AnimatedGuiObjectCV result = null;
        for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            if (result==null)
                result = obj;
            else
            if (obj.getLeftBound()<result.getLeftBound())
                result = obj;
        }
        return result;
    }

    /** Method to find the object with the leftmost center in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the leftmost center
     */

    public static AnimatedGuiObjectCV objectWithLeftmostCenter(Collection<AnimatedGuiObjectCV> objects) {
        if (objects==null || objects.isEmpty()) return null;
        AnimatedGuiObjectCV result = null;
        for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            if (result==null)
                result = obj;
            else
            if (obj.getCenterX()<result.getCenterX())
                result = obj;
        }
        return result;
    }

    /** Method to find the object with the rightmost bound in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the rightmost bound
     */

    public static AnimatedGuiObjectCV objectWithRightmostBound(Collection<AnimatedGuiObjectCV> objects) {
        if (objects==null || objects.isEmpty()) return null;
        AnimatedGuiObjectCV result = null;
        for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            if (result==null)
                result = obj;
            else
            if (obj.getRightBound()>result.getRightBound())
                result = obj;
        }
        return result;
    }

    /** Method to find the object with the rightmost center in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the rightmost center
     */

    public static AnimatedGuiObjectCV objectWithRightmostCenter(Collection<AnimatedGuiObjectCV> objects) {
        if (objects==null || objects.isEmpty()) return null;
        AnimatedGuiObjectCV result = null;
        for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            if (result==null)
                result = obj;
            else
            if (obj.getCenterX()>result.getCenterX())
                result = obj;
        }
        return result;
    }

    /** Method to find the object with the topmost bound in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the topmost bound
     */

    public static AnimatedGuiObjectCV objectWithTopmostBound(Collection<AnimatedGuiObjectCV> objects) {
        if (objects==null || objects.isEmpty()) return null;
        AnimatedGuiObjectCV result = null;
        for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            if (result==null)
                result = obj;
            else
            if (obj.getTopBound()<result.getTopBound())
                result = obj;
        }
        return result;
    }

    /** Method to find the object with the topmost center in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the topmost center
     */

    public static AnimatedGuiObjectCV objectWithTopmostCenter(Collection<AnimatedGuiObjectCV> objects) {
        if (objects==null || objects.isEmpty()) return null;
        AnimatedGuiObjectCV result = null;
        for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            if (result==null)
                result = obj;
            else
            if (obj.getCenterY()<result.getCenterY())
                result = obj;
        }
        return result;
    }

    /** Method to find the object with the bottommost bound in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the bottommost bound
     */

    public static AnimatedGuiObjectCV objectWithBottommostBound(Collection<AnimatedGuiObjectCV> objects) {
        if (objects==null || objects.isEmpty()) return null;
        AnimatedGuiObjectCV result = null;
        for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            if (result==null)
                result = obj;
            else
            if (obj.getBottomBound()>result.getBottomBound())
                result = obj;
        }
        return result;
    }

    /** Method to find the object with the bottommost center in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the bottommost center
     */

    public static AnimatedGuiObjectCV objectWithBottommostCenter(Collection<AnimatedGuiObjectCV> objects) {
        if (objects==null || objects.isEmpty()) return null;
        AnimatedGuiObjectCV result = null;
        for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            if (result==null)
                result = obj;
            else
            if (obj.getCenterY()>result.getCenterY())
                result = obj;
        }
        return result;
    }

    /** Method to sort a collection of objects according to their horizontal positions (= x coordinates of their centers).
     * @param objects The collection of objects
     * @return An ArrayList with the objects sorted in ascending order according to their horizontal positions
     */

    public static ArrayList<AnimatedGuiObjectCV> sortObjectsHorizCenters(Collection<AnimatedGuiObjectCV> objects) {
        if (objects==null || objects.isEmpty()) return null;
        AnimatedGuiObjectCV objArray[] = new AnimatedGuiObjectCV[objects.size()];
        int i=0;
        for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();)
            objArray[i++] = it.next();
        for (i=objArray.length-2;i>=0;i--)
            for (int j=0;j<=i;j++)
                if (objArray[j].getCenterX()>objArray[j+1].getCenterX()) {
                    AnimatedGuiObjectCV tmp = objArray[j];
                    objArray[j] = objArray[j+1];
                    objArray[j+1] = tmp;
                }
        ArrayList<AnimatedGuiObjectCV> result = new ArrayList<>();
        for (i=0;i<objArray.length;i++)
            result.add(objArray[i]);
        return result;
    }

    /** Method to sort a collection of objects according to their vertical positions (= y coordinates of their centers).
     * @param objects The collection of objects
     * @return An ArrayList with the objects sorted in ascending order according to their vertical positions
     */

    public static ArrayList<AnimatedGuiObjectCV> sortObjectsVertCenters(Collection<AnimatedGuiObjectCV> objects) {
        if (objects==null || objects.isEmpty()) return null;
        AnimatedGuiObjectCV objArray[] = new AnimatedGuiObjectCV[objects.size()];
        int i=0;
        for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();)
            objArray[i++] = it.next();
        for (i=objArray.length-2;i>=0;i--)
            for (int j=0;j<=i;j++)
                if (objArray[j].getCenterY()>objArray[j+1].getCenterY()) {
                    AnimatedGuiObjectCV tmp = objArray[j];
                    objArray[j] = objArray[j+1];
                    objArray[j+1] = tmp;
                }
        ArrayList<AnimatedGuiObjectCV> result = new ArrayList<>();
        for (i=0;i<objArray.length;i++)
            result.add(objArray[i]);
        return result;
    }

    /** Method to rotate a point around a center.
     *
     * @param toRotateX Point to be rotated: x coordinate
     * @param toRotateY Point to be rotated: y coordinate
     * @param centerX Center of the rotation: x coordinate
     * @param centerY Center of the rotation: y coordinate
     * @param angle Rotation angle (in radians)
     * @return The point after the rotation
     */

    public static Point rotate(int toRotateX, int toRotateY, int centerX, int centerY, double angle) {
        int normX = toRotateX - centerX;
        int normY = toRotateY - centerY;
        int rotX = (int) (normX * Math.cos(angle) - normY * Math.sin(angle)) + centerX;
        int rotY = (int) (normX * Math.sin(angle) + normY * Math.cos(angle)) + centerY;
        return new Point(rotX,rotY);
    }

    /** Method to rotate a point around a center.
     *
     * @param toRotate Point to be rotated
     * @param center Center of the rotation
     * @param angle Rotation angle (in radians)
     * @return The point after the rotation
     */

    public static Point rotate(Point toRotate, Point center, double angle) {
        return rotate(toRotate.x,toRotate.y,center.x,center.y,angle);
    }

    /** Method to calculate a number of points lying equidistantly on a circle around a center
     * @param centerX Center of the circle - X coordinate
     * @param centerY Center of the circle - Y coordinate
     * @param radius Radius of the circle
     * @param numberOfPoints Number of points to be placed on the circle
     * @return An array with 'numberOfPoints' Point objects specifying the coordinates of the points
     */

    public static Point[] pointsOnCircle(int centerX, int centerY, int radius, int numberOfPoints) {
        Point result[] = new Point[numberOfPoints];
        for (int i=0;i<numberOfPoints;i++) {
            int x = (int)(centerX+radius* Math.sin(2* Math.PI/numberOfPoints*i));
            int y = (int)(centerY-radius* Math.cos(2* Math.PI/numberOfPoints*i));
            result[i] = new Point(x,y);
        }
        return result;
    }

    /** Method to calculate a number of points lying equidistantly on an ellipse around a center.
     * The ellipse is implemented by a circle that is compressed vertically by a specified compression factor
     * and then rotated by a specified angle around its center.
     * @param centerX Center of the ellipse - X coordinate
     * @param centerY Center of the ellipse - Y coordinate
     * @param radius Radius of the ellipse
     * @param comprFactor; // Vertical compression of the circle (0<=comprFactor<=1), yielding the ellipse
     * @param rotAngle; // Rotation angle of the resulting ellipse (in radians)
     * @param numberOfPoints Number of points to be placed on the circle
     * @return An array with 'numberOfPoints' Point objects specifying the coordinates of the points
     */

    public static Point[] pointsOnEllipse(int centerX, int centerY, int radius, double comprFactor, double rotAngle, int numberOfPoints) {
        Point result[] = new Point[numberOfPoints];
        Point centerEll = new Point(centerX,centerY);
        Point startPoint = new Point(centerX+radius,centerY);
        for (int i=0;i<numberOfPoints;i++) {
            Point ptOnCircle = GraphicsUtilsCV.rotate(startPoint,centerEll,i*2* Math.PI/numberOfPoints);
            int compressedY = centerY + (int)(comprFactor*(centerY-ptOnCircle.y));
            Point ptOnEllipseUnrotated = new Point(ptOnCircle.x,compressedY);
            result[i] = GraphicsUtilsCV.rotate(ptOnEllipseUnrotated,centerEll,rotAngle);
        }
        return result;
    }

    /** Get the smallest rectangle that encloses a given set of objects of class AnimatedGuiObjectsCV.
     *
     * @param guiObjects The set of objects for which the enclosing rectangle shall be computed
     * @return The minimal rectangle that encloses all objects (null if guiobjects==null or is empty)
     */

    static public Rect getEnclosingRect(Collection<AnimatedGuiObjectCV> guiObjects) {
        if (guiObjects==null||guiObjects.isEmpty())
            return null;
        int left = Integer.MAX_VALUE,
            top = Integer.MAX_VALUE,
            right = Integer.MIN_VALUE,
            bottom = Integer.MIN_VALUE;
        for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            if (obj.getMinX()<left) left=obj.getMinX();
            if (obj.getMinY()<top) top=obj.getMinY();
            if (obj.getMaxX()>right) right=obj.getMaxX();
            if (obj.getMaxY()>bottom) bottom=obj.getMaxY();
        }
        return new Rect(left,top,right,bottom);
    }

    /** Method to find all objects that are connected to a given object of class AnimatedGuiObjectCV by lines of class DependentLineObjectToObjectCV.
     * @param guiobj The object for which the connected objects shall be found
     * @return An ArrayList with the connected objects
     */

    public static ArrayList<AnimatedGuiObjectCV> getObjectsWithLineTo(AnimatedGuiObjectCV guiobj) {
      ArrayList<AnimatedGuiObjectCV> result = new ArrayList<>();
      ArrayList<DependentGuiObjectCV> depObjects = guiobj.getDependentGuiObjects();
      for (Iterator<DependentGuiObjectCV> it = depObjects.iterator(); it.hasNext();) {
          DependentGuiObjectCV depObject = it.next();
          if (depObject instanceof DependentLineObjectToObjectCV) {
              AnimatedGuiObjectCV[] connectedObjects = ((DependentLineObjectToObjectCV) depObject).getEndpoints();
              if (connectedObjects[0]==guiobj)
                  result.add(connectedObjects[1]);
              else
                  result.add(connectedObjects[0]);
          }
      }
      return result;
    }

    /** Objects shall be left-aligned near the left border of the display.  */

    public static final int ALIGN_LEFT = 0;

    /** Objects shall be right-aligned near the right border of the display (i.e. their right bounds are aligned and lie close to the border).  */

    public static final int ALIGN_RIGHTRIGHT = 1;

    /** Objects shall be left-aligned near the right border of the display (i.e. their left borders are aligned and the objects lie close to the right border,
     * the object with the largest width lying closest).  */

    public static final int ALIGN_RIGHTLEFT = 2;

    /** Method to calculate the positions for a number of objects of class AnimatedGuiObjectsCV
     * that shall be placed right- or left-aligned on the display, one below the other.
     * Can be used to calculate the target points of an animation that shall move these objects.
     * @param guiObjects The objects for which the positions shall be calculated
     * @param marginHoriz Horizontal margin = Distance to the left or right border of the display (depending on the alignment)
     * @param marginTop Top margin = Distance of the first object to the top border of the display.
     * @param vertSpacing Vertical spacing between the objects
     * @param displayWidth Width of the display (ignored if aligmentType==ALIGN_LEFT)
     * @param alignmentType How the objects shall be aligned - ALIGN_LEFT, ALIGN_RIGHTRIGHT, ALIGN_RIGHTLEFT
     * @return HashMap with the positions - key set = the graphical objects, value set = the corresponding positions (x/y coordinates of their centers).
     */

    public static HashMap<AnimatedGuiObjectCV, Point> positionsVerticalAligned(Collection<AnimatedGuiObjectCV> guiObjects, int marginHoriz, int marginTop, int vertSpacing, int displayWidth, int alignmentType) {
        HashMap<AnimatedGuiObjectCV, Point> result = new HashMap<>();
        int currYpos = marginTop;
        int maxObjWidth = 0;
        if (alignmentType==ALIGN_RIGHTLEFT) {
            for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext();) {
                AnimatedGuiObjectCV obj = it.next();
                if (obj.getWidth() > maxObjWidth) maxObjWidth = obj.getWidth();
            }
        }
        for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            Point pos;
            switch (alignmentType) {
                case ALIGN_LEFT: pos = new Point(marginHoriz+obj.getWidth()/2,currYpos+obj.getHeight()/2); break;
                case ALIGN_RIGHTRIGHT: pos = new Point(displayWidth-marginHoriz-obj.getWidth()/2,currYpos+obj.getHeight()/2); break;
                case ALIGN_RIGHTLEFT: pos = new Point(displayWidth-marginHoriz-maxObjWidth+obj.getWidth()/2,currYpos+obj.getHeight()/2); break;
                default: pos = new Point(0,0);
            }
            result.put(obj,pos);
            currYpos += obj.getHeight()+vertSpacing;
        }
        return result;
    }

    /** Method to place a number of objects of class AnimatedGuiObjectsCV on the display.
     * The objects will be placed right- or left-aligned on the display, one below the other.
     * @param guiObjects The objects to be placed
     * @param marginHoriz Horizontal margin = Distance to the left or right border of the display (depending on the alignment)
     * @param marginTop Top margin = Distance of the first object to the top border of the display.
     * @param vertSpacing Vertical spacing between the objects
     * @param displayWidth Width of the display (ignored if aligmentType==ALIGN_LEFT)
     * @param alignmentType How the objects shall be aligned - ALIGN_LEFT, ALIGN_RIGHTRIGHT, ALIGN_RIGHTLEFT
     */

    public static void placeObjectsVerticalAligned(Collection<AnimatedGuiObjectCV> guiObjects, int marginHoriz, int marginTop, int vertSpacing, int displayWidth, int alignmentType) {
        HashMap<AnimatedGuiObjectCV, Point> positions = positionsVerticalAligned(guiObjects,marginHoriz,marginTop,vertSpacing,displayWidth,alignmentType);
        for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext();) {
            AnimatedGuiObjectCV obj = it.next();
            obj.setCenter(positions.get(obj));
        }
    }

    /** Method to create a bitmap showing a string.
     *
     * @param text String to be shown
     * @param textSize Text size (in pixels)
     * @param textColor Color of the text
     * @param backgroundColor Color of the background
     *
     * @return A new Bitmap object showing the string.
     */

    public static Bitmap textToBitmap(String text, int textSize, int textColor, int backgroundColor) {
        // Create Paint object for the text
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        Typeface tf = Typeface.create("arial", Typeface.NORMAL);
        textPaint.setTypeface(tf);
        int textWidth = (int) textPaint.measureText(text);
        // Create Paint object for the background
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(backgroundColor);
        // Create bitmap
        Bitmap bitmap = Bitmap.createBitmap(textWidth,textSize, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawPaint(bgPaint);
        c.drawText(text,0,textSize-textPaint.descent(),textPaint);
        return bitmap;
    }

    /** Method to make pixels of a given bitmap with a specific color transparent.
     * Can e.g. be used to generate a bitmap with a transparent background.
     *
     * @param bitmap The bitmap for which pixels shall be made transparent
     * @param colorToMakeTransparent Color of the pixels to be made transparent
     * @return A new Bitmap object, generated from the give bitmap with transparent pixels.
     */

    public static Bitmap makeBitmapTransparent(Bitmap bitmap, int colorToMakeTransparent) {
        int width =  bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int noPixels = result.getHeight()*result.getWidth();
        int pixels[] = new int[noPixels];
        bitmap.getPixels(pixels,0,result.getWidth(),0,0,result.getWidth(),result.getHeight());
        result.setPixels(pixels,0,width,0,0,width,height);
        for(int i =0;i<noPixels;i++)
            if( pixels[i] == colorToMakeTransparent)
                pixels[i] = Color.alpha(Color.TRANSPARENT);
        result.setPixels(pixels,0,result.getWidth(),0,0,result.getWidth(),result.getHeight());
        return result;
    }

    /** Method that checks if a string contains letters with descents.
     * The string is assumed to contain ony letters and digits.
     * @param s The string to check
     * @return true if the string contains letters with descents, false otherwise
     */

    public static boolean hasDescents(String s) {
        char c;
        for (int i=0;i<s.length();i++) {
            c = s.charAt(i);
            if (c=='g'||c=='j'||c=='p'||c=='q'||c=='y')
                return true;
        }
        return false;
    }

    /** Method that checks if a string contains letters with ascents.
     * The string is assumed to contain ony letters and digits.
     * @param s The string to check
     * @return true if the string contains letters with ascents, false otherwise
     */

    public static boolean hasAscents(String s) {
        char c;
        for (int i=0;i<s.length();i++) {
            c = s.charAt(i);
            if (Character.isUpperCase(c)|| Character.isDigit(c))
                return true;
            if (c>='h'&&c<='l') return true;
            if (c=='b'||c=='d'||c=='f'||c=='t')
                return true;
        }
        return false;
    }

}