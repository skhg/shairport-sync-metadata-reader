package ie.jackhiggins.shairportsyncmetadatareader.tempo;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public interface TempoRetrievalService {

    @Async
    CompletableFuture<Optional<Integer>> getSongBpm(String artist, String album, String title);
}
