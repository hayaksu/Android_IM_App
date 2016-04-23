package com.sinch.messagingtutorialskeleton;

/**
 * Created by Haya on 10/27/2015.
 */
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.TextMining.PrepareData;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "6zuqfy2rPtBTFIhHcrQy1TbuCwXLvhJ1mcFZEA4R", "iK6g3dGw9Am5XMZo8O7nph3Lve5bGq8XiTQ8OtD8");
        // Reading from TransDatabase
        List<ParseObject> textMSGList=null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TransDatabase");//TransDatabase
            try {
                textMSGList = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        //array to covert list to array
        try {
            String[] array = new String[textMSGList.size()];
            int g=0;
            //add the data in the list to the array
            for (ParseObject comment : textMSGList) {
                array[g]=comment.get("FilteredMsg").toString();
                // Log.i("testing ",array[g]);
                g++;
            }
            //  Apriori.main(array);

        }
        catch (Exception e)
        {
            Toast.makeText(this, "تأكد من اتصالك بالانترنت", Toast.LENGTH_LONG).show();

        }

 /*       try {
         System.out.println("start");
            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("ParseMessage");
            System.out.println("start2");

        List<ParseObject> Objectslist = null;
            System.out.println("start4");

            Objectslist = query1.find();

        for (int index = 40; index < 53; ++index) {
            String str = Objectslist.get(index).getString("messageText");
            System.out.println("start5");

            PrepareData.AddaConversation(str);
        }

        } catch (ParseException e) {
            e.printStackTrace();
        }*/


/*        //array to covert list to array
        String [] array = new String[textMSGList.size()];
        int g=0;
        //add the data in the list to the array
        for (ParseObject comment : textMSGList) {
            array[g]=comment.get("FilteredMsg").toString();
         Log.i("testing ",array[g]);
            g++;
        }


String [] test ={"1 2 3 4",
        "1 5 6 7",
        "1 8 9 10",
        "1 2 3 4",
        "1 2 3 4",
        "10 11 12 13"};
        try {
            Log.i("before ","before apriori");
            Apriori.main(array);
            Log.i("after ", "after apriori");

        } catch (Exception e) {
            Log.i("try exception", "try exception");

            e.printStackTrace();
        }

*/

    }
}
