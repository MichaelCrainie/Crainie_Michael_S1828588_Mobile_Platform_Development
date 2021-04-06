//Crainie_Michael_S1828588
package org.me.gcu.equakestartercode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import static org.me.gcu.equakestartercode.InfoHolder.itemCleanString;
import static org.me.gcu.equakestartercode.InfoHolder.itemDescriptions;
import static org.me.gcu.equakestartercode.InfoHolder.itemFullInfo;
import static org.me.gcu.equakestartercode.InfoHolder.itemLatitude;
import static org.me.gcu.equakestartercode.InfoHolder.itemLink;
import static org.me.gcu.equakestartercode.InfoHolder.itemLongitude;
import static org.me.gcu.equakestartercode.InfoHolder.itemMagFloat;
import static org.me.gcu.equakestartercode.InfoHolder.itemMagnitude;
import static org.me.gcu.equakestartercode.InfoHolder.itemRefinedDate;
import static org.me.gcu.equakestartercode.InfoHolder.itemTitles;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    //private TextView rawDataDisplay;
    private Button startButton;
    private Button mapButton;
    private Button backButton;
    private ListView listView;
    private ArrayList<String> list;
    private TextView textView;
    private Button getDateButton;
    private EditText startDate;
    private EditText endDate;
    private ArrayAdapter<String> arrayAdapter;
    private String result = "";
    private String url1 = "";
    private Date finalStartDate;
    private Date finalEndDate;
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private boolean refinedSearch = false;
    private float biggestDepth = 0;
    private float shallowestDepth = 30;
    private float mostNorth = -100;
    private float mostSouth = 100;
    private float mostEast = 100;
    private float mostWest = -100;
    private String biggestDepthString;
    private String mostNorthString;
    private String mostSouthString;
    private String mostEastString;
    private String mostWestString;
    private String shallowestDepthString = "";
    private float strongestMag = 0;
    private float weakestMag = 100;
    private String strongestMagString;
    private String weakestMagString;
    private Button endDateButton;
    private Button startDateButton;
    public Calendar c;
    public DatePickerDialog pick;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);
        mapButton = (Button) findViewById(R.id.mapButton);
        getDateButton = (Button) findViewById(R.id.dateButton);
        startDateButton = (Button) findViewById(R.id.calendarButton2);
        endDateButton = (Button) findViewById(R.id.calendarButton);
        startDate = (EditText)findViewById(R.id.startDate);


        endDate = (EditText)findViewById(R.id.endDate);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);
        endDateButton.setOnClickListener(this);
        endDateButton.setOnClickListener(this);
        getDateButton.setOnClickListener(this);
        list = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);


        startDateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                pick = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                pick.show();

            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                pick = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                pick.show();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(itemFullInfo.get(position));

            }
        });


    }

    public void onClick(View aview) {
        if(aview == startButton) {
            resetVariables();
            textView.setText("Click an object in the list view for more in depth information about the earthquake \n \nSearch a range of dates below to see earthquakes that occurred during that time period, this box will show the deepest/shallowest, highest/lowest magnitude and most north, south, east and west earthquake");
            InfoHolder.itemCleanString.clear();
            refinedSearch = false;
            startProgress();

        }

        if(aview == mapButton)
        {
            //setContentView(R.layout.mapfragment);
            Intent intent = new Intent(this, Map.class);
            startActivity(intent);
        }



        if(aview == getDateButton)
        {
            resetVariables();
            refinedSearch = true;

            String startDateTemp = startDate.getText().toString();
            String endDateTemp = endDate.getText().toString();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            try {
                finalStartDate = df.parse(startDateTemp);
                finalEndDate = df.parse(endDateTemp);
                if(finalStartDate.before(finalEndDate))
                {
                    Log.e("Hi", "start date smaller" + finalStartDate);
                }

                if(finalStartDate.after(finalEndDate))
                {
                    Log.e("Hi", "start date bigger" + finalEndDate);
                }

            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please use a valid format", Toast.LENGTH_LONG).show();
            }

            startProgress();
        }

    }

    public void resetVariables()
    {
        textView.setText("");
        list.clear();
        itemMagFloat.clear();
        itemMagnitude.clear();
        itemTitles.clear();
        itemLongitude.clear();
        itemLatitude.clear();
        itemDescriptions.clear();
        itemRefinedDate.clear();
        itemCleanString.clear();
        itemFullInfo.clear();
        itemLink.clear();
        biggestDepth = 0;
        shallowestDepth = 30;
        mostNorth = -100;
        mostSouth = 100;
        mostEast = 100;
        mostWest = -100;
        biggestDepthString = "";
        mostNorthString = "";
        mostSouthString = "";
        mostEastString = "";
        mostWestString = "";
        shallowestDepthString = "";
        strongestMag = 0;
        weakestMag = 100;
        strongestMagString = "";
        weakestMagString = "";
        InfoHolder.itemCleanString.clear();
    }







    public void startProgress() {
        new Thread(new Task(urlSource)).start();
    } //


    private class Task implements Runnable {

        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";



            try {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                }
                in.close();
            } catch (IOException ae) {
            }

            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    parseData(result);

                }
            });
        }

         ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.row, InfoHolder.itemCleanString) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                for (int i = 0; i < InfoHolder.itemTitles.size(); i++) {
                    if (itemMagFloat.get(position) / 10 >= 0 && itemMagFloat.get(position) / 10 <= 0.4)
                    {
                        view.setBackgroundColor(getResources().getColor(R.color.LightGreen));
                    }

                    if (itemMagFloat.get(position) / 10 >= 0.5 && itemMagFloat.get(position) / 10 <= 0.9)
                    {
                        view.setBackgroundColor(getResources().getColor(R.color.Green));
                    }

                    if (itemMagFloat.get(position) / 10 >= 1 && itemMagFloat.get(position) / 10 <= 1.4)
                    {
                        view.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    }

                    if (itemMagFloat.get(position) / 10 >= 1.5 && itemMagFloat.get(position) / 10 <= 1.9)
                    {
                        view.setBackgroundColor(getResources().getColor(R.color.Yellow));
                    }

                    if (itemMagFloat.get(position) / 10 >= 2 && itemMagFloat.get(position) / 10 <= 2.4)
                    {
                        view.setBackgroundColor(getResources().getColor(R.color.Orange));
                    }

                    if (itemMagFloat.get(position) / 10 >= 2.5 && itemMagFloat.get(position) / 10 <= 2.9)
                    {
                        view.setBackgroundColor(getResources().getColor(R.color.OrangeRed));
                    }

                    if (itemMagFloat.get(position) / 10 >= 3)
                    {
                        view.setBackgroundColor(getResources().getColor(R.color.Red));
                    }



                }
                return view;
            }
        };


    private void parseData(String dataToParse) {
        ItemClass item = new ItemClass();
        list.clear();
        InfoHolder.itemCleanString.clear();
        itemMagFloat.clear();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        item = new ItemClass();
                    }
                    if (xpp.getName().equalsIgnoreCase("title"))
                    {
                        String text = xpp.nextText();
                        item.setTitle(text);
                    }
                    else if (xpp.getName().equalsIgnoreCase("description"))
                    {
                        String text = xpp.nextText();
                        item.setDescription(text);
                    }

                    else if (xpp.getName().equalsIgnoreCase("link"))
                    {
                        String text = xpp.nextText();
                        item.setLink(text);
                    }

                    else if (xpp.getName().equalsIgnoreCase("pubDate"))
                    {
                        String text = xpp.nextText();
                        item.setPubDate(text);
                    }

                    else if (xpp.getName().equalsIgnoreCase("category"))
                    {
                        String text = xpp.nextText();
                        item.setCategory(text);
                    }

                    else if (xpp.getName().equalsIgnoreCase("lat"))
                    {
                        String text = xpp.nextText();
                        item.setGeoLat(text);
                    }

                    else if (xpp.getName().equalsIgnoreCase("long"))
                    {
                        String text = xpp.nextText();
                        item.setGeoLong(text);
                    }

                }

                else if (eventType == XmlPullParser.END_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item") && refinedSearch == true) {


                       try {


                           if (item.refinedDate().after(finalStartDate) && item.refinedDate().before(finalEndDate) || item.refinedDate().equals(finalStartDate) && item.refinedDate().before(finalEndDate) || item.refinedDate().after(finalStartDate) && item.refinedDate().equals(finalEndDate) || item.refinedDate().equals(finalStartDate) && item.refinedDate().equals(finalEndDate)) {
                               if (item.calcDepth() > biggestDepth)
                               {
                                   //textView.setText(item.dateFinderText() + "has the biggest depth in range with a depth of " + item.calcDepth());
                                   biggestDepth = item.calcDepth();
                                   biggestDepthString = item.dateFinderText() + " = Deepest! Depth: " + item.calcDepth();
                               }

                               if(item.calcDepth() < shallowestDepth)
                               {
                                   shallowestDepth = item.calcDepth();
                                   shallowestDepthString = item.dateFinderText() + " = Shallowest! Depth: " + item.calcDepth();
                               }

                               if(Float.parseFloat(item.getGeoLat()) > mostNorth)
                               {
                                  // textView.setText(textView.getText() + item.dateFinderText() + "is the most northern with latitude of " + item.getGeoLat());
                                   mostNorth = Float.parseFloat(item.getGeoLat());
                                   mostNorthString = item.dateFinderText() + " = Most Northern! Lat: " + item.getGeoLat();
                               }

                               if(Float.parseFloat(item.getGeoLat()) < mostSouth)
                               {
                                   mostSouth = Float.parseFloat(item.getGeoLat());
                                   mostSouthString = item.dateFinderText() + " = Most Southern! Lat: " + item.getGeoLat();
                               }

                               if(Float.parseFloat(item.getGeoLong()) > mostWest)
                               {
                                   mostWest = Float.parseFloat(item.getGeoLong());
                                   mostWestString = item.dateFinderText() + " = Most Eastern! Long: " + item.getGeoLong();
                               }

                               if(Float.parseFloat(item.getGeoLong()) < mostEast)
                               {
                                   mostEast = Float.parseFloat(item.getGeoLong());
                                   mostEastString = item.dateFinderText() + " = Most Western! Long: " + item.getGeoLong();
                               }

                               if(Float.parseFloat(item.magCalc())/ 10 > strongestMag)
                               {
                                   float strongestMagTemp = Float.parseFloat(item.magCalc());
                                   strongestMag = strongestMagTemp/10;
                                   strongestMagString = item.dateFinderText() + " = Strongest! Magnitude " + strongestMag;
                               }

                               if(Float.parseFloat(item.magCalc()) / 10 < weakestMag)
                               {
                                   float weakestMagTemp = Float.parseFloat(item.magCalc());
                                   weakestMag = weakestMagTemp/10;
                                   weakestMagString = item.dateFinderText() + " = Weakest! Magnitude " + Float.parseFloat(item.magCalc()) / 10;
                               }


                               textView.setText(biggestDepthString + "\n" + shallowestDepthString + "\n" + mostNorthString  + mostSouthString + mostWestString + mostEastString + strongestMagString + "\n" + weakestMagString);
                               list.add(item.magnitudeString());
                               listView.setAdapter(arrayAdapter);
                               arrayAdapter.notifyDataSetChanged();
                           }

                           else {
                               resetVariables();
                               listView.setAdapter(arrayAdapter);
                               arrayAdapter.notifyDataSetChanged();
                               Log.e("READ", "Item Date " + item.refinedDate() + " start date is " + finalStartDate + " end date is " + finalEndDate);
                           }
                       }

                       catch (Exception e)
                       {

                       }


                    }


                    if(xpp.getName().equalsIgnoreCase("item") && refinedSearch == false)
                    {
                        list.add(item.magnitudeString());
                        listView.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                // Get the next event
                eventType = xpp.next();

            } // End of while
        } catch (XmlPullParserException parseError)
        {
            Log.e("ParseError", "Parsing Error " + parseError.toString());
        } catch (IOException ioError)
        {
            Log.e("IO Error", "IO error " + ioError.toString());
        }



    }

    }
}