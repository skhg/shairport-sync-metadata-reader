package ie.jackhiggins.shairportsyncmetadatareader.web

import ie.jackhiggins.shairportsyncmetadatareader.reader.Track
import ie.jackhiggins.shairportsyncmetadatareader.tracks.ActiveTrackService
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner.class)
@WebMvcTest(MetadataController.class)
class MetadataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private MetadataController controller

    @MockBean
    private ActiveTrackService activeTrackService

    @Test
    void getCurrentMetadata_songPlaying_returnsSongData(){
        when(activeTrackService.getCurrentBpm()).thenReturn(Optional.of(96))
        when(activeTrackService.getCurrentTrack()).thenReturn(Track.builder()
                .artist("Todd Terje")
                .album("It's Album Time")
                .title("Preben Goes to Acapulco")
                .build())

        mockMvc.perform(get("/metadata")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
{
    "artist" : "Todd Terje",
    "album" : "It's Album Time",
    "title" : "Preben Goes to Acapulco",
    "bpm" : 96
}
"""))
    }

    @Test
    void getCurrentMetadata_noSongPlaying_returnsEmptyData(){
        when(activeTrackService.getCurrentBpm()).thenReturn(Optional.empty())
        when(activeTrackService.getCurrentTrack()).thenReturn(Track.builder()
                .build())

        mockMvc.perform(get("/metadata")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
{
    "artist" : "",
    "album" : "",
    "title" : "",
    "bpm" : null
}
"""))
    }

    @Test
    void getCurrentMetadata_songWithNoBpmDataPlaying_returnsMixedData(){
        when(activeTrackService.getCurrentBpm()).thenReturn(Optional.empty())
        when(activeTrackService.getCurrentTrack()).thenReturn(Track.builder()
                .album("Pirates Choice")
                .title("Utrus Horas")
                .artist("Orchestra Baobab")
                .build())

        mockMvc.perform(get("/metadata")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
{
    "artist" : "Orchestra Baobab",
    "album" : "Pirates Choice",
    "title" : "Utrus Horas",
    "bpm" : null
}
"""))
    }
}
