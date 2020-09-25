package ie.jackhiggins.shairportsyncmetadatareader.tracks;

import ie.jackhiggins.shairportsyncmetadatareader.reader.Track;
import ie.jackhiggins.shairportsyncmetadatareader.tempo.TempoRetrievalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ActiveTrackService {

    private final TempoRetrievalService tempoRetrievalService;

    Track currentTrack = new Track();
    Optional<Integer> currentBpm = Optional.empty();

    public ActiveTrackService(TempoRetrievalService tempoRetrievalService){
        this.tempoRetrievalService = tempoRetrievalService;
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
        if(currentTrack.isNotBlank()){
            tempoRetrievalService.getSongBpm(
                    currentTrack.getArtist().orElse(StringUtils.EMPTY),
                    currentTrack.getAlbum().orElse(StringUtils.EMPTY),
                    currentTrack.getTitle().orElse(StringUtils.EMPTY))
                    .whenComplete((result, ex) -> setMetadataBpm(result));
        }
    }

    void setMetadataBpm(Integer bpm){
        log.info("Got BPM of {} for playing track", bpm);
        currentBpm = Optional.ofNullable(bpm);
    }
}
