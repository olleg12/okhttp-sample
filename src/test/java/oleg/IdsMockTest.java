package oleg;


import okhttp3.*;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;


public class IdsMockTest {
    @Test
    public void createCustomerTest() throws IOException, InterruptedException {

        MockWebServer server = new MockWebServer();
        MockResponse mockResponse = new MockResponse().setBody("{\n" +
                "  \"id\": \"123456\",\n" +
                "  \"href\": \"/v1/customers/123456\",\n" +
                "  \"name\": \"Initech\",\n" +
                "  \"createdAt\": \"1970-01-01 00:00:00\",\n" +
                "  \"modifiedAt\": \"1970-01-01 00:00:00\"\n" +
                "}").setResponseCode(500);


//        server.setDispatcher(dispatcher);
        server.enqueue(new MockResponse().setResponseCode(500));
        server.start(9233);
        HttpUrl baseUrl = server.url("/v1/customers/");
        OkHttpClient client = new OkHttpClient();
        Request request=new Request.Builder()
                .url(baseUrl)
                .post(
                        RequestBody.create(
                                MediaType.parse("plain/text"),
                                "{\n" +
                                        "  \"id\": \"123456\",\n" +
                                        "  \"name\": \"Initech\"\n" +
                                        "}"
                        )
                )
                .build();
        Response response = client.newCall(request).execute();
        String s1 = baseUrl.toString();
        URL url = new URL(s1);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        RecordedRequest recordedRequest = server.takeRequest();
        String s = recordedRequest.getBody().readUtf8();
        assertEquals(
                "{\n" +
                        "  \"id\": \"123456\",\n" +
                        "  \"name\": \"Initech\"\n" +
                        "}",
                s);
        server.shutdown();
    }


}
