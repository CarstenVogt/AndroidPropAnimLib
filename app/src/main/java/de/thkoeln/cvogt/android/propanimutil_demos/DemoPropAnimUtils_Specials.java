package de.thkoeln.cvogt.android.propanimutil_demos;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import de.thkoeln.cvogt.android.propanim_utilities.AnimatedGuiObjectCV;
import de.thkoeln.cvogt.android.propanim_utilities.AnimatedCounterCV;
import de.thkoeln.cvogt.android.propanim_utilities.AnimationViewCV;
import de.thkoeln.cvogt.android.propanim_utilities.GUIUtilitiesCV;

public class DemoPropAnimUtils_Specials extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demo1();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
/*
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.menu_4demos, menu);
*/
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
        AnimationViewCV view = new AnimationViewCV(this );
        setContentView(view);
        // Counter from 5 to 15
        int posX = GUIUtilitiesCV.getDisplayWidth()/5;
        int posY = GUIUtilitiesCV.getDisplayHeight()/4;
        final AnimatedCounterCV counter1 = new AnimatedCounterCV(this,"Counter1",5, 15, posX, posY, 100, 1000);
        // counter1.addLinearPathAnimator(posX,posY-200,10000);
        view.addAnimatedGuiObject(counter1);
        counter1.startAnimation();
        // Counter from 5 to 15, being stopped after 3 seconds
        posX = 2*GUIUtilitiesCV.getDisplayWidth()/5;
        posY = GUIUtilitiesCV.getDisplayHeight()/4;
        final AnimatedCounterCV counter2 = new AnimatedCounterCV(this,"Counter2",5, 15, posX, posY, 100, 1000);
        view.addAnimatedGuiObject(counter2);
        counter2.startAnimation();
        (new Thread() {
            public  void run() {
                try {
                    sleep(3000);
                    counter2.stop();
                } catch (InterruptedException exc) {}
            }
        }).start();
        AnimatedGuiObjectCV stopText = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_TEXT,this,"Stop","Stop",posX,posY+60,80);
        stopText.addAppearanceAnimator(3000);
        view.addAnimatedGuiObject(stopText);
        stopText.startAnimation();
        // Counter from 5 down to 0
        posX = 3*GUIUtilitiesCV.getDisplayWidth()/5;
        posY = GUIUtilitiesCV.getDisplayHeight()/4;
        final AnimatedCounterCV counter3 = AnimatedCounterCV.createCountDownCounter(this,"Counter3",5, posX, posY, 100, 2000);
        // counter3.addLinearPathAnimator(posX,posY+300,10000);
        view.addAnimatedGuiObject(counter3);
        counter3.startAnimation();
        // Counter from 5 to 15, value set to 11 after 3 seconds
        posX = 4*GUIUtilitiesCV.getDisplayWidth()/5;
        posY = GUIUtilitiesCV.getDisplayHeight()/4;
        final AnimatedCounterCV counter4 = new AnimatedCounterCV(this,"Counter2",5, 15, posX, posY, 100, 1000);
        view.addAnimatedGuiObject(counter4);
        counter4.startAnimation();
        (new Thread() {
            public  void run() {
                try {
                    sleep(3000);
                    counter4.setValue(11);
                } catch (InterruptedException exc) {}
            }
        }).start();
        AnimatedGuiObjectCV valueChangedText = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_TEXT,this,"ValChanged","val chgd",posX,posY+60,80);
        valueChangedText.addAppearanceAnimator(3500);
        view.addAnimatedGuiObject(valueChangedText);
        valueChangedText.startAnimation();
    }

    public void demo2() {

    }

    public void demo3() {

    }

    public void demo4() {

    }

}
