package ie.jackhiggins.shairportsyncmetadatareader.reader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private String userAgent;
    private String ipAddress;

    public Optional<String> getUserAgent(){
        return Optional.ofNullable(userAgent);
    }

    public Optional<String> getIpAddress(){
        return Optional.ofNullable(ipAddress);
    }
}
