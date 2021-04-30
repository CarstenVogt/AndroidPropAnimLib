package de.thkoeln.cvogt.android.propanimutil_demos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_Groups1 extends Activity {

    String colorNames[] = { "BLACK", "GREEN", "BLUE", "MAGENTA", "RED", "OLIVE", "ORANGE2" };
    AnimationViewCV view;
    AnimatedGuiObjectsGroupCV objectGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demo1();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.menu_4demos, menu);
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
        if (item.getItemId()==R.id.menuItemDemo4)
            demo4();
        return true;
    }

    private void demo1() {
        // animationView to display the objects
        AnimationViewCV view = new AnimationViewCV(this);
        setContentView(view);
        // objects to be displayed
        AnimatedGuiObjectsGroupCV wordsGroup = new AnimatedGuiObjectsGroupCV("Words"),
                rectangleGroup = new AnimatedGuiObjectsGroupCV("Rectangles");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_es);
        String[] woerter = { "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez", "once", "doce" };
        final int width = 200, height = 60, flagWidth = 80;
        Point[] pos = GraphicsUtilsCV.pointsOnCircle(GUIUtilitiesCV.getDisplayWidth()/2,GUIUtilitiesCV.getDisplayHeight()/2,GUIUtilitiesCV.getDisplayWidth()/5,woerter.length);
        for (int i = 0; i<woerter.length;i++) {
            DrawableTextWithIconCV drw = new DrawableTextWithIconCV(this, woerter[i], bitmap, width, height, flagWidth, 4);
            drw.setColor(Color.YELLOW);
            drw.setStrokewidthBorder(3);
            drw.setColorBorder(Color.BLACK);
            wordsGroup.add(new AnimatedGuiObjectCV<Drawable>("X", drw, pos[i].x,pos[i].y, 0));
        }
        view.addAnimatedGuiObjects(wordsGroup);
        // animations for wordsGroup
        wordsGroup.addFadeInAnimator(3000);
        zeigeErklaertext(view,"Fade In",60,2*GUIUtilitiesCV.getDisplayWidth()/10,6*GUIUtilitiesCV.getDisplayHeight()/10,0,3000);
        int delay = 3000;
        wordsGroup.addTranslationAnimator(500,300,2000,delay);
        int posXtext = 2*GUIUtilitiesCV.getDisplayWidth()/10, posYtext = 6*GUIUtilitiesCV.getDisplayHeight()/10;
        zeigeErklaertext(view,"Translation",60, posXtext, posYtext, delay,2000);
        delay+=2000;
        // objectGroup.addStackingAnimator(220,800,50,-50,2000,delay);
        wordsGroup.addStackingAnimatorWithZoom(220,800,50,-50,.95,2000,delay);
        zeigeErklaertext(view,"Stacking w. Zoom",60, posXtext+100, posYtext, delay,2000);
        delay+=2000;
        wordsGroup.addFadeOutAnimator(3500,delay);
        zeigeErklaertext(view,"Fade Out",60, posXtext, posYtext, delay,3500);
        delay+=3500;
        wordsGroup.startAnimations();
        // animations for rectangleGroup
        for (int i=0;i<8;i++) {
            AnimatedGuiObjectCV obj = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT, "RECT" + i, ColorsCV.getColorForName(colorNames[i % colorNames.length]), 220 + ((int) (300 * Math.random())), 100 + 150 * i, 300, 100);
            obj.setVisible(false);
            obj.addAppearanceAnimator(delay);
            rectangleGroup.add(obj);
        }
        view.addAnimatedGuiObjects(rectangleGroup);
        rectangleGroup.addFadeInAnimator(3000,delay);
        zeigeErklaertext(view,"Fade In",60, posXtext, posYtext, delay,3000);
        delay+=3000;
        rectangleGroup.addTranslationAnimator(500,300,2000,delay);
        zeigeErklaertext(view,"Translation",60, posXtext, posYtext, delay,2000);
        delay+=2000;
        // objectGroup.addStackingAnimator(220,800,50,-50,2000,delay);
        rectangleGroup.addStackingAnimatorWithZoom(220,800,50,-50,.9,2000,delay);
        zeigeErklaertext(view,"Stacking w. Zoom",60, posXtext+100, posYtext, delay,2000);
        delay+=3000;
        ArrayList<Point> targets = new ArrayList();
        for (int i=0;i<8;i++)
            targets.add(new Point(100+((int)(500* Math.random())),100+((int)(900* Math.random()))));
        rectangleGroup.addLinearPathsAnimator(targets,new DecelerateInterpolator(),3000,delay);
        zeigeErklaertext(view,"Linear Paths",60, posXtext, posYtext, delay,3000);
        // objectGroup.addLinearPathsConstSpeedAnimator(targets,null,5000,delay);
        delay+=3000;
        rectangleGroup.addFadeOutAnimator(3000,delay);
        zeigeErklaertext(view,"Fade Out",60, posXtext, posYtext, delay,3000);
        rectangleGroup.startAnimations();
    }

    private void demo2() {
        int delay = 0;
        // animationView to display the objects
        final AnimatedGuiObjectsGroupCV rectangleGroup = new AnimatedGuiObjectsGroupCV("RectangleGroup");
        for (int i=0;i<8;i++)
            rectangleGroup.add(new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT"+i, ColorsCV.getColorForName(colorNames[i%colorNames.length]),220+((int)(300* Math.random())),100+150*i,200+((int)(200* Math.random())),50+((int)(100* Math.random()))));
        // view to display the object
        AnimationViewCV view = new AnimationViewCV(this);
        view.addAnimatedGuiObjects(rectangleGroup);
        setContentView(view);
        // animations
        rectangleGroup.addAlignRightAnimator(2000,delay);
        (new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {}
                rectangleGroup.addAlignLeftAnimator(2000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rectangleGroup.startAnimations();
                    }
                });
            }
        }).start();
        int posXtext = 300, posYtext = 200;
        zeigeErklaertext(view,"Align Right",60, posXtext, posYtext, 0,2000);
        zeigeErklaertext(view,"Align Left",60, posXtext, posYtext, 2000,2000);
        // delay+=2000;
        // rectangleGroup.addAlignLeftAnimator(2000,delay);
        delay+=5000;
        rectangleGroup.addAlignLeftToXAnimator(100,2000,delay);
        zeigeErklaertext(view,"Align Left to X=100",60, posXtext, posYtext, delay,2000);
        delay+=3000;
        rectangleGroup.addAlignRightToXAnimator(GUIUtilitiesCV.getDisplayWidth()-100,2000,delay);
        zeigeErklaertext(view,"Align Right to X=width-100",60, posXtext+100, posYtext, delay,2000);
        rectangleGroup.startAnimations();
    }

    private void demo3() {
        AnimationViewCV view = new AnimationViewCV(this);
        AnimatedGuiObjectsGroupCV objectGroup = new AnimatedGuiObjectsGroupCV("ObjectGroup");
        for (int i=0;i<10;i++)
            objectGroup.add(new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE"+i, ColorsCV.getColorForName(colorNames[i%colorNames.length]),
                    200+((int)((GUIUtilitiesCV.getDisplayWidth()-200)* Math.random())),100+((int)((GUIUtilitiesCV.getDisplayHeight()-600)* Math.random())),
                    100,100));
        view.addAnimatedGuiObjects(objectGroup);
        setContentView(view);
        objectGroup.addPlaceOnCircleAnimator(GUIUtilitiesCV.getDisplayWidth()/2,GUIUtilitiesCV.getDisplayHeight()/2,300,3000,500);
        int posXtext = 300, posYtext = 200;
        zeigeErklaertext(view,"Place On Circle",60, posXtext, posYtext, 500,3000);
        objectGroup.addPlaceOnEllipseAnimator(GUIUtilitiesCV.getDisplayWidth()/2,GUIUtilitiesCV.getDisplayHeight()/2,300,0.5, Math.PI/4,3000,4000);
        zeigeErklaertext(view,"Place On Ellipse (1)",60, posXtext, posYtext, 4000,3000);
        objectGroup.addPlaceOnEllipseAnimator(GUIUtilitiesCV.getDisplayWidth()/2,GUIUtilitiesCV.getDisplayHeight()/2,500,0.5,-Math.PI/4,3000,7500);
        zeigeErklaertext(view,"Place On Ellipse (2)",60, posXtext, posYtext, 7500,3000);
        objectGroup.startAnimations();
    }

    private void demo4() {

        view = new AnimationViewCV(this);
        setContentView(view);
        objectGroup = new AnimatedGuiObjectsGroupCV("ObjectGroup");
        for (int i=0;i<5;i++)
            objectGroup.add(new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_RECT,"RECT"+i, ColorsCV.getColorForName(colorNames[i%colorNames.length]),
                    200+((int)((GUIUtilitiesCV.getDisplayWidth()-200)* Math.random())),100+((int)((GUIUtilitiesCV.getDisplayHeight()-600)* Math.random())),
                    50+((int)(100* Math.random())),50+((int)(100* Math.random()))));
        view.addAnimatedGuiObjects(objectGroup);

        final EingabePopupWindow pw = new EingabePopupWindow(this);

        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                // objects to be displayed
                objectGroup.addAlignAndDistrAnimator(pw.alignmentCmd,pw.alignmentParam,pw.horizDistrCmd,pw.horizDistrParam,pw.vertDistrCmd,pw.vertDistrParam,2000,500);
                objectGroup.startAnimations();
            }
        });

        pw.showAtLocation(pw.layout, Gravity.BOTTOM, 0, 0); // Anzeige des PopupWindows

        int breite = (int ) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics());
        int hoehe = (int ) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 340, getResources().getDisplayMetrics());
        pw.update(0,0,breite,hoehe);

    }

    private void zeigeErklaertext(AnimationViewCV view, String text, int textSize, int startX, int startY, int startDelay, int duration) {
        zeigeErklaertext(view,text,textSize,startX,startY,startX,startY,startDelay,duration);
    }

    private void zeigeErklaertext(AnimationViewCV view, String text, int textSize, int startX, int startY, int targetX, int targetY, int startDelay, int duration) {
        AnimatedGuiObjectCV textview = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_BITMAPTEXT,this,"",text,startX,startY,textSize);
        textview.setVisible(false);
        textview.addAppearanceAnimator(startDelay);
        textview.addLinearPathAnimator(targetX,targetY,duration,startDelay).addListener(new AnimationUtilsCV.EndListener_Delete(textview));
        view.addAnimatedGuiObject(textview);
        // textview.addSizeAnimator(0,0,500, startDelay+duration);
        textview.startAnimation();
    }

}

