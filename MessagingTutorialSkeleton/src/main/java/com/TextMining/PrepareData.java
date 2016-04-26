package com.TextMining;

import android.provider.Settings;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marwa Khan on 25/3/2016.
 * Class Description: This class is used to perform the following tasks 
 * 1- Text tokanization to split each message and conver it to vector of number to speed up processing the results 
 * 2- Text Normalization to remove unrelated words such as stop words 
 * 3- Stemming to return a word to its root 
 */
public class PrepareData {

    public static int Indexcounter = 0;
    public static boolean HasURL;
    public static boolean IsURLphishing;

    // This method will read a message and do text preprocessing tasks on it  
    // then uplade it back to the server ass numbesr
    public static String AddaConversation(String Message) {

        long start = System.currentTimeMillis();

        String CurrentStr = "";
        String IndexMsg = "";

        HasURL = false;
        IsURLphishing = false;

        try {
                Message = Message.replaceAll("\n", " ");
                String[] ArrayMsg = Message.split(" ");

            ParseQuery<ParseObject> queryToIndex = ParseQuery.getQuery("LastWordIndex");
            queryToIndex.whereEqualTo("objectId", "Cb3oM2RsGL");

            Indexcounter = ((queryToIndex.find()).get(0).getInt("LastIndex"));

            System.out.println("Start Index:" + Indexcounter);
            for (int wordIndex = 0; wordIndex < ArrayMsg.length; ++wordIndex ) {

                    CurrentStr = ArrayMsg[wordIndex];

                    CurrentStr = WordNormalization.FilterWords(CurrentStr);

                    if (!CurrentStr.equals(" ") && !CurrentStr.isEmpty()) {
                        CurrentStr = WordNormalization.RemoveWords(CurrentStr);

                        if (!CurrentStr.isEmpty()) {
                            CurrentStr = StemWord.Stem(CurrentStr);
                            CurrentStr = CurrentStr.replaceAll("([\u0620-\u064A\u06C0-\u06CF])\\1+", "$1$1");
                        }
                    }


                    if (!CurrentStr.isEmpty() && !CurrentStr.equals(" ")) {
                        ArrayList<Integer> IndexWordsList = UploadSpecificMessageWords(CurrentStr);
                        IndexMsg += IndexWordsList.toString().replaceAll("[\\[\\],]", "") + " ";
                    }
                }
                  //FilteredMsg
                  //TransDatabase
                if (!IndexMsg.isEmpty()) {
                    ParseObject parseIndexedWords = new ParseObject("TransDatabase");
                    parseIndexedWords.put("FilteredMsg", IndexMsg);
                    parseIndexedWords.saveInBackground();

                }

            ParseQuery<ParseObject> queryUpdate = ParseQuery.getQuery("LastWordIndex");
            ParseObject parseIndex = queryUpdate.get("Cb3oM2RsGL");

            parseIndex.put("LastIndex", Indexcounter);
            parseIndex.saveInBackground();

            System.out.println("LastIndex:" + Indexcounter);


            ParseQuery<ParseObject> queryToCheck = ParseQuery.getQuery("LastWordIndex");
            queryToCheck.whereEqualTo("objectId", "Cb3oM2RsGL");
            int IndexChecker = ((queryToIndex.find()).get(0).getInt("LastIndex"));

            if (IndexChecker != Indexcounter)
            {
                System.out.println("InsideChecker");
                parseIndex.put("LastIndex", Indexcounter);
                parseIndex.saveInBackground();
            }

        } catch (Exception e) {
        }

        // do operation to be timed here
        long time = System.currentTimeMillis() - start;

        System.out.println("Time:"+time);
        return IndexMsg;
    }


    //This method will check if the word index is already found in the server 
    private static ArrayList<Integer> UploadSpecificMessageWords(String str) {

        ArrayList<Integer> IndexedWord = new ArrayList<Integer>();

        String[] strList = str.split(" ");

        int CurrentIndex = 0;

        try {

            ParseObject parseWords = new ParseObject("IndexedWordsDB");


            for (String word : strList) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("IndexedWordsDB");

                query.whereEqualTo("Word", word);


                List<ParseObject> OutputList = query.find();

                if (word.length() > 1) {
                    if (OutputList.size() == 0 ) {


                        ++Indexcounter;

                        System.out.println("Inside uplaode:" + Indexcounter);
                        CurrentIndex = Indexcounter;

                        parseWords.put("Word", word);
                        parseWords.put("Index", Indexcounter);

                        parseWords.saveInBackground();

                    } else {
                        CurrentIndex = OutputList.get(0).getInt("Index");
                    }

                    IndexedWord.add(CurrentIndex);
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return IndexedWord;
    }
    
    
    //Method to read all training messages and fill the database with their results after indexing all of them 
     public static void ReadAllConversations() {

        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");

            List<ParseObject> Objectslist = query.find();

            String CurrentMsg = "";
            String CurrentStr = "";

            String IndexMsg = "";

            for (int index = 0; index < Objectslist.size(); ++index) {
                CurrentMsg = Objectslist.get(index).getString("messageText");

                CurrentMsg = CurrentMsg.replaceAll("\n", " ");
                String[] ArrayMsg = CurrentMsg.split(" ");

                IndexMsg = "";

                for (int wordIndex = 0; wordIndex < ArrayMsg.length; ++wordIndex) {

                    CurrentStr = ArrayMsg[wordIndex];

                    //System.out.println(CurrentStr);

                    CurrentStr = WordNormalization.FilterWords(CurrentStr);
                    System.out.println("After Normalize:" + CurrentStr+"*");

                    if (!CurrentStr.equals(" ") && !CurrentStr.isEmpty()) {
                        CurrentStr = WordNormalization.RemoveWords(CurrentStr);
                        //System.out.println("After Remove:" + CurrentStr);

                        if (!CurrentStr.isEmpty()) {
                            CurrentStr = StemWord.Stem(CurrentStr);
                            //      System.out.println("After Stem:" + CurrentStr);

                            if (!CurrentStr.isEmpty() && !CurrentStr.equals(" ") && CurrentStr.charAt(0) != CurrentStr.charAt(1)) {
                                System.out.println("Before remove dup:" + CurrentStr);
                                CurrentStr = CurrentStr.replaceAll("([\u0620-\u064A\u06C0-\u06CF])\\1+", "$1");
                                System.out.println("After remove dup:" + CurrentStr);
                            }

                        }
                    }
                    if (!CurrentStr.isEmpty() && !CurrentStr.equals(" ")) {
                        System.out.println("To Index:" + CurrentStr);

                        ArrayList<Integer> IndexWordsList = UploadMessageWords(CurrentStr);

                        for (int indexdStr : IndexWordsList) {
                            IndexMsg += indexdStr + " ";
                        }
                    }
                }

                if (!IndexMsg.isEmpty()) {
                    ParseObject parseIndexedWords = new ParseObject("TransDatabase");
                    parseIndexedWords.put("FilteredMsg", IndexMsg);
                    parseIndexedWords.saveInBackground();
                }
            }

            ParseQuery<ParseObject> queryUpdate = ParseQuery.getQuery("LastWordIndex");
            ParseObject parseIndex = queryUpdate.get("Cb3oM2RsGL");

            parseIndex.put("LastIndex", Indexcounter);
            parseIndex.saveInBackground();

        } catch (Exception e) {
        }
    }
    
    
    
}
