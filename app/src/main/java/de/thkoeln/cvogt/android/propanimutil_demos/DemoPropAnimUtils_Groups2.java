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

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_Groups2 extends Activity {

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
        int noObjects = 8;
        int objSize = 75;
        // animationView to display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        // objects to be displayed
        final AnimatedGuiObjectsGroupCV objGroup = new AnimatedGuiObjectsGroupCV("ObjectGroup");
        // objects to be displayed
        AnimatedGuiObjectCV objCenter;
        // Circle to mark the center
        objCenter = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CENTER", Color.BLACK, GUIUtilitiesCV.getDisplayWidth()/2,GUIUtilitiesCV.getDisplayHeight()/2,objSize);
        objGroup.add(objCenter);
        // Circles to be moved outward
        Point[] pos = GraphicsUtilsCV.pointsOnCircle(objCenter.getCenterX(),objCenter.getCenterY(),GUIUtilitiesCV.getDisplayWidth()/6,noObjects);
        for (int i=0;i<noObjects;i++)
            objGroup.add(new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "X"+i, Color.GREEN, pos[i].x, pos[i].y, objSize));
        view.addAnimatedGuiObjects(objGroup);
        objGroup.addContractOrExpandAnimator(3,2000,1000);
        int posXtext = 300, posYtext = 200;
        zeigeErklaertext(view,"Contract / Expand (Factor 3)",60, posXtext+200, posYtext, 1000,2000);
        (new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {}
                objGroup.addContractOrExpandAnimator(0.333333,2000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        objGroup.startAnimations();
                    }
                });
            }
        }).start();
        zeigeErklaertext(view,"Contract / Expand (Factor 1/3)",60, posXtext+200, posYtext, 3000,2000);
        (new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {}
                objGroup.addZoomAnimator(3,2000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        objGroup.startAnimations();
                    }
                });
            }
        }).start();
        zeigeErklaertext(view,"Zoom (Factor 3)",60, posXtext, posYtext, 5000,2000);
        (new Thread() {
            public void run() {
                try {
                    sleep(7000);
                } catch (InterruptedException e) {}
                objGroup.addZoomAnimator(0.333333,2000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        objGroup.startAnimations();
                    }
                });
            }
        }).start();
        zeigeErklaertext(view,"Zoom (Factor 1/3)",60, posXtext, posYtext, 7000,2000);
        /*
        objGroup.addContractOrExpandAnimator(0.333333,2000,3000);
        objGroup.addZoomAnimator(3,2000,6000);
        objGroup.addZoomAnimator(0.333333,2000,8000);
        */
