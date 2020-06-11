package ie.jackhiggins.shairportsyncmetadatareader.tempo.songbpmdotcom;

import ie.jackhiggins.shairportsyncmetadatareader.tempo.TempoRetrievalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@Profile("production")
public class SongBpmDotComService implements TempoRetrievalService {

    private final WebClient client;

    @Autowired
    public SongBpmDotComService(@Value("${songbpmdotcom-api.baseurl}") String apiBaseUrl){
        client = WebClient
                .builder()
                .baseUrl(apiBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Async
    @Override
    public CompletableFuture<Integer> getSongBpm(String artist, String album, String title) {
        WebClient.RequestBodyUriSpec post = client.post();

        RequestBody body = RequestBody.builder()
                .query(RequestBody.formatQueryString(artist, album, title))
                .build();

        post.uri("/search");
        post.bodyValue(body);

        WebClient.ResponseSpec retrieve = post.retrieve();
        Response searchResponse = retrieve.bodyToMono(Response.class)
                .block();

        //todo handle failed requests

        log.debug("{}", searchResponse);

        //todo handle empty response

        if(searchResponse!=null){
            return CompletableFuture.completedFuture(
                    searchResponse.getSearch()
                            .getSongs().stream()
                            .findFirst()
                            .map(Song::getTempo)
                            .orElse(null));
        }else{
            CompletableFuture<Integer> failedFuture = CompletableFuture.completedFuture(null);
            failedFuture.cancel(true);
            return failedFuture;
        }
    }
}
