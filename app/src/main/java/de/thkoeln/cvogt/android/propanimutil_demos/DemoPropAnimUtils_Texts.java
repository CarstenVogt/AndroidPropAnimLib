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
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_Texts extends Activity {

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

    private void demo1() {
        // animationView to display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        // objects to be displayed
        AnimatedGuiObjectCV objBitmapText1, objBitmapText2, objText1, background1, objText2, background2;
        // bitmap with text moving along a straight line and changing its size
        objBitmapText1 = AnimatedGuiObjectCV.createBitmapText(this,"BITMAP1","gtgtgt",200,300,150);
        int width = objBitmapText1.getWidth(), height = objBitmapText1.getHeight();
        objBitmapText1.addLinearPathAnimator(1050,300,5000,1000);
        objBitmapText1.addSizeAnimator(2*width,2*height,5000,1000);
        view.addAnimatedGuiObject(objBitmapText1);
        // text with descents and a background rectangle moving along a straight line and changing its size
        objText1 = AnimatedGuiObjectCV.createText("TEXT1","gtgtgt",200,650,150);
        objText1.addLinearPathAnimator(1050,650,5000,1000);
        width = objText1.getWidth();
        height = objText1.getHeight();
        objText1.addSizeAnimator(2*width,2*height,5000,1000);
        objText1.setZindex(2);
        view.addAnimatedGuiObject(objText1);
        background1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"BACKGROUND1",0xFFFFAAAA,200,650,width,height);
        background1.addSizeAnimator(2*width,2*height,5000,1000);
        background1.addLinearPathAnimator(1050,650,5000,1000);
        view.addAnimatedGuiObject(background1);
        // bitmap with text moving along a straight line and changing its size
        objBitmapText2 = AnimatedGuiObjectCV.createBitmapText(this,"BITMAP2","TEXT",200,1000,150);
        width = objBitmapText2.getWidth();
        height = objBitmapText2.getHeight();
        objBitmapText2.addLinearPathAnimator(1050,1000,5000,1000);
        objBitmapText2.addSizeAnimator(2*width,2*height,5000,1000);
        view.addAnimatedGuiObject(objBitmapText2);
        // text without descents and with a background rectangle moving along a straight line and changing its size and color
        objText2 = AnimatedGuiObjectCV.createText("TEXT2","TEXT",200,1350,150);
        width = objText2.getWidth();
        height = objText2.getHeight();
        objText2.addLinearPathAnimator(1050,1350,5000,1000);
        objText2.addSizeAnimator(2*width,2*height,5000,1000);
        objText2.setColor(Color.GREEN);
        objText2.addColorAnimator(Color.BLUE,5000,1000);
        objText2.setZindex(2);
        view.addAnimatedGuiObject(objText2);
        background2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"BACKGROUND2",0xFFFFAAAA,200,1350,width,height);
        background2.addSizeAnimator(2*width,2*height,5000,1000);
        background2.addLinearPathAnimator(1050,1350,5000,1000);
        view.addAnimatedGuiObject(background2);
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        view.startAnimations();
    }

    private void demo2() {
        // animationView to display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        // ticker text with a background rectangle
        AnimatedGuiObjectCV objText1 = AnimatedGuiObjectCV.createText("TEXT1","TICKER",400,650,150);
        objText1.setZindex(2);
        objText1.addLinearPathAnimator(400,650,10000);
        view.addAnimatedGuiObject(objText1);
        int width = objText1.getWidth();
        int height = objText1.getHeight();
        AnimatedGuiObjectCV background1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"BACKGROUND1",0xFFFFAAAA,400,650,width,height);
        background1.addLinearPathAnimator(400,650,10000);
        view.addAnimatedGuiObject(background1);
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        view.startAnimations();
    }

}
