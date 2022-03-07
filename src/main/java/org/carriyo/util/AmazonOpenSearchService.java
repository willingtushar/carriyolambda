package org.carriyo.util;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.opensearch.client.Request;
import org.opensearch.client.Response;
import org.opensearch.client.RestClient;

import java.io.IOException;

import static org.carriyo.util.UserTransformer.getDocumentJsonString;

public class AmazonOpenSearchService {

    // todo: move these values in application.yaml file
    private static final String serviceName = "es";
    private static final String region = "ap-south-1";
    private static final String host = "https://search-carriyo-es-lt7cgh3srz3tlchj4qqlg5ddoe.ap-south-1.es.amazonaws.com";
    private static final String indexingPath = "/lambda-index/_doc";
    private static final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

    public void processData(DynamodbEvent.DynamodbStreamRecord record) throws IOException {
        RestClient searchClient = getESClient(serviceName, region);
        String id = record.getDynamodb().getKeys().get("userid").getS();

        if(record.getEventName().equals("REMOVE")){
            Request request = new Request("DELETE", indexingPath + "/" + id);
            Response response = searchClient.performRequest(request);
            System.out.println(response.toString());
        }else{
            Request request = new Request("PUT", indexingPath + "/" + id);
            HttpEntity entity = new NStringEntity(getDocumentJsonString(record.getDynamodb().getNewImage()), ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            Response response = searchClient.performRequest(request);
            System.out.println(response.toString());
        }
    }

    public static RestClient getESClient(String serviceName, String region) {
        AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(serviceName);
        signer.setRegionName(region);
        HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer, credentialsProvider);
        return RestClient.builder(HttpHost.create(host)).setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)).build();
    }
}
