package de.thkoeln.cvogt.android.propanimutil_demos;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_Intro extends Activity {

    int screenwidth, screenheight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenwidth = GUIUtilitiesCV.getDisplayWidth();
        screenheight = GUIUtilitiesCV.getDisplayHeight();
        introClip();
    }

    public void introClip() {
        final AnimationViewCV view = new AnimationViewCV(this);
        int centerX = screenwidth/2;
        int centerY = screenheight/2-100;
        int radius = screenwidth/2-100;
        int orientation = getResources().getConfiguration().orientation;

        // Icons: appearing, rotating, disappearing
        int numberOfObjects = 12;
        int iconSize = 200;
        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(6);
        int duration = 1700;
        int iconTargetY = screenheight/4;
        float comprFactorEllipse = 0.5f;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            iconTargetY = screenheight / 8;
            comprFactorEllipse = 0.25f;
        }
        Point[] targetPoints = GraphicsUtilsCV.pointsOnEllipse(centerX,centerY,radius,comprFactorEllipse,0,numberOfObjects);
        for (int i=0;i<numberOfObjects;i++) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_thk_android);
            // bm = GraphicsUtilsCV.makeBitmapTransparent(bm, Color.WHITE);
            AnimatedGuiObjectCV icon = new AnimatedGuiObjectCV(this,"BITMAP",bm,centerX,centerY,iconSize,iconSize);
            // AnimatedGuiObjectCV icon = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CENTER", Color.BLUE, centerX,centerY,iconSize);
            // icon.addDependentGuiObject(new DependentBorderCV(icon,12, Color.BLACK));
            Animator anim1 = icon.addLinearPathAnimator(targetPoints[i].x,targetPoints[i].y,duration,1000);
            Animator anim2 = icon.addEllipseAnimator(centerX,centerY,radius,comprFactorEllipse,0,true,0,new AccelerateDecelerateInterpolator(),2*duration,0);
            Animator anim3 = icon.addLinearPathAnimator(screenwidth/2,iconTargetY,duration);
            Animator anim4 = icon.addSizeAnimator((int)(1.5*iconSize),((int)(1.5*iconSize)),duration);
            icon.playSequentially(anim1, anim2, icon.playTogether(anim3,anim4));
            view.addAnimatedGuiObject(icon);
        }
        // Headlines
        int delay = 5*duration-1000;
        AnimatedGuiObjectCV objText1 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT,this,"TEXT1","Property Animation Utilities",0,-50,30,0);
        int width = objText1.getWidth(), height = objText1.getHeight();
        objText1.addBezierPathAnimator(screenwidth,centerY/2,centerX,centerY-(int)(3*height),duration,delay);
        objText1.addSizeAnimator(2*width,2*height,duration,delay);
        objText1.addRotationAnimator((float)(2* Math.PI),duration,delay);
        view.addAnimatedGuiObject(objText1);
        AnimatedGuiObjectCV objText2 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT,this,"TEXT2","Prof. Dr. Carsten Vogt",0,-50,30,0);
        width = objText2.getWidth();
        height = objText2.getHeight();
        objText2.addBezierPathAnimator(screenwidth,centerY/2+100,centerX,centerY,duration,delay);
        objText2.addSizeAnimator(2*width,2*height,duration,delay);
        objText2.addRotationAnimator((float)(2* Math.PI),duration,delay);
        view.addAnimatedGuiObject(objText2);
        AnimatedGuiObjectCV objText3 = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT,this,"TEXT3","Technische Hochschule Köln",0,-50,30,0);
        width = objText3.getWidth();
        height = objText3.getHeight();
        objText3.addBezierPathAnimator(screenwidth,centerY/2+200,centerX,centerY+(int)(3*height),duration,delay);
        objText3.addSizeAnimator(2*width,2*height,duration,delay);
        objText3.addRotationAnimator((float)(2* Math.PI),duration,delay);
        view.addAnimatedGuiObject(objText3);
        // Buttons
        delay += 2000;
        final int buttonWidth = 240;
        final int buttonHeight = 150;
