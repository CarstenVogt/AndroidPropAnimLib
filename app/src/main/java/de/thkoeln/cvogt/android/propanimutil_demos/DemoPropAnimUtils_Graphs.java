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
 3.5.2021

 */

package de.thkoeln.cvogt.android.propanimutil_demos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import de.thkoeln.cvogt.android.propanim_utilities.*;

public class DemoPropAnimUtils_Graphs extends Activity {

    int screenwidth, screenheight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenwidth = GUIUtilitiesCV.getDisplayWidth();
        screenheight = GUIUtilitiesCV.getDisplayHeight();
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
        if (item.getItemId() == R.id.menuItemDemo1)
            demo1();
        if (item.getItemId() == R.id.menuItemDemo2)
            demo2();
        if (item.getItemId() == R.id.menuItemDemo3)
            demo3();
        if (item.getItemId() == R.id.menuItemDemo4)
            demo4();
        return true;
    }

    public void demo1() {
        // A complete tree with three levels
        // fundamental constants
        final int numberChildren = 5;
        final int objectSize = 50;
        Paint linePaint = new Paint();
        // paint for the edges
        linePaint.setStrokeWidth(6);
        linePaint.setColor(Color.BLACK);
        // HashMap defining the graph
        HashMap<Object, Collection> graph = new HashMap();
        // root of the tree
        AnimatedGuiObjectCV root = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE_ROOT", Color.RED,0,0,objectSize);
        graph.put(root,new HashSet());
        // second level of the tree
        for (int i=0;i<numberChildren;i++) {
            // child vertex
            AnimatedGuiObjectCV<Drawable> child = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CIRCLE"+i, Color.BLUE,0,0,objectSize);
            // line between root and child
            new DependentLineObjectToObjectCV(root,child,linePaint);
            // add child vertex to the graph with an edge to the root
            graph.put(child,new HashSet());
            graph.get(root).add(child);
            for (int j=0;j<numberChildren;j++) {
                // grandchild vertex
                AnimatedGuiObjectCV<Drawable> grandchild = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "CIRCLE" + i + "" + j, Color.GREEN, 0, 0, objectSize);
                // line between child and grandchild
                new DependentLineObjectToObjectCV(child, grandchild, linePaint);
                // add grandchild vertex to the graph with an edge to the child
                graph.put(grandchild, new HashSet());
                graph.get(child).add(grandchild);
            }
        }
        // calculate the positions of the vertices
        HashMap<Object, Point> positions = GraphsUtilsCV.placeVertices_TreeLayout(graph,root,0,0,screenwidth,screenheight,5*objectSize);
        // set the positions of the Drawables and add them to the view
        AnimationViewCV view = new AnimationViewCV(this);
        for (Iterator it = positions.keySet().iterator(); it.hasNext();) {
            AnimatedGuiObjectCV drw = (AnimatedGuiObjectCV) it.next();
            Point pos = positions.get(drw);
            drw.setCenterX(pos.x);
            drw.setCenterY(pos.y);
            view.addAnimatedGuiObject(drw);
        }
        zeigeErklaertext(view,"Move the vertices with your finger",60, 500,100,0,5000);
        setContentView(view);
        view.setOnTouchListener(new AnimationUtilsCV.OnTouchListener_Relocation());
        view.startAnimations();
    }

    public void demo2() {
        // A complete tree with three levels
        // fundamental constants
        final int numberChildren = 5;
        final int objectSize = 50;
        Paint linePaint = new Paint();
        // paint for the edges
        linePaint.setStrokeWidth(6);
        linePaint.setColor(Color.BLACK);
        // HashMap defining the graph
        HashMap<Object, Collection> graph = new HashMap();
        // root of the tree
        AnimatedGuiObjectCV root = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"ROOT", Color.RED,screenwidth/2,screenheight/2,objectSize);
        root.setZindex(3);
        graph.put(root,new HashSet());
        // second level of the tree
        for (int i=0;i<numberChildren;i++) {
            // child vertex
            AnimatedGuiObjectCV<Drawable> child = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE,"CHILD"+i, Color.BLUE,screenwidth/2,screenheight/2,objectSize);
            child.setZindex(2);
            // line between root and child
            new DependentLineObjectToObjectCV(root,child,linePaint);
            // add child vertex to the graph with an edge to the root
            graph.put(child,new HashSet());
            graph.get(root).add(child);
            for (int j=0;j<numberChildren;j++) {
                // grandchild vertex
                AnimatedGuiObjectCV<Drawable> grandchild = new AnimatedGuiObjectCV(AnimatedGuiObjectCV.TYPE_DRAWABLE_CIRCLE, "GRANDCHILD" + i + "" + j, Color.GREEN,screenwidth/2,screenheight/2, objectSize);
                grandchild.setZindex(1);
                // line between child and grandchild
                new DependentLineObjectToObjectCV(child, grandchild, linePaint);
                // add grandchild vertex to the graph with an edge to the child
                graph.put(grandchild, new HashSet());
                graph.get(child).add(grandchild);
            }
        }
        // calculate the positions of the vertices
        HashMap<Object, Point> positions = GraphsUtilsCV.placeVertices_BalloonLayout(graph,root,0,0,screenwidth,screenheight);
        AnimationViewCV view = new AnimationViewCV(this);
        // specify animations to move the vertices to their final positions
        GraphsUtilsCV.animateGraphPlacement(graph,root,positions,3000,1000);
        for (Iterator it = graph.keySet().iterator(); it.hasNext();)
            view.addAnimatedGuiObject((AnimatedGuiObjectCV)it.next());
        zeigeErklaertext(view,"Balloon Layout",60, 300,200,0,10000);
        // display the view
        setContentView(view);
        view.setOnTouchListener(new AnimationUtilsCV.OnTouchListener_Relocation());
        view.startAnimations();
    }

    public void demo3() {    // see commented code at the bottom for how to proceed when the graph is specified by an SQLite database
        // A graph with German and foreign words with a force-driven layout
        // Languages and flags
        final int DEUTSCH = 0, ENGLISCH = 1, SPANISCH = 2;
        Bitmap[] flaggen = new Bitmap[3];
        flaggen[DEUTSCH] = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_de);
        flaggen[ENGLISCH] = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_gb);
        flaggen[SPANISCH] = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_es);
        // Definition of the graph with its vertices and edges
        String[] woerter = { "Haus", "Heim", "Schale", "house", "home", "shell", "hostel", "casa", "edificio", "concha" };
        int[] sprachenFuerWoerter = { DEUTSCH,DEUTSCH,DEUTSCH,ENGLISCH,ENGLISCH,ENGLISCH,ENGLISCH,SPANISCH,SPANISCH,SPANISCH};
        int[][] kanten = { {0,3}, {0,4}, {0,5}, {0,7}, {0,8}, {1,4}, {1,6}, {2,5}, {2,9} };
        // Generate the Drawables that represent the vertices of the node and store them in a temporary array
        final int objectWidth = 400, objectHeight = 100, flagWidth = (int) (0.3*objectWidth);
        AnimatedGuiObjectCV[] woerterDrawables = new AnimatedGuiObjectCV[woerter.length];
        for (int i=0;i<woerter.length;i++) {
            LayerDrawable drw = new DrawableTextWithIconCV(this,woerter[i],flaggen[sprachenFuerWoerter[i]],objectWidth,objectHeight,flagWidth,4);
            woerterDrawables[i] = new AnimatedGuiObjectCV<Drawable>(woerter[i], drw, 50+objectWidth/2, 100+ 2*i*objectHeight);
        }
        // Add the graphical representations of the edges as dependent objects to the vertices
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(6);
        linePaint.setColor(Color.BLACK);
        for (int i=0;i<kanten.length;i++)
            new DependentLineObjectToObjectCV(woerterDrawables[kanten[i][0]],woerterDrawables[kanten[i][1]],linePaint,false);
        // Specify the graph by a HashMap mapping the vertices to lists with their adjacent vertices
        HashMap graph = new HashMap();
        for (int i=0;i<woerter.length;i++)
            graph.put(woerterDrawables[i],new HashSet<>());
        for (int i=0;i<kanten.length;i++)
            ((HashSet)graph.get(woerterDrawables[kanten[i][0]])).add(woerterDrawables[kanten[i][1]]);
        // Place the vertices
        HashMap<Object, Point> positions = GraphsUtilsCV.placeVertices_ForceDirectedLayout(graph,0,0,screenwidth,screenheight);
        // HashMap<Object,Point> positions = GraphicsUtilsCV.placeVertices_TreeLayout(graph,woerterDrawables[0],0,0,screenwidth,screenheight,500);
        // HashMap<Object,Point> positions = GraphicsUtilsCV.placeVertices_BalloonLayout(graph,woerterDrawables[0],0,0,screenwidth,screenheight);
        // Set the positions of the Drawables and add them to the view
        AnimationViewCV view = new AnimationViewCV(this);
        for (Iterator it = positions.keySet().iterator(); it.hasNext();) {
            AnimatedGuiObjectCV drw = (AnimatedGuiObjectCV) it.next();
            Point pos = positions.get(drw);
            drw.setCenterX(pos.x);
            drw.setCenterY(pos.y);
            view.addAnimatedGuiObject(drw);
        }
        zeigeErklaertext(view,"Force-Directed Layout",80, 500,200,0,10000);
        setContentView(view);
        view.setOnTouchListener(new AnimationUtilsCV.OnTouchListener_Relocation());
        // view.startAnimations();
    }

    public void demo4() {    // see commented code at the bottom for how to proceed when the graph is specified by an SQLite database
        // A graph with German and foreign words with a layout for bipartite graphs
        // Languages and flags
        final int DEUTSCH = 0, ENGLISCH = 1, SPANISCH = 2;
        Bitmap[] flaggen = new Bitmap[3];
        flaggen[DEUTSCH] = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_de);
        flaggen[ENGLISCH] = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_gb);
        flaggen[SPANISCH] = BitmapFactory.decodeResource(getResources(), R.drawable.flagge_es);
        // Definition of the graph with its vertices and edges
        String[] woerter = { "Haus", "Heim", "Schale", "house", "home", "shell", "hostel", "casa", "edificio", "concha" };
        int[] sprachenFuerWoerter = { DEUTSCH,DEUTSCH,DEUTSCH,ENGLISCH,ENGLISCH,ENGLISCH,ENGLISCH,SPANISCH,SPANISCH,SPANISCH};
        int[][] kanten = { {0,3}, {0,4}, {0,5}, {0,7}, {0,8}, {1,4}, {1,6}, {2,5}, {2,9} };
        // Generate the Drawables that represent the vertices of the node and store them in a temporary array
        final int objectWidth = 400, objectHeight = 100, flagWidth = (int) (0.3*objectWidth);
        AnimatedGuiObjectCV[] woerterDrawables = new AnimatedGuiObjectCV[woerter.length];
        for (int i=0;i<woerter.length;i++) {
            DrawableTextWithIconCV drw;
            drw = new DrawableTextWithIconCV(this,woerter[i],flaggen[sprachenFuerWoerter[i]],objectWidth,objectHeight,flagWidth,4);
            woerterDrawables[i] = new AnimatedGuiObjectCV<Drawable>(woerter[i], drw, 50+objectWidth, 100+ 4*i*objectHeight);
            /* alternative with size animation:
            if (i==0)
                drw = new DrawableTextWithIconCV(this,woerter[i],flaggen[sprachenFuerWoerter[i]],objectWidth,objectHeight,flagWidth,4);
             else
                drw = new DrawableTextWithIconCV(this,woerter[i],flaggen[sprachenFuerWoerter[i]],objectWidth/4,objectHeight/4,flagWidth/4,4);
            woerterDrawables[i] = new AnimatedGuiObjectCV<Drawable>(woerter[i], drw, 50+objectWidth/4, 100+ 4*i*objectHeight);
            */
        }
        // Add the graphical representations of the edges as dependent objects to the vertices
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(6);
        linePaint.setColor(Color.BLACK);
        for (int i=0;i<kanten.length;i++)
            new DependentLineObjectToObjectCV(woerterDrawables[kanten[i][0]],woerterDrawables[kanten[i][1]],linePaint,false);
        // Specify the graph by a HashMap mapping the vertices to lists with their adjacent vertices
        HashMap graph = new HashMap();
        for (int i=0;i<woerter.length;i++)
            graph.put(woerterDrawables[i],new HashSet<>());
        for (int i=0;i<kanten.length;i++)
            ((HashSet)graph.get(woerterDrawables[kanten[i][0]])).add(woerterDrawables[kanten[i][1]]);
        // Place the vertices
        HashMap<Object, Point> positions = GraphsUtilsCV.placeVertices_BipartiteGraphLayout(graph,woerterDrawables[0],20,200,screenwidth-10,woerterDrawables[0].getWidth(),woerterDrawables[0].getHeight(),30);
        // Add the vertices and their animations to the view
        AnimationViewCV view = new AnimationViewCV(this);
        GraphsUtilsCV.animateGraphPlacement(graph,woerterDrawables[0],positions,1,3000,1000);
        // alternative with size animation:
        // GraphsUtilsCV.animateGraphPlacement(graph,woerterDrawables[0],positions,4,3000,1000);
        for (Iterator it = graph.keySet().iterator(); it.hasNext();)
            view.addAnimatedGuiObject((AnimatedGuiObjectCV)it.next());
        // display the view
        zeigeErklaertext(view,"Bipartite Graph Layout",80, 500,100,0,10000);
        setContentView(view);
        view.setOnTouchListener(new AnimationUtilsCV.OnTouchListener_Relocation());
        view.startAnimations();
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

    /*  WHEN THE GRAPH IS SPECIFIED BY AN SQLITE DATABASE: FIRST GENERATE A HASHMAP REPRESENTATION OF THE GRAPH
        (more details: package graphics_utils > comments on class GraphsUtils)

        // import an external database from the assets (if necessary)
        FileUtilitiesCV.copyDatabaseFileFromAssets(this,"WordsGraph.db");

        // generate the HashMap representation from the SQL database
        SQLiteDatabase db = openOrCreateDatabase("WordsGraph.db", Activity.MODE_PRIVATE, null);
        HashMap<Integer,HashSet<Integer>> graph = GraphsUtilsCV.generateGraphHashMapFromSQL(db,"Knoten","ID","Kanten","Knoten1","Knoten2");

        // for control purposes: write the generated HashMap to the LogCat
        for (Iterator<Integer> it=graph.keySet().iterator();it.hasNext();) {
            int key = it.next();
            String output = "Vertex "+key+":";
            HashSet<Integer> adjList = graph.get(key);
            for (Iterator<Integer> it2=adjList.iterator();it2.hasNext();)
                output += " "+it2.next();
            Log.v("DEMO",output);
        }

     */

}