// show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        objGroup.startAnimations();
    }

    public void demo2() {
        String[] woerter = { "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez", "once", "doce" };
        final int width = 100, height = 30, flagWidth = 40;
        // animationView to display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        // objects to be displayed
        AnimatedGuiObjectsGroupCV objGroup = new AnimatedGuiObjectsGroupCV("ObjectGroup");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_es);
        Point[] pos = GraphicsUtilsCV.pointsOnCircle(GUIUtilitiesCV.getDisplayWidth()/2,GUIUtilitiesCV.getDisplayHeight()/2,GUIUtilitiesCV.getDisplayWidth()/8,woerter.length);
        for (int i = 0; i<woerter.length;i++) {
            DrawableTextWithIconCV drw = new DrawableTextWithIconCV(this, woerter[i], bitmap, width, height, flagWidth, 4);
            drw.setColor(Color.YELLOW);
            drw.setStrokewidthBorder(3);
            drw.setColorBorder(Color.BLACK);
            objGroup.add(new AnimatedGuiObjectCV<Drawable>("X", drw, pos[i].x, pos[i].y, 0));
        }
        view.addAnimatedGuiObjects(objGroup);
        objGroup.addZoomAnimator(3,2000,1000);
        int posXtext = 300, posYtext = 200;
        zeigeErklaertext(view,"Zoom (Factor 3)",60, posXtext+200, posYtext, 1000,2000);
        objGroup.addArcPathAnimator((float) Math.PI,2000,3000);
        zeigeErklaertext(view,"Arc Path (Angle PI)",60, posXtext+200, posYtext, 3000,2000);
        objGroup.addRotationAnimator((float) Math.PI/2,2000,5000);
        zeigeErklaertext(view,"Rotation (Angle PI/2)",60, posXtext+200, posYtext, 5000,2000);
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        objGroup.startAnimations();
    }

    public void demo3() {
        int noObjects = 8;
        int objSize = 75;
        // animationView to display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        // objects to be displayed
        AnimatedGuiObjectsGroupCV objGroup = new AnimatedGuiObjectsGroupCV("ObjectGroup");
        // objects to be displayed
        AnimatedGuiObjectCV objCenter;
        // Circle to mark the center
        objCenter = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CENTER", Color.BLACK, GUIUtilitiesCV.getDisplayWidth()/2,GUIUtilitiesCV.getDisplayHeight()/2,objSize);
        objCenter.setZindex(2);
        objGroup.add(objCenter);
        // Circles to be moved outward
        Point[] pos = GraphicsUtilsCV.pointsOnCircle(objCenter.getCenterX(),objCenter.getCenterY(),GUIUtilitiesCV.getDisplayWidth()/3,noObjects);
        for (int i=0;i<noObjects;i++) {
            AnimatedGuiObjectCV circle = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "X", Color.GREEN, pos[i].x, pos[i].y, objSize);
            circle.setZindex(2);
            objGroup.add(circle);
        }
        view.addAnimatedGuiObjects(objGroup);
        int leftmostCoord = GraphicsUtilsCV.objectWithLeftmostBound(objGroup).getLeftBound();
        int topmostCoord = GraphicsUtilsCV.objectWithTopmostBound(objGroup).getTopBound();
        int rightmostCoord = GraphicsUtilsCV.objectWithRightmostBound(objGroup).getRightBound();
        int bottommostCoord = GraphicsUtilsCV.objectWithBottommostBound(objGroup).getBottomBound();
        int left = leftmostCoord,
            top = GUIUtilitiesCV.getDisplayHeight()/2-200,
            right = rightmostCoord,
            bottom = GUIUtilitiesCV.getDisplayHeight()/2+200;
        // left = 100; right =left+objSize/2; top = GUIUtilitiesCV.getDisplayHeight()/2; bottom = top+objSize-20;
        objGroup.addMoveToAreaAnimator(left,top,right,bottom,3000,1000);
        AnimatedGuiObjectCV frame = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"FRAME", Color.LTGRAY,left+(right-left)/2,top+(bottom-top)/2,right-left,bottom-top);
        view.addAnimatedGuiObject(frame);
        int posXtext = 300, posYtext = 500;
        zeigeErklaertext(view,"Move To Area (1)",60, posXtext, posYtext, 1000,3000);
        // zeigeErklaertext(view,"Move To Area ("+left+","+top+","+right+","+bottom+")",60, posXtext+200, posYtext, 1000,6000);
        left = GUIUtilitiesCV.getDisplayWidth()/2-200;
        top = topmostCoord;
        right = GUIUtilitiesCV.getDisplayWidth()/2+200;
        bottom = bottommostCoord;
        // left = 10; right = GUIUtilitiesCV.getDisplayWidth()-10;
        objGroup.addMoveToAreaAnimator(left,top,right,bottom,3000,4000);
        frame = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"FRAME", Color.LTGRAY,left+(right-left)/2,top+(bottom-top)/2,right-left,bottom-top);
        view.addAnimatedGuiObject(frame);
        zeigeErklaertext(view,"Move To Area (2)",60, posXtext, posYtext, 4000,3000);
        left = 50;
        top = 100;
        right = GUIUtilitiesCV.getDisplayWidth()-50;
        bottom = 400;
        objGroup.addMoveToAreaAnimator(left,top,right,bottom,3000,7000);
        frame = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"FRAME", Color.LTGRAY,left+(right-left)/2,top+(bottom-top)/2,right-left,bottom-top);
        view.addAnimatedGuiObject(frame);
        zeigeErklaertext(view,"Move To Area (3)",60, posXtext, posYtext, 7000,3000);
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        objGroup.startAnimations();
    }

    public void demo4() {
        String[] woerter = { "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez", "once" };
        final int width = 300, height = 90, flagWidth = 120;
        // animationView to display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        // objects to be displayed
        final AnimatedGuiObjectsGroupCV objGroup = new AnimatedGuiObjectsGroupCV("ObjectGroup");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_es);
        for (int i = 0; i<woerter.length;i++) {
            DrawableTextWithIconCV drw = new DrawableTextWithIconCV(this, woerter[i], bitmap, width, height, flagWidth, 4);
            drw.setColor(Color.YELLOW);
            drw.setStrokewidthBorder(3);
            drw.setColorBorder(Color.BLACK);
            objGroup.add(new AnimatedGuiObjectCV<Drawable>("X", drw, 200, 200+i*(height+20), 0));
        }
        view.addAnimatedGuiObjects(objGroup);
        objGroup.addPlaceOnEllipseAndZoomAnimator(GUIUtilitiesCV.getDisplayWidth()/2,
                GUIUtilitiesCV.getDisplayHeight()/2,GUIUtilitiesCV.getDisplayWidth()/3,0.35,0,0.9,2000,1000);
        int posXtext = 500, posYtext = 200;
        zeigeErklaertext(view,"Place On Ellipse And Zoom",60, posXtext+200, posYtext, 1000,2000);
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        objGroup.startAnimations();
    }

    private void zeigeErklaertext(AnimationViewCV view, String text, int textSize, int startX, int startY, int startDelay, int duration) {
        zeigeErklaertext(view,text,textSize,startX,startY,startX,startY,startDelay,duration);
    }

    private void zeigeErklaertext(AnimationViewCV view, String text, int textSize, int startX, int startY, int targetX, int targetY, int startDelay, int duration) {
        AnimatedGuiObjectCV textview = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT,this,"",text,startX,startY,textSize);
        textview.setVisible(false);
        textview.addAppearanceAnimator(startDelay);
        textview.addLinearPathAnimator(targetX,targetY,duration,startDelay).addListener(new AnimationUtilsCV.EndListener_Delete(textview));
        view.addAnimatedGuiObject(textview);
        // textview.addSizeAnimator(0,0,500, startDelay+duration);
        textview.startAnimation();
    }

}
