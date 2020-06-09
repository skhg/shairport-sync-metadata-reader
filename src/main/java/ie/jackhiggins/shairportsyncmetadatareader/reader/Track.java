package ie.jackhiggins.shairportsyncmetadatareader.reader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Track {
    private String artist;
    private String album;
    private String title;

    public Optional<String> getArtist(){
        return Optional.ofNullable(artist);
    }

    public Optional<String> getAlbum(){
        return Optional.ofNullable(album);
    }

    public Optional<String> getTitle(){
        return Optional.ofNullable(title);
    }

    public boolean isValid(){
        return StringUtils.isNotBlank(artist) && StringUtils.isNotBlank(album) && StringUtils.isNotBlank(title);
    }
}
