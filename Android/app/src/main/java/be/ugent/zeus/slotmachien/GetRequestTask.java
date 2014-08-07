package be.ugent.zeus.slotmachien;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

public class GetRequestTask extends RequestTask {

    public GetRequestTask(MainActivity main) {
        super(main);
    }

    @Override
    protected HttpResponse doInBackground(String... params) {
        HttpGet request = new HttpGet(SLOTMACHIEN_URL);

        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