/*
        final AnimatedGuiObjectCV<Drawable> background1 = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"BACKGROUND", Color.DKGRAY,screenwidth/2,screenheight/2,0,0);
        background1.addSizeAnimator(screenwidth-100,screenheight-400,3000,delay);
        background1.addColorAnimator(Color.BLUE,3000,delay);
        view.addAnimatedGuiObject(background1);
        final AnimatedGuiObjectCV<Drawable> background2 = new AnimatedGuiObjectCV<Drawable>(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"BACKGROUND", Color.DKGRAY,screenwidth/2,screenheight/2,0,0);
        // background2.addLinearPathAnimator(100,100,3000,1000);
        background2.addSizeAnimator(screenwidth-400,screenheight-800,2000,delay);
        background2.addColorAnimator(0xFF87CEFA,3000,delay);
        view.addAnimatedGuiObject(background2);
        background1.setZindex(1);
        background2.setZindex(2);
*/
        // buttons
        int delay_videobuttons = 1500;
        Button b1 = new Button(this);
        b1.setWidth(buttonWidth);
        b1.setHeight(buttonHeight);
        b1.setBackgroundColor(0x00FFFFFF);
        b1.setBackgroundResource(R.drawable.button_layout);
        b1.setText("Video 1");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(DemoPropAnimUtils_Intro.this,"Not yet available",Toast.LENGTH_LONG).show();
                // (new VideooutputPopup(DemoPropAnimUtils_Intro.this,"http://www.nt.th-koeln.de/vogt/vma/videos/UtilitiesPropAnimCV_1_480.mp4")).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/RYjBFQDCfPQ")));
            }
        });
        final AnimatedGuiObjectCV<View> buttonVideo1 = new AnimatedGuiObjectCV<View>(b1, "BUTTONVIDEO1", -150, -250);
        buttonVideo1.addBezierPathAnimator(screenwidth+400,400,-400,screenheight+300,screenwidth/2-130, 3*screenheight/4, duration, delay_videobuttons);
        // buttonVideo.addLinearPathAnimator(screenwidth/2+100, screenheight/2+100, duration, delay);
        buttonVideo1.setZindex(3);
        view.addAnimatedGuiObject(buttonVideo1);
        Button b2 = new Button(this);
        b2.setWidth(buttonWidth);
        b2.setHeight(buttonHeight);
        b2.setBackgroundColor(0x00FFFFFF);
        b2.setBackgroundResource(R.drawable.button_layout);
        b2.setText("Video 2");
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(DemoPropAnimUtils_Intro.this,"Not yet available",Toast.LENGTH_LONG).show();
                // (new VideooutputPopup(DemoPropAnimUtils_Intro.this,"http://www.nt.th-koeln.de/vogt/vma/videos/UtilitiesPropAnimCV_2_720.mp4")).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/HTr16eRqq60")));
            }
        });
        final AnimatedGuiObjectCV<View> buttonVideo2 = new AnimatedGuiObjectCV<View>(b2, "BUTTONVIDEO2", -150, -250);
        buttonVideo2.addBezierPathAnimator(screenwidth+400,400,-400,screenheight+300,screenwidth/2+130, 3*screenheight/4, duration, delay_videobuttons);
        // buttonVideo.addLinearPathAnimator(screenwidth/2+100, screenheight/2+100, duration, delay);
        buttonVideo2.setZindex(3);
        view.addAnimatedGuiObject(buttonVideo2);
        Button b3 = new Button(this);
        b3.setWidth(buttonWidth);
        b3.setHeight(buttonHeight);
        b3.setBackgroundColor(0x00FFFFFF);
        b3.setBackgroundResource(R.drawable.button_layout);
        b3.setText("Video 3");
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(DemoPropAnimUtils_Intro.this,"Not yet available",Toast.LENGTH_LONG).show();
                // (new VideooutputPopup(DemoPropAnimUtils_Intro.this,"http://www.nt.th-koeln.de/vogt/vma/videos/UtilitiesPropAnimCV_3_480.mp4",true)).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/oHWNgBJRwCg")));
            }
        });
        final AnimatedGuiObjectCV<View> buttonVideo3 = new AnimatedGuiObjectCV<View>(b3, "BUTTONVIDEO3", -150, -250);
        buttonVideo3.addBezierPathAnimator(screenwidth+400,400,-400,screenheight+300,screenwidth/2+390, 3*screenheight/4, duration, delay_videobuttons);
        // buttonVideo.addLinearPathAnimator(screenwidth/2+100, screenheight/2+100, duration, delay);
        buttonVideo3.setZindex(3);
        view.addAnimatedGuiObject(buttonVideo3);
        // Set view and start animations
        setContentView(view);
        view.startAnimations();
    }

    /** Popup window to display a video.
     */

    private class VideooutputPopup extends PopupWindow {

        LinearLayout layout;
        VideoView videoView;
        Context context;
        boolean portrait;

        /** Constructor
         *
         * @param context Context (= activity) using this popup window.
         * @param videoUrlString URL of the video to be displayed.
         */

        public VideooutputPopup(final Context context, final String videoUrlString) {
           this(context,videoUrlString,false);
        }

        public VideooutputPopup(final Context context, final String videoUrlString, boolean portrait) {
            super(context);
            this.context = context;
            this.portrait = portrait;
            if (!portrait)
               videoView = new VideoView(context);
            else
               videoView = new FullScreenVideoView(context);
            final MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(Uri.parse(videoUrlString));
            Button closeButton = new Button(context);
            /*
            closeButton.setText("Close");
            closeButton.setTypeface(closeButton.getTypeface(), Typeface.BOLD_ITALIC);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.RIGHT;
            closeButton.setLayoutParams(lp);
            closeButton.setBackgroundColor(0xFFFFFFFF);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Close the window
                    dismiss();
                }
            });
            Button showControllerButton = new Button(context);
            showControllerButton.setText("Show Controller");
            showControllerButton.setTypeface(closeButton.getTypeface(), Typeface.BOLD_ITALIC);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp2.gravity = Gravity.RIGHT;
            showControllerButton.setLayoutParams(lp2);
            showControllerButton.setBackgroundColor(0xFFFFFFFF);
            showControllerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaController!=null)
                        mediaController.show();
                }
            });
*/
            // Generate the layout
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setBackgroundColor(0xFFFFFFFF);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layout.setLayoutParams(layoutParams);
            layout.setGravity(Gravity.CENTER_HORIZONTAL);
            // layout.addView(closeButton);
            layout.addView(videoView);
            // layout.addView(showControllerButton);
            setContentView(layout);
            setFocusable(true);
            videoView.start();
            videoView.requestFocus();
        }

        /** Display the PopupWindow at a specific location with a specific size.
         *
         * @param left X coordinate of the top left corner
         * @param top Y coordinate of the top left corner
         * @param width Width of the window
         * @param height Height of the window
         */

        public void show(int left, int top, int width, int height) {
            int orientation = context.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT && !portrait)
                showBigToast(context,"Videos are best viewed in landscape mode.",Gravity.CENTER);
            showAtLocation(layout, Gravity.TOP|Gravity.LEFT, left, top);
            update(left,top,width,height);
        }

        /**
         * Display the PopupWindow at a standard location with a standard size.
         */

        public void show() {
            final int left = 20;
            final int top = 10;
            show(left,top, Resources.getSystem().getDisplayMetrics().widthPixels-left,Resources.getSystem().getDisplayMetrics().heightPixels-top);
        }

    }

    void showBigToast(Context context, String text, int gravity) {
        Toast toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        ViewGroup toastView = (ViewGroup)toast.getView();
        TextView tv = (TextView)toastView.getChildAt(0);
        tv.setTextSize(22);
        toast.setGravity(gravity,0,0);
        toast.show();
    }

    public class FullScreenVideoView extends VideoView {

        // https://stackoverflow.com/questions/39782591/make-video-in-videoview-full-screen-in-portrait-mode?rq=1

        public FullScreenVideoView(Context context) {
            super(context);
        }

        public FullScreenVideoView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public FullScreenVideoView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(16*screenheight/9, screenheight);
            setMeasuredDimension(16*screenheight/9, screenheight);
        }

    }

}
