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
 3.5.2021

*/

package de.thkoeln.cvogt.android.propanimutil_demos;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_ArcCircleSpiralpath extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demo1();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.menu_4demos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menuItemDemo1)
            demo1();
        if (item.getItemId()==R.id.menuItemDemo2)
            demo2();
        if (item.getItemId()==R.id.menuItemDemo3)
            demo3();
            // demo3_landscape();
        if (item.getItemId()==R.id.menuItemDemo4)
            demo4();
        return true;
    }

    private void demo1() {
        AnimatedGuiObjectCV kreise[], kreisBackground;
        AnimationViewCV view;
        view = new AnimationViewCV(this);
        // Place a grey circle in the background
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int centerX = screenwidth/2;
        int centerY = screenheight/2-150;
        int radiusBackground = screenwidth/3;
        kreisBackground = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.LTGRAY,centerX,centerY,2*radiusBackground);
        view.addAnimatedGuiObject(kreisBackground);
        // Place eight smaller circles on the background circle
        int radiusAnimatedCircle = screenwidth/15;
        kreise = new AnimatedGuiObjectCV[6];
        kreise[0] = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.RED,centerX-radiusBackground,centerY,2*radiusAnimatedCircle);
        kreise[1] = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.BLUE,centerX-radiusBackground,centerY,2*radiusAnimatedCircle);
        kreise[2] = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.GREEN,centerX-radiusBackground,centerY,2*radiusAnimatedCircle);
        kreise[3] = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.CYAN,centerX-radiusBackground,centerY,2*radiusAnimatedCircle);
        kreise[4] = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.MAGENTA,centerX-radiusBackground,centerY,2*radiusAnimatedCircle);
        kreise[5] = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.YELLOW,centerX-radiusBackground,centerY,2*radiusAnimatedCircle);
        // Let the small circles rotate around the background circle
        for (int i=0;i<3;i++) {
            kreise[i].addArcPathAnimator(centerX,centerY,2* Math.PI+(2*i+1)* Math.PI/3+ Math.PI/6,new LinearInterpolator(),5000,1000);  // the animations start with one second delay to allow the animationView to appear on the screen before
            view.addAnimatedGuiObject(kreise[i]);
        }
        for (int i=3;i<6;i++) {
            kreise[i].addArcPathAnimator(centerX,centerY,-2* Math.PI-(2*(i-2)* Math.PI/3- Math.PI/6),new LinearInterpolator(),5000,1000);  // the animations start with one second delay to allow the animationView to appear on the screen before
            view.addAnimatedGuiObject(kreise[i]);
        }
        setContentView(view);
        view.startAnimations();
    }

    private void demo2() {
        AnimatedGuiObjectCV kreise[], kreisBackground1;
        AnimationViewCV view = new AnimationViewCV(this);
        AnimatedGuiObjectCV kreisBackground2, animDrawables[];
        // Place a grey circle in the background
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int centerX = screenwidth/2;
        int centerY = screenheight/4;
        int radiusBackground = screenwidth/6;
        kreisBackground1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.LTGRAY,centerX,centerY,2*radiusBackground);
        view.addAnimatedGuiObject(kreisBackground1);
        // Place a number of smaller circles equidistantly on the background circle
        // and let them rotate infinitely around the background circle
        int sizeAnimatedGuiObject = screenwidth/12;
        int numberObjects = 8;
        int[] colors = { Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.DKGRAY, Color.BLACK};
        Point[] points = GraphicsUtilsCV.pointsOnCircle(centerX,centerY,radiusBackground,numberObjects);
        animDrawables = new AnimatedGuiObjectCV[numberObjects];
        for (int i=0;i<numberObjects;i++) {
            int type;
            if (i%2==0) type = AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE; else type =AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE;
            animDrawables[i] = new AnimatedGuiObjectCV(type,"OBJ"+i,colors[i%colors.length],points[i].x,points[i].y,sizeAnimatedGuiObject);
            // clockwise rotation:
            // animDrawables[i].addCirclePathAnimator(centerX,centerY,2000,1000);  // the animations start with one second delay to allow the animationView to appear on the screen before
            // counterclockwise rotation:
            animDrawables[i].addCirclePathAnimator(centerX,centerY,false,new LinearInterpolator(),2000,1000);  // the animations start with one second delay to allow the animationView to appear on the screen before
            if (i%2==1) {
                ObjectAnimator rotAnim = (ObjectAnimator) animDrawables[i].addInfiniteRotationAnimator(2000);
                // rotAnim.setRepeatCount(ValueAnimator.INFINITE);
            }
            view.addAnimatedGuiObject(animDrawables[i]);
        }
        // Place a grey circle in the background
        centerX = screenwidth/2;
        centerY = 3*screenheight/4-200;
        kreisBackground2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.LTGRAY,centerX,centerY,2*radiusBackground);
        view.addAnimatedGuiObject(kreisBackground2);
        // Place a number of smaller circles equidistantly on the background circle
        // and let them rotate around the background circle for a specific angle
        int numberCircles = 8;
        kreise = new AnimatedGuiObjectCV[numberCircles];
        points = GraphicsUtilsCV.pointsOnCircle(centerX,centerY,radiusBackground,numberCircles);
        for (int i=0;i<numberCircles;i++) {
            kreise[i] = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE",colors[i%colors.length],points[i].x,points[i].y,sizeAnimatedGuiObject);
            kreise[i].addArcPathAnimator(centerX,centerY,3* Math.PI/2,2000,1000);  // the animations start with one second delay to allow the animationView to appear on the screen before
            view.addAnimatedGuiObject(kreise[i]);
        }
        setContentView(view);
        view.startAnimations();
    }

    private void demo3() {
        AnimationViewCV view = new AnimationViewCV(this);
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int centerX = screenwidth/2;
        int centerY = screenheight/2-200;
        int numberOfObjects = 30;
        int roundDuration = 8000;
        int radiusObject = 50;
        int radiusCenter = 600;
        int radiusEllipse = screenwidth/2+100;
        double comprFactorEllipse = 0.2;
        double rotAngleEllipse = Math.PI/4;
        int[] colors = { Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.DKGRAY, Color.BLACK};
        AnimatedGuiObjectCV kreisBackground = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.LTGRAY,centerX,centerY,radiusCenter);
        kreisBackground.setZindex(2);
        view.addAnimatedGuiObject(kreisBackground);
        Point[] startpoints = GraphicsUtilsCV.pointsOnEllipse(centerX,centerY,radiusEllipse,comprFactorEllipse,rotAngleEllipse,numberOfObjects);
        for (int i=0;i<numberOfObjects;i++) {
            AnimatedGuiObjectCV kreis = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE"+i, Color.RED, startpoints[i].x, startpoints[i].y, radiusObject);
            kreis.addEllipseAnimator(centerX,centerY,radiusEllipse,comprFactorEllipse,rotAngleEllipse,true,new LinearInterpolator(),roundDuration,1000);
            if (i<numberOfObjects/2) {
                kreis.setZindex(1);
                Animator anim = kreis.addZAnimator(3,1,roundDuration/2,roundDuration/2,1000+(i*roundDuration)/numberOfObjects);
                ((ObjectAnimator)anim).setRepeatCount(ValueAnimator.INFINITE);
            }
            else {
                kreis.setZindex(3);
                Animator anim = kreis.addZAnimator(1,3,roundDuration/2,roundDuration/2,1000+((i-numberOfObjects/2)*roundDuration)/numberOfObjects);
                ((ObjectAnimator)anim).setRepeatCount(ValueAnimator.INFINITE);
            }
            view.addAnimatedGuiObject(kreis);
        }
        rotAngleEllipse = -Math.PI/4;
        startpoints = GraphicsUtilsCV.pointsOnEllipse(centerX,centerY,radiusEllipse,comprFactorEllipse,rotAngleEllipse,numberOfObjects);
        for (int i=0;i<numberOfObjects;i++) {
            AnimatedGuiObjectCV kreis = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE"+i, Color.BLACK, startpoints[i].x, startpoints[i].y, radiusObject);
            if (i<numberOfObjects/2) kreis.setZindex(1);
            else kreis.setZindex(3);
            kreis.addEllipseAnimator(centerX,centerY,radiusEllipse,comprFactorEllipse,rotAngleEllipse,true,new LinearInterpolator(),roundDuration,1000);
            if (i<numberOfObjects/2) {
                kreis.setZindex(1);
                Animator anim = kreis.addZAnimator(3,1,roundDuration/2,roundDuration/2,1000+(i*roundDuration)/numberOfObjects);
                ((ObjectAnimator)anim).setRepeatCount(ValueAnimator.INFINITE);
            }
            else {
                kreis.setZindex(3);
                Animator anim = kreis.addZAnimator(1,3,roundDuration/2,roundDuration/2,1000+((i-numberOfObjects/2)*roundDuration)/numberOfObjects);
                ((ObjectAnimator)anim).setRepeatCount(ValueAnimator.INFINITE);
            }
            view.addAnimatedGuiObject(kreis);
        }
        /*
        rotAngleEllipse = Math.PI/2;
        startpoints = GraphicsUtilsCV.pointsOnEllipse(centerX,centerY,radiusEllipse,comprFactorEllipse,rotAngleEllipse,numberOfObjects);
        for (int i=0;i<numberOfObjects;i++) {
            AnimatedGuiObjectCV kreis = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE"+i, Color.BLUE, startpoints[i].x, startpoints[i].y, radiusObject);
            kreis.addEllipseAnimator(centerX,centerY,radiusEllipse,comprFactorEllipse,rotAngleEllipse,true,new LinearInterpolator(),roundDuration,1000);
            view.addAnimatedGuiObject(kreis);
        }
        rotAngleEllipse = 0;
        startpoints = GraphicsUtilsCV.pointsOnEllipse(centerX,centerY,radiusEllipse,comprFactorEllipse,rotAngleEllipse,numberOfObjects);
        for (int i=0;i<numberOfObjects;i++) {
            AnimatedGuiObjectCV kreis = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE"+i, Color.GREEN, startpoints[i].x, startpoints[i].y, radiusObject);
            kreis.addEllipseAnimator(centerX,centerY,radiusEllipse,comprFactorEllipse,rotAngleEllipse,true,new LinearInterpolator(),roundDuration,1000);
            view.addAnimatedGuiObject(kreis);
        }
        */
        setContentView(view);
        view.startAnimations();
    }

    private void demo3_landscape() {
        AnimationViewCV view = new AnimationViewCV(this);
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int centerX = screenwidth/2;
        int centerY = screenheight/2-100;
        int numberOfObjects = 30;
        int roundDuration = 8000;
        int radiusObject = 50;
        int radiusCenter = 600;
        int radiusEllipse = screenwidth/2-200;
        double comprFactorEllipse = 0.2;
        double rotAngleEllipse = Math.PI/8;
        int[] colors = { Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.DKGRAY, Color.BLACK};
        AnimatedGuiObjectCV kreisBackground = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.LTGRAY,centerX,centerY,radiusCenter);
        kreisBackground.setZindex(2);
        view.addAnimatedGuiObject(kreisBackground);
        Point[] startpoints = GraphicsUtilsCV.pointsOnEllipse(centerX,centerY,radiusEllipse,comprFactorEllipse,rotAngleEllipse,numberOfObjects);
        for (int i=0;i<numberOfObjects;i++) {
            AnimatedGuiObjectCV kreis = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE"+i, Color.RED, startpoints[i].x, startpoints[i].y, radiusObject);
            kreis.addEllipseAnimator(centerX,centerY,radiusEllipse,comprFactorEllipse,rotAngleEllipse,true,new LinearInterpolator(),roundDuration,1000);
            if (i<numberOfObjects/2) {
                kreis.setZindex(1);
                Animator anim = kreis.addZAnimator(3,1,roundDuration/2,roundDuration/2,1000+(i*roundDuration)/numberOfObjects);
                ((ObjectAnimator)anim).setRepeatCount(ValueAnimator.INFINITE);
            }
            else {
                kreis.setZindex(3);
                Animator anim = kreis.addZAnimator(1,3,roundDuration/2,roundDuration/2,1000+((i-numberOfObjects/2)*roundDuration)/numberOfObjects);
                ((ObjectAnimator)anim).setRepeatCount(ValueAnimator.INFINITE);
            }
            view.addAnimatedGuiObject(kreis);
        }
        rotAngleEllipse = -Math.PI/8;
        startpoints = GraphicsUtilsCV.pointsOnEllipse(centerX,centerY,radiusEllipse,comprFactorEllipse,rotAngleEllipse,numberOfObjects);
        for (int i=0;i<numberOfObjects;i++) {
            AnimatedGuiObjectCV kreis = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE"+i, Color.BLACK, startpoints[i].x, startpoints[i].y, radiusObject);
            if (i<numberOfObjects/2) kreis.setZindex(1);
            else kreis.setZindex(3);
            kreis.addEllipseAnimator(centerX,centerY,radiusEllipse,comprFactorEllipse,rotAngleEllipse,true,new LinearInterpolator(),roundDuration,1000);
            if (i<numberOfObjects/2) {
                kreis.setZindex(1);
                Animator anim = kreis.addZAnimator(3,1,roundDuration/2,roundDuration/2,1000+(i*roundDuration)/numberOfObjects);
                ((ObjectAnimator)anim).setRepeatCount(ValueAnimator.INFINITE);
            }
            else {
                kreis.setZindex(3);
                Animator anim = kreis.addZAnimator(1,3,roundDuration/2,roundDuration/2,1000+((i-numberOfObjects/2)*roundDuration)/numberOfObjects);
                ((ObjectAnimator)anim).setRepeatCount(ValueAnimator.INFINITE);
            }
            view.addAnimatedGuiObject(kreis);
        }
        setContentView(view);
        view.startAnimations();
    }

    private void demo4() {
        AnimationViewCV view = new AnimationViewCV(this);
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int centerX = screenwidth/2;
        int centerY = screenheight/2-200;
        int numberOfCircles = 8;
        int[] colors = { Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.DKGRAY, Color.BLACK};
        for (int i=0;i<numberOfCircles;i++) {
            AnimatedGuiObjectCV kreis = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE"+i, colors[i%colors.length], centerX , centerY, 100);
            kreis.addSpiralPathOutwardAnimator(5, screenwidth/2-100, new LinearInterpolator(), 5000, 1000*(i+1));
            kreis.addArcPathAnimator(centerX,centerY,2* Math.PI*i/numberOfCircles,1000,5000+1000*(i+1));
            kreis.addSpiralPathInwardAnimator(centerX, centerY, 5, new AccelerateInterpolator(), 5000, 6000+1000*numberOfCircles+100*i);
            view.addAnimatedGuiObject(kreis);
        }
        setContentView(view);
        view.startAnimations();
    }

    /*
        private void demo2() {
        AnimatedGuiObjectCV kreise[], kreisBackground;
        AnimationViewCV view;
        view = new AnimationViewCV(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // Place a grey circle in the background
        int screenheight = displayMetrics.heightPixels;
        int screenwidth = displayMetrics.widthPixels;
        int centerX = screenwidth/2;
        int centerY = screenheight/2-150;
        int radiusBackground = screenwidth/3;
        kreisBackground = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.LTGRAY,centerX,centerY,2*radiusBackground);
        view.addAnimatedGuiObject(kreisBackground);
        // Place a number of smaller circles equidistantly on the background circle
        // and let them rotate around the background circle for a specific angle
        int radiusAnimatedCircle = screenwidth/15;
        int numberCircles = 8;
        kreise = new AnimatedGuiObjectCV[numberCircles];
        int[] colors = { Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.DKGRAY, Color.BLACK};
        Point[] points = GraphicsUtilsCV.pointsOnCircle(centerX,centerY,radiusBackground,numberCircles);
        for (int i=0;i<numberCircles;i++) {
            kreise[i] = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE",colors[i%colors.length],points[i].x,points[i].y,2*radiusAnimatedCircle);
            kreise[i].addArcPathAnimator(centerX,centerY,3*Math.PI/2,2000,1000);  // the animations start with one second delay to allow the animationView to appear on the screen before
            view.addAnimatedGuiObject(kreise[i]);
        }
        setContentView(view);
        view.startAnimations();
    }

    private void demo3() {
        AnimationViewCV view = new AnimationViewCV(this);
        AnimatedGuiObjectCV kreisBackground, animDrawables[];
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // Place a grey circle in the background
        int screenheight = displayMetrics.heightPixels;
        int screenwidth = displayMetrics.widthPixels;
        int centerX = screenwidth/2;
        int centerY = screenheight/2-150;
        int radiusBackground = screenwidth/3;
        kreisBackground = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.LTGRAY,centerX,centerY,2*radiusBackground);
        view.addAnimatedGuiObject(kreisBackground);
        // Place a number of smaller circles equidistantly on the background circle
        // and let them rotate infinitely around the background circle
        int sizeAnimatedGuiObject = screenwidth/8;
        int numberObjects = 8;
        int[] colors = { Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.DKGRAY, Color.BLACK};
        Point[] points = GraphicsUtilsCV.pointsOnCircle(centerX,centerY,radiusBackground,numberObjects);
        animDrawables = new AnimatedGuiObjectCV[numberObjects];
        for (int i=0;i<numberObjects;i++) {
            int type;
            if (i%2==0) type = AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE; else type =AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE;
            animDrawables[i] = new AnimatedGuiObjectCV(type,"OBJ"+i,colors[i%numberObjects],points[i].x,points[i].y,sizeAnimatedGuiObject);
            // clockwise rotation:
            // animDrawables[i].addCirclePathAnimator(centerX,centerY,2000,1000);  // the animations start with one second delay to allow the animationView to appear on the screen before
            // counterclockwise rotation:
            animDrawables[i].addCirclePathAnimator(centerX,centerY,false,new LinearInterpolator(),2000,1000);  // the animations start with one second delay to allow the animationView to appear on the screen before
            if (i%2==1) {
                ObjectAnimator rotAnim = (ObjectAnimator) animDrawables[i].addInfiniteRotationAnimator(2000);
                // rotAnim.setRepeatCount(ValueAnimator.INFINITE);
            }
            view.addAnimatedGuiObject(animDrawables[i]);
        }
        setContentView(view);
        view.startAnimations();

    }

     */

}
