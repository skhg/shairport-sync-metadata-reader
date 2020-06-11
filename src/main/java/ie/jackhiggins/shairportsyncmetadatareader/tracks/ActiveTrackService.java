package ie.jackhiggins.shairportsyncmetadatareader.tracks;

import ie.jackhiggins.shairportsyncmetadatareader.reader.Track;
import ie.jackhiggins.shairportsyncmetadatareader.tempo.TempoRetrievalService;
import ie.jackhiggins.shairportsyncmetadatareader.visualiser.VisualiserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ActiveTrackService {

    private final TempoRetrievalService tempoRetrievalService;
    private final VisualiserService visualiserService;

    Track currentTrack = new Track();
    Optional<Integer> currentBpm = Optional.empty();

    public ActiveTrackService(TempoRetrievalService tempoRetrievalService, VisualiserService visualiserService){
        this.tempoRetrievalService = tempoRetrievalService;
        this.visualiserService = visualiserService;
    }

    public Track getCurrentTrack(){
        return currentTrack.toBuilder().build();
    }

    public Optional<Integer> getCurrentBpm(){
        return currentBpm;
    }

    public void setCurrentTrack(Track newTrack){
        if(!newTrack.equals(currentTrack)){
            currentTrack = newTrack;
            log.info("New track playing: {}", newTrack);
            onTrackChanged();
        }
    }

    private void onTrackChanged(){
        if(!currentTrack.isValid()){
            return;
        }

        tempoRetrievalService.getSongBpm(
                currentTrack.getArtist().orElse(StringUtils.EMPTY),
                currentTrack.getAlbum().orElse(StringUtils.EMPTY),
                currentTrack.getTitle().orElse(StringUtils.EMPTY))
                .whenComplete((result, ex) -> setMetadataBpm(result));
    }

    void setMetadataBpm(Integer bpm){
        log.info("Got BPM of {} for playing track", bpm);
        currentBpm = Optional.ofNullable(bpm);
        currentBpm.ifPresent(visualiserService::sendCurrentTempo);
        // This would be more suitable as a pub/sub model but we only have one client for now
    }
}
