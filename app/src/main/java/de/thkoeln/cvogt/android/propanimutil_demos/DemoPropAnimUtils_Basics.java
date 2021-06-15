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
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_Basics extends Activity {

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
        // animationView to display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        // objects to be displayed
        AnimatedGuiObjectCV square1, square2, square3, oval, rectangle, circle1, circle2, bitmap;
        // blinking square
        square1 = AnimatedGuiObjectCV.createSquare("SQUARE", Color.BLACK,120,120,200);
        square1.addBlinkAnimator(500);
        view.addAnimatedGuiObject(square1);
        // oval with oscillating size
        oval = AnimatedGuiObjectCV.createOval("OVAL", Color.RED,700,200,500,200);
        oval.setRotationAngle((float) Math.PI/4);
        ArrayList<Point> al = new ArrayList<Point>();
        for (int i=0;i<16;i++)
            if (i%2==0)
                al.add(new Point(500,200));
            else
                al.add(new Point(800,300));
        oval.addSizeAnimator(al,5000,1000);  // this animation and all others start with one second delay to allow the animationView to appear on the screen before
        view.addAnimatedGuiObject(oval);
        // rectangle moving along a straight line and changing its color
        rectangle = AnimatedGuiObjectCV.createRect("RECT", Color.BLUE,220,700,400,200);
        rectangle.addLinearPathAnimator(1000,700,5000,1000);
        rectangle.addColorAnimator(0xFF00FFFF,5000,1000);
        view.addAnimatedGuiObject(rectangle);
        // bitmap image moving along a straight line and growing
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
        bm = GraphicsUtilsCV.makeBitmapTransparent(bm, Color.WHITE);
        bitmap = AnimatedGuiObjectCV.createFromBitmap(this,"BITMAP",bm,GUIUtilitiesCV.getDisplayWidth()-250,700,200,200);
        bitmap.addLinearPathAnimator(200,1500,5000,1000);
        // bitmap.addSizeAnimator(400,400,5000,1000);
        bitmap.addZoomAnimator(3,2500,1000);
        bitmap.addZoomAnimator(0.33333,2500,3500);
        bitmap.addZoomAnimator(0.1,2500,6000);
        bitmap.addZoomAnimator(10,2500,8500);
        bitmap.addZoomAnimator(0.05,2500,11000);
        bitmap.addZoomAnimator(20,2500,13500);
        view.addAnimatedGuiObject(bitmap);
        // circle1 moving along a zigzag line
        circle1 = AnimatedGuiObjectCV.createCircle("CIRCLE", Color.GREEN,120,1000,200);
        al = new ArrayList<Point>();
        for (int i=0;i<16;i++)
            al.add(new Point(120+i*70,1000+i%2*500));
        circle1.addLinearSegmentsPathAnimator(al,5000,1000);
        view.addAnimatedGuiObject(circle1);
        // square moving downward and shrinking
        square2 = AnimatedGuiObjectCV.createSquare("SQUARE2", Color.DKGRAY,1300,200,200);
        square2.addSizeAnimator(0,0,4000,1000);
        square2.addLinearPathAnimator(1300,1600,4000,1000);
        view.addAnimatedGuiObject(square2);
        // circle moving sideways on a line, first growing and then disappearing
        circle2 = AnimatedGuiObjectCV.createCircle("CIRCLE2", Color.BLUE,500,GUIUtilitiesCV.getDisplayHeight()-400,200);
        ArrayList<Point> sizes = new ArrayList<Point>();
        sizes.add(new Point(200,200));
        sizes.add(new Point(400,400));
        sizes.add(new Point(0,0));
        circle2.addSizeAnimator(sizes,5000,1000);
        // circle2.addLinearPathAnimator(750,1700,5000,1000);
        circle2.addLinearPathAnimator(1000,GUIUtilitiesCV.getDisplayHeight()-400,5000,1000);
        view.addAnimatedGuiObject(circle2);
        AnimatedGuiObjectCV line = AnimatedGuiObjectCV.createRect("RECT", Color.BLACK,750,GUIUtilitiesCV.getDisplayHeight()-400,700,4);
        // AnimatedGuiObjectCV line = new AnimatedGuiObjectCV("LINE",Color.RED,new Point(400,1900),new Point(1100,1900),6);
        view.addAnimatedGuiObject(line);
        // square appearing after a period of time
        square3 = AnimatedGuiObjectCV.createSquare("SQUARE3", Color.MAGENTA,200,GUIUtilitiesCV.getDisplayHeight()-400,200);
        square3.addAppearanceAnimator(3000);
        square3.setVisible(false);
        // alternativ: square3.addColorAnimator(Color.MAGENTA,5000,1000);,
        // zuvor Farbe auf Color.MAGENTA&0x00FFFFFF setzen
        view.addAnimatedGuiObject(square3);
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        view.startAnimations();
    }

    private void demo2() {
        // UPDATE: For the effects of this demo, better use the AnimatedGuiObjectsGroupCV class with its animation methods
        // animationView to display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        // objects to be displayed
        int numberObjects = 4;
        int distToBorder = 30, distToTop = 30, spacing = 30, objHeight = 100;
        ArrayList<AnimatedGuiObjectCV> groupLeft, groupRightRight, groupRightLeft;
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        // first group: initial placement left aligned, then movement to right-aligned positions at the right display borders
        groupLeft = new ArrayList<>();
        for (int i=0;i<numberObjects;i++) {
            AnimatedGuiObjectCV obj = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT, "RECT1" + i, Color.BLACK, 0, 0, 300+i*40, objHeight);
            groupLeft.add(obj);
            view.addAnimatedGuiObject(obj);
        }
        GraphicsUtilsCV.placeObjectsVerticalAligned(groupLeft,distToBorder,distToTop,spacing,0,GraphicsUtilsCV.ALIGN_LEFT);
        HashMap<AnimatedGuiObjectCV, Point> animTargets = GraphicsUtilsCV.positionsVerticalAligned(groupLeft,distToBorder,distToTop+2*numberObjects*(objHeight+spacing),spacing,screenwidth,GraphicsUtilsCV.ALIGN_RIGHTRIGHT);
        AnimationUtilsCV.addLinearPathAnimators(animTargets,3000,2000);
        // second group: right-alignment close to the right border of the display, then translation to the left border
        groupRightRight = new ArrayList<>();
        for (int i=0;i<numberObjects;i++) {
            AnimatedGuiObjectCV obj = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT, "RECT2" + i, Color.RED, 0, 0, 300+i*40, objHeight);
            groupRightRight.add(obj);
            view.addAnimatedGuiObject(obj);
        }
        GraphicsUtilsCV.placeObjectsVerticalAligned(groupRightRight,distToBorder,distToTop,objHeight+2*spacing,screenwidth,GraphicsUtilsCV.ALIGN_RIGHTRIGHT);
        AnimationUtilsCV.addTranslationAnimators(groupRightRight,-screenwidth+500,1000,3000,5500);
        // third group: initial placement left-alignmed close to the right border of the display, then movement to the left border with left-alignment there
        groupRightRight = new ArrayList<>();
        for (int i=0;i<numberObjects;i++) {
            AnimatedGuiObjectCV obj = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT, "RECT3" + i, Color.BLUE, 0, 0, 300+i*40, objHeight);
            groupRightRight.add(obj);
            view.addAnimatedGuiObject(obj);
        }
        GraphicsUtilsCV.placeObjectsVerticalAligned(groupRightRight,distToBorder,distToTop+objHeight+spacing,objHeight+2*spacing,screenwidth,GraphicsUtilsCV.ALIGN_RIGHTLEFT);
        animTargets = GraphicsUtilsCV.positionsVerticalAligned(groupRightRight,distToBorder,distToTop,spacing,screenwidth,GraphicsUtilsCV.ALIGN_LEFT);
        AnimationUtilsCV.addLinearPathAnimators(animTargets,3000,9000);
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        view.startAnimations();
    }

    private void demo3() {
        // UPDATE: For the effects of this demo, better use the AnimatedGuiObjectsGroupCV class with its animation methods
        int[] colors = { Color.BLUE, Color.RED, Color.GREEN, Color.BLACK, Color.MAGENTA, Color.YELLOW, Color.CYAN };
        // animationView to display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        // objects to be displayed
        int numberObjects = 10;
        ArrayList<AnimatedGuiObjectCV> objects = new ArrayList<>();
        for (int i=0;i<numberObjects;i++) {
            int color = colors[i%colors.length];
            AnimatedGuiObjectCV obj = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT, "RECT" + i, color, 0, 0, 300+i*50, 100+i*25);
            objects.add(obj);
            view.addAnimatedGuiObject(obj);
        }
        GraphicsUtilsCV.placeObjectsVerticalAligned(objects,50,100,50,0,GraphicsUtilsCV.ALIGN_LEFT);
        AnimationUtilsCV.addStackingAnimators(objects,500,1000,50,-50,2000,1000);
        AnimationUtilsCV.addStackingAnimators(objects,500,500,50,50,2000,4000);
        AnimationUtilsCV.addStackingAnimators(objects,1000,500,-50,50,2000,7000);
        AnimationUtilsCV.addStackingAnimators(objects,1000,1000,-50,-50,2000,10000);
        AnimationUtilsCV.addStackingAnimators(objects,750,500,0,50,2000,13000);
        AnimationUtilsCV.addStackingAnimators(objects,500,750,50,0,2000,16000);
        setContentView(view);
        view.startAnimations();
    }

    private void demo4() {
        int noObjects = 16;
        int objSize = 50;
        // animationView to display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        // objects to be displayed
        AnimatedGuiObjectCV objCenter;
        // Circle to mark the center
        objCenter = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CENTER", Color.BLACK,GUIUtilitiesCV.getDisplayWidth()/2,GUIUtilitiesCV.getDisplayHeight()/2,objSize);
        view.addAnimatedGuiObject(objCenter);
        // Circles to be moved inward
        Point[] pos = GraphicsUtilsCV.pointsOnCircle(objCenter.getCenterX(),objCenter.getCenterY(),GUIUtilitiesCV.getDisplayWidth()/4,noObjects);
        for (int i=0;i<noObjects;i++) {
            AnimatedGuiObjectCV circle = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "X", Color.BLUE, pos[i].x, pos[i].y, objSize);
            circle.addAttractOrRepulseAnimator(objCenter.getCenterX(), objCenter.getCenterY(), 0.5, 2000, 1000);
            view.addAnimatedGuiObject(circle);
        }
        // Circles to be moved outward
        pos = GraphicsUtilsCV.pointsOnCircle(objCenter.getCenterX(),objCenter.getCenterY(),GUIUtilitiesCV.getDisplayWidth()/8,noObjects);
        for (int i=0;i<noObjects;i++) {
            AnimatedGuiObjectCV circle = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "X", Color.GREEN, pos[i].x, pos[i].y, objSize);
            circle.addAttractOrRepulseAnimator(objCenter.getCenterX(), objCenter.getCenterY(), 2, 2000, 3000);
            view.addAnimatedGuiObject(circle);
        }
        // Circles to be moved across the center
        pos = GraphicsUtilsCV.pointsOnCircle(objCenter.getCenterX(),objCenter.getCenterY(),3*GUIUtilitiesCV.getDisplayWidth()/8,noObjects);
        for (int i=0;i<noObjects/2;i++) {
            AnimatedGuiObjectCV circle = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "X", Color.RED, pos[i].x, pos[i].y, objSize);
            circle.addAttractOrRepulseAnimator(objCenter.getCenterX(), objCenter.getCenterY(), -1, 4000, 5000);
            view.addAnimatedGuiObject(circle);
        }
        for (int i=noObjects/2;i<noObjects;i++) {
            AnimatedGuiObjectCV circle = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "X", Color.BLACK, pos[i].x, pos[i].y, objSize);
            circle.addAttractOrRepulseAnimator(objCenter.getCenterX(), objCenter.getCenterY(), -1, 4000, 5000);
            view.addAnimatedGuiObject(circle);
        }
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        view.startAnimations();
    }

    // Method to test the getCorners(), getXXXBounds() and getMin/MaxX/Y() methods for different types of objects

    public void testGetCornersBounds() {
        final AnimationViewCV view = new AnimationViewCV(this);
        Log.v("DEMO","Square: getCorners()");
        AnimatedGuiObjectCV square = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE,"SQUARE", Color.BLACK,120,120,200);
        view.addAnimatedGuiObject(square);
        Point[] corners = square.getCorners();
        Log.v("DEMO","  left top:     "+corners[0].x+" "+corners[0].y);
        Log.v("DEMO","  right top:    "+corners[1].x+" "+corners[1].y);
        Log.v("DEMO","  right bottom: "+corners[2].x+" "+corners[2].y);
        Log.v("DEMO","  left bottom:  "+corners[3].x+" "+corners[3].y);
        Log.v("DEMO","Rotated Square: getCorners()");
        AnimatedGuiObjectCV rotSquare = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"ROTSQUARE", Color.BLUE,450,180,200,200,(float) Math.PI/4);
        view.addAnimatedGuiObject(rotSquare);
        corners = rotSquare.getCorners();
        Log.v("DEMO","  left top:     "+corners[0].x+" "+corners[0].y);
        Log.v("DEMO","  right top:    "+corners[1].x+" "+corners[1].y);
        Log.v("DEMO","  right bottom: "+corners[2].x+" "+corners[2].y);
        Log.v("DEMO","  left bottom:  "+corners[3].x+" "+corners[3].y);
        Log.v("DEMO"," getLeftBound() / minX:       "+ rotSquare.getLeftBound()+" "+rotSquare.getMinX());
        Log.v("DEMO"," getTopBound() / minY:    "+ rotSquare.getTopBound()+" "+rotSquare.getMinY());
        Log.v("DEMO"," getRightBound() / maxX:  "+ rotSquare.getRightBound()+" "+rotSquare.getMaxX());
        Log.v("DEMO"," getBottomBound() / maxY: "+ rotSquare.getBottomBound()+" "+rotSquare.getMaxY());
        Log.v("DEMO","Rotated Oval: getCorners()");
        AnimatedGuiObjectCV rotOval = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_OVAL,"ROTOVAL", Color.RED,800,180,400,200,(float) Math.PI/4);
        view.addAnimatedGuiObject(rotOval);
        corners = rotOval.getCorners();
        Log.v("DEMO","  left top:     "+corners[0].x+" "+corners[0].y);
        Log.v("DEMO","  right top:    "+corners[1].x+" "+corners[1].y);
        Log.v("DEMO","  right bottom: "+corners[2].x+" "+corners[2].y);
        Log.v("DEMO","  left bottom:  "+corners[3].x+" "+corners[3].y);
        Log.v("DEMO"," getLeftBound() / minX:       "+ rotOval.getLeftBound()+" "+rotOval.getMinX());
        Log.v("DEMO"," getTopBound() / minY:    "+ rotOval.getTopBound()+" "+rotOval.getMinY());
        Log.v("DEMO"," getRightBound() / maxX:  "+ rotOval.getRightBound()+" "+rotOval.getMaxX());
        Log.v("DEMO"," getBottomBound() / maxY: "+ rotOval.getBottomBound()+" "+rotOval.getMaxY());
        Log.v("DEMO","View: see later");
        Button button = new Button(this);
        button.setText(" BUTTON");
        button.setWidth(600);
        button.setHeight(200);
        button.setBackgroundColor(0x00FFFFFF);
        button.setBackgroundResource(R.drawable.button_layout);
        final AnimatedGuiObjectCV buttonGUIObject = new AnimatedGuiObjectCV<View>(button,"BUTTON",50,450);
        view.addAnimatedGuiObject(buttonGUIObject);
        button.setOnClickListener(
                new View.OnClickListener() {
                    // Show corner values after layout run completed
                    @Override
                    public void onClick(View b) {
                        // https://stackoverflow.com/questions/32400211/view-getx-and-gety-return-0-0-after-they-have-been-added-to-the-activity
                        Point[] corners = buttonGUIObject.getCorners();
                        Log.v("DEMO","View:");
                        Log.v("DEMO","  left top:     "+corners[0].x+" "+corners[0].y);
                        Log.v("DEMO","  right top:    "+corners[1].x+" "+corners[1].y);
                        Log.v("DEMO","  right bottom: "+corners[2].x+" "+corners[2].y);
                        Log.v("DEMO","  left bottom:  "+corners[3].x+" "+corners[3].y);
                    }
                });
        Log.v("DEMO","Rotated View: see later");
        Button button2 = new Button(this);
        button2.setText(" BUTTON2");
        button2.setWidth(600);
        button2.setHeight(200);
        button2.setBackgroundColor(0x00FFFFFF);
        button2.setBackgroundResource(R.drawable.button_layout);
        final AnimatedGuiObjectCV buttonGUIObject2 = new AnimatedGuiObjectCV<View>(button2,"BUTTON2",500,450);
        buttonGUIObject2.addRotationAnimator((float)(Math.PI/2),2000,1000);
        view.addAnimatedGuiObject(buttonGUIObject2);
        button2.setOnClickListener(
                new View.OnClickListener() {
                    // Show corner values after layout run completed
                    @Override
                    public void onClick(View b) {
                        // https://stackoverflow.com/questions/32400211/view-getx-and-gety-return-0-0-after-they-have-been-added-to-the-activity
                        Point[] corners = buttonGUIObject2.getCorners();
                        Log.v("DEMO","Rotated View:");
                        Log.v("DEMO","  left top:     "+corners[0].x+" "+corners[0].y);
                        Log.v("DEMO","  right top:    "+corners[1].x+" "+corners[1].y);
                        Log.v("DEMO","  right bottom: "+corners[2].x+" "+corners[2].y);
                        Log.v("DEMO","  left bottom:  "+corners[3].x+" "+corners[3].y);
                    }
                });
        Log.v("DEMO","Rotated Path Drawable: getCorners()");
        Path starPath = new Path();
        starPath.moveTo(0,200);
        starPath.lineTo(150,150);
        starPath.lineTo(200,0);
        starPath.lineTo(250,150);
        starPath.lineTo(400,200);
        starPath.lineTo(250,250);
        starPath.lineTo(200,400);
        starPath.lineTo(150,250);
        starPath.lineTo(0,200);
        AnimatedGuiObjectCV<Drawable> star = new AnimatedGuiObjectCV<Drawable>(starPath,"PATHSHAPE", Color.GREEN,300,1000,400,400, Math.PI/4);
        view.addAnimatedGuiObject(star);
        corners = star.getCorners();
        Log.v("DEMO","  left top:     "+corners[0].x+" "+corners[0].y);
        Log.v("DEMO","  right top:    "+corners[1].x+" "+corners[1].y);
        Log.v("DEMO","  right bottom: "+corners[2].x+" "+corners[2].y);
        Log.v("DEMO","  left bottom:  "+corners[3].x+" "+corners[3].y);
        Log.v("DEMO"," getLeftBound() / minX:       "+ star.getLeftBound()+" "+star.getMinX());
        Log.v("DEMO"," getTopBound() / minY:    "+ star.getTopBound()+" "+star.getMinY());
        Log.v("DEMO"," getRightBound() / maxX:  "+ star.getRightBound()+" "+star.getMaxX());
        Log.v("DEMO"," getBottomBound() / maxY: "+ star.getBottomBound()+" "+star.getMaxY());
        Log.v("DEMO","Rotated Layer Drawable: getCorners()");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_nl);
        DrawableTextWithIconCV drw5 = new DrawableTextWithIconCV(this,"Hoi",bitmap,500,80,150,4);
        AnimatedGuiObjectCV<Drawable> layered = new AnimatedGuiObjectCV<Drawable>("WortNiederlaendisch", drw5, 300, 1200, Math.PI/8);
        view.addAnimatedGuiObject(layered);
        corners = layered.getCorners();
        Log.v("DEMO","  left top:     "+corners[0].x+" "+corners[0].y);
        Log.v("DEMO","  right top:    "+corners[1].x+" "+corners[1].y);
        Log.v("DEMO","  right bottom: "+corners[2].x+" "+corners[2].y);
        Log.v("DEMO","  left bottom:  "+corners[3].x+" "+corners[3].y);
        Log.v("DEMO"," getLeftBound() / minX:       "+ layered.getLeftBound()+" "+layered.getMinX());
        Log.v("DEMO"," getTopBound() / minY:    "+ layered.getTopBound()+" "+layered.getMinY());
        Log.v("DEMO"," getRightBound() / maxX:  "+ layered.getRightBound()+" "+layered.getMaxX());
        Log.v("DEMO"," getBottomBound() / maxY: "+ layered.getBottomBound()+" "+layered.getMaxY());
        setContentView(view);
        view.startAnimations();
    }

    // demo5() is not called here. It was only used for a screenshot showing different types of AnimatedGuiObjects

    private void demo5() {

        AnimationViewCV view = new AnimationViewCV(this);

        AnimatedGuiObjectCV square1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE,"SQUARE", Color.BLACK,120,120,200);
        view.addAnimatedGuiObject(square1);

        AnimatedGuiObjectCV oval = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_OVAL,"OVAL", Color.RED,700,160,600,200);
        view.addAnimatedGuiObject(oval);

        AnimatedGuiObjectCV circle1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.GREEN,200,400,200);
        view.addAnimatedGuiObject(circle1);

        AnimatedGuiObjectCV rectangle = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,720,420,400,200);
        view.addAnimatedGuiObject(rectangle);

        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
        bm = GraphicsUtilsCV.makeBitmapTransparent(bm, Color.WHITE);
        AnimatedGuiObjectCV bitmap = new AnimatedGuiObjectCV(this,"BITMAP",bm,220,700,300,300);
        view.addAnimatedGuiObject(bitmap);

        AnimatedGuiObjectCV objBitmapText1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT,this,"BITMAP1","gtgtgt",700,700,100);
        view.addAnimatedGuiObject(objBitmapText1);

        Button button1 = new Button(this);
        button1.setText(" BUTTON 1");
        button1.setWidth(430);
        button1.setHeight(200);
        button1.setBackgroundColor(0x00FFFFFF);
        button1.setBackgroundResource(R.drawable.button_layout);
        AnimatedGuiObjectCV buttonGUIObject1 = new AnimatedGuiObjectCV<View>(button1,"BUTTON1",50,950);
        view.addAnimatedGuiObject(buttonGUIObject1);

        // a blossom
        int radius = 75;
        Path fourCircles = new Path();
        fourCircles.addCircle(radius,2*radius,radius, Path.Direction.CW);
        fourCircles.addCircle(2*radius,radius,radius, Path.Direction.CW);
        fourCircles.addCircle(3*radius,2*radius,radius, Path.Direction.CW);
        fourCircles.addCircle(2*radius,3*radius,radius, Path.Direction.CW);
        AnimatedGuiObjectCV<Drawable> trefoil = new AnimatedGuiObjectCV<Drawable>(fourCircles,"PATHSHAPE_ROUND", Color.BLUE,750,1050,4*radius,4*radius,0);
        view.addAnimatedGuiObject(trefoil);
        AnimatedGuiObjectCV<Drawable> circle = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CENTER", Color.YELLOW,750,1050,(int)(1.5*radius));
        view.addAnimatedGuiObject(circle);

        final int objectWidth = 470, objectHeight = 120, flagWidth = 160;
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_gb);
        DrawableTextWithIconCV drw2 = new DrawableTextWithIconCV(this,"house",bitmap2,objectWidth,objectHeight,flagWidth,4);
        // AnimatedGuiObjectCV<Drawable> obj2 = new AnimatedGuiObjectCV<Drawable>("WortEnglisch1", drw2,objectWidth/2+50, 950 + objectHeight);
        AnimatedGuiObjectCV<Drawable> obj2 = new AnimatedGuiObjectCV<Drawable>("WortEnglisch1", drw2,300,1300);
        view.addAnimatedGuiObject(obj2);

        // show the animationView
        setContentView(view);

    }



}
