/* This work is provided under GPLv3, the GNU General Public License 3
   http://www.gnu.org/licenses/gpl-3.0.html */

/**
 * The app demonstrates the functionality and the usage of the
 * package de.thkoeln.cvogt.android.propanim_utilities
 * which provides utility classes for the Android property animation technique.
 * For details, please watch the introductory videos
 * and read the comments for the classes AnimatedGuiObjectCV and AnimationViewCV.

 Prof. Dr. Carsten Vogt
 Technische Hochschule Köln, Germany
 Fakultät für Informations-, Medien- und Elektrotechnik
 carsten.vogt@th-koeln.de
 3.5.2021

 */
package de.thkoeln.cvogt.android.propanimutil_demos;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_Animatorsequence extends Activity {

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

    private void demo1() {
        AnimatedGuiObjectCV square;
        AnimationViewCV view;
        // animationView to display the objects
        view = new AnimationViewCV(this);
        // square that traverses three linear paths in a zigzag fashion and simultaneously changes its size and color
        square = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE,"SQUARE", Color.BLACK,200,200,0);
        Animator animPath1 = square.addLinearPathAnimator(1000,200,2000,1000);
        Animator animSize1 = square.addSizeAnimator(400,400,2000,1000);
        Animator animPath2 = square.addLinearPathAnimator(200,1000,2000);
        Animator animColor = square.addColorAnimator(Color.RED,2000);
        Animator animSize2 = square.addSizeAnimator(200,200,1000,1000);
        Animator animPath3 = square.addLinearPathAnimator(1000,1000,2000);
        square.playSequentially(square.playTogether(animPath1,animSize1),
                square.playTogether(animPath2,animColor),
                square.playTogether(animSize2,animPath3));
        view.addAnimatedGuiObject(square);
        setContentView(view);
        // start the animation set
        view.startAnimations();
    }

    private void demo2() {
        AnimatedGuiObjectCV square;
        AnimationViewCV view;
        // animationView to display the objects
        view = new AnimationViewCV(this);
        // square that traverses three sequential paths (Bezier curve > circle arc > Bezier curve) and changes its size and color
        // DIESE FOLGE AUS BEZIER > KREISBOGEN > BEZIER RUCKELT AN DEN ÜBERGÄNGEN
        square = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE, "SQUARE", Color.BLACK, 100, 100, 0);
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        Animator animSize1 = square.addSizeAnimator(200, 200, 4000, 1000);
        Animator animBezier1 = square.addBezierPathAnimator(screenwidth-100,100, screenwidth-400, screenheight/2-100, 4000, 1000);
        Animator animColor1 = square.addColorAnimator(Color.RED, 4000, 1000);
        Animator animArc = square.addArcPathAnimator(screenwidth/2, screenheight/2, Math.PI, 2400);
        Animator animBezier2 = square.addBezierPathAnimator(100, 100, screenwidth-100, 100, 4000);
        Animator animSize2 = square.addSizeAnimator(0, 0, 4000);
        Animator animColor2 = square.addColorAnimator(Color.BLACK, 4000);
        square.playSequentially(square.playTogether(animSize1,animBezier1,animColor1),
                animArc,
                square.playTogether(animSize2,animBezier2,animColor2));
        view.addAnimatedGuiObject(square);
        setContentView(view);
        // start the animation set
        view.startAnimations();
    }

    private void demo3() {
        AnimationViewCV view;
        int demonr = 2;
        // animationView to display the objects
        view = new AnimationViewCV(this);
        if (demonr==1) {
            AnimatedGuiObjectCV square;
            // DIESE FOLGE VON LINEAREN PFADEN LÄUFT OHNE RUCKELN AN DEN ÜBERGÄNGEN DURCH
            square = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE, "SQUARE", Color.BLACK, 100, 100, 400);
            Animator anim1 = square.addLinearPathAnimator(100, 300, 1000, 1000);
            Animator anim2 = square.addLinearPathAnimator(100, 500, 1000);
            Animator anim3 = square.addLinearPathAnimator(100, 700, 1000);
            Animator anim4 = square.addLinearPathAnimator(100, 900, 1000);
            square.playSequentially(anim1, anim2, anim3, anim4);
            view.addAnimatedGuiObject(square);
        }
        if (demonr==2) {
            AnimatedGuiObjectCV circle;
            // DIESE FOLGE VON KREISBÖGEN RUCKELT AN DEN ÜBERGÄNGEN GANZ LEICHT
            // Place four grey circles in the background
            AnimatedGuiObjectCV kreisBackground1, kreisBackground2, kreisBackground3, kreisBackground4;
            int screenwidth = GUIUtilitiesCV.getDisplayWidth();
            int screenheight = GUIUtilitiesCV.getDisplayHeight();
            int margin = 50;
            int radiusBackground = (screenwidth-2*margin)/4;
            int centerX1 = margin+radiusBackground;
            int centerY1 = margin+radiusBackground;
            kreisBackground1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.LTGRAY,centerX1,centerY1,2*radiusBackground);
            view.addAnimatedGuiObject(kreisBackground1);
            int centerX2 = margin+3*radiusBackground;
            int centerY2 = margin+radiusBackground;
            kreisBackground2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.LTGRAY,centerX2,centerY2,2*radiusBackground);
            view.addAnimatedGuiObject(kreisBackground2);
            int centerX3 = margin+3*radiusBackground;
            int centerY3 = margin+3*radiusBackground;
            kreisBackground3 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.LTGRAY,centerX3,centerY3,2*radiusBackground);
            view.addAnimatedGuiObject(kreisBackground3);
            int centerX4 = margin+radiusBackground;
            int centerY4 = margin+3*radiusBackground;
            kreisBackground4 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE", Color.LTGRAY,centerX4,centerY4,2*radiusBackground);
            view.addAnimatedGuiObject(kreisBackground4);
            int radiusCircle = radiusBackground/4;
            circle = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE", Color.RED, margin+radiusBackground, margin+2*radiusBackground,2*radiusCircle);
            Animator anim1 = circle.addArcPathAnimator(centerX1,centerY1, -Math.PI / 2, 2000, 1000);
            Animator anim2 = circle.addArcPathAnimator(centerX2,centerY2, 3* Math.PI / 2, 6000);
            Animator anim3 = circle.addArcPathAnimator(centerX3,centerY3, -Math.PI / 2, 2000);
            Animator anim4 = circle.addArcPathAnimator(centerX4,centerY4, 3* Math.PI / 2, 6000);
            Animator anim5 = circle.addArcPathAnimator(centerX1,centerY1, -3* Math.PI / 2, 6000);
            circle.playSequentially(anim1,anim2,anim3,anim4,anim5);
            view.addAnimatedGuiObject(circle);
        }
        if (demonr==3) {
            AnimatedGuiObjectCV square;
            // DIESE FOLGE VON ZWEI KREISBÖGEN RUCKELT AM ÜBERGANG GANZ LEICHT
            square = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_SQUARE, "SQUARE", Color.BLACK, 50, 50, 200);
            Animator anim1 = square.addArcPathAnimator(50, 650, Math.PI / 2, 2000, 1000);
            Animator anim2 = square.addArcPathAnimator(1150, 650, -Math.PI / 2, 2000);
            square.playSequentially(anim1, anim2);
            view.addAnimatedGuiObject(square);
            view.addAnimatedGuiObject(square);
        }
        // display the animationView
        setContentView(view);
        // start the animation set
        view.startAnimations();
    }

}
