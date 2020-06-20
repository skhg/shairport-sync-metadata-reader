package ie.jackhiggins.shairportsyncmetadatareader.visualiser;

import com.github.tomakehurst.wiremock.WireMockServer;
import ie.jackhiggins.shairportsyncmetadatareader.reader.Track;
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
    public void sendCurrentTempo_sendsPutRequestInCorrectFormat(){
        visualiserService.sendCurrentTempo(123);

        verify(putRequestedFor(urlEqualTo("/tempo"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson("{\"bpm\":123}"))
        );
    }

    @Test
    public void sendCurrentTrack_sendsPutRequestInCorrectFormat(){
        visualiserService.sendCurrentTrack(Track.builder()
                .title("Bloodless")
                .album("My Finest Work Yet")
                .artist("Andrew Bird")
                .build());

        verify(putRequestedFor(urlEqualTo("/song"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson("{\"title\":\"Bloodless\",\"album\":\"My Finest Work Yet\",\"artist\":\"Andrew Bird\"}"))
        );
    }
}
