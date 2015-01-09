package com.theandroidsuit.frasesparapensar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.WorkSource;
import android.provider.UserDictionary;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.theandroidsuit.frasesparapensar.bean.Pensamiento;
import com.theandroidsuit.frasesparapensar.util.FrasesParaPensarUtils;
import com.theandroidsuit.frasesparapensar.util.JSONParser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity {

    private static final String TAG = "com.theandroidsuit.frasesparapensar.MainActivity";

    private List<Pensamiento> listWisdom = null;
    private static Pensamiento pensamiento = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupListadoPensamientos();
        setupNuevoPensamiento(false);
        setupSiguientePensamiento();
    }

    private void setupNuevoPensamiento(boolean force) {

        if (null == pensamiento || force) {
            getNewSentence();
        }

        drawSentence();
        setupColors();
        setupShareIntent();

    }


    private void setupShareIntent() {


        ImageView bs = (ImageView) findViewById(R.id.buttonShare);
        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* button to share */
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                StringBuffer text = new StringBuffer();
                text.append("\"").append(pensamiento.getFrase()).append("\"").append(" -- ").append(pensamiento.getAutor());

                if(pensamiento.hasExtra()){
                    text.append(" (").append(pensamiento.getExtra()).append(")");
                }

                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, FrasesParaPensarUtils.SHARE_TITLE);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, text.toString());

                startActivity(Intent.createChooser(sharingIntent, FrasesParaPensarUtils.CHARE_CHOOSER));

            }
        });


    }

    private void setupSiguientePensamiento(){

        LinearLayout ll = (LinearLayout) findViewById(R.id.contentContainer);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupNuevoPensamiento(true);
            }
        });

    }

    private void setupColors() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.contentContainer);

        Calendar calendar = Calendar.getInstance();

        // HOUR
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String hourStr = String.valueOf(hour);
        if (hour < 10)
            hourStr = "0" + hourStr;

        // MINUTE
        int minute = calendar.get(Calendar.MINUTE);
        String minuteStr = String.valueOf(minute);
        if (minute < 10)
            minuteStr = "0" + minuteStr;

        // SECOND
        int second = calendar.get(Calendar.SECOND);
        String secondStr = String.valueOf(second);
        if (second < 10)
            secondStr = "0" + secondStr;

        // Color
        try {

            String hexaDecColorStr = hourStr + minuteStr + secondStr;
            int hexaDecColor = Color.parseColor("#"+hexaDecColorStr);

            int hexaDecColorComplementary = Color.rgb(
                    255 - Color.red(hexaDecColor),
                    255 - Color.green(hexaDecColor),
                    255 - Color.blue(hexaDecColor)
            );

            ll.setBackgroundColor(Integer.valueOf(hexaDecColorComplementary));

            Log.d(TAG + ".color", "" + hexaDecColor);
            Log.d(TAG + ".complementaryColor", "" + hexaDecColorComplementary);

            TextView frase = (TextView) findViewById(R.id.frase);
            TextView autor = (TextView) findViewById(R.id.autor);
            TextView extra = (TextView) findViewById(R.id.extra);

            frase.setTextColor(hexaDecColor);
            autor.setTextColor(hexaDecColor);
            extra.setTextColor(hexaDecColor);

            Typeface type;

            Random rnd = new Random(System.currentTimeMillis());
            int randomFont = rnd.nextInt(FrasesParaPensarUtils.MAX_FONT_TYPE);

            switch (randomFont) {
                case 0:
                    type = Typeface.createFromAsset(getAssets(), "fonts/Playball.ttf");
                    break;
                case 1:
                    type = Typeface.createFromAsset(getAssets(), "fonts/DJB Elliephont.ttf");
                    break;
                case 2:
                    type = Typeface.createFromAsset(getAssets(), "fonts/DJB Scruffy Angel.ttf");
                    break;
                case 3:
                    type = Typeface.createFromAsset(getAssets(), "fonts/DJB Tweenybopper.ttf");
                    break;
                case 4:
                    type = Typeface.createFromAsset(getAssets(), "fonts/SedonaScriptFLF.ttf");
                    break;
                default:
                    type = Typeface.createFromAsset(getAssets(), "fonts/DancingScript-Regular.ttf");
                    break;
            }

            frase.setTypeface(type);
            autor.setTypeface(type);
            extra.setTypeface(type);

        }catch (IllegalArgumentException e){
            Log.d(TAG, e.getMessage());
        }
    }

    private void getNewSentence() {
        Log.d(TAG, "getNewSentence");

        if (null == listWisdom || listWisdom.isEmpty()) {
            setupListadoPensamientos();
        }

        Random rnd = new Random(System.currentTimeMillis());
        int randomWisdomLocation = rnd.nextInt(listWisdom.size());

        pensamiento = listWisdom.get(randomWisdomLocation);
    }

    private void drawSentence() {
        TextView frase = (TextView) findViewById(R.id.frase);
        TextView autor = (TextView) findViewById(R.id.autor);
        TextView extra = (TextView) findViewById(R.id.extra);

        frase.setText("\"" + pensamiento.getFrase() + ".\"");
        autor.setText(capitalize(pensamiento.getAutor()));
        extra.setText(pensamiento.getExtra());
    }

    private String capitalize(String autor) {
        StringBuffer sb = new StringBuffer();

        String[] tokens = autor.trim().split(" ");

        for(int i = 0; i < tokens.length; i++){
            if (null != tokens[i] && tokens[i].trim().length() > 0);{
                sb.append(Character.toUpperCase(tokens[i].charAt(0)));
                sb.append(tokens[i].toLowerCase().substring(1));
                sb.append(" ");
            }

        }

        return sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /********************************************/
    private  void setupListadoPensamientos() {
        Log.d(TAG, "setupListadoPensamientos");

        if (null == listWisdom) {
            JSONParser jParser = new JSONParser();
            listWisdom = jParser.getDatafromAsset(this);
        }
    }


}
