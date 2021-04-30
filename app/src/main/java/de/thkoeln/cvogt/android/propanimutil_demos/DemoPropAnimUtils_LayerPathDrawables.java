package de.thkoeln.cvogt.android.propanimutil_demos;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import java.util.ArrayList;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_LayerPathDrawables extends Activity {

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
        if (item.getItemId()==R.id.menuItemDemo4)
            demo4();
        return true;
    }

    private void demo1() {
        // Demo with graphical objects showing words in different languages and corresponding flags
        // The view to display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        // Layout values for the objects
        final int width = 500, height = 150, flagWidth = 200;
        // Word 1
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_de);
        DrawableTextWithIconCV drw1 = new DrawableTextWithIconCV(DemoPropAnimUtils_LayerPathDrawables.this,"Hallo",bitmap,width,height,flagWidth,4);
        AnimatedGuiObjectCV<Drawable> obj1 = new AnimatedGuiObjectCV<Drawable>("WortDeutsch", drw1, 300, height);
        Animator animPath1 = obj1.addLinearPathAnimator(1000,200,2000,1000);
        // Animator animSize1 = obj1.addSizeAnimator(400,400,2000,1000);
        Animator animPath2 = obj1.addLinearPathAnimator(200,1000,2000);
        // Animator animColor = obj1.addColorAnimator(Color.RED,2000);
        // Animator animSize2 = obj1.addSizeAnimator(200,200,1000,1000);
        Animator animPath3 = obj1.addLinearPathAnimator(1000,height,2000);
        obj1.playSequentially(animPath1,animPath2,animPath3);
        view.addAnimatedGuiObject(obj1);
        // Word 2
        // Log.v("DEMO","--------------");
        // Log.v("DEMO","demo1 1: "+(new Date()).getTime());
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_gb);
        // Log.v("DEMO","demo1 2: "+(new Date()).getTime());
        DrawableTextWithIconCV drw2 = new DrawableTextWithIconCV(DemoPropAnimUtils_LayerPathDrawables.this,"Hello",bitmap,width,height,flagWidth,4);
        // Log.v("DEMO","demo1 3: "+(new Date()).getTime());
        AnimatedGuiObjectCV<Drawable> obj2 = new AnimatedGuiObjectCV<Drawable>("WortEnglisch", drw2, 300, 3*height);
        // Log.v("DEMO","demo1 4: "+(new Date()).getTime());
        obj2.addBezierPathAnimator(2000,-500,-100,2000,1000,3*height,4000,1000);
        view.addAnimatedGuiObject(obj2);
        // Log.v("DEMO","demo1 5: "+(new Date()).getTime());
        // Log.v("DEMO","--------------");
        // Word 3
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_fr);
        DrawableTextWithIconCV drw3 = new DrawableTextWithIconCV(DemoPropAnimUtils_LayerPathDrawables.this,"Salu",bitmap,width,height,flagWidth,4);
        AnimatedGuiObjectCV<Drawable> obj3 = new AnimatedGuiObjectCV<Drawable>("WortFranzoesisch", drw3, 300, 5*height, 0);
        ArrayList<Point> al = new ArrayList<Point>();
        al.add(new Point(300,5*height));
        al.add(new Point(533,100));
        al.add(new Point(767,1000));
        al.add(new Point(1000,5*height));
        obj3.addLinearSegmentsPathAnimator(al,5000,1000);
        obj3.addRotationAnimator((float)(2* Math.PI),5000,1000);
        view.addAnimatedGuiObject(obj3);
        // Word 4
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_es);
        DrawableTextWithIconCV drw4 = new DrawableTextWithIconCV(DemoPropAnimUtils_LayerPathDrawables.this,"Hola",bitmap,width,height,flagWidth,4);
        drw4.setColor(Color.YELLOW);
        drw4.setStrokewidthBorder(10);
        drw4.setColorBorder(Color.BLUE);
        AnimatedGuiObjectCV<Drawable> obj4 = new AnimatedGuiObjectCV<Drawable>("WortSpanisch", drw4, 300, 7*height, 0);
        obj4.addRotationAnimator((float) Math.PI,4000,1000);
        obj4.addArcPathAnimator(650,7*height, Math.PI,4000,1000);
        obj4.addColorBorderAnimator(Color.RED,5000,1000);
        obj4.addStrokewidthBorderAnimator(20,5000,1000);
        view.addAnimatedGuiObject(obj4);
        // Word 5
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_nl);
        DrawableTextWithIconCV drw5 = new DrawableTextWithIconCV(DemoPropAnimUtils_LayerPathDrawables.this,"Hoi",bitmap,width,height,flagWidth,4);
        AnimatedGuiObjectCV<Drawable> obj5 = new AnimatedGuiObjectCV<Drawable>("WortNiederlaendisch", drw5, 300, 9*height,0);
        obj5.addLinearPathAnimator(1000,9*height,5000,1000);
        obj5.addSizeAnimator(width/2,height/2,5000,1000);
        obj5.addRotationAnimator((float)(2*Math.PI),5000,1000);
        obj5.addColorAnimator(Color.YELLOW,5000,1000);
        view.addAnimatedGuiObject(obj5);
        setContentView(view);
        // Wort 6
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_de);
        DrawableTextWithIconCV drw6 = new DrawableTextWithIconCV(DemoPropAnimUtils_LayerPathDrawables.this,"Hallo",bitmap,width,height,flagWidth,4);
        AnimatedGuiObjectCV obj6 = new AnimatedGuiObjectCV("WortDeutsch2", drw6, 300, 11*height,0);
        obj6.addFadeOutAnimator(3000,1000);
        obj6.addFadeInAnimator(3000,4000);
        view.addAnimatedGuiObject(obj6);
        view.startAnimations();
    }

    private void demo2() {
        final int objectWidth = 350, objectHeight = 90, flagWidth = 120;
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        AnimationViewCV view = new AnimationViewCV(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_de);
        DrawableTextWithIconCV drw1 = new DrawableTextWithIconCV(DemoPropAnimUtils_LayerPathDrawables.this,"Haus",bitmap,objectWidth,objectHeight,flagWidth,4);
        AnimatedGuiObjectCV<Drawable> obj1 = new AnimatedGuiObjectCV<Drawable>("WortDeutsch1", drw1, 200, objectHeight);
        obj1.addPropertyChangedListener(new CollisionHandler_Loesung());
        view.addAnimatedGuiObject(obj1);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_gb);
        DrawableTextWithIconCV drw2 = new DrawableTextWithIconCV(this,"house",bitmap,objectWidth,objectHeight,flagWidth,4);
        AnimatedGuiObjectCV<Drawable> obj2 = new AnimatedGuiObjectCV<Drawable>("WortEnglisch1", drw2, screenwidth-objectWidth, objectHeight);
        obj2.addPropertyChangedListener(new CollisionHandler_Loesung());
        view.addAnimatedGuiObject(obj2);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_de);
        DrawableTextWithIconCV drw3 = new DrawableTextWithIconCV(this,"Stadt",bitmap,objectWidth,objectHeight,flagWidth,4);
        AnimatedGuiObjectCV<Drawable> obj3 = new AnimatedGuiObjectCV<Drawable>("WortDeutsch2", drw3, 200, 8*objectHeight);
        obj3.addPropertyChangedListener(new CollisionHandler_Loesung());
        view.addAnimatedGuiObject(obj3);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_gb);
        DrawableTextWithIconCV drw4 = new DrawableTextWithIconCV(this,"city",bitmap,objectWidth,objectHeight,flagWidth,4);
        AnimatedGuiObjectCV<Drawable> obj4 = new AnimatedGuiObjectCV<Drawable>("WortEnglisch2", drw4, screenwidth-objectWidth, 8*objectHeight);
        obj4.addPropertyChangedListener(new CollisionHandler_Loesung());
        view.addAnimatedGuiObject(obj4);
        setContentView(view);
        view.setGestureListener(new MyGestureListener());
        view.startAnimations();
    }

    private void demo3() {
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        // A triangle - moving, growing, rotating, and changing its color
        Path trianglePath = new Path();
        trianglePath.moveTo(0,200);
        trianglePath.lineTo(200,200);
        trianglePath.lineTo(0,0);
        trianglePath.lineTo(0,200);
        AnimatedGuiObjectCV<Drawable> triangle = new AnimatedGuiObjectCV<Drawable>(trianglePath,"PATHSHAPE", Color.BLACK,200,200,200,200,0);
        triangle.addLinearPathAnimator(1000,400,3000,1000);
        triangle.addColorAnimator(Color.RED,3000,1000);
        triangle.addRotationAnimator((float) Math.PI,3000,1000);
        triangle.addSizeAnimator(400,400,3000,1000);
        AnimationViewCV view = new AnimationViewCV(this);
        view.addAnimatedGuiObject(triangle);
        // a rotating blossom
        int radius = 75;
        Path fourCircles = new Path();
        fourCircles.addCircle(radius,2*radius,radius, Path.Direction.CW);
        fourCircles.addCircle(2*radius,radius,radius, Path.Direction.CW);
        fourCircles.addCircle(3*radius,2*radius,radius, Path.Direction.CW);
        fourCircles.addCircle(2*radius,3*radius,radius, Path.Direction.CW);
        AnimatedGuiObjectCV<Drawable> trefoil = new AnimatedGuiObjectCV<Drawable>(fourCircles,"PATHSHAPE_ROUND", Color.BLUE,screenwidth/2,800,4*radius,4*radius,0);
        trefoil.addInfiniteRotationAnimator(5000);
        trefoil.addCirclePathAnimator(screenwidth/2,1400,5000,1000);
        view.addAnimatedGuiObject(trefoil);
        AnimatedGuiObjectCV<Drawable> circle = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CENTER", Color.YELLOW,screenwidth/2,800,(int)(1.5*radius));
        circle.addCirclePathAnimator(screenwidth/2,1400,5000,1000);
        view.addAnimatedGuiObject(circle);
        // a blinking star
        AnimatedGuiObjectCV<Drawable> starPart1 = AnimatedGuiObjectCV.createStar("STAR_PART1", 6, screenwidth/2,1400,200,80, Color.GREEN, Math.PI/6);
        starPart1.addBlinkAnimator(2000);
        // starPart1.addFadeInAnimator(3000);
        // starPart1.addFadeOutAnimator(3000,3000);
        view.addAnimatedGuiObject(starPart1);
        AnimatedGuiObjectCV<Drawable> starPart2 = AnimatedGuiObjectCV.createStar("STAR_PART2",6,screenwidth/2,1400,200,80, Color.RED);
        // starPart2 = AnimationUtilsCV.createStar("STAR_PART2",4,screenwidth/2,screenheight/2,screenwidth/2,80, Color.RED);
        starPart2.addBlinkAnimator(2000);
        // starPart2.addFadeInAnimator(3000);
        // starPart2.addFadeOutAnimator(3000,3000);
        view.addAnimatedGuiObject(starPart2);
        // show the view and start the animations
        setContentView(view);
        view.startAnimations();
    }

    void demo4() {
        AnimationViewCV view = new AnimationViewCV(this);
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        AnimatedGuiObjectCV background = AnimatedGuiObjectCV.createRect("BACKGROUND",Color.BLUE,screenwidth/2,screenheight/2,0,0);
        background.addSizeAnimator(screenwidth,screenwidth,1000);
        view.addAnimatedGuiObject(background);
        AnimatedGuiObjectCV[] stars = new AnimatedGuiObjectCV[12];
        Point[] targets = GraphicsUtilsCV.pointsOnCircle(screenwidth/2,screenheight/2,screenwidth/3,12);
        for (int i=0;i<12;i++) {
            stars[i] = AnimatedGuiObjectCV.createStar("STAR",5,0,0,40,20,Color.YELLOW);
            stars[i].setZindex(2);
            // stars[i].addLinearPathAnimator(targets[i].x,targets[i].y,2000,2000);
            stars[i].addBezierPathAnimator(screenwidth/2,screenheight,targets[i].x,targets[i].y,2000,1000);
            view.addAnimatedGuiObject(stars[i]);
        }
        setContentView(view);
        view.startAnimations();
    }

    class CollisionHandler_Loesung implements AnimatedGuiObjectCV.OnPropertyChangedListener {

        @Override
        public void onPropertyChanged(AnimatedGuiObjectCV obj, String propertyName) {
            if (obj.getName().equals("LOESUNG")) return;
            // Check if obj has collided with another object
            AnimatedGuiObjectCV collidingObj = AnimationUtilsCV.PropertyChangedListener_Collision.findCollidingObject(obj);
            if (collidingObj==null||collidingObj.getName().equals("LOESUNG"))
                return;
            AnimationViewCV view = obj.getDisplayingView();
            view.removeAnimatedGuiObject(obj);
            view.removeAnimatedGuiObject(collidingObj);
            int posX = obj.getCenterX();
            int posY = obj.getCenterY();
            int duration = 2000;
            boolean korrekt = (obj.getName().equals("WortDeutsch1")&&collidingObj.getName().equals("WortEnglisch1"))
                    || (collidingObj.getName().equals("WortDeutsch1")&&obj.getName().equals("WortEnglisch1"))
                    || (obj.getName().equals("WortDeutsch2")&&collidingObj.getName().equals("WortEnglisch2"))
                    || (collidingObj.getName().equals("WortDeutsch2")&&obj.getName().equals("WortEnglisch2"));
            AnimatedGuiObjectCV<Drawable> loesungGuiObject;
            if (korrekt)
                loesungGuiObject = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT, DemoPropAnimUtils_LayerPathDrawables.this, "LOESUNG", "CORRECT", posX, posY, 100);
             else
                loesungGuiObject = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT, DemoPropAnimUtils_LayerPathDrawables.this, "LOESUNG", "WRONG", posX, posY, 100);
            loesungGuiObject.addSizeAnimator(400,200,duration);
            loesungGuiObject.addSizeAnimator(0,0,2*duration,duration);
            view.addAnimatedGuiObject(loesungGuiObject);
            loesungGuiObject.startAnimation();
        }

    }

    private class MyGestureListener extends AnimationViewCV.GestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            super.onFling(e1,e2,velocityX,velocityY);
            if (guiobjects.get(0) == null) return true;
            guiobjects.get(0).clearAnimatorList();
            guiobjects.get(0).addLinearPathAnimator((int)(e1.getX()+4*(e2.getX()-e1.getX())),(int)(e1.getY()+4*(e2.getY()-e1.getY())), 4000);
            guiobjects.get(0).startAnimation();
            // Log.v("DEMO","e1X: "+(int)e1.getX()+"e1X: "+(int)e1.getY()+"e2X: "+(int)e2.getX()+"e2Y: "+(int)e2.getY());
            return true;
        }

    }

}
