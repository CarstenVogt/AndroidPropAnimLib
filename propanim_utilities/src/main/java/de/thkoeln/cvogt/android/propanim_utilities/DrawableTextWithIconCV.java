package de.thkoeln.cvogt.android.propanim_utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

/**
 * Created by vogt on 11.02.2018.
 */

/** This class can be used to generate Drawable objects that show texts on their left and icons on their right,
 * surrounded by a rectangle with rounded corners. Objects of this class can be passed as parameters to
 * a constructor of the class AnimatedGuiObjectCV (i.e. AnimatedGuiObjectCV(String name, Drawable drawable, ...))
 * and then animated.
 */

public class DrawableTextWithIconCV extends LayerDrawable {

    /** The layer indices of the components */

    private int borderIndex, textIndex, iconIndex;

    /** Objects for the text and the icon to be shown */

    private DrawableTextCV textDrawable;
    // private BitmapDrawable textDrawable;   // alternative: Bitmap for the text > requires much more space
    private BitmapDrawable iconDrawable;

    /** Background color of the Drawable */

    private int colorBack;

    /** Border color of the Drawable */

    private int colorBorder;

    /** Strokewidth of the border */

    private int strokewidthBorder;

    /** Values for layout calculation */

    double relativeIconWidth;   // The width occupied by the icon relative to the total width
    final double relativeOffsetCornerSize = 1.0/8; // The height / width of the internal offsets and the radius of the corners relative to the total height
    private int initWidth;  // The initial width of the drawable
    private int textOffsetFromTop; // The offset of the text part from the top (depends on whether the text has ascents and/or descents

    final int textScaleFactor = 4;  // Scale factor used when creating the drawable (or the bitmap) for the text in order to provide a better resolution

    /** Constructor
     *
     * @param context The context in which the Drawable shall be displayed. This parameter may be null which, however, will call a deprecated version of the BitmapDrawable constructor and therefore might cause problems.
     * @param text The text to be displayed
     * @param icon The icon to be displayed
     * @param minimumWidth The minimum width of the whole Drawable (will be made wider if text and icon required more space)
     * @param height The height of the whole Drawable
     * @param iconWidth The width of the icon within the Drawable
     * @param strokewidthBorder The width of the surrounding border line (pixels)
     */

    public DrawableTextWithIconCV(Context context, String text, Bitmap icon, int minimumWidth, int height, int iconWidth, int strokewidthBorder) {
        this(context,text,icon,minimumWidth,height,iconWidth,strokewidthBorder, Color.WHITE, Color.BLACK);
    }

    /** Constructor
     *
     * @param context The context in which the Drawable shall be displayed. This parameter may be null which, however, will call a deprecated version of the BitmapDrawable constructor and therefore might cause problems.
     * @param text The text to be displayed
     * @param icon The icon to be displayed
     * @param minimumWidth The minimum width of the whole Drawable (will be made wider if ttext and icon required more space)
     * @param height The height of the whole Drawable
     * @param iconWidth The width of the icon within the Drawable
     * @param strokewidthBorder The width of the surrounding border line (pixels)
     * @param colorBack The background color
     * @param colorBorder The color of the surrounding border line
     */

