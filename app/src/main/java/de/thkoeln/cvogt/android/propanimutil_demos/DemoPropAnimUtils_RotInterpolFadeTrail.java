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

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.PathInterpolator;

import de.thkoeln.cvogt.android.propanim_utilities.*;

// Rotation, Time Interpolators, Trails, Fading

public class DemoPropAnimUtils_RotInterpolFadeTrail extends Activity {

    AnimationViewCV view;
    int screenheight, screenwidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenheight = GUIUtilitiesCV.getDisplayHeight();
        screenwidth = GUIUtilitiesCV.getDisplayWidth();
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
        if (item.getItemId()==R.id.menuItemDemo4)
            demo4();
        return true;
    }

    // Rotation

    public void demo1() {
        view = new AnimationViewCV(this);
        // multiple rectangles moving along a straight line and rotating from different start angels to different target angles
        AnimatedGuiObjectCV rect1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"ROTRECT1", Color.BLACK,150,150,150,100,0);
        rect1.addLinearPathAnimator(screenwidth-350,150,5000,1000);
        rect1.addRotationAnimator((float)(2* Math.PI),5000,1000);
        view.addAnimatedGuiObject(rect1);
        AnimatedGuiObjectCV rect2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"ROTRECT2", Color.BLUE,150,300,150,100,0);
        rect2.addLinearPathAnimator(screenwidth-250,300,5000,1000);
        rect2.addRotationAnimator((float)(7* Math.PI/4),5000,1000);
        view.addAnimatedGuiObject(rect2);
        AnimatedGuiObjectCV rect3 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"ROTRECT3", Color.RED,150,450,150,100,0);
        rect3.addLinearPathAnimator(screenwidth-450,450,5000,1000);
        rect3.addRotationAnimator(-(float)(3* Math.PI/2),5000,1000);
        view.addAnimatedGuiObject(rect3);
        AnimatedGuiObjectCV rect4 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"ROTRECT4", Color.BLACK,150,600,150,100,30);
        rect4.addLinearPathAnimator(screenwidth-200,600,5000,1000);
        rect4.addRotationAnimator((float)(5* Math.PI/4),5000,1000);
        view.addAnimatedGuiObject(rect4);
        AnimatedGuiObjectCV rect5 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"ROTRECT5", Color.BLUE,150,750,150,100,45);
        rect5.addLinearPathAnimator(screenwidth-200,750,5000,1000);
        rect5.addRotationAnimator(-(float)(3* Math.PI/4),5000,1000);
        view.addAnimatedGuiObject(rect5);
        AnimatedGuiObjectCV rect6 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"ROTRECT6", Color.RED,150,900,150,100,60);
        rect6.addLinearPathAnimator(screenwidth-400,750,5000,1000);
        rect6.addRotationAnimator((float)(5* Math.PI/4),5000,1000);
        view.addAnimatedGuiObject(rect6);
        // an image bitmap moving along a straight line and rotating
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
        bm = GraphicsUtilsCV.makeBitmapTransparent(bm, Color.WHITE);
        AnimatedGuiObjectCV drawableBitmap = new AnimatedGuiObjectCV(this,"ROTBITMAP",bm,150,1150,200,200,0);
        drawableBitmap.addLinearPathAnimator(screenwidth-350,1050,5000,1000);
        drawableBitmap.addRotationAnimator((float)(2* Math.PI),5000,1000);
        view.addAnimatedGuiObject(drawableBitmap);
        // a text bitmap moving along a straight line, rotating, and growing
        AnimatedGuiObjectCV drawableBitmapWithText = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT,this,"ROTBITMAP_TEXT","TEXT",150,1400,120,0);
        drawableBitmapWithText.addLinearPathAnimator(screenwidth-350,1400,5000,1000);
        drawableBitmapWithText.addRotationAnimator((float)(3* Math.PI/2),2500,1000);
        drawableBitmapWithText.addRotationAnimator(0,2500,3500);
        drawableBitmapWithText.addSizeAnimator(500,400,5000,1000);
        view.addAnimatedGuiObject(drawableBitmapWithText);
        // a TextDrawable moving along a straight line, rotating, and growing
        AnimatedGuiObjectCV textdrawable = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_TEXT,this,"ROT_TEXT","TEXT",150,1600,120,0);
        textdrawable.addLinearPathAnimator(screenwidth-350,1600,5000,1000);
        textdrawable.addRotationAnimator((float)(3* Math.PI/2),2500,1000);
        textdrawable.addRotationAnimator(0,2500,3500);
        textdrawable.addSizeAnimator(500,400,5000,1000);
        view.addAnimatedGuiObject(textdrawable);
        // a shaking TextDrawable
        AnimatedGuiObjectCV textdrawable2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_TEXT,this,"SHAKE_TEXT","TEXT",150,1600,120,0);
        textdrawable2.addShakeAnimator((float)(Math.PI/6),2000,5);
        view.addAnimatedGuiObject(textdrawable2);
        // Square rotating infinitely
        AnimatedGuiObjectCV square = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE,"SQUARE", Color.BLUE,200,1800,200,200,0);
        square.addInfiniteRotationAnimator(1000);
        view.addAnimatedGuiObject(square);
        // Shaking rectangle
        AnimatedGuiObjectCV rect7 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"SHAKERECT", Color.RED,600,1800,400,100,0);
        rect7.addShakeAnimator((float)(Math.PI/6),2000,5);
        view.addAnimatedGuiObject(rect7);
        // display the view and start the animations
        setContentView(view);
        view.startAnimations();
    }

    // Time Interpolators

    public void demo2() {
        AnimatedGuiObjectCV rect1, rect2, rect3, rect4, rect5, rect6, rect7, rect8, rect9;
        view = new AnimationViewCV(this);
        // all rectangles are moving from left to right along a straight line
        int startX = 200, startY = 170, targetX = 1100, width = 300, height = 100, duration = 5000;
        // acceleration:
        rect1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect1.addLinearPathAnimator(targetX,startY,new AccelerateInterpolator(),duration,1000);  // this animation and all others start with one second delay to allow the animationView to appear on the screen before
        view.addAnimatedGuiObject(rect1);
        startY += 3*height/2;
        // deceleration:
        rect2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect2.addLinearPathAnimator(targetX,startY,new DecelerateInterpolator(),duration,1000);
        view.addAnimatedGuiObject(rect2);
        startY += 3*height/2;
        // acceleration / deceleration:
        rect3 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect3.addLinearPathAnimator(targetX,startY,new AccelerateDecelerateInterpolator(),duration,1000);
        view.addAnimatedGuiObject(rect3);
        startY += 3*height/2;
        // anticipate:
        rect4 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect4.addLinearPathAnimator(targetX,startY,new AnticipateInterpolator(5),duration,1000);
        view.addAnimatedGuiObject(rect4);
        startY += 3*height/2;
        // overshoot:
        rect5 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect5.addLinearPathAnimator(targetX,startY,new OvershootInterpolator(5),duration,1000);
        view.addAnimatedGuiObject(rect5);
        startY += 3*height/2;
        // anticipate overshoot:
        rect6 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect6.addLinearPathAnimator(targetX,startY,new AnticipateOvershootInterpolator(5),duration,1000);
        view.addAnimatedGuiObject(rect6);
        startY += 3*height/2;
        // bounce:
        rect7 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect7.addLinearPathAnimator(targetX,startY,new BounceInterpolator(),duration,1000);
        view.addAnimatedGuiObject(rect7);
        startY += 3*height/2;
        // cycle:
        rect8 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect8.addLinearPathAnimator(targetX,startY,new CycleInterpolator(2),duration,1000);
        view.addAnimatedGuiObject(rect8);
        startY += 3*height/2;
        // path:
        rect9 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect9.addLinearPathAnimator(targetX,startY,new PathInterpolator(0.95f,0.5f),duration,1000);
        view.addAnimatedGuiObject(rect9);
        startY += 3*height/2;
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        view.startAnimations();

    }

    // Fading

    public void demo3() {
        view = new AnimationViewCV(this);
        // Kreis 1
        AnimatedGuiObjectCV kreis1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE1", Color.BLUE,400,screenheight/2-600,500);
        kreis1.addFadeInAnimator(4000);
        view.addAnimatedGuiObject(kreis1);
        // Kreis 2
        AnimatedGuiObjectCV kreis2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE2", Color.RED,600,screenheight/2,500);
        kreis2.addFadeOutAnimator(4000);
        view.addAnimatedGuiObject(kreis2);
        // Display zeigen
        setContentView(view);
        view.startAnimations();

    }

    // Trails

    public void demo4() {
        view = new AnimationViewCV(this);
        // Kreis 1
        AnimatedGuiObjectCV kreis1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE1", Color.BLUE,100,screenheight/2-500,90);
        kreis1.setZindex(2);
        kreis1.addBezierPathAnimator(screenwidth/4,-200,3*screenwidth/4,screenheight+200,screenwidth-100,300,4000,1000);
        kreis1.addPropertyChangedListener(new TrailHandler1());
        view.addAnimatedGuiObject(kreis1);
        // Kreis 2
        AnimatedGuiObjectCV kreis2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE2", Color.RED,100,screenheight/2-200,90);
        kreis2.setZindex(2);
        kreis2.addBezierPathAnimator(screenwidth/4,0,3*screenwidth/4,screenheight+400,screenwidth-100,600,4000,1000);
        kreis2.addPropertyChangedListener(new TrailHandler2());
        view.addAnimatedGuiObject(kreis2);
        // Display zeigen
        setContentView(view);
        view.startAnimations();

    }

    class TrailHandler1 implements AnimatedGuiObjectCV.OnPropertyChangedListener {
        @Override
        public void onPropertyChanged(AnimatedGuiObjectCV obj, String propertyName) {
            AnimatedGuiObjectCV<Drawable> dot = new AnimatedGuiObjectCV<Drawable>(
                    AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "DOT", Color.BLACK, obj.getCenterX(), obj.getCenterY(), 20);
            dot.addColorAnimator(Color.WHITE,5000);
            obj.getDisplayingView().addAnimatedGuiObject(dot);
            dot.startAnimation();
        }
    }

    class TrailHandler2 implements AnimatedGuiObjectCV.OnPropertyChangedListener {
        @Override
        public void onPropertyChanged(AnimatedGuiObjectCV obj, String propertyName) {
            AnimatedGuiObjectCV<Drawable> dot = new AnimatedGuiObjectCV<Drawable>(
                    AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "COPY", Color.RED, obj.getCenterX(), obj.getCenterY(), 90);
            dot.addColorAnimator(Color.WHITE,2000);
            obj.getDisplayingView().addAnimatedGuiObject(dot);
            dot.startAnimation();
        }
    }

}
