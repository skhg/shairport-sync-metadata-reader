package ie.jackhiggins.shairportsyncmetadatareader.web;

import ie.jackhiggins.shairportsyncmetadatareader.reader.Track;
import ie.jackhiggins.shairportsyncmetadatareader.tracks.ActiveTrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/metadata")
@Tag(name = "metadata", description = "Track Metadata API")
public class MetadataController {

    final ActiveTrackService activeTrackService;

    public MetadataController(ActiveTrackService activeTrackService) {
        this.activeTrackService = activeTrackService;
    }

    @Operation(summary = "Get the metadata for the last track played.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned the metadata for the last track.")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MetadataDTO getCurrentMetadata(){
        Track currentTrack = activeTrackService.getCurrentTrack();
        Optional<Integer> currentBpm = activeTrackService.getCurrentBpm();

        MetadataDTO.MetadataDTOBuilder builder = MetadataDTO.builder()
                .artist(currentTrack.getArtist().orElse(StringUtils.EMPTY))
                .title(currentTrack.getTitle().orElse(StringUtils.EMPTY))
                .album(currentTrack.getAlbum().orElse(StringUtils.EMPTY));
        currentBpm.ifPresent(bpm -> builder.bpm(bpm));

        return builder.build();
    }
}
