# FinalProjectICS4U

Documentation Links:  
[Feasability Study](https://docs.google.com/document/d/1z3Mruk432Xv6suJtBWFKQ9512MqgPJRksRtH97oHaEU/edit?usp=sharing)  
[Testing Report](https://docs.google.com/document/d/1AZFFl9AR-EfF3B0ZXs2_IG1beyGUZanUzXr49nYu2Jw/edit?usp=sharing)

Must change the following variables:  
In CustomerBillingSystem.java  
String apiUrl = "https://open.er-api.com/v6/latest/" + baseCurrency + "?apikey=Insert Exchange Rate Api Key Here";  
public static final String ACCOUNT_SID = "Insert Twilio SID Here"; //api keys for Twilio (text messages)  
public static final String AUTH_TOKEN = "Insert Auth Token Here";  
  
In SendEmail.java  
final String senderEmail = "Insert Email Here"; // change email address  
final String senderPassword = "Insert Password Here"; // change password  
