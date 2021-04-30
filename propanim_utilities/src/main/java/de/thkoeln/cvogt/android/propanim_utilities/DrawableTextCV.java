package de.thkoeln.cvogt.android.propanim_utilities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by vogt on 03.03.2018.
 */

/** This class can be used to generate Drawable objects that show texts.
 * Objects of this class can be passed as parameters to a constructor of the class AnimatedGuiObjectCV
 * (i.e. AnimatedGuiObjectCV(String name, Drawable drawable, ...)) and then animated.
 */

public class DrawableTextCV extends Drawable {

    /** Typeface to be used */

    private static final Typeface typeface = Typeface.create("arial", Typeface.NORMAL);

    /** Text to be displayed */

    private String text;

    /** Paint to be used */

    private final Paint paint;

    /**
     * Variable that tells if the text has descents (i.e. characters that go below the baseline).
     * If so, room will be left for these descents between base line of the text
     * and the bottom border of the enclosing AnimatedGuiObjectCV. Otherwise (e.g. for texts
     * containing only uppercase letter), the text baseline
     * will be the bottom border of the enclosing object.
     */
    
    private boolean hasDescents;

    /**
     * Variable to specify whether a fixed text or a text ticker shall be displayed
     */

    private boolean textIsTicker = true;

    /** Constructor
     *
     * @param text The text to be displayed
     * @param height The height of the displayed text (in px)
     */

    public DrawableTextCV(String text, int height) {
     this(text,-1,height);
    }

    /** Constructor
     *
     * @param text The text to be displayed
     * @param width The width of the displayed text (in px). The text will be scaled horizontally accordingly.
     * @param height The height of the displayed text (in px)
     */

    public DrawableTextCV(String text, int width, int height) {
       this(text,width,height,false);
    }

    /** Constructor
     *
     * @param text The text to be displayed
     * @param width The width of the displayed text (in px). The text will be scaled horizontally accordingly.
     * @param height The height of the displayed text (in px)
     * @param tickerText Whether a fixed text or a text ticker shall be displayed (YET TO BE IMPLEMENTED)
     */

    public DrawableTextCV(String text, int width, int height, boolean tickerText) {
        // Log.v("DEMO","DrawableTextCV 1: "+text+" "+(new Date()).getTime());
        this.text = text;
        this.hasDescents = GraphicsUtilsCV.hasDescents(text);
        this.textIsTicker = tickerText;
        // Log.v("DEMO","DrawableTextCV 2: "+text+" "+(new Date()).getTime());
        paint = new Paint();
        paint.setTypeface(typeface);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setFlags(Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(height);
        // Log.v("DEMO","DrawableTextCV 3: "+text+" "+(new Date()).getTime());
        setBounds(0,0,(int)paint.measureText(text),(int)paint.getTextSize());
        // Log.v("DEMO","DrawableTextCV 4: "+text+" "+(new Date()).getTime());
        if (width>0)
            paint.setTextScaleX(width/paint.measureText(text));
        else
            paint.setTextScaleX(1);
        // Log.v("DEMO","DrawableTextCV 5: "+text+" "+(new Date()).getTime());
   }

    int leftTransp = 0;

    public void draw(Canvas canvas) {
        if (!textIsTicker) {
            canvas.drawText(text, getBounds().left, getBounds().bottom-paint.descent(), paint);
            return;
        }
        canvas.clipRect(getBounds());
        canvas.drawText(text, getBounds().left-leftTransp, getBounds().bottom-paint.descent(), paint);
        leftTransp++;
    }

    public Paint getPaint() {
        return paint;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left,top,right,bottom);
        paint.setTextSize(getHeight());
        paint.setTextScaleX(1);
        float scale = getWidth()*1.0f/paint.measureText(text);
        if (Math.abs(1.0-scale)<0.01) scale = 1;
        paint.setTextScaleX(scale);
        // Log.v("DEMO","Left: "+getBounds().left+" Width: "+getWidth()+" Top: "+getBounds().top+" Height: "+getHeight());
    }

    public int getWidth() {
        return (int) paint.measureText(text);
        // return (getBounds().right-getBounds().left);
    }

    public int getHeight() {
        return (getBounds().bottom-getBounds().top);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setIsTextTicker(boolean textIsTicker) {
        this.textIsTicker = textIsTicker;
    }

}
