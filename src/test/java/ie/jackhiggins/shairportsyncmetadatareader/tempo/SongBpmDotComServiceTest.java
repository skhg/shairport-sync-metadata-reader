package ie.jackhiggins.shairportsyncmetadatareader.tempo;

import com.github.tomakehurst.wiremock.WireMockServer;
import ie.jackhiggins.shairportsyncmetadatareader.tempo.songbpmdotcom.SongBpmDotComService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class SongBpmDotComServiceTest {

    WireMockServer wireMockServer = new WireMockServer();

    SongBpmDotComService songBpmDotComService;

    @BeforeEach
    public void setup(){
        wireMockServer.start();
        configureFor("localhost", 8080);

        songBpmDotComService = new SongBpmDotComService("http://localhost:8080");
    }

    @AfterEach
    public void tearDown(){
        wireMockServer.stop();
    }

    @Test
    public void getSongBpm_requestWithSampleData_makesCorrectRequest(){
        stubFor(post(urlMatching("/search"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("")
                ));

        songBpmDotComService.getSongBpm("test","test","test");

        verify(postRequestedFor(urlEqualTo("/search"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader(HttpHeaders.ACCEPT, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson("{\"query\":\"test - test\"}"))
        );
    }
}
