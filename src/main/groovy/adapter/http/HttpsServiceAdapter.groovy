package adapter.http

import groovy.util.logging.Slf4j
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils


@Slf4j
public class HttpsServiceAdapter {

    private final String url;
    private CloseableHttpClient httpclient
    private BasicCookieStore cookieStore


    public HttpsServiceAdapter(String url) {
        this.url = url;
    }


    public void connect() {
        log.info("Connecting to url: " + url);

        if (httpclient != null) {
            throw new Exception("Client is already connected. Connection may not be closed correctly or the instance is shared for different users.")
        }

        cookieStore = new BasicCookieStore();
        httpclient =  new HttpClients().createDefault();
    }

    public void disconnect() {
        if (httpclient != null) {
            httpclient.close();
            httpclient = null;
        }
        if (cookieStore != null) {
            cookieStore.clear();
        }
    }



    public List<Map<String, String>> callJsonServiceAsGET(String servicePath, String parameter,
                                                          final ResponseParser responseParser) {

        HttpGet request = new HttpGet(url+servicePath+parameter);
        // Create a custom response handler
        ResponseHandler responseHandler = new ResponseHandler() {
            @Override
            def handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    def responseString = EntityUtils.toString(entity)
                    return entity != null ? responseParser.parseResponseGET(responseString) : null;
                } else {
                    HttpEntity entity = response.getEntity();
                    EntityUtils.consume(entity);
                    throw new ClientProtocolException("Unexpected response status: " + response.getStatusLine());
                }
            }

        };

        try {
            return ((List<Map<String, String>>) (( httpclient).execute(request, responseHandler)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
