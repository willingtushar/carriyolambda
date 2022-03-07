package org.carriyo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import org.carriyo.util.AmazonOpenSearchService;

import java.io.IOException;

public class DDBEventHandler implements RequestHandler<DynamodbEvent, String>
{
    public String handleRequest(DynamodbEvent ddbEvent, Context context) {
        System.out.println("number of records:" + ddbEvent.getRecords().size());
        System.out.println(ddbEvent);

        for (int i=0;i<ddbEvent.getRecords().size();i++) {
            try {
                new AmazonOpenSearchService().processData(ddbEvent.getRecords().get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Successfully processed " + ddbEvent.getRecords().size() + " records.";
    }
}
