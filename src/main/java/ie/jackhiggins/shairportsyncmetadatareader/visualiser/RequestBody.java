package ie.jackhiggins.shairportsyncmetadatareader.visualiser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RequestBody {
    private Integer bpm;
}
