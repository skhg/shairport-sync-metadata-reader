package ie.jackhiggins.shairportsyncmetadatareader.tracks;

import ie.jackhiggins.shairportsyncmetadatareader.reader.Track;
import ie.jackhiggins.shairportsyncmetadatareader.tempo.TempoRetrievalService;
import ie.jackhiggins.shairportsyncmetadatareader.visualiser.VisualiserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ActiveTrackServiceTest {

    @Mock
    TempoRetrievalService tempoRetrievalService;

    @Mock
    VisualiserService visualiserService;

    @InjectMocks
    ActiveTrackService activeTrackService;

    @Test
    public void setCurrentTrack_noChange_doesNotCallOtherServices(){
        activeTrackService.currentTrack = Track.builder()
                .album("Dance Moves")
                .artist("Franc Moody")
                .title("Dopamine")
                .build();

        activeTrackService.setCurrentTrack(Track.builder()
                .album("Dance Moves")
                .artist("Franc Moody")
                .title("Dopamine")
                .build());

        verifyNoInteractions(visualiserService);
        verifyNoInteractions(tempoRetrievalService);
    }

    @Test
    public void setCurrentTrack_nextTrackHasNoData_doesNotCallOtherServices(){
        activeTrackService.currentTrack = Track.builder()
                .album("Dance Moves")
                .artist("Franc Moody")
                .title("Dopamine")
                .build();

        activeTrackService.setCurrentTrack(Track.builder()
                .album(StringUtils.EMPTY)
                .artist(StringUtils.EMPTY)
                .title(StringUtils.EMPTY)
                .build());

        verifyNoInteractions(visualiserService);
        verifyNoInteractions(tempoRetrievalService);
    }

    @Test
    public void setCurrentTrack_trackChanged_getsNewBpmAndSendsIt(){
        CompletableFuture<Optional<Integer>> completedResult = new CompletableFuture<>();
        completedResult.complete(Optional.of(1));

        when(tempoRetrievalService.getSongBpm(any(), any(), any())).thenReturn(completedResult);

        activeTrackService.setCurrentTrack(Track.builder()
                .album("Part 1 Everything Not Saved Will Be Lost")
                .artist("Foals")
                .title("Exits")
                .build());

        verify(tempoRetrievalService, times(1)).getSongBpm("Foals", "Part 1 Everything Not Saved Will Be Lost", "Exits");
        verify(visualiserService, times(1)).sendCurrentTempo(1);
    }
}
