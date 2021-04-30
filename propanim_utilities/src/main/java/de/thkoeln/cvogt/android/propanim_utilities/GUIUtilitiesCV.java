package de.thkoeln.cvogt.android.propanim_utilities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Android utility class with basic GUI functions.
 * 
 * @author  Carsten Vogt, Cologne University of Applied Sciences / Technische Hochschule Koeln
 * 
 * @version 1.0
 * @since 2012-06-29
 * 
 */

public class GUIUtilitiesCV {

	/**
	 * Shows a toast message.
	 * @param callingActivity Activity calling this method (if null, no toast will be displayed)
	 * @param message Message to be displayed
	 */

	public static void showToast(Activity callingActivity, String message) {
		if (callingActivity==null) return;
		Toast t = Toast.makeText(callingActivity,message, Toast.LENGTH_LONG);
		t.setGravity(Gravity.TOP, 10, 50);
		t.show();
	}

	/**
	 * Displays a PDF document from the Web.
	 * @param callingActivity Activity calling this method (if null, the PDF document will not be displayed)
	 * @param urlString URL of the PDF document
	 */

	public static void showPDF(Activity callingActivity, String urlString) {
		if (callingActivity==null) return;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse(urlString), "application/pdf");
		try {
			callingActivity.startActivity(intent);
		}
		catch (ActivityNotFoundException e) {
			Toast.makeText(callingActivity,
					"Keine App zur PDF-Anzeige vorhanden",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Gets the display width.
	 * @return The width of the display (in px)
	 */

	public static int getDisplayWidth() {
		return Resources.getSystem().getDisplayMetrics().widthPixels;
	}

	/**
	 * Gets the display height.
	 * @return The height of the display (in px)
	 */

	public static int getDisplayHeight() {
		return Resources.getSystem().getDisplayMetrics().heightPixels;
	}

    /**
	 * Shows a toast with a specific text size with a specific gravity on the display
	 * @param context The context from where the method is called
	 * @param text The text to be displayed
	 * @param textSize The font size for the text
	 * @param gravity The gravity (Gravity.TOP, Gravity.CENTER, ...)
     */

	static void showToast(Context context, String text, float textSize, int gravity) {
		Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
		ViewGroup toastView = (ViewGroup)toast.getView();
		TextView tv = (TextView)toastView.getChildAt(0);
		tv.setTextSize(textSize);
		toast.setGravity(gravity,0,0);
		toast.show();
	}

}

/*
class PopupWindowDemo extends Activity {

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup);  // Anzeige eines Buttons, über den man das PopupWindow erscheinen lassen kann (siehe res/layout/popup.xml)
		setTitle("Popup");

		Button popupbutton = (Button) findViewById(R.id.popupbutton);
		popupbutton.setOnClickListener(new MyOnClickListener()); // Setzen des Button-Listeners (siehe unten)

	}

	// Listener für den Button in der Activity-Oberfläche: lässt das PopupWindow erscheinen

	class MyOnClickListener implements View.OnClickListener {

		public void onClick(View v) {

			final MyPopupWindow pw = new MyPopupWindow(PopupWindowDemo.this);  // erzeugt das PopupWindow (Definition siehe unten: class MyPopupWindow)

			pw.setOnDismissListener(new PopupWindow.OnDismissListener() {  // ordnet ihm einen "OnDismissListener" zu, der bei Schlie�en des PopupWindows aktiv wird
				public void onDismiss() {
					Toast myToast = Toast.makeText(PopupWindowDemo.this, "Hallo " + pw.eingegebenerName, Toast.LENGTH_LONG);
					myToast.setGravity(Gravity.CENTER, 0, 0);
					View myToastView = myToast.getView();
					myToastView.setBackgroundColor(Color.BLACK);
					TextView myToastMessage = (TextView) myToastView.findViewById(android.R.id.message);
					myToastMessage.setTextSize(24);
					myToastMessage.setBackgroundColor(Color.BLACK);
					myToastMessage.setTextColor(Color.WHITE);
					myToast.show();  // bei Schließen des PopupWindows: Toast-Anzeige des Texts, der im PopupWindow eingegeben wurde (siehe unten)
				}
			});

			pw.showAtLocation(pw.layout, Gravity.CENTER, 0, 0); // Anzeige des PopupWindows

			// pw.update(0,0,320,380);  Hier: Angabe der Höhe und Breite in absoluten Pixeln (px)

			// Alternativ: Umrechnung in dichteunabhängige Pixel (dp)

			int breite = (int ) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics());
			int hoehe = (int )TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 380, getResources().getDisplayMetrics());
			pw.update(0,0,breite,hoehe);

		}

	}
}

// class MyPopupWindow definiert das PopupWindow

class MyPopupWindow extends PopupWindow {

	EditText eingabefeld;  // Textfeld zur Eingabe eines Strings
	String eingegebenerName;  // Speicher für den eingegebenen String
	LinearLayout layout;   // Layout des PopupWindows

	MyPopupWindow(Activity aktuelleActivity) {
		super(aktuelleActivity);
		// Zur Abwechslung wird hier das Layout im Java-Programm selbst aufgebaut.
		// Alternativ könnte man auch hier eine XML-Layout-Datei referenzieren.
		// Aufbau des Layouts: Vertikale Anordnung eines festen Texts, eines Eingabefelds und eines Buttons
		layout = new LinearLayout(aktuelleActivity);
		layout.setBackgroundColor(0xFFFFFFFF);
		layout.setOrientation(LinearLayout.VERTICAL);
		TextView aufforderungstext = new TextView(aktuelleActivity);
		aufforderungstext.setText("Wie heißt Du?");
		aufforderungstext.setTextSize(18);
		aufforderungstext.setHeight(80);
		aufforderungstext.setGravity(Gravity.BOTTOM);
		eingabefeld = new EditText(aktuelleActivity);
		eingabefeld.setTextSize(18);
		Button okButton = new Button(aktuelleActivity);
		okButton.setText("OK");
		okButton.setTextSize(18);
		okButton.setOnClickListener(new ButtonListener());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 20, 10, 10);
		layout.addView(aufforderungstext,layoutParams);
		layout.addView(eingabefeld,layoutParams);
		layout.addView(okButton,layoutParams);
		setContentView(layout);
		setFocusable(true);  // damit das Popup Window Eingaben entgegegennimmt
	}

	// Listener für den Button im Popup Window: übernimmt den eingegebenen Text in die Variable 'eingegebenerName' und schließt das Fenster durch 'dismiss()',
	// worauf der OnDismissListener (siehe oben) aktiv wird.

	class ButtonListener implements View.OnClickListener {
		public void onClick(View v) {
			eingegebenerName = eingabefeld.getText().toString();
			dismiss();
		}
	}
}
*/
