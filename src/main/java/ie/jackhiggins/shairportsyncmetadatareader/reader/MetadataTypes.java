package ie.jackhiggins.shairportsyncmetadatareader.reader;

/**
 * Enum named based on descriptions at https://github.com/mikebrady/shairport-sync/blob/master/rtsp.c
 */
public enum MetadataTypes {
    SHAIRPORT_SYNC_METADATA("ssnc"),
    PLAYER_METADATA("core");

    private final String code;

    public String getCode(){
        return code;
    }

    MetadataTypes(String code) {
        this.code = code;
    }
}
