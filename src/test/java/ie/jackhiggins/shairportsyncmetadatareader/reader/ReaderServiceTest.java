package ie.jackhiggins.shairportsyncmetadatareader.reader;

import ie.jackhiggins.shairportsyncmetadatareader.tracks.ActiveTrackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ReaderServiceTest {

    //todo: we need test cases here to cover mangled or malformed input

    @InjectMocks
    ReaderService readerService;

    @Mock
    ActiveTrackService activeTrackService;

    @Captor
    ArgumentCaptor<Track> trackChangeCaptor;

    @Test
    public void runReader_switchesTracks() throws IOException {
        readerService.streamLocation = "./src/test/resources/track_changes.log";
        readerService.runReader();

        verify(activeTrackService, times(14)).setCurrentTrack(trackChangeCaptor.capture());

        List<Track> allTrackChanges = trackChangeCaptor.getAllValues();

        Track camberwell = Track.builder()
                .title("Camberwell")
                .artist("#1 Dads")
                .album("About Face")
                .build();

        Track hookedOnYou = Track.builder()
                .artist("Cerrone")
                .album("The Remixes")
                .title("Hooked On You (The Reflex Revision)")
                .build();

        Track thisPlace = Track.builder()
                .title("This Place")
                .album("This Place")
                .artist("Retiree")
                .build();

        List<Track> expectedTrackChanges = Arrays.asList(
                camberwell,
                camberwell,
                camberwell,
                camberwell,
                hookedOnYou,
                hookedOnYou,
                hookedOnYou,
                thisPlace,
                thisPlace,
                thisPlace,
                thisPlace,
                thisPlace,
                thisPlace,
                thisPlace
        );

        assertThat(allTrackChanges, is(equalTo(expectedTrackChanges)));
    }
}
