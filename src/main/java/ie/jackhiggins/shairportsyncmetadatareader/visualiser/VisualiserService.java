package ie.jackhiggins.shairportsyncmetadatareader.visualiser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.SignalType;

import static java.time.Duration.ofSeconds;

@Slf4j
@Service
public class VisualiserService {

    private final WebClient client;

    @Autowired
    public VisualiserService(@Value("${visualiser.baseurl}") String baseUrl){
        client = WebClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void sendCurrentTempo(Integer bpm){
        WebClient.RequestBodyUriSpec post = client.put();

        RequestBody body = RequestBody.builder()
                 .bpm(bpm)
                 .build();

        post.uri("/tempo");
        post.bodyValue(body);
        post.exchange()
                .timeout(ofSeconds(5))
                .doFinally(this::onTempoSendFinished)
                .block();
    }

    private void onTempoSendFinished(SignalType signalType){
        log.info("Tempo sending finished {}", signalType);
    }
}
