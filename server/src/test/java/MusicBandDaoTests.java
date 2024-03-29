import collectionitems.*;
import data.database.DaoInitializationException;
import data.database.bands.MusicBandDao;
import data.database.QueryExecutionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class MusicBandDaoTests {
    private MusicBandDao musicBandDao;
    private List<MusicBand> allBands;

    @Before
    public void init(){
        try{
            musicBandDao = new MusicBandDao("jdbc:postgresql://localhost:5432/studs",
                    "s264432", "ajf870", "test_table1");
            allBands = new ArrayList<>();
            MusicBand band1 = new MusicBand();
            band1.setId(2);
            band1.setName("band1");
            band1.setCreationDate(LocalDateTime.parse("2022-08-26T17:56:20.217787600"));
            Coordinates coordinates1 = new Coordinates();
            coordinates1.setX(1f);
            coordinates1.setY(1f);
            band1.setCoordinates(coordinates1);
            band1.setNumberOfParticipants(3);
            band1.setAlbumsCount(5);
            allBands.add(band1);

            MusicBand band2 = new MusicBand();
            band2.setId(3);
            band2.setName("band2");
            band2.setCreationDate(LocalDateTime.parse("2022-07-26T17:56:20.217787600"));
            Coordinates coordinates2 = new Coordinates();
            coordinates2.setX(2.05f);
            coordinates2.setY(2.948f);
            band2.setCoordinates(coordinates2);
            band2.setNumberOfParticipants(2);
            band2.setAlbumsCount(7);
            band2.setDescription("band 2 description");
            band2.setGenre(MusicGenre.BLUES);
            Album bestAlbum2 = new Album();
            bestAlbum2.setName("best_album");
            bestAlbum2.setTracks(70132);
            bestAlbum2.setLength(15);
            bestAlbum2.setSales(14.07f);
            band2.setBestAlbum(bestAlbum2);
            allBands.add(band2);
        }
        catch (DaoInitializationException | WrongArgumentException ex){
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetBandsFromDb(){
        try{
            assertEquals(allBands, musicBandDao.getBandsFromDb());
        }
        catch (QueryExecutionException ex){
            fail(ex.getMessage());
        }
    }

    @Test
    public void addBandToDbTest(){
        try {
            MusicBand band = new MusicBand();
            band.setName("Added Band");
            musicBandDao.addBandToDb(band, "TestUser");
            allBands.add(band);
            assertEquals(allBands, musicBandDao.getBandsFromDb());
        }
        catch (QueryExecutionException | WrongArgumentException ex){
            fail(ex.getMessage());
        }
    }
}
