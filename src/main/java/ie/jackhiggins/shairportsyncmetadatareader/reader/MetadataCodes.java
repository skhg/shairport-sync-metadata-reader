package ie.jackhiggins.shairportsyncmetadatareader.reader;

/**
 * Enum named based on descriptions at https://github.com/mikebrady/shairport-sync/blob/master/rtsp.c
 */
public enum MetadataCodes {
    CLIENT_USER_AGENT("snua"),
    ACTIVE_MODE_ENTERED("abeg"),
    ACTIVE_MODE_EXITED("aend"),
    CLIENT_ACTIVE_REMOTE_TOKEN("acre"),
    CLIENT_DACP_ID("daid"),
    CLIENT_IP("clip"),
    SERVER_IP("svip"),
    PLAY_STREAM_END("pend"),
    PLAY_STREAM_BEGIN("pbeg"),
    PLAY_STREAM_FLUSH("pfls"),
    PLAY_STREAM_RESUME("prsm"),
    PLAY_STREAM_VOLUME("pvol"),
    FLSR("flsr"),
    PICTURE_SEND_START("pcst"),
    PICTURE_DATA("PICT"),
    PICTURE_SEND_END("pcen"),
    PLAY_SEQUENCE_PROGRESS("prgr"),
    METADATA_SEQUENCE_START("mdst"),
    MPER("mper"),
    MEDIA_ALBUM("asal"),
    MEDIA_ARTIST("asar"),
    ASCP("ascp"),
    ASGN("asgn"),
    MEDIA_TITLE("minm"),
    ASTN("astn"),
    ASTC("astc"),
    ASDN("asdn"),
    ASDC("asdc"),
    ASDK("asdk"),
    CAPS("caps"),
    ASTM("astm"),
    METADATA_SEQUENCE_END("mden"),
    DAPO("dapo"),
    CLIENT_DEVICE_NAME("snam");


    private final String code;

    public String getCode(){
        return code;
    }

    MetadataCodes(String code) {
        this.code= code;
    }
}
