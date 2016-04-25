**Android Instant Messaging application with Arabic spam detection**

This is an Android IM app project that follows this Tutorial - https://www.sinch.com/tutorials/android-messaging-tutorial-using-sinch-and-parse/

The tutorial will walk you through building an instant messaging app using Sinch Android SDK and Parse cloud

Set up developer accounts for Sinch(messaging SDK) and Parse(BaaS). For both Sinch and Parse, you will need to create an app in the developer portal. Hold on to the app keys and secrets that these services generate; you will need them in the application.

-

We update the tutorial to include Arabic spam message detection.

 The application will alert the user for any upcoming spam message.
 
-

The detection process is based on Textmining approach:
-

**Text Manipulation**
- Collect real spam messages from IM
- Remove ignore words
- Normalize and stem
- Index words saved on DB

**Data Mining technique**
- Apriori Algorithm
- Infer Association Rules and save in DB

-

**Requirements**

- Two Android devices (or emulators) for testing
- Android Studio (download at https://developer.android.com/sdk/installing/studio.html)
- Andoid API 19 
- Some coding experience (preferably Java)

