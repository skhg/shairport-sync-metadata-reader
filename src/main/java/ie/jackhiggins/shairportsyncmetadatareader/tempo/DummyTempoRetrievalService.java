package ie.jackhiggins.shairportsyncmetadatareader.tempo;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Profile("!production")
public class DummyTempoRetrievalService implements TempoRetrievalService {

    @Async
    @Override
    public CompletableFuture<Integer> getSongBpm(String artist, String album, String title) {
        return CompletableFuture.completedFuture(100);
    }
}
