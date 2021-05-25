package de.thkoeln.cvogt.android.propanimutil_demos;

import android.app.ListActivity;
import android.content.*;
import android.content.res.*;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class MainActivity extends ListActivity {

    String[] choices = { "Video Introduction", "Basic Shapes & Paths", "Texts", "Arc / Circle / Elliptic / Spiral Path",
            "Bezier Path", "Rotation, Time Interpolators, Fading, Trails", "Lines & Borders",
            "Animator Sequence", "Touch Events & Gestures", "Graphs",
            "Collision & Proximity", "Drawables & Views", "Layer Drawables & Path Shapes", "Groups: Paths, Alignment, Distribution", "Groups: Zoom, Rotation, Ellipse, Area",
            "Reset", "GUI Objects for Special Purposes"
            //, "Layout Embedding"
    };

    ListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,choices);
        setListAdapter(adapter);
    }

    protected void onListItemClick(ListView liste, View datenElement, int position, long id) {
        super.onListItemClick(liste, datenElement, position, id);
        String auswahl = ((TextView)datenElement).getText().toString();
        if (auswahl.equals("Video Introduction"))
            startActivity (new Intent(this, DemoPropAnimUtils_Intro.class));
        if (auswahl.equals("Basic Shapes & Paths"))
            startActivity (new Intent(this, DemoPropAnimUtils_Basics.class));
        if (auswahl.equals("Texts"))
            startActivity (new Intent(this, DemoPropAnimUtils_Texts.class));
        if (auswahl.equals("Arc / Circle / Elliptic / Spiral Path"))
            startActivity (new Intent(this, DemoPropAnimUtils_ArcCircleSpiralpath.class));
        if (auswahl.equals("Bezier Path"))
            startActivity (new Intent(this, DemoPropAnimUtils_Bezierpath.class));
        if (auswahl.equals("Rotation, Time Interpolators, Fading, Trails"))
            startActivity (new Intent(this, DemoPropAnimUtils_RotInterpolFadeTrail.class));
        if (auswahl.equals("Lines & Borders"))
            startActivity (new Intent(this, DemoPropAnimUtils_DependentObjects.class));
        if (auswahl.equals("Animator Sequence"))
            startActivity (new Intent(this, DemoPropAnimUtils_Animatorsequence.class));
        if (auswahl.equals("Touch Events & Gestures"))
            startActivity (new Intent(this, DemoPropAnimUtils_TouchGestures.class));
        if (auswahl.equals("Graphs"))
            startActivity (new Intent(this, DemoPropAnimUtils_Graphs.class));
        if (auswahl.equals("Collision & Proximity"))
            startActivity (new Intent(this, DemoPropAnimUtils_Collision.class));
        if (auswahl.equals("Drawables & Views"))
            startActivity (new Intent(this, DemoPropAnimUtils_DrawablesViews.class));
        if (auswahl.equals("Layer Drawables & Path Shapes"))
            startActivity (new Intent(this, DemoPropAnimUtils_LayerPathDrawables.class));
        if (auswahl.equals("Groups: Paths, Alignment, Distribution"))
            startActivity (new Intent(this, DemoPropAnimUtils_Groups1.class));
        if (auswahl.equals("Groups: Zoom, Rotation, Ellipse, Area"))
            startActivity (new Intent(this, DemoPropAnimUtils_Groups2.class));
        if (auswahl.equals("Reset"))
            startActivity (new Intent(this, DemoPropAnimUtils_Reset.class));
        if (auswahl.equals("GUI Objects for Special Purposes"))
            startActivity (new Intent(this, DemoPropAnimUtils_Specials.class));
        // if (auswahl.equals("Layout Embedding"))
        //    startActivity (new Intent(this, DemoPropAnimUtils_LayoutEmbedding.class));
    }

    /** Popup window to display a video.
     */

    private static class VideooutputPopup extends PopupWindow {

        LinearLayout layout;
        Context context;

        /** Constructor
         *
         * @param context Context (= activity) using this popup window.
         * @param videoUrlString URL of the video to be displayed.
         */

        public VideooutputPopup(final Context context, final String videoUrlString) {
            super(context);
            this.context = context;
            VideoView videoView = new VideoView(context);
            final MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(Uri.parse(videoUrlString));
            Button closeButton = new Button(context);
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
            // Generate the layout
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setBackgroundColor(0xFFFFFFFF);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            // layoutParams.setMargins(50,15,50,0);
            layout.setLayoutParams(layoutParams);
            // layout.addView(closeButton);
            layout.addView(videoView);
            layout.addView(showControllerButton);
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
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
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

    private static void showBigToast(Context context, String text, int gravity) {
        Toast toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        ViewGroup toastView = (ViewGroup)toast.getView();
        TextView tv = (TextView)toastView.getChildAt(0);
        tv.setTextSize(30);
        toast.setGravity(gravity,0,0);
        toast.show();
    }

}
