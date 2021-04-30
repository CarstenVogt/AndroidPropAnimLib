package de.thkoeln.cvogt.android.propanimutil_demos;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_TouchGestures extends Activity {

    AnimationViewCV animationView;
    int screenwidth;
    int screenheight;

    // for demos 2 and 3
    final int diameterBall = 150;
    final int sidelengthBitmap = 400;
    Bitmap explosionBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        explosionBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
        explosionBitmap = GraphicsUtilsCV.makeBitmapTransparent(explosionBitmap, Color.WHITE);
        screenwidth = GUIUtilitiesCV.getDisplayWidth();
        screenheight = GUIUtilitiesCV.getDisplayHeight();
        demo1();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.menu_3demos, menu);
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
        return true;
    }

    // Demos 1 and 2: Touch Events

    public void demo1() {
        animationView = new AnimationViewCV(this);
        animationView.setOnTouchListener(new DemoOnTouchListener());
        for (int i=0;i<4;i++) {
            AnimatedGuiObjectCV<Drawable> drw
                    = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE", Color.RED, 50+diameterBall/2, 50+diameterBall/2+i*(3*diameterBall/2), diameterBall);
            drw.addLinearPathAnimator(screenwidth, 50+diameterBall/2+i*(3*diameterBall/2), 5000, 1000);
            animationView.addAnimatedGuiObject(drw);
        }
        AnimatedGuiObjectCV<Drawable> drw1
                = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT, "RECT", Color.BLUE, 300, 1400, 400, 200, 0);
        drw1.addLinearPathAnimator(1200,1400,5000,2000);
        drw1.addRotationAnimator((float)(2* Math.PI),5000,2000);
        animationView.addAnimatedGuiObject(drw1);
        setContentView(animationView);
        animationView.startAnimations();
    }

    public void demo2() {
        animationView = new AnimationViewCV(this);
        animationView.setOnTouchListener(new AnimationUtilsCV.OnTouchListener_Relocation());
        final int numberOfObjects = 4;
        AnimatedGuiObjectCV drw[] = new AnimatedGuiObjectCV[numberOfObjects];
        for (int i=0;i<numberOfObjects;i++) {
            drw[i] = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE", Color.BLUE, 50+diameterBall/2+i*(3*diameterBall/2), 50+diameterBall/2+i*(3*diameterBall/2), diameterBall);
            animationView.addAnimatedGuiObject(drw[i]);
        }
        Paint paint = new Paint();
        paint.setStrokeWidth(6);
        paint.setColor(Color.BLUE);
        for (int i=0;i<numberOfObjects;i++)
            for (int j=i+1; j<numberOfObjects; j++)
                new DependentLineObjectToObjectCV(drw[i],drw[j],paint);
        setContentView(animationView);
        animationView.startAnimations();
    }

    class DemoOnTouchListener implements AnimationViewCV.OnTouchListener {
        @Override
        public boolean onTouch(AnimationViewCV view, AnimatedGuiObjectCV guiobject, MotionEvent motionEvent) {
            if (guiobject==null) return true;
            if (guiobject.getName().equals("CIRCLE")) {  // Explosion
                animationView.removeAnimatedGuiObject(guiobject);
                int posX = guiobject.getCenterX();
                int posY = guiobject.getCenterY();
                int duration = 3000;
                AnimatedGuiObjectCV<Drawable> drawableBitmap = new AnimatedGuiObjectCV(DemoPropAnimUtils_TouchGestures.this,"BITMAP",explosionBitmap,
                        posX,posY,sidelengthBitmap,sidelengthBitmap);
                ArrayList<Point> sizes = new ArrayList<Point>();
                sizes.add(new Point(sidelengthBitmap,sidelengthBitmap));
                sizes.add(new Point(700,700));
                sizes.add(new Point(0,0));
                drawableBitmap.addSizeAnimator(sizes,duration);
                drawableBitmap.addLinearPathAnimator(posX-800,posY-800,duration);
                animationView.addAnimatedGuiObject(drawableBitmap);
                drawableBitmap.startAnimation();
                return true;
            }
            if (guiobject.getName().equals("RECT")) {  // New color
                if (guiobject.getColor() == Color.BLUE)
                    guiobject.setColor(Color.GREEN);
                else
                    guiobject.setColor(Color.BLUE);
                return true;
            }
            return true;
        }
    }

    // Demo 3: Gestures

    public void demo3() {
        AnimationViewCV view;
        GestureDetector myGestureDetector;
        String text = new String("ELEVEN PLUS TWO");
        view = new AnimationViewCV(this);
        view.setGestureListener(new MyGestureListener());
        AnimatedGuiObjectCV<Drawable> textDrawable
                = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT,this,"TEXT",text, screenwidth/2-300, screenheight/2-100, 40);
        textDrawable.addSizeAnimator(800,200,3000,1000);
        textDrawable.addLinearPathAnimator(screenwidth/2,screenheight/2-160,3000,1000);
        view.addAnimatedGuiObject(textDrawable);
        // rect.addLinearPathAnimator(500,700,3000,1000);
        // background1.addSizeAnimator(screenwidth,screenheight,3000,1000);
        // background1.addColorAnimator(Color.BLUE,3000,1000);
        setContentView(view);
        view.startAnimations();
    }

     private class MyGestureListener extends AnimationViewCV.GestureListener {

         int[] permutation = { 3,4,6,5,15,14,7,8,9,10,11,12,1,2,13 };
         String text = new String("ELEVEN PLUS TWO");

        @Override
        public void onLongPress(MotionEvent e) {
            view.removeAllAnimatedGuiObjects();
            int screenwidth = GUIUtilitiesCV.getDisplayWidth()-100;
            int screenheight = GUIUtilitiesCV.getDisplayHeight()-200;
            AnimatedGuiObjectCV<Drawable> textDrawable
                    = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT, DemoPropAnimUtils_TouchGestures.this,"TEXT",text, screenwidth/2-200, screenheight/2+300, 40);
            textDrawable.addSizeAnimator(800,200,3000,1000);
            textDrawable.addLinearPathAnimator(screenwidth/2,screenheight/2-300,3000,1000);
            view.addAnimatedGuiObject(textDrawable);
            view.startAnimations();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            super.onDoubleTap(e);
            // AnimatedGuiObjectCV guiobject = ((AnimationViewCV) view).getAnimatedGuiObjectAtPoint((int)e.getX(),(int)e.getY());
            if (!guiobjects.isEmpty()&&guiobjects.get(0)!=null) {
                view.removeAnimatedGuiObject(guiobjects.get(0));
                int posX = guiobjects.get(0).getCenterX();
                int posY = guiobjects.get(0).getCenterY();
                int duration1 = 300;
                int duration2 = 1000;
                int duration3 = 1000;
                int totalHeight = GUIUtilitiesCV.getDisplayHeight()-400;
                int totalWidth = GUIUtilitiesCV.getDisplayWidth()-100;
                Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
                AnimatedGuiObjectCV<Drawable> drawableBitmap = new AnimatedGuiObjectCV(DemoPropAnimUtils_TouchGestures.this,"BITMAP",bm,posX,posY,200,200);
                drawableBitmap.addSizeAnimator(1200,1200,duration1);
                drawableBitmap.addLinearPathAnimator(posX-500,posY-500,duration1);
                drawableBitmap.addSizeAnimator(0,0,2*duration1,duration1);
                drawableBitmap.addLinearPathAnimator(posX,posY,2*duration1,duration1);
                view.addAnimatedGuiObject(drawableBitmap);
                AnimatedGuiObjectCV<Drawable> drwNew[] = new AnimatedGuiObjectCV[text.length()];
                for (int i=0;i<text.length();i++) {
                    drwNew[i] = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT, DemoPropAnimUtils_TouchGestures.this,"TEXT"+i,""+text.charAt(i), posX, posY, 100, 0);
                    int targetX = 0, targetY = 0;
                    switch(i) {
                        case 0: targetX = 50; targetY = 50; break;
                        case 1: targetX = totalWidth/4; targetY = 50; break;
                        case 2: targetX = totalWidth/2; targetY = 50; break;
                        case 3: targetX = 3*totalWidth/4; targetY = 50; break;
                        case 4: targetX = totalWidth; targetY = 50; break;
                        case 5: targetX = 50; targetY = totalHeight/4; break;
                        case 6: targetX = totalWidth; targetY = totalHeight/4; break;
                        case 7: targetX = 50; targetY = totalHeight/2; break;
                        case 8: targetX = totalWidth; targetY = totalHeight/2; break;
                        case 9: targetX = 50; targetY = 3*totalHeight/4; break;
                        case 10: targetX = totalWidth; targetY = 3*totalHeight/4; break;
                        case 11: targetX = 50; targetY = totalHeight; break;
                        case 12: targetX = totalWidth/4; targetY = totalHeight; break;
                        case 13: targetX = totalWidth/2; targetY = totalHeight; break;
                        case 14: targetX = 3*totalWidth/4; targetY = totalHeight; break;
                        case 15: targetX = totalWidth; targetY = totalHeight; break;
                    }
                    drwNew[i].addLinearPathAnimator(targetX, targetY, duration2, duration1-200);
                    drwNew[i].addRotationAnimator((float)(6* Math.PI), duration3+duration2+duration1+800);
                    TimeInterpolator ipol = new AccelerateInterpolator();
                    drwNew[i].addLinearPathAnimator(100+totalWidth/text.length()*(permutation[i]-1), totalWidth/2, ipol, duration3, duration1-200+duration2+1000);
                    drwNew[i].addSizeAnimator(80, 80, ipol, duration3, duration1-200+duration2+1000);
                    view.addAnimatedGuiObject(drwNew[i]);
                }
                view.startAnimations();
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            super.onFling(e1,e2,velocityX,velocityY);
            // AnimatedGuiObjectCV guiobject = ((AnimationViewCV) view).getAnimatedGuiObjectAtPoint((int)e1.getX(),(int)e1.getY());
            if (guiobjects.isEmpty()||(guiobjects.get(0) == null)) return true;
            guiobjects.get(0).clearAnimatorList();
            guiobjects.get(0).addLinearPathAnimator((int)e2.getX(), (int)e2.getY(), new AccelerateInterpolator(), 500);
            guiobjects.get(0).startAnimation();
            return true;
        }

    }

}
