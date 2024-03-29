package connection;

import collectionitems.MusicBand;

import java.io.Serializable;

public class MusicBandRequest implements Serializable {
    public String name = null;
    public String arg = null;
    public MusicBand band = null;
    public String username = null;
    public String password = null;

    public String toString(){
        return "Command: " + "name = " + name + " arg = " + arg + " band = " + band;
    }
}
