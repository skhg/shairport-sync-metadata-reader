package ie.jackhiggins.shairportsyncmetadatareader.tempo.songbpmdotcom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Song {

    private String spotifyUrl;
    private int tempo;
}
