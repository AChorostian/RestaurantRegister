import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class NewTableTest
{
    private Table table;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Sphinx", "Sopot Ul. Matejki 99",startTime,endTime);
        table = new Table("nr. 5",5,restaurant);
    }

    @Test
    public void classTest()
    {
        assertThat(table,isA(Table.class));
    }

    @Test
    public void seatsTest()
    {
        assertThat(table.getSeats(),is(equalTo(5)));
    }

    @Test
    public void reservationsTest()
    {
        assertThat(table.getReservations(),allOf(isA(List.class),hasSize(0)));
    }
}

class TableIdTest
{
    private static List<ITable> tables;
    private static int beforeIdCounter;

    @BeforeAll
    public static void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Sphinx", "Sopot Ul. Matejki 99",startTime,endTime);
        Table.resetIdCounter();
        beforeIdCounter = Table.getIdCounter();
        tables = new ArrayList<>();
        tables.add( new Table("nr. 1",5,restaurant));
        tables.add( new Table("nr. 2",5,restaurant));
        tables.add( new Table("nr. 3",5,restaurant));
        tables.add( new Table("VIP",5,restaurant));
    }

    @Test
    public void positiveNumberIdTest()
    {
        assertThat(tables, hasItems(hasProperty("id", greaterThanOrEqualTo(0))));
    }

    @Test
    public void increasingIdTest()
    {
        assertThat(tables, hasItems(hasProperty("id",in(Arrays.asList(0,1,2,3)))));
    }

    @Test
    public void changingIdCounterTest()
    {
        assertThat(Table.getIdCounter(),equalTo(beforeIdCounter+4));
    }
}

class TableNameTest
{
    private Table table;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Sphinx", "Sopot Ul. Matejki 99",startTime,endTime);
        table = new Table("nr. 5",5,restaurant);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Nazwa z wielkiej litery","nazwa z małej litery","dowolne znaki _&*_%@","2141241"})
    public void correctNameTest(String name)
    {
        table.setName(name);
        assertThat(table,hasProperty("name",equalTo(name)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"","zbyt długi tekst nie powinien być używany w tym miejscu"})
    public void incorrectNameTest(String name)
    {
        assertThrows(IllegalArgumentException.class, ()-> table.setName(name));
    }
}

class TableSeatsTest
{
    private Table table;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Sphinx", "Sopot Ul. Matejki 99",startTime,endTime);
        table = new Table("nr. 5",5,restaurant);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 14 , 1000})
    public void correctSeatsTest(int seats)
    {
        table.setSeats(seats);
        assertThat(table,hasProperty("seats",equalTo(seats)));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1000,-1,0})
    public void incorrectSeatsTest(int seats)
    {
        assertThrows(IllegalArgumentException.class, ()-> table.setSeats(seats));
    }
}

class TableRelationsTest
{
    private Table table;
    private User user;

    @BeforeEach
    void setUp()
    {
        LocalTime rStartTime = LocalTime.of(10,0);
        LocalTime rEndTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",rStartTime,rEndTime);
        user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        table = new Table("nr. 5",5,restaurant);
    }

    @Test
    void addingReservationsToList()
    {
        int beforeAdding = table.getReservations().size();
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        Reservation reservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        int afterAdding = table.getReservations().size();
        assertAll(
                ()-> assertThat(beforeAdding,equalTo(afterAdding-1)),
                ()-> assertThat(reservation.getTable(),equalTo(table)),
                ()-> assertThat(table.getReservations(),hasItems(hasProperty("table", equalTo(table))))
        );
    }

}
