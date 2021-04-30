package de.thkoeln.cvogt.android.propanimutil_demos;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_DrawablesViews extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demo1();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.menu_2demos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menuItemDemo1)
            demo1();
        if (item.getItemId()==R.id.menuItemDemo2)
            demo2();
        return true;
    }

    public void demo1() {
        AnimatedGuiObjectCV<Drawable> rectangle, circle;
        AnimatedGuiObjectCV<View> buttonGUIObject1, buttonGUIObject2, buttonGUIObject3;
        AnimationViewCV view;
        // animationView to display the objects
        view = new AnimationViewCV(this);
        // three buttons
        Button button1 = new Button(this);
        button1.setText(" BUTTON 1");
        button1.setWidth(430);
        button1.setHeight(200);
        button1.setBackgroundColor(0x00FFFFFF);
        button1.setBackgroundResource(R.drawable.button_layout);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"CLICK1", Toast.LENGTH_LONG).show();
            }
        });
        buttonGUIObject1 = AnimatedGuiObjectCV.createFromView(button1,"BUTTON1",50,50);  // create gui object through the static create method
        buttonGUIObject1.addLinearPathAnimator(800,50,5000,1000);
        view.addAnimatedGuiObject(buttonGUIObject1);
        Button button2 = new Button(this);
        button2.setText(" BUTTON 2");
        button2.setWidth(430);
        button2.setHeight(200);
        button2.setBackgroundColor(0x00FFFFFF);
        button2.setBackgroundResource(R.drawable.button_layout);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"CLICK2", Toast.LENGTH_LONG).show();
            }
        });
        buttonGUIObject2 = new AnimatedGuiObjectCV<View>(button2,"BUTTON2",50,250);  // create gui object through the constructor (equivalent to static method)
        buttonGUIObject2.addLinearPathAnimator(100,1000,5000,1000);
        buttonGUIObject2.addRotationAnimator((float)(Math.PI),5000,1000);
        view.addAnimatedGuiObject(buttonGUIObject2);
        Button button3 = new Button(this);
        button3.setText(" BUTTON 3");
        button3.setWidth(600);
        button3.setHeight(200);
        button3.setBackgroundColor(0x00FFFFFF);
        button3.setBackgroundResource(R.drawable.button_layout);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"CLICK3", Toast.LENGTH_LONG).show();
            }
        });
        buttonGUIObject3 = new AnimatedGuiObjectCV<View>(button3,"BUTTON3",50,450);
        buttonGUIObject3.addBezierPathAnimator(1500,100,600,1200,5000,1000);
        // buttonGUIObject3.addSizeAnimator(50,500,5000,1000);  // The size animator does not work properly for View objects
        view.addAnimatedGuiObject(buttonGUIObject3);
        // rectangle moving along a straight line and changing its color
        rectangle = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,20,800,400,200);
        rectangle.addLinearPathAnimator(1000,600,5000,1000);
        rectangle.addColorAnimator(0xFF00FFFF,5000,1000);
        view.addAnimatedGuiObject(rectangle);
        // circle moving along a zigzag line
        circle = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.GREEN,20,1100,200);
        ArrayList<Point> al = new ArrayList<Point>();
        for (int i=0;i<16;i++)
            al.add(new Point(20+i*70,900+i%2*500));
        circle.addLinearSegmentsPathAnimator(al,5000,1000);
        view.addAnimatedGuiObject(circle);
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        view.startAnimations();
    }

    public void demo2() {
        final int numberOfButtons = 4;
        final int buttonWidth = 600;
        final int buttonHeight = 200;
        final int screenheight = GUIUtilitiesCV.getDisplayHeight();
        final int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        // animationView to display the objects
        AnimationViewCV view;
        view = new AnimationViewCV(this);
        // background
        final AnimatedGuiObjectCV<Drawable> background1 = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"BACKGROUND", Color.DKGRAY,screenwidth/2,screenheight/2,0,0);
        // background1.addLinearPathAnimator(0,0,3000,1000);
        background1.addSizeAnimator(screenwidth-100,screenheight-400,3000,1000);
        background1.addColorAnimator(Color.BLUE,3000,1000);
        view.addAnimatedGuiObject(background1);
        final AnimatedGuiObjectCV<Drawable> background2 = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"BACKGROUND", Color.DKGRAY,screenwidth/2,screenheight/2,0,0);
        // background2.addLinearPathAnimator(100,100,3000,1000);
        background2.addSizeAnimator(screenwidth-400,screenheight-800,2000,2000);
        background2.addColorAnimator(0xFF87CEFA,3000,1000);
        view.addAnimatedGuiObject(background2);
        background1.setZindex(1);
        background2.setZindex(2);
        // buttons
        final AnimatedGuiObjectCV<View> buttonGUIObjects[] = new AnimatedGuiObjectCV[numberOfButtons];
        final AnimatedGuiObjectCV<Drawable> endText = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT, DemoPropAnimUtils_DrawablesViews.this,"TEXT","> The End <",screenwidth/2,screenheight/2,300);
        endText.setSize(0,0);
        view.addAnimatedGuiObject(endText);
        for (int i=numberOfButtons-1;i>=0;i--) {
            Button button = new Button(this);
            button.setText("BUTTON "+i);
            button.setWidth(buttonWidth);
            button.setHeight(buttonHeight);
            button.setBackgroundColor(0x00FFFFFF);
            button.setBackgroundResource(R.drawable.button_layout);
            final int iConst = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonGUIObjects[iConst].clearAnimatorList();
                    buttonGUIObjects[iConst].addBezierPathAnimator(0,0,screenwidth+100,0,2000);
                    buttonGUIObjects[iConst].addRotationAnimator((float)(Math.PI/2),2000);
                    buttonGUIObjects[iConst].startAnimation();
                    buttonGUIObjects[iConst]=null;
                    boolean finished = true;
                    for (int i=0;i<numberOfButtons;i++)
                        finished = finished && buttonGUIObjects[i]==null;
                    if (finished) {
                        background2.clearAnimatorList();
                        background2.addLinearPathAnimator(screenwidth/2,screenheight/2,2000,2000);
                        background2.addSizeAnimator(0,0,2000,2000);
                        background2.addColorAnimator(Color.BLACK,2000,2000);
                        background2.startAnimation();
                        background1.clearAnimatorList();
                        background1.addLinearPathAnimator(screenwidth/2,screenheight/2,2000,3000);
                        background1.addSizeAnimator(0,0,2000,3000);
                        background1.addColorAnimator(Color.BLACK,2000,3000);
                        background1.startAnimation();
                        background1.addLinearPathAnimator(0,0,3000,1000);
                        background1.addSizeAnimator(screenwidth,screenheight,3000,1000);
                        background1.addColorAnimator(Color.BLUE,3000,1000);
                        endText.setSize(0,0);
                        endText.addSizeAnimator(400,200,2000,5000);
                        endText.startAnimation();
                    }
                }
            });
            buttonGUIObjects[i] = new AnimatedGuiObjectCV<View>(button, "BUTTON "+i, 150, 150);
            buttonGUIObjects[i].addBezierPathAnimator(screenwidth+400*i,400*i,-400*i,screenheight+300*i,screenwidth/2-150, 400+250*i, 3000+250*i, 1000+500*i);
            buttonGUIObjects[i].setZindex(3);
            view.addAnimatedGuiObject(buttonGUIObjects[i]);
        }
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        view.startAnimations();
    }

}
