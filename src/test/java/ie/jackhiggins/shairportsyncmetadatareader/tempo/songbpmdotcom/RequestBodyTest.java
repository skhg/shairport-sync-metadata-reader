package ie.jackhiggins.shairportsyncmetadatareader.tempo.songbpmdotcom;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class RequestBodyTest {

    @Test
    public void formatQueryString_formatsTitleArtist(){
        String result = RequestBody.formatQueryString("my title", "my artist", "my album");

        assertThat(result, is(equalTo("my title - my artist")));
    }
}
