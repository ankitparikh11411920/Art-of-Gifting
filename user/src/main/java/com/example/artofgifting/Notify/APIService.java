package com.example.artofgifting.Notify;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAXzAAmBk:APA91bHmbPJBjG9y4wLpLSyHZ1BK7wpXVI3KBUj9xpFgTHWGFwdT44fgpbS7q1LA2Q4Q5RH5Ka6tJerM-wb2GwJ9Bw7EgLItcF2yPBSc0ziJZlWXbSfx1if2ZzU89P9ovSb2sl9BT0sB" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
