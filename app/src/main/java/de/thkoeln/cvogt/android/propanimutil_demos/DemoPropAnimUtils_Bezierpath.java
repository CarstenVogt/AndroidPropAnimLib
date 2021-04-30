package de.thkoeln.cvogt.android.propanimutil_demos;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_Bezierpath extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demo1();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.menu_3demos, menu);
        // mi.inflate(R.menu.menu_4demos, menu);
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
        // if (item.getItemId()==R.id.menuItemDemo4)
        //    demo4();
        return true;
    }

    private void demo1() {
        String colorNames[] = { "BLACK", "GREEN", "BLUE", "MAGENTA", "RED", "OLIVE", "ORANGE2" };
        AnimationViewCV view;
        view = new AnimationViewCV(this);
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        AnimatedGuiObjectCV kreise[] = new AnimatedGuiObjectCV[7];
        for (int i=0;i<kreise.length;i++) {
            kreise[i] = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE",ColorsCV.getColorForName(colorNames[i]),100,screenheight/2-300,90);
            if (i%2==0)
                kreise[i].addBezierPathAnimator(screenwidth/4+i*100,-i*250,screenwidth-(i+1)*120,screenheight/2-300,4000,1000);  // the animation starts with one second delay to allow the animationView to appear on the screen before
            else
                kreise[i].addBezierPathAnimator(screenwidth/4+i*100,-i*150,screenwidth/4+i*300,screenheight+i*300,screenwidth-(i+1)*120,screenheight/2-300,4000,1000);  // the animation starts with one second delay to allow the animationView to appear on the screen before
            view.addAnimatedGuiObject(kreise[i]);
        }
        setContentView(view);
        view.startAnimations();
        /*
        AnimationViewCV animationView;
        animationView = new AnimationViewCV(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenheight = displayMetrics.heightPixels;
        int screenwidth = displayMetrics.widthPixels;
        AnimatedGuiObjectCV kreis0, kreis1, kreis2, kreis3, kreis4;
        kreis0 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE",0xFFFF8C44,10,screenheight/2-300,100);
        kreis0.addBezierPathAnimator(screenwidth/4,-500,screenwidth-800,screenheight/2-400,4000,1000);  // this animation and all others start with one second delay to allow the animationView to appear on the screen before
        animationView.addAnimatedGuiObject(kreis0);
        kreis1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.RED,10,screenheight/2-300,100);
        kreis1.addBezierPathAnimator(screenwidth/2,10,screenwidth-650,screenheight/2-400,3000,1000);
        animationView.addAnimatedGuiObject(kreis1);
        kreis2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.BLUE,10,screenheight/2-300,100);
        kreis2.addBezierPathAnimator(3*screenwidth/4,screenheight-100,screenwidth-500,screenheight/2-400,3000,1000);
        animationView.addAnimatedGuiObject(kreis2);
        kreis3 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.GREEN,10,screenheight/2-300,100);
        kreis3.addBezierPathAnimator(5*screenwidth/4,screenheight+200,screenwidth-350,screenheight/2-400,4000,1000);
        animationView.addAnimatedGuiObject(kreis3);
        kreis4 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.BLACK,10,screenheight/2-300,100);
        kreis4.addBezierPathAnimator(screenwidth-100,screenheight-200,3*(screenwidth-200)/4,-1000,screenwidth-200,screenheight/2-400,4000,1000);
        animationView.addAnimatedGuiObject(kreis4);
        setContentView(animationView);
        animationView.startAnimations();
        */
    }

    private void demo2() {
        AnimationViewCV view;
        view = new AnimationViewCV(this);
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int[] colors = ColorsCV.getColorValues();
        for (int i=0;i<colors.length;i++) {
            int diameter = 120;
            int color = colors[i];
            if (color==0xFFFFFFFF) continue;
            AnimatedGuiObjectCV animatedObject = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE",color,50+diameter/2,screenheight/2-300,diameter);
            int startDelay = 1000+i*300;
            int duration = 2000;
            int control1X = 400;
            int control1Y = -700;
            int control2X = 1200;
            int control2Y = 600;
            int targetX = screenwidth-100-diameter;
            int targetY = screenheight-350-diameter;
            animatedObject.addBezierPathAnimator(control1X,control1Y,control2X,control2Y,targetX,targetY,duration,startDelay);
            view.addAnimatedGuiObject(animatedObject);
        }
        setContentView(view);
        view.startAnimations();
    }

    private void demo3() {
        final int lines = 19;
        final int columns = 19;
        AnimationViewCV view;
        view = new AnimationViewCV(this);
        view.setBackgroundColor(0xFF000000);
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int targetColumn = screenwidth/4;
        int stepwidth = (screenwidth-targetColumn) / columns;
        int diameter = stepwidth-10;
        AnimatedGuiObjectCV kreise[][] = new AnimatedGuiObjectCV[lines][columns];
        for (int y=lines-1;y>=0;y--)
            for (int x=0;x<columns;x++) {
                int color = Color.WHITE;
                if (x==y||x==lines-(y+1)) color = Color.RED;
                kreise[y][x] = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE",color,10+diameter/2,screenheight/2-300,diameter);
                int startDelay = 1000+5*((lines-(y+1))*columns+x);
                if (x==y) startDelay += 2000;
                if (x==lines-(y+1)) startDelay += 3000;
                kreise[y][x].addBezierPathAnimator(screenwidth/2+x*10,-100-y*100,targetColumn+x*stepwidth,screenheight-600-(lines-(y+1))*stepwidth,4000,startDelay);
                view.addAnimatedGuiObject(kreise[y][x]);
            }
        setContentView(view);
        view.startAnimations();
    }
