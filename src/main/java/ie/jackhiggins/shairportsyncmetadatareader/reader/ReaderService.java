package ie.jackhiggins.shairportsyncmetadatareader.reader;

import ie.jackhiggins.shairportsyncmetadatareader.tracks.ActiveTrackService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ReaderService {

    @Value("${shairport-metadata-stream.location}")
    String streamLocation;

    @Value("${shairport-metadata-stream.retryWaitSeconds:30}")
    int retryWaitSeconds;

    Supplier<Boolean> onStreamLoop = this::keepReading;
    Runnable onStreamEnded = this::streamEnded;

    final ActiveTrackService activeTrackService;

    Pattern itemPattern = Pattern.compile("<item><type>(\\w+)<\\/type><code>(\\w+)<\\/code>");
    Pattern dataPattern = Pattern.compile("(.+)<\\/data><\\/item>");

    private Optional<MetadataCodes> nextCode = Optional.empty();
    private Optional<MetadataTypes> nextType = Optional.empty();

    private Track playingTrack = new Track();

    private boolean keepReading(){
        return true;
    }

    @SneakyThrows
    private void streamEnded(){
        log.debug("Metadata stream has ended.");
        TimeUnit.SECONDS.sleep(retryWaitSeconds);
    }

    public ReaderService(ActiveTrackService activeTrackService) {
        this.activeTrackService = activeTrackService;
    }

    @Async
    public void runReader() throws IOException {
        RandomAccessFile pipeIn = new RandomAccessFile(streamLocation, "r");

        while(onStreamLoop.get()){
            String line = pipeIn.readLine();

            if(line == null){
                onStreamEnded.run();
            }else{
                processRawLine(line);
            }
        }
    }

    private void processRawLine(String rawLine){
        if(rawLine.startsWith("<item")){
            Matcher matcher = itemPattern.matcher(rawLine);
            if(matcher.find()){
                setNextDataType(hexToString(matcher.group(1)), hexToString(matcher.group(2)));
            }
        } else if(rawLine.startsWith("<data")){
            //Can be safely ignored
        }else{
            Matcher matcher = dataPattern.matcher(rawLine);
            if(matcher.find()){
                handleNextData(matcher.group(1));
            }
        }
    }

    @SneakyThrows
    private String hexToString(String hexString) {
        byte[] bytes = Hex.decodeHex(hexString.toCharArray());
        return new String(bytes, "UTF-8");
    }

    private String base64toString(String base64string){
        byte[] decodedBytes = Base64.getDecoder().decode(base64string);
        return new String(decodedBytes);
    }

    private void setNextDataType(String type, String code){
        nextType = Optional.empty();

        for(MetadataTypes metaType: MetadataTypes.values()){
            if(type.equals(metaType.getCode())){
                nextType = Optional.of(metaType);
            }
        }

        nextCode = Optional.empty();

        for(MetadataCodes metaCode: MetadataCodes.values()){
            if(code.equals(metaCode.getCode())){
                nextCode = Optional.of(metaCode);
            }
        }
    }

    private void handleNextData(String rawData){
        nextCode.ifPresent(code->{
            switch(code){
                case METADATA_SEQUENCE_START: playingTrack = new Track(); break;
                case MEDIA_ALBUM: playingTrack.setAlbum(base64toString(rawData)); break;
                case MEDIA_ARTIST: playingTrack.setArtist(base64toString(rawData)); break;
                case MEDIA_TITLE: playingTrack.setTitle(base64toString(rawData)); break;
                case METADATA_SEQUENCE_END: activeTrackService.setCurrentTrack(playingTrack); break;
                default:  log.debug("Ignored data for Type: {} Code: {}", nextType, nextCode); break;
            }
        });
    }
}
