package com.TextMining;

/**
 * Created by Marwa Khan on 1/4/2016.
 * Class Description: This class will stem each word to its root 
 * Note: the utilized stemmer is based on Apache stemmer, but with view changes 
 */
public class StemWord {

    // variables Initilization 
    public static final char ALEF = '\u0627';
    public static final char BEH = '\u0628';
    public static final char TEH = '\u062A';
    public static final char FEH = '\u0641';
    public static final char KAF = '\u0643';
    public static final char LAM = '\u0644';
    public static final char NOON = '\u0646';
    public static final char HEH = '\u0647';
    public static final char WAW = '\u0648';
    public static final char YEH = '\u064A';


    public static final String prefixes[] = {
            ("" + WAW + LAM + LAM), ("" + WAW + ALEF + LAM), ("" + BEH + ALEF + LAM),
            ("" + KAF + ALEF + LAM), ("" + FEH + ALEF + LAM),
            ("" + ALEF + LAM),("" + LAM + LAM),("" + WAW)};

    public static final String suffixes[] = {
            ("" + WAW + ALEF),
            ("" + HEH + ALEF),
            ("" + ALEF + NOON),
            ("" + ALEF + TEH), ("" + WAW + NOON), ("" + YEH + NOON ),("" + YEH + HEH), ("" + HEH), ("" + YEH)
    };


    // class constructer
    public static String Stem(String str)
    {
        String CurrentStr = "";


        if (!str.contains(" ")) {
            CurrentStr = RemovePrefix(str);
            CurrentStr = RemoveSuffix(CurrentStr);
        }
        else
        {
            CurrentStr = StemConnectedWords(str);
        }
        return CurrentStr;
    }

    //Method to stem a given word 
    // Input: String 
    // Output: String 
    private static String StemConnectedWords(String str)
    {
        String[] StrArray = str.split(" ");

        String CurrentString = "";
        for(int index= 0 ; index < StrArray.length; ++index)
        {
            StrArray[index] = RemovePrefix(StrArray[index]);
            StrArray[index] = RemoveSuffix(StrArray[index]);

            CurrentString += StrArray[index] + " ";
        }
        CurrentString = CurrentString.substring(0, CurrentString.length() - 1);

        return CurrentString;
    }

    // Method to remove prefix 
    private static String RemovePrefix(String word)
    {
        String modifiedWord = word;
        boolean isDone = false;

        for(int index= 0 ; index < prefixes.length; ++index)
        {
            if (word.startsWith(prefixes[index]))
                if (prefixes[index].equals(WAW)&& !(word.length() > 3) && !isDone) {
                    isDone = true;
                    modifiedWord = word.substring(word.indexOf(prefixes[index]) + 1);
                }
                else if ((prefixes[index].length() + 2 < word.length()) ) {
                    isDone = true;
                    modifiedWord = word.substring(word.indexOf(prefixes[index]) + prefixes[index].length());
                }
        }
        return modifiedWord;
    }

    // method to remove suffix
    private static String RemoveSuffix(String word)
    {
        String modifiedWord = word;

        boolean isDone = false;

        for(int index= 0 ; index < suffixes.length; ++index)
        {
            if (word.endsWith(suffixes[index]) ) {


                if ((suffixes[index].equals("" + ALEF + TEH) || suffixes[index].equals("" + ALEF + NOON)) )
                {
                    if((suffixes[index].length() + 3 < word.length()) && !isDone){

                        isDone = true;
                        modifiedWord = word.substring(0, word.lastIndexOf(suffixes[index]));
                    }
                    else
                        isDone = true;

                }
                else if (suffixes[index].length() + 2 < word.length() && !isDone) {

                    isDone = true;
                    modifiedWord = word.substring(0, word.lastIndexOf(suffixes[index]));
                }
            }
        }

        if (modifiedWord.endsWith("ؤ") || modifiedWord.endsWith("ئ")) {
            modifiedWord = modifiedWord.replace("ؤ", "ء");
            modifiedWord = modifiedWord.replace("ئ", "ء");
        }

        return modifiedWord;
    }
}
