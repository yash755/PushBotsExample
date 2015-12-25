# PushBotsExample
Full Implementation of Push Bots

##First Refer to https://pushbots.com/developer/docs/android for creating Create GCM Sender ID & GCM API Key     
   
## Also make new project on PushBot to get AppID


####Then you have to:
     Go to Res folder -> Values --> Open pushbots and change these contents. 
     Change your app ID and Sender ID.
    
    
    <string name="pb_appid">567d7d0f177959a5518b456a</string>
    <string name="pb_senderid">268834069533</string>
    
     
     Go to SendNotification.java Activity
     Change value of below lines with your.
     
     headers.put("x-pushbots-appid","567d7d0f177959a5518b456a");
     headers.put("x-pushbots-secret","4f6d1eaa3ce697bb64a40021864b2ea9");
     
     
####Note you can run it without any changes also,but in that case my GCM Sender ID & GCM API Key will be used.
     
     
     

####Clone and run 

     Rest you will be able to understand by urself.It's very easy
     Dummy tags are taken for testing
     

Volley module is imported for sending request.

     You are most welcomed to raise issues or solve bugs.
    
    

     

