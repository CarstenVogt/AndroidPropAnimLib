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
 15.6.2021

 */

package de.thkoeln.cvogt.android.propanim_utilities;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.Date;

/** Class for counters that count from a specified start value to a specified end value.
*/

public class AnimatedCounterCV extends AnimatedGuiObjectCV {

    private Context context;
    private int startValue, endValue, currentValue;
    private int countIntervalLength, duration;
    private ObjectAnimator animator;
    private boolean counterStopped=false;

    /**
     * Constructor
     * @param context  Context
     * @param name  Name of the object
     * @param startValue  Start value of the counter
     * @param endValue  End value of the counter
     * @param centerX  X coordinate of the center of the counter
     * @param centerY  Y coordinate of the center of the counter
     * @param textHeight  Height of the counter text (in pixels)
     * @param countIntervalLength  Length of time after which the counter is switched to the next vakue (in ms)
     */

    public AnimatedCounterCV(Context context, String name, int startValue, int endValue, int centerX, int centerY, int textHeight, int countIntervalLength) {
        super(AnimatedGuiObjectCV.TYPE_DRAWABLE_TEXT,context,name,startValue+"",centerX,centerY,textHeight);
        this.context = context;
        this.startValue = startValue;
        this.endValue = endValue;
        this.countIntervalLength = countIntervalLength;
        this.duration = countIntervalLength*Math.abs(endValue-startValue);
        animator = ObjectAnimator.ofInt(this, "valueForAnimator", startValue, endValue);
        animator.setEvaluator(new CountEvaluator());
        animator.setDuration(duration);
        addAnimator(animator);
    }

    /**
     * Setter method to set the counter value.
     * @param value  The counter value to be set. Must lie between the start value and the end value, no effect if not.
     */

    public void setValue(final int value) {
        if (startValue<endValue && (value<startValue||value>endValue)) return;
        if (startValue>endValue && (value>startValue||value<endValue)) return;
        if (value==currentValue) return;
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startValue=value;
                removeAnimator(animator);
                animator = ObjectAnimator.ofInt(AnimatedCounterCV.this, "valueForAnimator", startValue, endValue);
                duration = Math.abs(startValue-endValue)*countIntervalLength;
                animator.setEvaluator(new CountEvaluator());
                animator.setDuration(duration);
                addAnimator(animator);
                startAnimation();
            }
        });
        // duration = countIntervalLength*Math.abs(endValue-startValue);
        // animator.setDuration(duration);
    }

    /**
     * Setter method to set the counter value (to be called only by the counter animator).
     * @param value  The counter value to be set
     */

    public void setValueForAnimator(int value) {
        if (value==currentValue) return;
        currentValue = value;
        int centerX = getCenterX();
        int centerY = getCenterY();
        this.guiObject = new DrawableTextCV(value+"",getWidth(),getHeight());
        setCenterX(centerX);
        setCenterY(centerY);
    }

    /**
     * Getter method for the current counter value
     */

    public int getValue() {
        return currentValue;
    }

    /**
     * Method to stop the counter
     */

    public void stop() {
        counterStopped = true;
    }

    /**
     * Evaluator to calculate the current counter value
     */

    private class CountEvaluator implements TypeEvaluator<Integer> {

        boolean started;
        long startTime;

        public CountEvaluator() {

        }

        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            if (!started) {
                startTime = (new Date()).getTime();
                started = true;
            }
            if (!counterStopped) {
                long thisTime = (new Date()).getTime();
                // int newValue = (int)Math.floor(startValue*(0.9999-fraction)+1);
                // Using the fraction parameter, as in the above formula, has shown to be less precise
                // than using the difference between the current time and the start time, as in the formula below
                // return startValue + (int) Math.floor(0.999 * (endValue - startValue) * ((float) (thisTime - startTime) / duration) + 1);
                return startValue + (int) (((endValue - startValue) * (thisTime - startTime)) / duration);
            }
            else return currentValue;
        }

    }

    /**
     * Factory method to create a counter that counts from some start value down to zero.
     * @param context  Context
     * @param name  Name of the object
     * @param startValue  Start value of the counter (must be greater than zero)
     * @param centerX  X coordinate of the center of the counter
     * @param centerY  Y coordinate of the center of the counter
     * @param textHeight  Height of the counter text (in pixels)
     * @param countIntervalLength  Length of time after which the counter is switched to the next vakue (in ms)
     * @return  Reference to the created counter object (or null if the startValue parameter is not greater than zero)
     */

    public static AnimatedCounterCV createCountDownCounter(Context context, String name, int startValue, int centerX, int centerY, int textHeight, int countIntervalLength) {
        if (startValue<=0) return null;
        return new AnimatedCounterCV(context,name,startValue,0,centerX,centerY,textHeight,countIntervalLength);
    }

}

/*

    private int startValue, currValue;

    private int logarray[];
    private long lastTime = 0, startTime;
    private int oldvalue;
    private float oldfraction;

    class MyTextEvaluator implements TypeEvaluator<DrawableTextCV> {

        @Override
        public DrawableTextCV evaluate(float fraction, DrawableTextCV startObj, DrawableTextCV endObj) {
            // Log.v("DEMO",(startValue*(.99-fraction)+1)+"");
            int newValue = (int)Math.floor(startValue*(0.9999-fraction)+1);

            if (newValue!=oldvalue) {
                long thisTime = (new Date()).getTime();
                Log.v("DEMO",(thisTime-lastTime)+"");
                lastTime=thisTime;
                oldvalue=newValue;
            }

// logarray[newValue]++;
// Log.v("DEMO",logarray[0]+" "+logarray[1]+" "+logarray[2]+" "+logarray[3]+" "+logarray[4]+" "+logarray[5]+" "+logarray[6]+" "+logarray[7]+" "+logarray[8]+" "+logarray[9]+" "+logarray[10]);
long thisTime = (new Date()).getTime();
// Log.v("DEMO",fraction+"  "+newValue+"  "+thisTime+"  "+(thisTime-lastTime));
// Log.v("DEMO",(fraction-oldfraction)/(thisTime-lastTime)+"");
            Log.v("DEMO",(thisTime-startTime)+"   "+fraction+"  "+(thisTime-startTime)/4000.0+"  "+(fraction-(thisTime-startTime)/4000.0));
                    lastTime=thisTime;
                    oldfraction=fraction;
                    String text = newValue+"";
                    return new DrawableTextCV(text,getWidth(),getHeight());
                    }

                    }


                    */