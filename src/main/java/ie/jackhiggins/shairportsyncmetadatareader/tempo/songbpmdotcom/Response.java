package ie.jackhiggins.shairportsyncmetadatareader.tempo.songbpmdotcom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private Search search;
}
