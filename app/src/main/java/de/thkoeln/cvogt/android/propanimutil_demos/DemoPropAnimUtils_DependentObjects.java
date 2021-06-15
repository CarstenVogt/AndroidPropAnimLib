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
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_DependentObjects extends Activity {

    int screenwidth, screenheight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenwidth = GUIUtilitiesCV.getDisplayWidth();
        screenheight = GUIUtilitiesCV.getDisplayHeight();
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
        if (item.getItemId() == R.id.menuItemDemo1)
            demo1();
        if (item.getItemId() == R.id.menuItemDemo2)
            demo2();
        if (item.getItemId() == R.id.menuItemDemo3)
            demo3();
        if (item.getItemId() == R.id.menuItemDemo4)
            demo4();
        return true;
    }

    public void demo1() {
        final AnimationViewCV view = new AnimationViewCV(this);
        int numberOfObjects = 12;
        int circleSize = 100;
        int centerX = screenwidth/2;
        int centerY = screenheight/2-100;
        int radius = screenwidth/2-100;
        Point[] targetPoints = GraphicsUtilsCV.pointsOnCircle(centerX,centerY,radius,numberOfObjects);
        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(6);
        int duration = 3000;
        for (int i=0;i<numberOfObjects;i++) {
            AnimatedGuiObjectCV circle = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CENTER", Color.BLUE, centerX,centerY,circleSize);
            new DependentBorderCV(circle,12, Color.BLACK);
            new DependentLineObjectToPointCV(circle,new Point(centerX,centerY),linePaint);
            Animator anim1 = circle.addLinearPathAnimator(targetPoints[i].x,targetPoints[i].y,duration,1000);
            Animator anim2 = circle.addArcPathAnimator(centerX,centerY, Math.PI,duration);
            Animator anim3 = circle.addLinearPathAnimator(centerX,centerY,duration);
            Animator anim4 = circle.addLinearPathAnimator(centerX,centerY-radius,duration);
            Animator anim5 = circle.addArcPathAnimator(centerX,centerY,2* Math.PI*i/numberOfObjects,duration);
            Animator anim6 = circle.addLinearPathAnimator(targetPoints[i].x,centerY,duration);
            Animator anim7 = circle.addLinearPathAnimator(centerX,centerY,duration);
            circle.playSequentially(anim1, anim2, anim3, anim4, anim5, anim6, anim7);
            view.addAnimatedGuiObject(circle);
        }
        setContentView(view);
        view.startAnimations();
    }

    public void demo2() {
        final AnimationViewCV animationView = new AnimationViewCV(this);
        int numberOfObjects = 24;
        int circleSize = 50;
        int centerX = screenwidth/2;
        int centerY = screenheight/2-100;
        int radius = screenwidth/2-200;
        Point[] pointsStart = GraphicsUtilsCV.pointsOnCircle(centerX,centerY,radius,numberOfObjects);
        Point[] pointsOuter = GraphicsUtilsCV.pointsOnCircle(centerX,centerY,radius+150,numberOfObjects);
        AnimatedGuiObjectCV drw[] = new AnimatedGuiObjectCV[numberOfObjects];
        for (int i=0;i<numberOfObjects;i++) {
            drw[i] = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE", Color.BLUE, pointsStart[i].x, pointsStart[i].y, circleSize);
            drw[i].addLinearPathAnimator(pointsOuter[i].x, pointsOuter[i].y, 2000, 1000);
            drw[i].addLinearPathAnimator(pointsStart[i].x, pointsStart[i].y, 2000, 3000);
            animationView.addAnimatedGuiObject(drw[i]);
        }
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(6);
        linePaint.setColor(Color.BLUE);
        for (int i=0;i<numberOfObjects;i++)
            for (int j=i+1; j<numberOfObjects; j++)
                new DependentLineObjectToObjectCV(drw[i],drw[j],linePaint);
        setContentView(animationView);
        animationView.setOnTouchListener(new AnimationUtilsCV.OnTouchListener_Relocation());
        animationView.startAnimations();
    }

    public void demo3() {
        AnimationViewCV view = new AnimationViewCV(this);
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int centerX = screenwidth/2;
        int centerY = screenheight/2-200;
        int numberOfObjects = 5;
        int[] colors = { Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.DKGRAY, Color.BLACK};
        AnimatedGuiObjectCV guiobj[] = new AnimatedGuiObjectCV[numberOfObjects];
        Point[] pos = GraphicsUtilsCV.pointsOnCircle(centerX,centerY,screenwidth/3,numberOfObjects);
        for (int i=0;i<numberOfObjects;i++) {
            int width = 100+(int)(150* Math.random()), height = 100+(int)(150* Math.random());
            guiobj[i] = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT, "RECT"+i, colors[i%colors.length], pos[i].x , pos[i].y, width, height);
            guiobj[i].addArcPathAnimator(centerX,centerY,2* Math.PI,8000,1000);
            new DependentBorderCV(guiobj[i],6, Color.BLACK);
            view.addAnimatedGuiObject(guiobj[i]);
        }
        AnimatedGuiObjectCV center = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT, "CENTER", Color.BLACK,centerX,centerY,400,100);
        view.addAnimatedGuiObject(center);
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(6);
        linePaint.setColor(Color.BLACK);
        for (int i=0;i<numberOfObjects;i++) {
            DependentLineObjectToObjectCV d1 = new DependentLineObjectToObjectCV(guiobj[i],guiobj[(i+1)%numberOfObjects],linePaint,false);
            DependentLineObjectToObjectCV d2 = new DependentLineObjectToObjectCV(guiobj[i],center,linePaint,false);
            // d1.setThresholdDistanceX(0);
            // d1.setThresholdDistanceY(0);
            // d2.setThresholdDistanceX(0);
            // d2.setThresholdDistanceY(0);
       }
        setContentView(view);
        view.setOnTouchListener(new AnimationUtilsCV.OnTouchListener_Relocation());
        view.startAnimations();
    }

    public void demo3b() {
        AnimatedGuiObjectCV rect1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT1", Color.RED,400,200,300,150);
        AnimatedGuiObjectCV rect2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT2", Color.GREEN,1200,200,150,300);
        AnimatedGuiObjectCV square = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE,"SQUARE", Color.BLUE,800,700,300);
        // Add borders to the objects
        new DependentBorderCV(rect1,6, Color.BLACK);
        new DependentBorderCV(rect2,6, Color.BLACK);
        new DependentBorderCV(square,6, Color.BLACK);
        // Connect the objects by lines
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(6);
        linePaint.setColor(Color.BLACK);
        new DependentLineObjectToObjectCV(rect1,rect2,linePaint,false);
        new DependentLineObjectToObjectCV(rect2,square,linePaint,false);
        new DependentLineObjectToObjectCV(square,rect1,linePaint,false);
        // Display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        view.addAnimatedGuiObject(rect1);
        view.addAnimatedGuiObject(rect2);
        view.addAnimatedGuiObject(square);
        setContentView(view);
        view.setOnTouchListener(new AnimationUtilsCV.OnTouchListener_Relocation());
        //view.startAnimations();
    }

    public void demo4() {
        final AnimationViewCV view = new AnimationViewCV(this);
        // An animated line
        int centerX = screenwidth/2;
        int centerY = screenheight/2-100;
        AnimatedGuiObjectCV endpoint1 = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE1", Color.RED, centerX, centerY,40);
        AnimatedGuiObjectCV endpoint2 = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE2", Color.GREEN, centerX, centerY, 40);
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(6);
        linePaint.setColor(Color.BLUE);
        new DependentLineObjectToObjectCV(endpoint1,endpoint2,linePaint);
        endpoint1.addLinearPathAnimator(centerX-400,centerY,2000,1000);
        endpoint2.addLinearPathAnimator(centerX+400,centerY,2000,1000);
        endpoint1.addLinearPathAnimator(centerX-400,centerY-400,2000,3000);
        endpoint2.addLinearPathAnimator(centerX+400,centerY+400,2000,3000);
        endpoint1.addBezierPathAnimator(centerX-200,centerY-800,centerX-100,centerY+1200,centerX+400,centerY+400,3000,5000);
        view.addAnimatedGuiObject(endpoint1);
        view.addAnimatedGuiObject(endpoint2);
        // A moving rectangle with a border
        AnimatedGuiObjectCV rect = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.RED,200,screenheight-400,300,100,0);
        new DependentBorderCV(rect,30, Color.LTGRAY);
        rect.addLinearPathAnimator(screenwidth-300,screenheight-400,4000,1000);
        rect.addRotationAnimator((float)(3* Math.PI/2),4000,1000);
        view.addAnimatedGuiObject(rect);
        setContentView(view);
        view.startAnimations();
    }

}