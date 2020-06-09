package ie.jackhiggins.shairportsyncmetadatareader.tempo.songbpmdotcom;

import lombok.Builder;
import lombok.Data;

import java.text.MessageFormat;

@Data
@Builder
public class RequestBody {
    private String query;

    private static MessageFormat messageFormat = new MessageFormat("{0} - {1}");

    public static String formatQueryString(String title, String artist, String album){
        return messageFormat.format(new Object[]{title, artist});
    }
}