class EingabePopupWindow extends PopupWindow {

    LinearLayout layout;
    int alignmentCmd, alignmentParam;
    int horizDistrCmd, horizDistrParam;
    int vertDistrCmd, vertDistrParam;

    EingabePopupWindow(Activity aktuelleActivity) {
        super(aktuelleActivity);
        // Beschaffung des Layouts des PopupWindows aus der zugeordneten XML-Datei
        LayoutInflater inflater = (LayoutInflater) aktuelleActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (LinearLayout) inflater.inflate(R.layout.inputgroupdemo,null,false);
        // Komponenten des Layouts
        ((Button) layout.findViewById(R.id.okBtn)).setOnClickListener(new ButtonListener());
        setContentView(layout);

        setFocusable(true);  // damit das Popup Window Eingaben entgegegennehmen kann
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            if (((RadioButton) layout.findViewById(R.id.alignleftBtn)).isChecked()) alignmentCmd = AnimatedGuiObjectsGroupCV.ALIGN_LEFT;
            if (((RadioButton) layout.findViewById(R.id.alignrightBtn)).isChecked()) alignmentCmd = AnimatedGuiObjectsGroupCV.ALIGN_RIGHT;
            if (((RadioButton) layout.findViewById(R.id.aligntopBtn)).isChecked()) alignmentCmd = AnimatedGuiObjectsGroupCV.ALIGN_TOP;
            if (((RadioButton) layout.findViewById(R.id.alignbottomBtn)).isChecked()) alignmentCmd = AnimatedGuiObjectsGroupCV.ALIGN_BOTTOM;
            if (((RadioButton) layout.findViewById(R.id.alignvertctrBtn)).isChecked()) alignmentCmd = AnimatedGuiObjectsGroupCV.ALIGN_VERT_CENTERS;
            if (((RadioButton) layout.findViewById(R.id.alignhorizctrBtn)).isChecked()) alignmentCmd = AnimatedGuiObjectsGroupCV.ALIGN_HORIZ_CENTERS;
            if (((RadioButton) layout.findViewById(R.id.alignnoneBtn)).isChecked()) alignmentCmd = 0;
            EditText alignmentParamEtxt = (EditText) layout.findViewById(R.id.alignParam);
            if (alignmentParamEtxt.getText().length()==0)
                alignmentParam = Integer.MIN_VALUE;
            else alignmentParam = Integer.parseInt(alignmentParamEtxt.getText().toString());
            if (((RadioButton) layout.findViewById(R.id.distrhorizboundsBtn)).isChecked()) horizDistrCmd = AnimatedGuiObjectsGroupCV.DISTR_HORIZ_BOUNDS;
            if (((RadioButton) layout.findViewById(R.id.distrhorizcentersBtn)).isChecked()) horizDistrCmd = AnimatedGuiObjectsGroupCV.DISTR_HORIZ_CENTERS;
            if (((RadioButton) layout.findViewById(R.id.distrhoriznoneBtn)).isChecked()) horizDistrCmd = 0;
            EditText horizdistrParamEtxt = (EditText) layout.findViewById(R.id.horizDistanceParam);
            if (horizdistrParamEtxt.getText().length()==0)
                horizDistrParam = Integer.MIN_VALUE;
            else horizDistrParam = Integer.parseInt(horizdistrParamEtxt.getText().toString());
            if (((RadioButton) layout.findViewById(R.id.distrvertboundsBtn)).isChecked()) vertDistrCmd = AnimatedGuiObjectsGroupCV.DISTR_VERT_BOUNDS;
            if (((RadioButton) layout.findViewById(R.id.distrvertcentersBtn)).isChecked()) vertDistrCmd = AnimatedGuiObjectsGroupCV.DISTR_VERT_CENTERS;
            if (((RadioButton) layout.findViewById(R.id.distrvertnoneBtn)).isChecked()) vertDistrCmd = 0;
            EditText vertdistrParamEtxt = (EditText) layout.findViewById(R.id.vertDistanceParam);
            if (vertdistrParamEtxt.getText().length()==0)
                vertDistrParam = Integer.MIN_VALUE;
            else vertDistrParam = Integer.parseInt(vertdistrParamEtxt.getText().toString());
            dismiss();
        }
    }

}
