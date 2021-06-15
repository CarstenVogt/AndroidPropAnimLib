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
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import de.thkoeln.cvogt.android.propanim_utilities.*;

// MIT ANIMIERTEM VIEW, DER IN EIN LINEAR LAYOUT EINGEBETTET IST > FUNKTIONIERT NOCH NICHT

public class DemoPropAnimUtils_LayoutEmbedding extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutforanimation);
        AnimationViewCV_TestForEmbedding view = findViewById(R.id.animlayout);
        AnimatedGuiObjectCV<Drawable> rectangle, circle;
        AnimatedGuiObjectCV<View> buttonGUIObject1, buttonGUIObject2, buttonGUIObject3;
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
        buttonGUIObject1 = new AnimatedGuiObjectCV<View>(button1,"BUTTON1",50,50);
        buttonGUIObject1.addLinearPathAnimator(800,100,null,5000,1000);
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
        buttonGUIObject2 = new AnimatedGuiObjectCV<View>(button2,"BUTTON2",50,250);
        buttonGUIObject2.addLinearPathAnimator(400,1000,5000,1000);
        buttonGUIObject2.addRotationAnimator((float) Math.PI,5000,1000);
        view.addAnimatedGuiObject(buttonGUIObject2);
        Button button3 = new Button(this);
        button3.setText(" BUTTON 3");
        button3.setWidth(430);
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
        buttonGUIObject3.addSizeAnimator(600,300,5000,1000);
        view.addAnimatedGuiObject(buttonGUIObject3);
        // start the animations of all objects registered with the animationView
        view.startAnimations();


    }
}
