/*

GPS Shield Example

This example shows an application on 1Sheeld's GPS shield.

By using this example, you can send an SMS when the smartphone
is in a hundred meters distance from a fixed coordinates you
provide.

OPTIONAL:
To reduce the library compiled size and limit its memory usage, you
can specify which shields you want to include in your sketch by
defining CUSTOM_SETTINGS and the shields respective INCLUDE_ define. 

*/
#define CUSTOM_SETTINGS
#define INCLUDE_GPS_SHIELD
#define INCLUDE_TERMINAL_SHIELD
#define INCLUDE_INTERNET_SHIELD
/* Include 1Sheeld library.*/
#include <OneSheeld.h>

HttpRequest request("https://uber-clone-project-484d8.firebaseio.com/testgps.json");


/* Define a boolean flag. */
boolean isInRange = false;
float lat;
float lon;
char latBuffer[20];
char lonBuffer[20];
char myBuffer[50];
String Lat;
String Lon;

void setup() 
{
  /* Start communication.*/
  OneSheeld.begin();
  /* Subscribe to success callback for the request. */
  request.setOnSuccess(&onSuccess);
  /* Subscribe to json value replies. */
  request.getResponse().setOnJsonResponse(&onJsonReply);
  /* Subscribe to response errors. */
  request.getResponse().setOnError(&onResponseError);
  /* Subscribe to Internet errors. */
  Internet.setOnError(&onInternetError);  
}

void loop()
{
  lat = GPS.getLatitude();
  lon = GPS.getLongitude();
   /* Always check if the smartphone's GPS and a given latitude and longitude are in a range of 100 meters. */
  Lat = lat;
  Lon = lon;

  Lat.toCharArray(latBuffer, 20);
  Lon.toCharArray(lonBuffer, 20);

  
  //request.addParameter("Latitude", latBuffer);
  //request.addParameter("Longtitude", lonBuffer);
  
  Terminal.print("Latitude: ");
  Terminal.println(lat);
  Terminal.print("Longitude: ");
  Terminal.println(lon);
  Terminal.println();

  //request.addHeader("Content-Type","application/json");
  //request.setContentType("application/json" );
  String req = "{\"latitude\": "+ Lat +", \"longitude\": "+ Lon +"}";
  req.toCharArray(myBuffer, 50);
  request.addRawData(myBuffer);
  Internet.performPut(request);

  request.setOnSuccess(&onSuccess);
  request.getResponse().setOnJsonResponse(&onJsonReply);
  
delay(5000);
}

void onSuccess(HttpResponse & response)
{
  /* Using the response to query the Json chain till the required value. */
  response["rows"][0]["elements"][0]["duration_in_traffic"]["value"].query();
}

void onJsonReply(JsonKeyChain & hell,char * output)
{
  /* Getting the value and transform it to integer to deal with. */
  int eta = atoi(output);
  int mins = eta/60;

  /* Checking the ETA "estimated time of arrival" to the destination. */
  if(mins>=30) 
  {
    
  }
  else if(mins<30)
  {
    
  }
  /* Terminal line for debugging. */
  Terminal.println(eta/60);
}


/* Error handling functions. */
void onResponseError(int errorNumber)
{
  /* Print out error Number.*/
  Terminal.print("Response error:");
  switch(errorNumber)
  {
    case INDEX_OUT_OF_BOUNDS: Terminal.println("INDEX_OUT_OF_BOUNDS");break;
    case RESPONSE_CAN_NOT_BE_FOUND: Terminal.println("RESPONSE_CAN_NOT_BE_FOUND");break;
    case HEADER_CAN_NOT_BE_FOUND: Terminal.println("HEADER_CAN_NOT_BE_FOUND");break;
    case NO_ENOUGH_BYTES: Terminal.println("NO_ENOUGH_BYTES");break;
    case REQUEST_HAS_NO_RESPONSE: Terminal.println("REQUEST_HAS_NO_RESPONSE");break;
    case SIZE_OF_REQUEST_CAN_NOT_BE_ZERO: Terminal.println("SIZE_OF_REQUEST_CAN_NOT_BE_ZERO");break;
    case UNSUPPORTED_HTTP_ENTITY: Terminal.println("UNSUPPORTED_HTTP_ENTITY");break;
    case JSON_KEYCHAIN_IS_WRONG: Terminal.println("JSON_KEYCHAIN_IS_WRONG");break;
  }
}

void onInternetError(int requestId, int errorNumber)
{
  /* Print out error Number.*/
  Terminal.print("Request id:");
  Terminal.println(requestId);
  Terminal.print("Internet error:");
  switch(errorNumber)
  {
    case REQUEST_CAN_NOT_BE_FOUND: Terminal.println("REQUEST_CAN_NOT_BE_FOUND");break;
    case NOT_CONNECTED_TO_NETWORK: Terminal.println("NOT_CONNECTED_TO_NETWORK");break;
    case URL_IS_NOT_FOUND: Terminal.println("URL_IS_NOT_FOUND");break;
    case ALREADY_EXECUTING_REQUEST: Terminal.println("ALREADY_EXECUTING_REQUEST");break;
    case URL_IS_WRONG: Terminal.println("URL_IS_WRONG");break;
  }
}
