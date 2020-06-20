package ie.jackhiggins.shairportsyncmetadatareader.visualiser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SongRequestBody {
    private String title;
    private String artist;
    private String album;
}
