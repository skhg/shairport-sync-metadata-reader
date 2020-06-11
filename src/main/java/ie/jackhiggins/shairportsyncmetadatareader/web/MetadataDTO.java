package ie.jackhiggins.shairportsyncmetadatareader.web;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetadataDTO {

    @Schema(description = "Track artist", example = "Pink Floyd")
    private String artist;

    @Schema(description = "Track album", example = "The Dark Side of the Moon")
    private String album;

    @Schema(description = "Track title", example = "Money")
    private String title;

    @Schema(description = "Track beats per minute, as retrieved from web service", example = "124")
    private Integer bpm;
}
