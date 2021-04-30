package de.thkoeln.cvogt.android.propanimutil_demos;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.PathInterpolator;
import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_TimeInterpolators extends Activity {

    private AnimatedGuiObjectCV rect1, rect2, rect3, rect4, rect5, rect6, rect7, rect8, rect9;
    AnimationViewCV view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // animationView to display the objects
        view = new AnimationViewCV(this);
        // all rectangles are moving from left to right along a straight line
        int startX = 200, startY = 170, targetX = 1100, width = 300, height = 100, duration = 5000;
        // acceleration:
        rect1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect1.addLinearPathAnimator(targetX,startY,new AccelerateInterpolator(),duration,1000);  // this animation and all others start with one second delay to allow the animationView to appear on the screen before
        view.addAnimatedGuiObject(rect1);
        startY += 3*height/2;
        // deceleration:
        rect2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect2.addLinearPathAnimator(targetX,startY,new DecelerateInterpolator(),duration,1000);
        view.addAnimatedGuiObject(rect2);
        startY += 3*height/2;
        // acceleration / deceleration:
        rect3 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect3.addLinearPathAnimator(targetX,startY,new AccelerateDecelerateInterpolator(),duration,1000);
        view.addAnimatedGuiObject(rect3);
        startY += 3*height/2;
        // anticipate:
        rect4 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect4.addLinearPathAnimator(targetX,startY,new AnticipateInterpolator(5),duration,1000);
        view.addAnimatedGuiObject(rect4);
        startY += 3*height/2;
        // overshoot:
        rect5 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect5.addLinearPathAnimator(targetX,startY,new OvershootInterpolator(5),duration,1000);
        view.addAnimatedGuiObject(rect5);
        startY += 3*height/2;
        // anticipate overshoot:
        rect6 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect6.addLinearPathAnimator(targetX,startY,new AnticipateOvershootInterpolator(5),duration,1000);
        view.addAnimatedGuiObject(rect6);
        startY += 3*height/2;
        // bounce:
        rect7 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect7.addLinearPathAnimator(targetX,startY,new BounceInterpolator(),duration,1000);
        view.addAnimatedGuiObject(rect7);
        startY += 3*height/2;
        // cycle:
        rect8 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect8.addLinearPathAnimator(targetX,startY,new CycleInterpolator(2),duration,1000);
        view.addAnimatedGuiObject(rect8);
        startY += 3*height/2;
        // path:
        rect9 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT", Color.BLUE,startX,startY,width,height);
        rect9.addLinearPathAnimator(targetX,startY,new PathInterpolator(0.95f,0.5f),duration,1000);
        view.addAnimatedGuiObject(rect9);
        startY += 3*height/2;
        // show the animationView
        setContentView(view);
        // start the animations of all objects registered with the animationView
        view.startAnimations();
    }

}