    public DrawableTextWithIconCV(Context context, String text, Bitmap icon, int minimumWidth, int height, int iconWidth, int strokewidthBorder, int colorBack, int colorBorder) {
        super(new Drawable[0]);
        // Log.v("DEMO","DrawableTextWithIconCV 1: "+(new Date()).getTime());
        int offset = (int) (relativeOffsetCornerSize*height);
        if (GraphicsUtilsCV.hasAscents(text) && !GraphicsUtilsCV.hasDescents(text))
            textOffsetFromTop=(int)(1.5*offset);
        else textOffsetFromTop=offset;
        // generate the text drawable
        // Log.v("DEMO","DrawableTextWithIconCV 2: "+(new Date()).getTime());
        textDrawable = new DrawableTextCV(text,textScaleFactor*height-2*textScaleFactor*offset);
        textDrawable.setBounds(0,0,textDrawable.getBounds().right/textScaleFactor,textDrawable.getBounds().bottom/textScaleFactor);
        /* alternative version: Text as Bitmap > requires much more space
        Bitmap bitmap = GraphicsUtilsCV.textToBitmap(text,textBmScale*height-2*textBmScale*offset,Color.BLACK,Color.WHITE);  // make the bitmap bigger than required to get a better pixel resolution
        bitmap = GraphicsUtilsCV.makeBitmapTransparent(bitmap,Color.WHITE);
        textDrawable = new BitmapDrawable(context.getResources(),bitmap);
        */
        // set the width (either minimumWidth or the actual width required for textbitmap, icon and offsets
        int requiredWidth = // bitmap.getWidth()/textBmScale+iconWidth+4*offset;
                textDrawable.getWidth()+iconWidth+4*offset;
        initWidth = minimumWidth;
        if (initWidth<requiredWidth) initWidth=requiredWidth;
        // initialize the layout values and the colors
        this.relativeIconWidth = (double)iconWidth/initWidth;
        this.colorBack = colorBack;
        this.colorBorder = colorBorder;
        this.strokewidthBorder = strokewidthBorder;
        // Log.v("DEMO","DrawableTextWithIconCV 3: "+(new Date()).getTime());
        // generate the border
        GradientDrawable border = new GradientDrawable();
        border.setColor(this.colorBack);
        border.setCornerRadius(offset);
        border.setStroke(this.strokewidthBorder,this.colorBorder);
        border.setBounds(0,0,initWidth,height);
        // generate the icon
        // Log.v("DEMO","DrawableTextWithIconCV 4: "+(new Date()).getTime());
        if (context!=null)
            iconDrawable = new BitmapDrawable(context.getResources(),icon);
        else
            iconDrawable = new BitmapDrawable(icon);
        // Log.v("DEMO","DrawableTextWithIconCV 5: "+(new Date()).getTime());
        // add the three objects as layers to the (Layer)Drawable
        borderIndex = addLayer(border);
        setLayerSize(borderIndex,initWidth,height);
        textIndex = addLayer(textDrawable);
//        setLayerSize(textIndex,textDrawable.getIntrinsicWidth()/textScaleFactor,height-2*offset);
        setLayerSize(textIndex,textDrawable.getWidth()/textScaleFactor,height-2*offset);
        setLayerInset(textIndex,2*offset,textOffsetFromTop,iconWidth-2*offset,offset);
        iconIndex = addLayer(iconDrawable);
        setLayerSize(iconIndex,iconWidth-3*offset,height-2*offset);
        setLayerInset(iconIndex,initWidth-iconWidth+offset,offset,2*offset,offset);
        super.setBounds(0,0,initWidth,height);
        // Log.v("DEMO","DrawableTextWithIconCV 6: "+(new Date()).getTime());
    }

    /** Constructor
     *
     * @param text The text to be displayed
     * @param iconDrawable The icon to be displayed
     * @param minimumWidth The minimum width of the whole Drawable (will be made wider if text and icon required more space)
     * @param height The height of the whole Drawable
     * @param iconWidth The width of the icon within the Drawable
     * @param strokewidthBorder The width of the surrounding border line (pixels)
     */

    public DrawableTextWithIconCV(String text, BitmapDrawable iconDrawable, int minimumWidth, int height, int iconWidth, int strokewidthBorder) {
        this(text,iconDrawable,minimumWidth,height,iconWidth,strokewidthBorder, Color.WHITE, Color.BLACK);
    }

    /** Constructor
     *
     * @param text The text to be displayed
     * @param iconDrawable The icon to be displayed
     * @param minimumWidth The minimum width of the whole Drawable (will be made wider if ttext and icon required more space)
     * @param height The height of the whole Drawable
     * @param iconWidth The width of the icon within the Drawable
     * @param strokewidthBorder The width of the surrounding border line (pixels)
     * @param colorBack The background color
     * @param colorBorder The color of the surrounding border line
     */

