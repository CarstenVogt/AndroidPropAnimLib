package de.thkoeln.cvogt.android.propanim_utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Iterator;

// TESTVERSION FÜR ANIMIERTE VIEWS, DIE IN ANDERE LAYOUTS EINGEBETTET WERDEN KÖNNEN > FUNKTIONIERT NOCH NICHT

/**
 * Created by vogt on 5.1.2018.
 */

/** ViewGroup class to display animated objects of class AnimatedGuiObjectCV (i.e. animated Drawable and View objects) with.<BR>
 * For more comments on how to proceed, see the documentation of the class AnimatedGuiObjectCV.<BR><BR>
 * <BR><BR>
 * Author: Prof. Dr. Carsten Vogt, Technische Hochschule Koeln / University of Applied Sciences Cologne, Germany, carsten.vogt@th-koeln.de
 * Version: 1.0
 */

public class AnimationViewCV_TestForEmbedding extends RelativeLayout {

    /** Objects to be displayed (embedded objects of class Drawable or View). */

    private ArrayList<AnimatedGuiObjectCV> guiObjects;

    private Context context;

    /** Constructor: Generates an empty list for 'guiObjects'. */

    public AnimationViewCV_TestForEmbedding(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        guiObjects = new ArrayList<AnimatedGuiObjectCV>();
    }

    /** Adds an animated object to 'guiObjects'.
     *  If the embedded object is of class View, it is also added to the layout of this displaying view
     *  such that it will be drawn automatically to the screen. */

    public void addAnimatedGuiObject(AnimatedGuiObjectCV animatedGuiObject) {
        // animatedGuiObject.setDisplayingView((AnimationViewCV) this);
        guiObjects.add(animatedGuiObject);
        if (animatedGuiObject.getType()== AnimatedGuiObjectCV.TYPE_VIEW)
            super.addView((View)animatedGuiObject.getGuiObject());
    }

    /** Start the animations of all objects in 'guiObjects' immediately. */

    public void startAnimations() {
        for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext();)
            it.next().startAnimation();
    }

    /** Is called automatically by the runtime system.
     * Calls in turn the draw() methods of all objects in 'guiObjects' that contain Drawable objects.
     * All embedded View objects are drawn automatically (see comment for method addAnimatedGuiObject()). */

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       for (Iterator<AnimatedGuiObjectCV> it = guiObjects.iterator(); it.hasNext();) {
           AnimatedGuiObjectCV guiobj = it.next();
           if (guiobj.getType()!= AnimatedGuiObjectCV.TYPE_VIEW)
             guiobj.draw(canvas);
       }
    }

    // Callback-Methode, die vom Laufzeitsystem aufgerufen wird, wenn sich die Größe der Darstellung geändert hat.
    // Hier könnten bei Bedarf gespeicherte Variablenwerte geändert werden.

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    // onMeasure() spezifiziert, wie groß der View gezeichnet werden soll, wenn der Programmierer
    // seine Größe mit match_parent, wrap_content oder festen Werten festgelegt hat.
    // Die Methode wird vom Laufzeitsystem aufgerufen, wenn es ein Layout auf den Bildschirm bringen möchte
    // und dafür die Größen der darzustellenden Views berechnen muss.
    //
    // (Quelle des folgenden Codes und nähere Erklärung: http://stackoverflow.com/questions/12266899/onmeasure-custom-view-explanation)

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int desiredHeight = displayMetrics.heightPixels;
        int desiredWidth = displayMetrics.widthPixels;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);

    }

}
