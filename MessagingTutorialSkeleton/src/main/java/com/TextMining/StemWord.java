package com.TextMining;

/**
 * Created by Sakurmi on 10/29/2015.
 */
public class StemWord {

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

      //  System.out.println(CurrentString);

        CurrentString = CurrentString.substring(0, CurrentString.length() - 1);

        return CurrentString;
    }

    private static String RemovePrefix(String word)
    {
        String modifiedWord = word;
        boolean isDone = false;

        for(int index= 0 ; index < prefixes.length; ++index)
        {
            if (word.startsWith(prefixes[index]))
                if (prefixes[index].equals(WAW)&& !(word.length() > 3) && !isDone) {
                    isDone = true;
        //            System.out.println(prefixes[index]);
                    modifiedWord = word.substring(word.indexOf(prefixes[index]) + 1);
                }
                else if ((prefixes[index].length() + 2 < word.length()) ) {
                    isDone = true;
          //          System.out.println(prefixes[index]);
                    modifiedWord = word.substring(word.indexOf(prefixes[index]) + prefixes[index].length());
                }
        }


        return modifiedWord;
    }

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

            //        System.out.println(suffixes[index] + ":" + word.lastIndexOf(suffixes[index]));
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