    public DrawableTextWithIconCV(String text, BitmapDrawable iconDrawable, int minimumWidth, int height, int iconWidth, int strokewidthBorder, int colorBack, int colorBorder) {
        super(new Drawable[0]);
        // Log.v("DEMO","DrawableTextWithIconCV 1: "+(new Date()).getTime());
        int offset = (int) (relativeOffsetCornerSize*height);
        if (GraphicsUtilsCV.hasAscents(text) && !GraphicsUtilsCV.hasDescents(text))
            textOffsetFromTop=(int)(1.5*offset);
        else textOffsetFromTop=offset;
        // generate the text drawable
        // Log.v("DEMO","DrawableTextWithIconCV 2: "+(new Date()).getTime());
        textDrawable = new DrawableTextCV(text,textScaleFactor*height-2*textScaleFactor*offset);
        textDrawable.setBounds(0,0,textDrawable.getBounds().right/textScaleFactor,textDrawable.getBounds().bottom/textScaleFactor);
        /* alternative version: Text as Bitmap > requires much more space
        Bitmap bitmap = GraphicsUtilsCV.textToBitmap(text,textBmScale*height-2*textBmScale*offset,Color.BLACK,Color.WHITE);  // make the bitmap bigger than required to get a better pixel resolution
        bitmap = GraphicsUtilsCV.makeBitmapTransparent(bitmap,Color.WHITE);
        textDrawable = new BitmapDrawable(context.getResources(),bitmap);
        */
        // set the width (either minimumWidth or the actual width required for textbitmap, icon and offsets
        int requiredWidth = // bitmap.getWidth()/textBmScale+iconWidth+4*offset;
                textDrawable.getWidth()+iconWidth+4*offset;
        initWidth = minimumWidth;
        if (initWidth<requiredWidth) initWidth=requiredWidth;
        // initialize the layout values and the colors
        this.relativeIconWidth = (double)iconWidth/initWidth;
        this.colorBack = colorBack;
        this.colorBorder = colorBorder;
        this.strokewidthBorder = strokewidthBorder;
        // Log.v("DEMO","DrawableTextWithIconCV 3: "+(new Date()).getTime());
        // generate the border
        GradientDrawable border = new GradientDrawable();
        border.setColor(this.colorBack);
        border.setCornerRadius(offset);
        border.setStroke(this.strokewidthBorder,this.colorBorder);
        border.setBounds(0,0,initWidth,height);
        // generate the icon
        // Log.v("DEMO","DrawableTextWithIconCV 4: "+(new Date()).getTime());
        this.iconDrawable = iconDrawable;
        // Log.v("DEMO","DrawableTextWithIconCV 5: "+(new Date()).getTime());
        // add the three objects as layers to the (Layer)Drawable
        borderIndex = addLayer(border);
        setLayerSize(borderIndex,initWidth,height);
        textIndex = addLayer(textDrawable);
//        setLayerSize(textIndex,textDrawable.getIntrinsicWidth()/textScaleFactor,height-2*offset);
        setLayerSize(textIndex,textDrawable.getWidth()/textScaleFactor,height-2*offset);
        setLayerInset(textIndex,2*offset,textOffsetFromTop,iconWidth-2*offset,offset);
        iconIndex = addLayer(iconDrawable);
        setLayerSize(iconIndex,iconWidth-3*offset,height-2*offset);
        setLayerInset(iconIndex,initWidth-iconWidth+offset,offset,2*offset,offset);
        super.setBounds(0,0,initWidth,height);
        // Log.v("DEMO","DrawableTextWithIconCV 6: "+(new Date()).getTime());
    }

    /** Method to set the bounds of the Drawable. Used by animators of the object. */

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;
        int offset = (int)(relativeOffsetCornerSize*height);
        ((GradientDrawable) getDrawable(borderIndex)).setCornerRadius(offset);
        setLayerSize(borderIndex,width,height);
        setLayerSize(textIndex,textDrawable.getIntrinsicWidth()*width/initWidth/textScaleFactor,height-2*offset);
        setLayerInset(textIndex,2*offset,textOffsetFromTop*width/initWidth,(int)(relativeIconWidth*width-2*offset),offset);
        // Log.v("DEMO",text+" width: "+(textDrawable.getIntrinsicWidth()*width/initWidth/textBmScale-3*offset)+" "+getLayerWidth(textIndex));
        // setLayerInset(textIndex,2*offset,offset,textDrawable.getIntrinsicWidth()*width/initWidth/textBmScale-offset,offset);
        // Log.v("DEMO",">> "+text+": "+textDrawable.getIntrinsicWidth()*width/initWidth/textBmScale);
        setLayerSize(iconIndex,(int)(relativeIconWidth*width-3*offset),height-2*offset);
        setLayerInset(iconIndex,(int)((1-relativeIconWidth)*width+offset),offset,2*offset,offset);
        super.setBounds(left,top,right,bottom);
    }

    /** Method to get the background color */

    public int getColor() {
        return colorBack;
    }

    /** Method to set the background color */

    public void setColor(int colorBack) {
        this.colorBack = colorBack;
        ((GradientDrawable)getDrawable(borderIndex)).setColor(colorBack);
    }

    /** Method to get the border color */

    public int getColorBorder() {
        return colorBorder;
    }

    /** Method to set the border color */

    public void setColorBorder(int colorBorder) {
        this.colorBorder = colorBorder;
        ((GradientDrawable)getDrawable(borderIndex)).setStroke(this.strokewidthBorder,this.colorBorder);
    }

    /** Method to set the transparency of all components of the object (background, text, border, icon) */

    public void setTransparency(int transparency) {
        int trsp = transparency;
        if (trsp<0) trsp = 0;
        if (trsp>255) trsp = 255;
        trsp <<= 24;
        setColor((getColor()&0x00FFFFFF)|trsp);
        setColorBorder((getColorBorder()&0xFFFFFF)|trsp);
        textDrawable.setAlpha(transparency);
        iconDrawable.setAlpha(transparency);
    }

    /** Method to get the border strokewidth */

    public int getStrokewidthBorder() {
        return strokewidthBorder;
    }

    /** Method to set the border strokewidth */

    public void setStrokewidthBorder(int strokewidthBorder) {
        this.strokewidthBorder = strokewidthBorder;
        ((GradientDrawable)getDrawable(borderIndex)).setStroke(this.strokewidthBorder,this.colorBorder);
    }

}