/*
    private void demo4() {
        final int lines = 9;
        final int columns = 9;
        int colorbitmap[][] = new int[lines][columns];
        // Smiley
        int smileyRadius = 7*columns/16;
        int leftEyeCenterX = 5*columns/16;
        int rightEyeCenterX = 11*columns/16;
        int eyeCenterY = 3*lines/8;
        int eyeRadius = lines/10;
        for (int y=0;y<lines;y++)
            for (int x=0;x<columns;x++) {
                colorbitmap[y][x] = Color.WHITE;
                if (Math.sqrt((x-columns/2)*(x-columns/2)+(y-lines/2)*(y-lines/2))<smileyRadius) colorbitmap[y][x] = Color.YELLOW;
                if (Math.sqrt((x-leftEyeCenterX)*(x-leftEyeCenterX)+(y-eyeCenterY)*(y-eyeCenterY))<eyeRadius) colorbitmap[y][x] = Color.BLACK;
                if (Math.sqrt((x-rightEyeCenterX)*(x-rightEyeCenterX)+(y-eyeCenterY)*(y-eyeCenterY))<eyeRadius) colorbitmap[y][x] = Color.BLACK;
            }
        AnimationViewCV view;
        view = new AnimationViewCV(this);
        view.setBackgroundColor(0xFF000000);
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int targetStartX = screenwidth/4;
        int targetStartY = targetStartX+300;
        int stepwidth = (screenwidth-targetStartX) / columns;
        if ((screenheight-targetStartY)/lines<stepwidth)
            stepwidth = (screenheight-targetStartY)/lines;
        AnimatedGuiObjectCV pixel[][] = new AnimatedGuiObjectCV[lines][columns];
        for (int y=0;y<lines;y++)
            for (int x=0;x<columns;x++) {
                int color = colorbitmap[y][x];
                pixel[y][x] = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE,"SQUARE",color,10+stepwidth/2,screenheight/2-300,stepwidth);
                int startDelay = 1000+(5000*((lines-(y+1))*columns+x))/(lines*columns);
                // int startDelay = 1000;
                pixel[y][x].addBezierPathAnimator(screenwidth/2+x*10,-100-y*100,targetStartX+x*stepwidth,targetStartY+y*stepwidth,4000,startDelay);
                view.addAnimatedGuiObject(pixel[y][x]);
            }
        setContentView(view);
        view.startAnimations();
    }
*/

    private int randomInt(int min, int max) {
        return min + (int)(Math.random()*(max+1-min));
    }

}
