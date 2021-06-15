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
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_Reset extends Activity {

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
        // animationView to display the objects
        final AnimationViewCV view = new AnimationViewCV(this);
        // objects to be displayed
        final AnimatedGuiObjectCV square, oval, rectangle, circle, bitmap;
        // Oscillating oval
        oval = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_OVAL,"OVAL", Color.RED,700,200,500,200);
        ArrayList<Point> al = new ArrayList<Point>();
        for (int i=0;i<4;i++)
            if (i%2==0)
                al.add(new Point(500,200));
            else
                al.add(new Point(800,300));
        oval.addSizeAnimator(al,2000,1000);
        final AnimatedGuiObjectCV.ObjectProperties ovalProp = new AnimatedGuiObjectCV.ObjectProperties(oval);
        view.addAnimatedGuiObject(oval);
        // rectangle moving along a straight line, changing its color, and rotating
        rectangle = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,220,700,400,200,0);
        rectangle.addLinearPathAnimator(1000,700,2000,1000);
        rectangle.addColorAnimator(0xFF00FFFF,2000,1000);
        rectangle.addRotationAnimator((float)(Math.PI/4),2000,1000);
        final AnimatedGuiObjectCV.ObjectProperties rectangleProp = new AnimatedGuiObjectCV.ObjectProperties(rectangle);
        view.addAnimatedGuiObject(rectangle);
        // bitmap image moving along a straight line, growing, and rotating
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
        bm = GraphicsUtilsCV.makeBitmapTransparent(bm, Color.WHITE);
        bitmap = new AnimatedGuiObjectCV(this,"BITMAP",bm, GUIUtilitiesCV.getDisplayWidth()-250,700,200,200,0);
        bitmap.addLinearPathAnimator(200,1500,2000,1000);
        // bitmap.addSizeAnimator(400,400,5000,1000);
        bitmap.addZoomAnimator(3,1000,1000);
        bitmap.addZoomAnimator(0.33333,1000,2000);
        bitmap.addRotationAnimator((float)(Math.PI/4),2000,1000);
        final AnimatedGuiObjectCV.ObjectProperties bitmapProp = new AnimatedGuiObjectCV.ObjectProperties(bitmap);
        view.addAnimatedGuiObject(bitmap);
        // circle moving along a zigzag line
        circle = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.GREEN,120,1000,200);
        al = new ArrayList<Point>();
        for (int i=0;i<16;i++)
            al.add(new Point(120+i*70,1000+i%2*500));
        circle.addLinearSegmentsPathAnimator(al,2000,1000);
        final AnimatedGuiObjectCV.ObjectProperties circleProp = new AnimatedGuiObjectCV.ObjectProperties(circle);
        view.addAnimatedGuiObject(circle);
        // square moving downward, shrinking, and rotating
        square = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE,"SQUARE2", Color.DKGRAY,800,400,200,200,0);
        square.addSizeAnimator(100,100,2000,1000);
        square.addLinearPathAnimator(800,1600,2000,1000);
        square.addRotationAnimator((float)(Math.PI/4),2000,1000);
        final AnimatedGuiObjectCV.ObjectProperties squareProp = new AnimatedGuiObjectCV.ObjectProperties(square);
        view.addAnimatedGuiObject(square);
        // LayerDrawable
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_es);
        DrawableTextWithIconCV drw = new DrawableTextWithIconCV(this,"Hola", bitmap2, 300, 90, 120, 4);
        drw.setColor(Color.YELLOW);
        drw.setStrokewidthBorder(3);
        drw.setColorBorder(Color.BLACK);
        final AnimatedGuiObjectCV layerDrawable = new AnimatedGuiObjectCV<Drawable>("X", drw, 200, 1300, 0);
        layerDrawable.addLinearPathAnimator(600,1600,2000,0);
        layerDrawable.addSizeAnimator(450,180,2000,1000);
        layerDrawable.addRotationAnimator((float)(Math.PI/4),2000,1000);
        final AnimatedGuiObjectCV.ObjectProperties layerDrawableProp = new AnimatedGuiObjectCV.ObjectProperties(layerDrawable);
        view.addAnimatedGuiObject(layerDrawable);
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        view.startAnimations();
        // after the animations, reset the objects to their initial states
        (new Thread() {
            public void run() {
                try {
                    sleep(3500);
                } catch (InterruptedException e) {}
                square.setProperties(squareProp);
                oval.setProperties(ovalProp);
                circle.setProperties(circleProp);
                bitmap.setProperties(bitmapProp);
                rectangle.setProperties(rectangleProp);
                layerDrawable.setProperties(layerDrawableProp);
                view.invalidate();
            }
        }).start();
    }

    public void demo2() {
        // animationView to display the objects
        final AnimationViewCV view = new AnimationViewCV(this);
        // objects to be displayed
        final AnimatedGuiObjectCV square, oval, rectangle, circle, bitmap;
        // Oscillating oval
        oval = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_OVAL,"OVAL", Color.RED,700,200,500,200);
        ArrayList<Point> al = new ArrayList<Point>();
        for (int i=0;i<4;i++)
            if (i%2==0)
                al.add(new Point(500,200));
            else
                al.add(new Point(800,300));
        oval.addSizeAnimator(al,2000,1000);
        final AnimatedGuiObjectCV.ObjectProperties ovalProp = new AnimatedGuiObjectCV.ObjectProperties(oval);
        view.addAnimatedGuiObject(oval);
        // rectangle moving along a straight line, changing its color, and rotating
        rectangle = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,220,700,400,200,0);
        rectangle.addLinearPathAnimator(1000,700,2000,1000);
        rectangle.addColorAnimator(0xFF00FFFF,2000,1000);
        rectangle.addRotationAnimator((float)(Math.PI/4),2000,1000);
        final AnimatedGuiObjectCV.ObjectProperties rectangleProp = new AnimatedGuiObjectCV.ObjectProperties(rectangle);
        view.addAnimatedGuiObject(rectangle);
        // bitmap image moving along a straight line, growing, and rotating
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
        bm = GraphicsUtilsCV.makeBitmapTransparent(bm, Color.WHITE);
        bitmap = new AnimatedGuiObjectCV(this,"BITMAP",bm, GUIUtilitiesCV.getDisplayWidth()-250,700,200,200,0);
        bitmap.addLinearPathAnimator(200,1500,2000,1000);
        // bitmap.addSizeAnimator(400,400,5000,1000);
        bitmap.addZoomAnimator(3,1000,1000);
        bitmap.addZoomAnimator(0.33333,1000,2000);
        bitmap.addRotationAnimator((float)(Math.PI/4),2000,1000);
        final AnimatedGuiObjectCV.ObjectProperties bitmapProp = new AnimatedGuiObjectCV.ObjectProperties(bitmap);
        view.addAnimatedGuiObject(bitmap);
        // circle moving along a zigzag line
        circle = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.GREEN,120,1000,200);
        al = new ArrayList<Point>();
        for (int i=0;i<16;i++)
            al.add(new Point(120+i*70,1000+i%2*500));
        circle.addLinearSegmentsPathAnimator(al,2000,1000);
        final AnimatedGuiObjectCV.ObjectProperties circleProp = new AnimatedGuiObjectCV.ObjectProperties(circle);
        view.addAnimatedGuiObject(circle);
        // square moving downward, shrinking, and rotating
        square = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE,"SQUARE2", Color.DKGRAY,800,400,200,200,0);
        square.addSizeAnimator(100,100,2000,1000);
        square.addLinearPathAnimator(800,1600,2000,1000);
        square.addRotationAnimator((float)(Math.PI/4),2000,1000);
        final AnimatedGuiObjectCV.ObjectProperties squareProp = new AnimatedGuiObjectCV.ObjectProperties(square);
        view.addAnimatedGuiObject(square);
        // LayerDrawable
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_es);
        DrawableTextWithIconCV drw = new DrawableTextWithIconCV(this,"Hola", bitmap2, 300, 90, 120, 4);
        drw.setColor(Color.YELLOW);
        drw.setStrokewidthBorder(3);
        drw.setColorBorder(Color.BLACK);
        final AnimatedGuiObjectCV layerDrawable = new AnimatedGuiObjectCV<Drawable>("X", drw, 200, 1300, 0);
        layerDrawable.addLinearPathAnimator(600,1600,2000,0);
        // layerDrawable.addSizeAnimator(450,180,2000,1000);
        layerDrawable.addZoomAnimator(3,2000,1000);
        layerDrawable.addRotationAnimator((float)(Math.PI/4),2000,1000);
        final AnimatedGuiObjectCV.ObjectProperties layerDrawableProp = new AnimatedGuiObjectCV.ObjectProperties(layerDrawable);
        view.addAnimatedGuiObject(layerDrawable);
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        view.startAnimations();
        // after the animations, reset the objects to their initial states
        (new Thread() {
            public void run() {
                try {
                    sleep(3500);
                } catch (InterruptedException e) {}
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        square.setPropertiesAnimated(squareProp,3000,0);
                        oval.setPropertiesAnimated(ovalProp,3000,0);
                        circle.setPropertiesAnimated(circleProp,3000,0);
                        bitmap.setPropertiesAnimated(bitmapProp,3000,0);
                        rectangle.setPropertiesAnimated(rectangleProp,3000,0);
                        layerDrawable.setPropertiesAnimated(layerDrawableProp,3000,0);
                    }
                });
            }
        }).start();
    }

    public void demo3() {
        String colorNames[] = { "BLACK", "GREEN", "BLUE", "MAGENTA", "RED", "OLIVE", "ORANGE2" };
        final AnimationViewCV view = new AnimationViewCV(this);
        final AnimatedGuiObjectsGroupCV objectGroup = new AnimatedGuiObjectsGroupCV("ObjectGroup");
        for (int i=0;i<10;i++)
            objectGroup.add(new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE"+i, ColorsCV.getColorForName(colorNames[i%colorNames.length]),
                    200+((int)((GUIUtilitiesCV.getDisplayWidth()-200)* Math.random())),100+((int)((GUIUtilitiesCV.getDisplayHeight()-600)* Math.random())),
                    100,100));
        for (int i=0;i<10;i++)
            objectGroup.add(new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE"+i, ColorsCV.getColorForName(colorNames[i%colorNames.length]),
                    200+((int)((GUIUtilitiesCV.getDisplayWidth()-200)* Math.random())),100+((int)((GUIUtilitiesCV.getDisplayHeight()-600)* Math.random())),
                    100,100));
        view.addAnimatedGuiObjects(objectGroup);
        setContentView(view);
        objectGroup.addZoomAnimator(2,2000,500);
        final HashMap<String,AnimatedGuiObjectCV.ObjectProperties> propertiesHashMap = objectGroup.getObjectProperties();
        objectGroup.startAnimations();
        (new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {}
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        objectGroup.setObjectProperties(propertiesHashMap);
                        view.invalidate();
                    }
                });
            }
        }).start();
    }

    public void demo4() {
        String colorNames[] = { "BLACK", "GREEN", "BLUE", "MAGENTA", "RED", "OLIVE", "ORANGE2" };
        final AnimationViewCV view = new AnimationViewCV(this);
        final AnimatedGuiObjectsGroupCV objectGroup = new AnimatedGuiObjectsGroupCV("ObjectGroup");
        for (int i=0;i<10;i++)
            objectGroup.add(new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE"+i, ColorsCV.getColorForName(colorNames[i%colorNames.length]),
                    200+((int)((GUIUtilitiesCV.getDisplayWidth()-200)* Math.random())),100+((int)((GUIUtilitiesCV.getDisplayHeight()-600)* Math.random())),
                    100,100));
        view.addAnimatedGuiObjects(objectGroup);
        for (Iterator<AnimatedGuiObjectCV> it = objectGroup.iterator(); it.hasNext();)
            it.next().addSizeAnimator((int)(50+100* Math.random()),(int)(50+100* Math.random()),2000,500);
        setContentView(view);
        objectGroup.addPlaceOnCircleAnimator(GUIUtilitiesCV.getDisplayWidth()/2,GUIUtilitiesCV.getDisplayHeight()/2,300,2000,500);
        final HashMap<String,AnimatedGuiObjectCV.ObjectProperties> propertiesHashMap = objectGroup.getObjectProperties();
        objectGroup.startAnimations();
        (new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {}
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        objectGroup.setObjectPropertiesAnimated(propertiesHashMap,2000,500);
                    }
                });
            }
        }).start();
    }

}
