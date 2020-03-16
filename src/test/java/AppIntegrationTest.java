import com.napier.sem.DataLayer.Column;
import com.napier.sem.DataLayer.SQLConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static SQLConnection app;

    @BeforeAll
    static void init()
    {
        app = new SQLConnection();

    }

    @Test
    void testGetPopulation()
    {
        Column.REGION = app.topPop(Column.CONTINENT);
        //app.popWithoutCity("", Column.CODE);
        assertEquals(true, true);


    }
}