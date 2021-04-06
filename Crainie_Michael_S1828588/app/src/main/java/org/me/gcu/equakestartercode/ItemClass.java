//Crainie_Michael_S1828588
package org.me.gcu.equakestartercode;


import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

public class ItemClass {
    public String title;
    public String description;
    public String link;
    public String pubDate;
    public String category;
    public String geoLat;
    public String geoLong;
    public String refinedDate;
    public int whichMonth;
    public Date refinedDateTemp;
    public float depthFinal;
    public String finalTitle;
    
    public ItemClass()
    {
        title = "";
        description = "";
        link = "";
        pubDate = "";
        category = "";
        geoLat = "";
        geoLong = "";
        refinedDate = "";

    }

    public ItemClass(String itemTitle, String itemDescription, String itemLink, String itemPubDate, String itemCategory, String itemLatitude, String itemLongitude)
    {
        title = itemTitle;
        description = itemDescription;
        link = itemLink;
        pubDate = itemPubDate;
        category = itemCategory;
        geoLat = itemLatitude;
        geoLong = itemLongitude;
    }

    public String getTitle() { return title; }

    public void setTitle(String itemTitle) { title = itemTitle + "\n"; }

    public String getDescription() { return description; }

    public void setDescription(String itemDescription) { description = itemDescription; }

    public String getLink() { return link; }

    public void setLink(String itemLink) { link = itemLink; }

    public String getPubDate() { return pubDate; }

    public void setPubDate(String itemPubDate) { pubDate = itemPubDate; }

    public String getCategory() { return category; }

    public void setCategory(String itemCategory) { category = itemCategory; }

    public String getGeoLat() { return geoLat; }

    public void setGeoLat(String itemLatitude) { geoLat = itemLatitude + "\n"; }

    public String getGeoLong() { return geoLong; }

    public void setGeoLong(String itemLongitude) { geoLong = itemLongitude + "\n"; }

    public String toString()
    {
        String temp;

        temp = title + " " + description + " " + link + " " + pubDate + " " + category + " " + geoLat + " " + geoLong;

        return temp;
    }

    public String toBetterString()
    {
        String temp;

        temp = title + " " + geoLat + " " + geoLong;

        return temp;
    }

    public String magnitudeString()
    {

        String temp;
        temp = description;
        String[] values = temp.split(";");
        String magnitude = values[4].replaceAll("\\D+", "");
        String depth = values[3].replaceAll("\\D+", "");
        Log.e("rrrr", "Depth is:" + depth);
        String fullInfoString;
        fullInfoString = title + " " + description + " " + link + " " + pubDate + " " + category + " " + geoLat + " " + geoLong;
        String cleanString;
        cleanString = values[1] + "\n" + " Latitude: " + geoLat + " Longitude: " + geoLong + values[4];
        itemTitles.add(title);
        itemDescriptions.add(description);
        itemLatitude.add(geoLat);
        itemLink.add(link);
        itemLongitude.add(geoLong);
        itemFullInfo.add(fullInfoString);
        itemMagnitude.add(magnitude);
        float temp2 = Float.parseFloat(magnitude);
        itemMagFloat.add(temp2);
        Log.e("MAG is", magnitude);
        Log.e("Date is", pubDate);
        itemCleanString.add(cleanString);


        return cleanString;
    }

    public Date refinedDate()
    {

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
        try {
            Date dateOfQuake = df.parse(pubDate);
            itemRefinedDate.add(dateOfQuake);
            refinedDateTemp = dateOfQuake;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("READ", "Refining Date failed");
        }

        return refinedDateTemp;
    }

   public float calcDepth()
   {
        String tempDeath = description;
        String[] values = tempDeath.split(";");
        String depthStringValue = values[3].replaceAll("\\D+", "");
        depthFinal = Float.parseFloat(depthStringValue);
        Log.e("YO", "Depth is " + depthFinal);
        return depthFinal;
   }

   public String dateFinderText()
   {
       String tempFinal = description;
       String[] values = tempFinal.split(";");
       String area = values[1];
       area = area.replace(" Location:", "");
       return area;
   }

   public String magCalc()
   {
       String temp;
       temp = description;
       String[] values = temp.split(";");
       String magnitude = values[4].replaceAll("\\D+", "");
       return magnitude;
   }



}
