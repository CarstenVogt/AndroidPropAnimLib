package de.thkoeln.cvogt.android.propanim_utilities;

import java.util.TreeMap;

/**
 * Created by vogt on 12.01.2018.
 */

// http://www.tayloredmktg.com/rgb/

public class ColorsCV {

    private static final String[] colorNames = {
            "WHITE","YELLOW","SALMON","ORANGE","ORANGE2","RED","MAGENTA",
            "VIOLET2","VIOLETRED","LIGHTSKYBLUE","BLUE","CYAN","OLIVE","GREEN","SIENNA","BLACK" };

    private static final int[] colorValues =
            { 0xFFFFFFFF,0xFFFFFF00,0xFFFA8072,0xFFFF8C44,0xFFFFA500,0xFFFF0000,0xFFFF00FF
                    ,0xFFEE82EE,0xFFD02090,0xFF87CEFA,0xFF0000FF,0xFF00FFFF,0xFFDDDD00,0xFF00FF00,0xFFA0522D,0xFF000000 };

    private static TreeMap<String, Integer> colorMap;

    public static int getColorForName(String name) {
        return colorMap.get(name);
    }

    public static int getRandomColor() {
        return getColorForName(colorNames[(int)(colorNames.length* Math.random())]);
    }

    public static int[] getColorValues() {
        return colorValues;
    }

    static {
        colorMap = new TreeMap<String, Integer>();
        for (int i=0;i<colorNames.length;i++)
            colorMap.put(colorNames[i],colorValues[i]);
    }

}
