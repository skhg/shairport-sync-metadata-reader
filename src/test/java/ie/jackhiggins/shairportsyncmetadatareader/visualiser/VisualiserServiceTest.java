package ie.jackhiggins.shairportsyncmetadatareader.visualiser;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class VisualiserServiceTest {

    WireMockServer wireMockServer = new WireMockServer();

    VisualiserService visualiserService;

    @BeforeEach
    public void setup(){
        wireMockServer.start();
        configureFor("localhost", 8080);

        visualiserService = new VisualiserService("http://localhost:8080");
    }

    @AfterEach
    public void tearDown(){
        wireMockServer.stop();
    }

    @Test
    public void sendCurrentTempo_sendsPostRequestInCorrectFormat(){
        visualiserService.sendCurrentTempo(123);

        verify(postRequestedFor(urlEqualTo("/tempo"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson("{\"bpm\":123}"))
        );
    }
}
