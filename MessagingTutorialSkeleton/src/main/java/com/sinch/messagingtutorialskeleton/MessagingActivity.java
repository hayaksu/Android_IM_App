package com.sinch.messagingtutorialskeleton;

        //Importing class
        //app.Activity;
        //app.AlertDialog;
        //content.ComponentName;
        //content.DialogInterface;
        //content.Intent;
        //content.ServiceConnection;
        //os.Bundle;
        //os.IBinder;
        //android.view.View;
         //android.widget.EditText;
         //android.widget.ListView;
         //android.widget.Toast;
        
        
        
        
        
        
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.ComponentName;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.ServiceConnection;
        import android.os.Bundle;
        import android.os.IBinder;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.TextMining.PrepareData;
        import com.example.messagingtutorialskeleton.R;
        import com.parse.FindCallback;
        import com.parse.ParseException;
        import com.parse.ParseObject;
        import com.parse.ParseQuery;
        import com.parse.ParseUser;
        import com.sinch.android.rtc.PushPair;
        import com.sinch.android.rtc.messaging.Message;
        import com.sinch.android.rtc.messaging.MessageClient;
        import com.sinch.android.rtc.messaging.MessageClientListener;
        import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
        import com.sinch.android.rtc.messaging.MessageFailureInfo;
        import com.sinch.android.rtc.messaging.WritableMessage;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;


// puplic class
public class MessagingActivity extends Activity {

    private String recipientId;
    private EditText messageBodyField;
    private String messageBody;
    private MessageService.MessageServiceInterface messageService;
    private ListView messagesList;
    private String currentUserId;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private MessageClientListener messageClientListener = new MyMessageClientListener();
     MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);

        bindService(new Intent(this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);

        Intent intent = getIntent();
        recipientId = intent.getStringExtra("RECIPIENT_ID");
        currentUserId = ParseUser.getCurrentUser().getObjectId();

        messagesList = (ListView) findViewById(R.id.listMessages);
        messageAdapter = new MessageAdapter(this);
        messagesList.setAdapter(messageAdapter);
        populateMessageHistory();

        messageBodyField = (EditText) findViewById(R.id.messageBodyField);

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    //get previous messages from parse & display
    private void populateMessageHistory() {
        String[] userIds = {currentUserId, recipientId};
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
        query.whereContainedIn("senderId", Arrays.asList(userIds));
        query.whereContainedIn("recipientId", Arrays.asList(userIds));
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messageList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < messageList.size(); i++) {
                        WritableMessage message = new WritableMessage(messageList.get(i).get("recipientId").toString(), messageList.get(i).get("messageText").toString());
                        if (messageList.get(i).get("senderId").toString().equals(currentUserId)) {
                            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_OUTGOING);
                        } else {
                            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
                        }
                    }
                }
            }
        });
    }

    private void sendMessage() {
        messageBody = messageBodyField.getText().toString();
        if (messageBody.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_LONG).show();
            return;
        }

        messageService.sendMessage(recipientId, messageBody);
        messageBodyField.setText("");
    }

    @Override
    public void onDestroy() {
        messageService.removeMessageClientListener(messageClientListener);
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messageService = (MessageService.MessageServiceInterface) iBinder;
            messageService.addMessageClientListener(messageClientListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messageService = null;
        }
    }

    private class MyMessageClientListener implements MessageClientListener {
        @Override
        public void onMessageFailed(MessageClient client, Message message,
                                    MessageFailureInfo failureInfo) {
            Toast.makeText(MessagingActivity.this, "Message failed to send: "+failureInfo, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onIncomingMessage(MessageClient client, final Message message) {
            if (message.getSenderId().equals(recipientId)) {

                final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
                messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_INCOMING);

                String IndexedMessage = PrepareData.AddaConversation(writableMessage.getTextBody());

                if(CheckMessageForSpam(IndexedMessage)) {
       
                    AlertDialog alertDialog = new AlertDialog.Builder(MessagingActivity.this).create();
                    alertDialog.setTitle("تنبيه");
                    alertDialog.setMessage("هذه الرسالة مشبوهه، يرجى الحذر من الأرقام والروابط.");
        
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "موافق",
        
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
        
                    alertDialog.show();

                }//end if



            }
        }

        @Override
        public void onMessageSent(MessageClient client, Message message, String recipientId) {

System.out.println("onMessageSent");
System.out.println("message.getRecipientIds().get(0) "+message.getRecipientIds().get(0));

            final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());

            //Modified by haya
            //only add message to parse database if it doesn't already exist there
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
            query.whereEqualTo("sinchId", message.getMessageId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> messageList, ParseException e) {
                    if (e == null) {
                        if (messageList.size() == 0) {
                            ParseObject parseMessage = new ParseObject("ParseMessage");
                            parseMessage.put("senderId", currentUserId);
                            parseMessage.put("recipientId", writableMessage.getRecipientIds().get(0));
                            parseMessage.put("messageText", writableMessage.getTextBody());
                            parseMessage.put("sinchId", writableMessage.getMessageId());
                            parseMessage.saveInBackground();
                            messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_OUTGOING);

                        }
                    }
                }
            });
        }

        public boolean CheckMessageForSpam(String str) {
            boolean FoundPhishingPattern = false;

            ParseQuery<ParseObject> queryToIndex = ParseQuery.getQuery("PhishingPatternDB");
            ArrayList<String> PhishingPatterns = new ArrayList<String>();

            try {
                List<ParseObject> PhishingPatternList = queryToIndex.find();

                for (ParseObject object : PhishingPatternList) {
                    PhishingPatterns.add(object.getString("PhishingPatternMsg"));


                    for (int index = 0; index < PhishingPatterns.size(); index++) {
                        String[] currentPattern = PhishingPatterns.get(index).replace("[", "").replace("]", "").split(", ");

                        Pattern p;
                        Matcher m;
                        for (int wordIndex = 0; wordIndex < currentPattern.length; wordIndex++) {
                            
                            // just try to find a match
                            p = Pattern.compile("(\\s*(\\b" + currentPattern[wordIndex] + ")\\s*)");
                            m = p.matcher(str);

                            if (!m.find()) {
                               // if not found, exit the inner loop and take the next pattern in the outer loop
                                break;
                            }
                            if (wordIndex == (currentPattern.length - 1)) {
                                FoundPhishingPattern = true;
                                break;
                            }

                        }

                        if (FoundPhishingPattern) {
                            // System.out.println("Inside 3 break");

                            break;
                        }
                    }
                }// end first for
            }
            
            
                // alert message 
                
                   catch (ParseException e) {

                AlertDialog alertDialog = new AlertDialog.Builder(MessagingActivity.this).create();
                alertDialog.setTitle("تنبيه");
                alertDialog.setMessage("حدث خطأ ما !");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "موافق",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            
            return FoundPhishingPattern;
        }//end method
        @Override
        public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {}

        @Override
        public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {}
    }
}
