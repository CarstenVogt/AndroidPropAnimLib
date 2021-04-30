package de.thkoeln.cvogt.android.propanimutil_demos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import java.util.ArrayList;
import java.util.Iterator;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_Collision extends Activity {

    Bitmap explosionBitmap;
    int screenwidth, screenheight;
    final int objectSize = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        explosionBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
        explosionBitmap = GraphicsUtilsCV.makeBitmapTransparent(explosionBitmap, Color.WHITE);
        screenwidth = GUIUtilitiesCV.getDisplayWidth();
        screenheight = GUIUtilitiesCV.getDisplayHeight();
        demo4();
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

    public void demo1() {
        // animationView to display the objects
        final AnimationViewCV view = new AnimationViewCV(this);
        // two moving circles on collision course
        int duration = 3000;
        AnimatedGuiObjectCV<Drawable> circle1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE1", Color.BLACK,100+objectSize/2,100+objectSize/2,objectSize);
        circle1.addLinearPathAnimator(1000,1000,duration,1000);
        view.addAnimatedGuiObject(circle1);
        AnimatedGuiObjectCV<Drawable> circle2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE1", Color.RED,100+objectSize/2,600+objectSize/2,objectSize);
        circle2.addLinearPathAnimator(1000,100,duration,1000);
        view.addAnimatedGuiObject(circle2);
        circle2.addPropertyChangedListener(new AnimationUtilsCV.PropertyChangedListener_Collision(this, AnimationUtilsCV.PropertyChangedListener_Collision.REACTTYPE_EXPLODE));
        // two moving texts on collision course
        AnimatedGuiObjectCV<Drawable> text1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT,this,"Laurel","Laurel",300,1100,120);
        ArrayList<Point> al = new ArrayList<Point>();
        al.add(new Point(300,1100));
        al.add(new Point(600,1100));
        al.add(new Point(800,1250));
        text1.addLinearSegmentsPathAnimator(al,duration,3000);
        view.addAnimatedGuiObject(text1);
        AnimatedGuiObjectCV<Drawable> text2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT,this,"Hardy","Hardy",300,1400,120);
        al = new ArrayList<Point>();
        al.add(new Point(300,1400));
        al.add(new Point(600,1400));
        al.add(new Point(800,1250));
        text2.addLinearSegmentsPathAnimator(al,duration,3000);
        view.addAnimatedGuiObject(text2);
        text1.addPropertyChangedListener(new CollisionHandler_TextMerge());
        // two growing rectangles on collision course
        AnimatedGuiObjectCV<Drawable> rect1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT1", Color.GREEN,300,1700,300,200);
        rect1.addSizeAnimator(1200,200,duration,5000);
        view.addAnimatedGuiObject(rect1);
        AnimatedGuiObjectCV<Drawable> rect2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT2", Color.BLUE,1000,1800,300,200);
        rect2.addSizeAnimator(1200,200,duration,5000);
        view.addAnimatedGuiObject(rect2);
        rect1.addPropertyChangedListener(new CollisionHandler_ColorChanged());
        // show and animate the view
        setContentView(view);
        view.startAnimations();
    }

    public void demo2() {
        int dur1 = 6000, dur2 = 10000;
        // Kollision rotierender Objekte
        final AnimationViewCV view = new AnimationViewCV(this);
        // drw1 startet automatisch
        AnimatedGuiObjectCV<Drawable> drw1
                = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT, "RECT_UPPER", Color.RED, screenwidth/2, 300, 500, 100, 0);
        drw1.addLinearPathAnimator(drw1.getCenterX(),screenheight-400,dur1,1000);
        drw1.addRotationAnimator((float)(2* Math.PI),dur1,1000);
        drw1.addPropertyChangedListener(new AnimationUtilsCV.PropertyChangedListener_Collision(this, AnimationUtilsCV.PropertyChangedListener_Collision.REACTTYPE_STOP));
        view.addAnimatedGuiObject(drw1);
        // drw2 startet nach einem DoubleTap
        AnimatedGuiObjectCV<Drawable> drw2
                = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT, "RECT_LOWER", Color.BLUE, screenwidth/2, screenheight-800, 500, 100, 0);
        drw2.addPropertyChangedListener(new AnimationUtilsCV.PropertyChangedListener_Collision(this, AnimationUtilsCV.PropertyChangedListener_Collision.REACTTYPE_STOP));
        view.addAnimatedGuiObject(drw2);
        view.setGestureListener(new DoubleTapGestureListener(drw2,dur2));
        setContentView(view);
        view.startAnimations();
    }

    public void demo3() {
        // Objekte mit Gesten in Bewegung setzen, bei Kollision Explosion
        final AnimationViewCV view = new AnimationViewCV(this);
        final int numberOfObjects = 10;
        // static text
        // AnimatedGuiObjectCV<Drawable> text = new AnimatedGuiObjectCV(this,"Explanation","Fling the circles:",700,100,80);
        // view.addAnimatedGuiObject(text);
        // a number of circles
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        for (int i=0;i<numberOfObjects/2;i++) {
            AnimatedGuiObjectCV<Drawable> drw
                    = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE", Color.RED, 50+objectSize/2, 50+objectSize+i*2*objectSize, objectSize);
            drw.addPropertyChangedListener(new AnimationUtilsCV.PropertyChangedListener_Collision(this, AnimationUtilsCV.PropertyChangedListener_Collision.REACTTYPE_EXPLODE));
            view.addAnimatedGuiObject(drw);
            drw = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE", Color.RED, screenwidth-50-objectSize/2, 50+objectSize+i*2*objectSize, objectSize);
            drw.addPropertyChangedListener(new AnimationUtilsCV.PropertyChangedListener_Collision(this, AnimationUtilsCV.PropertyChangedListener_Collision.REACTTYPE_EXPLODE));
            view.addAnimatedGuiObject(drw);
        }
        // show the view
        view.setGestureListener(new FlingGestureListener());
        setContentView(view);
    }

    public void demo4() {
        int noObjectsInRowCol = 12;
        // Kreis durch Menge anderer Kreise bewegen, bei Annäherung Verdrängung
        final AnimationViewCV view = new AnimationViewCV(this);
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int objectSize = (int)(screenwidth/(noObjectsInRowCol*1.5));
        AnimatedGuiObjectCV.OnPropertyChangedListener lis = new ProximityHandler_Displacement();
        for (int i=0;i<noObjectsInRowCol;i++) {
            for (int j=0;j<noObjectsInRowCol;j++) {
                AnimatedGuiObjectCV<Drawable> drw
                        = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE"+i+""+j, Color.BLACK, 50 + (int)(i*1.5*objectSize), 350 + (int)(j*1.5*objectSize), objectSize);
                // drw.addPropertyChangedListener(lis);
                view.addAnimatedGuiObject(drw);
            }
        }
        AnimatedGuiObjectCV<Drawable> drw = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "MOVINGCIRCLE", Color.RED, 50, 50, objectSize);
        drw.addPropertyChangedListener(lis);
        drw.addLinearPathAnimator(screenwidth,screenheight-200,8000,1000);
        view.addAnimatedGuiObject(drw);
        // show the view
        setContentView(view);
        view.startAnimations();
    }

    public void demo4b() {
        // Kreis durch Menge anderer Kreise bewegen, bei Annäherung Farbänderung
        final AnimationViewCV view = new AnimationViewCV(this);
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int objectSize = 80;
        AnimatedGuiObjectCV.OnPropertyChangedListener lis = new ProximityHandler_ColorChange();
        for (int i=0;i<12;i++) {
            for (int j=0;j<12;j++) {
                AnimatedGuiObjectCV<Drawable> drw
                        = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE"+i+""+j, Color.BLACK, 50 + (int)(i*1.5*objectSize), 400 + (int)(j*1.5*objectSize), objectSize);
                drw.addPropertyChangedListener(lis);
                view.addAnimatedGuiObject(drw);
            }
        }
        AnimatedGuiObjectCV<Drawable> drw = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "MOVINGCIRCLE", Color.RED, 50, 50, objectSize);
        drw.addPropertyChangedListener(lis);
        drw.addLinearPathAnimator(screenwidth,screenheight-200,8000,1000);
        view.addAnimatedGuiObject(drw);
        // show the view
        setContentView(view);
        view.startAnimations();
    }

    class CollisionHandler_ColorChanged implements AnimatedGuiObjectCV.OnPropertyChangedListener {
        @Override
        public void onPropertyChanged(AnimatedGuiObjectCV obj, String propertyName) {
            AnimatedGuiObjectCV collidingObj = AnimationUtilsCV.PropertyChangedListener_Collision.findCollidingObject(obj);
            if (collidingObj==null) return;
            obj.removePropertyChangedListener(this);
            collidingObj.removePropertyChangedListener(this);
            obj.clearAnimatorList();
            collidingObj.clearAnimatorList();
            obj.addColorAnimator(Color.RED,2000);
            collidingObj.addColorAnimator(Color.RED,2000);
            obj.startAnimation();
            collidingObj.startAnimation();
        }
    }

    class CollisionHandler_TextMerge implements AnimatedGuiObjectCV.OnPropertyChangedListener {
        @Override
        public void onPropertyChanged(AnimatedGuiObjectCV obj, String propertyName) {
            if (!propertyName.equals("Center")) return;
            AnimatedGuiObjectCV collidingObj = AnimationUtilsCV.PropertyChangedListener_Collision.findCollidingObject(obj);
            if (collidingObj==null) return;
            obj.removePropertyChangedListener(this);
            collidingObj.removePropertyChangedListener(this);
            AnimationViewCV view = obj.getDisplayingView();
            view.removeAnimatedGuiObject(obj);
            view.removeAnimatedGuiObject(collidingObj);
            int posX = (obj.getLeftBound()+collidingObj.getLeftBound())/2;
            int posY = (obj.getTopBound()+collidingObj.getTopBound())/2;
            int duration = 3000;
            AnimatedGuiObjectCV<Drawable> mergedText = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT, DemoPropAnimUtils_Collision.this,"Merged",obj.getName()+" & "+collidingObj.getName(),posX+400,posY,120);
            mergedText.addLinearPathAnimator(1500,posY,duration);
            view.addAnimatedGuiObject(mergedText);
            mergedText.startAnimation();
        }
    }

    class ProximityHandler_ColorChange implements AnimatedGuiObjectCV.OnPropertyChangedListener {
        ArrayList<AnimatedGuiObjectCV> alreadyAnimated = new ArrayList<AnimatedGuiObjectCV>();
        @Override
        public void onPropertyChanged(AnimatedGuiObjectCV obj1, String propertyName) {
            ArrayList<AnimatedGuiObjectCV> objects = obj1.getDisplayingView().getAnimatedGuiObjects();
            for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();) {
                AnimatedGuiObjectCV obj2 = it.next();
                if (obj1==obj2 || obj2.getName().equals("MOVINGCIRCLE")) continue;
                if (GraphicsUtilsCV.distance(obj1.getCenter(),obj2.getCenter())<obj1.getWidth()*2
                        &&!alreadyAnimated.contains(obj2)) {
                    // if (propertyName.equals("Center"))
                    //    obj2.addColorAnimator(Color.RED, 3000);
                    //  else
                    obj2.addColorAnimator(Color.RED, 3000, 1000);
                    obj2.startAnimation();
                    alreadyAnimated.add(obj2);
                }
            }
        }
    }

    class ProximityHandler_Displacement implements AnimatedGuiObjectCV.OnPropertyChangedListener {
        final double factor = 0.05;
        @Override
        public void onPropertyChanged(AnimatedGuiObjectCV obj1, String propertyName) {
            ArrayList<AnimatedGuiObjectCV> objects = obj1.getDisplayingView().getAnimatedGuiObjects();
            for (Iterator<AnimatedGuiObjectCV> it = objects.iterator(); it.hasNext();) {
                AnimatedGuiObjectCV obj2 = it.next();
                if (obj1==obj2 || obj2.getName().equals("MOVINGCIRCLE")) continue;
                if (GraphicsUtilsCV.distance(obj1.getCenter(),obj2.getCenter())<obj1.getWidth()*2) {
                    int vectorX = (int)(factor*(obj2.getCenterX()-obj1.getCenterX())),
                        vectorY = (int)(factor*(obj2.getCenterY()-obj1.getCenterY()));
                    if (vectorX!=0||vectorY!=0)
                       obj2.move(vectorX,vectorY);
                }
            }
        }
    }

    // Gesture handler for fling gestures

    private class FlingGestureListener extends AnimationViewCV.GestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            super.onFling(e1,e2,velocityX,velocityY);
            if (guiobjects == null) return true;
            guiobjects.get(0).clearAnimatorList();
            guiobjects.get(0).addLinearPathAnimator((int)(e1.getX()+3*(e2.getX()-e1.getX())),(int)(e1.getY()+3*(e2.getY()-e1.getY())), new AccelerateInterpolator(), 5000);
            guiobjects.get(0).startAnimation();
            return true;
        }
    }

    private class DoubleTapGestureListener extends AnimationViewCV.GestureListener {

        AnimatedGuiObjectCV guiObject;
        int duration;
        boolean alreadyStarted = false;

        DoubleTapGestureListener(AnimatedGuiObjectCV guiObject, int duration) {
           this.guiObject = guiObject;
           this.duration = duration;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (alreadyStarted) return true;
            alreadyStarted = true;
            int screenwidth = GUIUtilitiesCV.getDisplayWidth();
            guiObject.addLinearPathAnimator(screenwidth/2,200,duration);
            guiObject.addRotationAnimator((float)(80* Math.PI),duration);
            guiObject.startAnimation();
            return true;
        }

    }

}
