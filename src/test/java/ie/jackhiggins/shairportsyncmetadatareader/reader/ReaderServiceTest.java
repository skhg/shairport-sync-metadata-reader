package ie.jackhiggins.shairportsyncmetadatareader.reader;

import ie.jackhiggins.shairportsyncmetadatareader.tracks.ActiveTrackService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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

    String tempFilePath;
    RandomAccessFile simulatedStreamWriter;

    @Test
    public void runReader_clientPausesAndDisconnects_switchesTrackBackToNone() throws IOException {
        readerService.streamLocation = "./src/test/resources/play_pause_disconnect.log";
        readerService.onStreamEnded = ()-> readerService.onStreamLoop = () -> false; //We deliberately do not want to try to continue reading the stream

        readerService.runReader();

//        verify(activeTrackService, times(4)).playingTrack(trackChangeCaptor.capture());
//        verify(activeTrackService, times(1)).playEnded();
//
//        List<Track> allTrackChanges = trackChangeCaptor.getAllValues();
    }

    @Test
    public void runReader_switchesTracks() throws IOException {
        readerService.streamLocation = "./src/test/resources/track_changes.log";
        readerService.onStreamEnded = ()-> readerService.onStreamLoop = () -> false; //We deliberately do not want to try to continue reading the stream

        readerService.runReader();

        verify(activeTrackService, times(14)).playingTrack(trackChangeCaptor.capture());

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

    @Test
    public void onStreamLoop_returnsTrueByDefault(){
        assertThat(readerService.onStreamLoop.get(), is(true));
    }

    @Test
    public void onStreamEnded_keepsStreamLoopTrueByDefault(){
        readerService.retryWaitSeconds = 1;
        readerService.onStreamEnded.run();
        assertThat(readerService.onStreamLoop.get(), is(true));
    }

    @Test
    public void runReader_getsNullLine_continuesUntilDeliberatelyClosed() throws IOException {
        // Setup a temporary named pipe which will be immediately removed on exit
        tempFilePath = "/tmp/" + UUID.randomUUID().toString();
        String createNamedPipeCommand = "mkfifo "+tempFilePath;
        Runtime.getRuntime().exec(createNamedPipeCommand);


        try{
            simulatedStreamWriter = new RandomAccessFile(tempFilePath, "rw");

            //set up the event lambdas to point to test functions so we can exit when required
            readerService.streamLocation = tempFilePath;
            readerService.onStreamLoop = this::onTestStreamLoop;
            readerService.onStreamEnded = () -> testStreamEndedCount++;

            readerService.runReader();

            assertThat(testStreamLoopCount, is(equalTo(5)));
            assertThat(testStreamEndedCount, is(equalTo(2)));
        }finally{
            File fifoFile = new File(tempFilePath);
            fifoFile.deleteOnExit();
        }

    }

    private int testStreamLoopCount = 0;

    @SneakyThrows
    private Boolean onTestStreamLoop(){
        testStreamLoopCount++;

        switch(testStreamLoopCount){
            case 1: {
                //On the first loop we have a line to process
                simulatedStreamWriter.write("firstLine\n".getBytes(StandardCharsets.UTF_8));
                return true;
            }
            case 2: {
                // Second loop simulates a closed stream
                simulatedStreamWriter.close();
                return true;
            }
            case 3: {
                // Third loop re-opens the stream and writes another line
                simulatedStreamWriter = new RandomAccessFile(tempFilePath, "rw");
                simulatedStreamWriter.write("secondLine\n".getBytes(StandardCharsets.UTF_8));
                return true;
            }
            case 4: {
                // Fourth loop closes the stream again
                simulatedStreamWriter.close();
                return true;
            }
            case 5: {
                //Finally we don't need to continue any more so we can exit the while..true loop
                return false;
            }
            default: throw new IllegalStateException();
        }
    }
    private int testStreamEndedCount = 0;
}
