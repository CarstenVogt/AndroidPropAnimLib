package de.thkoeln.cvogt.android.propanimutil_demos;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_Trail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnimationViewCV view;
        view = new AnimationViewCV(this);
        int screenheight = GUIUtilitiesCV.getDisplayHeight();
        int screenwidth = GUIUtilitiesCV.getDisplayWidth();
        // Kreis 1
        AnimatedGuiObjectCV kreis1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE1", Color.BLUE,100,screenheight/2-500,90);
        kreis1.setZindex(2);
        kreis1.addBezierPathAnimator(screenwidth/4,-200,3*screenwidth/4,screenheight+200,screenwidth-100,300,4000,1000);
        kreis1.addPropertyChangedListener(new TrailHandler1());
        view.addAnimatedGuiObject(kreis1);
        // Kreis 2
        AnimatedGuiObjectCV kreis2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE2", Color.RED,100,screenheight/2-200,90);
        kreis2.setZindex(2);
        kreis2.addBezierPathAnimator(screenwidth/4,0,3*screenwidth/4,screenheight+400,screenwidth-100,600,4000,1000);
        kreis2.addPropertyChangedListener(new TrailHandler2());
        view.addAnimatedGuiObject(kreis2);
        // Display zeigen
        setContentView(view);
        view.startAnimations();
    }

    class TrailHandler1 implements AnimatedGuiObjectCV.OnPropertyChangedListener {
        @Override
        public void onPropertyChanged(AnimatedGuiObjectCV obj, String propertyName) {
            AnimatedGuiObjectCV<Drawable> dot = new AnimatedGuiObjectCV<Drawable>(
                    AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "DOT", Color.BLACK, obj.getCenterX(), obj.getCenterY(), 20);
            dot.addColorAnimator(Color.WHITE,5000);
            obj.getDisplayingView().addAnimatedGuiObject(dot);
            dot.startAnimation();
        }
    }

    class TrailHandler2 implements AnimatedGuiObjectCV.OnPropertyChangedListener {
        @Override
        public void onPropertyChanged(AnimatedGuiObjectCV obj, String propertyName) {
            AnimatedGuiObjectCV<Drawable> dot = new AnimatedGuiObjectCV<Drawable>(
                    AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "COPY", Color.RED, obj.getCenterX(), obj.getCenterY(), 90);
            dot.addColorAnimator(Color.WHITE,2000);
            obj.getDisplayingView().addAnimatedGuiObject(dot);
            dot.startAnimation();
        }
    }

}
