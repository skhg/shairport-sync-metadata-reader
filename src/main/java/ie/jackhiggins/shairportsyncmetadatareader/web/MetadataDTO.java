package ie.jackhiggins.shairportsyncmetadatareader.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetadataDTO {

    private String artist;
    private String album;
    private String title;
    private Integer bpm;
}
