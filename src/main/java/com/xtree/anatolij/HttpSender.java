package com.xtree.anatolij;

import com.xtree.anatolij.data.Event;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HttpSender implements Closeable {
    public static final String USER_AGENT = "paranormal writer";
    final HttpClient client;
    String userId;
    String sessionId;

    public HttpSender() throws IOException, URISyntaxException {

        BasicCookieStore cookieStore = new BasicCookieStore();
        URI uri = new URI("http://www.prazskahlidka.cz/forums/ucp.php?mode=login");
        client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();

        List<NameValuePair> data = new ArrayList<NameValuePair>();


        data.add(new BasicNameValuePair("login", "Přihlásit se"));
        data.add(new BasicNameValuePair("redirect", "./ucp.php?mode=login"));
        data.add(new BasicNameValuePair("redirect", "index.php"));
        data.add(new BasicNameValuePair("sid", "00001111"));
        data.add(new BasicNameValuePair("username", "Anatoly_Kashpirovsky"));
        data.add(new BasicNameValuePair("password", "TruthForever"));

        HttpResponse responseLogin = postRequest(uri, data);
        Header[] cookieHeaders = responseLogin.getHeaders("Set-Cookie");
        userId = getCookie(cookieHeaders, "phpbb3_5jnti_u");
        sessionId = getCookie(cookieHeaders, "phpbb3_5jnti_sid");
    }

    private String getCookie(Header[] cookieHeaders, String name) {
        String user = null;
        for (Header cookieHeader : cookieHeaders) {
           // System.out.println(cookieHeader.getName() + ":" + cookieHeader.getValue());
            String[] cookieData = cookieHeader.getValue().split(";")[0].split("=");
            if (cookieData[0].equals(name)) {
                user = cookieData[1];
            }
        }
        return user;
    }

    public int submitTopic(String forum, String header, String message ) throws Exception {
        int counter = 0;
        int response = 0;
        while (counter < 3 && response != 200) {
            counter++;
            HttpResponse responsePost = topic(forum, header ,message);
            response = responsePost.getStatusLine().getStatusCode();
            if (response != 200) {
                log.info("status code isn't 200({}) retrying ", response);
                Thread.sleep(15_000);
            }
        }
        return response;
    }

    public int submitLog(String forum, String topic, String topicName, String message) throws Exception {
        int counter = 0;
        int response = 0;
        while (counter < 3 && response != 200) {
            counter++;
            HttpResponse responsePost = post(forum, topic, message, topicName);
            response = responsePost.getStatusLine().getStatusCode();
            if (response != 200) {
                log.info("status code isn't 200({}) retrying ", response);
                Thread.sleep(15_000);
            }
        }
        return response;
    }

    public void close() throws IOException {
        URI uri = null;
        try {
            uri = new URI("http://www.prazskahlidka.cz/forums/ucp.php?mode=logout&sid=" + sessionId);
            HttpResponse response = sendGet(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    private HttpResponse sendGet(URI uri) throws IOException {
        HttpGet request = new HttpGet(uri);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    private HttpResponse post(String forumId, String topicId, String message, String topicName) throws Exception {
        //send get and from response parse out these fields
        //URI uri = new URI("http://www.prazskahlidka.cz/forums/posting.php?mode=reply&f=33&t=2067");
        URI uri = new URI("http://www.prazskahlidka.cz/forums/posting.php?mode=reply&f=" + forumId + "&t=" + topicId);
        String creationTime = "";
        String formToken = "";
        String topicCurPostId = "";
        String lastClick = "";

        HttpGet request = new HttpGet(uri);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        //   StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            String valueCT = getValue(line, "creation_time");
            if (valueCT != null) {
                creationTime = valueCT;
            }
            String valueFT = getValue(line, "form_token");
            if (valueFT != null) {
                formToken = valueFT;
            }
            String valueTCPI = getValue(line, "topic_cur_post_id");
            if (valueTCPI != null) {
                topicCurPostId = valueTCPI;
            }
            String valueLC = getValue(line, "lastclick");
            if (valueLC != null) {
                lastClick = valueLC;
            }
            //http://stackoverflow.com/questions/10288946/get-form-token-and-time-creation-in-phpbb3
        }
        request.releaseConnection();
        //http://stackoverflow.com/questions/8513134/i-cant-create-thread-on-phpbb3-forum
        Thread.sleep(8_200);


        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("addbbcode20", "100"));
        data.add(new BasicNameValuePair("attach_sig", "on"));
        data.add(new BasicNameValuePair("creation_time", creationTime));
        data.add(new BasicNameValuePair("form_token", formToken));
        data.add(new BasicNameValuePair("lastclick", lastClick));
        data.add(new BasicNameValuePair("message", message));
        data.add(new BasicNameValuePair("post", "Submit"));
        data.add(new BasicNameValuePair("subject", topicName));
        data.add(new BasicNameValuePair("topic_cur_post_id", topicCurPostId));

        HttpResponse httpResponse = postRequest(uri, data);
        Thread.sleep(2_000);
        return httpResponse;
    }

    private HttpResponse topic(String forumId, String subject, String message) throws Exception {
        //send get and from response parse out these fields
        //URI uri = new URI("http://www.prazskahlidka.cz/forums/posting.php?mode=reply&f=33&t=2067");
        //http://www.prazskahlidka.cz/forums/posting.php?mode=post&f=33
        URI uri = new URI("http://www.prazskahlidka.cz/forums/posting.php?mode=post&f=" + forumId);
        String creationTime = "";
        String formToken = "";
        String topicCurPostId = "";
        String lastClick = "";

        HttpGet request = new HttpGet(uri);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        //   StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            String valueCT = getValue(line, "creation_time");
            if (valueCT != null) {
                creationTime = valueCT;
            }
            String valueFT = getValue(line, "form_token");
            if (valueFT != null) {
                formToken = valueFT;
            }
            String valueTCPI = getValue(line, "topic_cur_post_id");
            if (valueTCPI != null) {
                topicCurPostId = valueTCPI;
            }
            String valueLC = getValue(line, "lastclick");
            if (valueLC != null) {
                lastClick = valueLC;
            }
            //http://stackoverflow.com/questions/10288946/get-form-token-and-time-creation-in-phpbb3
        }
        request.releaseConnection();
        //http://stackoverflow.com/questions/8513134/i-cant-create-thread-on-phpbb3-forum
        Thread.sleep(8_200);


        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("addbbcode20", "100"));
        data.add(new BasicNameValuePair("attach_sig", "on"));
        data.add(new BasicNameValuePair("creation_time", creationTime));
        data.add(new BasicNameValuePair("form_token", formToken));
        data.add(new BasicNameValuePair("lastclick", lastClick));
        data.add(new BasicNameValuePair("message", message));
        data.add(new BasicNameValuePair("post", "Submit"));
        data.add(new BasicNameValuePair("subject", subject));
        data.add(new BasicNameValuePair("topic_type", "0"));
        data.add(new BasicNameValuePair("topic_time_limit", "0"));
        HttpResponse httpResponse = postRequest(uri, data);
        Thread.sleep(2_000);
        return httpResponse;
    }

    private HttpResponse postRequest(URI uri, List<NameValuePair> data) throws IOException {
        HttpPost post = new HttpPost(uri);
        post.setHeader("User-Agent", USER_AGENT);
        post.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));

        HttpResponse response = client.execute(post);
        post.releaseConnection();
        return response;
    }

    public static String getValue(String line, String name) {
        String xmlString = "input type=\"hidden\" name=\"" + name + "\"";
        if (line.contains(xmlString)) {
            int index = line.lastIndexOf(xmlString) + xmlString.length();
            int begin = line.indexOf("value=\"", index) + 7;
            int end = line.indexOf("\"", begin + 1);
            return line.substring(begin, end);
        }
        return null;
    }
}
