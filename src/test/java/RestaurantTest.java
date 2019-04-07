import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class NewRestaurantTest
{
    private Restaurant restaurant;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        restaurant = new Restaurant("Sphinx", "Gdańsk ul. Miastowa 53", startTime, endTime);
    }

    @Test
    public void classTest()
    {
        assertThat(restaurant,isA(Restaurant.class));
    }

    @Test
    public void nameTest()
    {
        assertThat(restaurant.getName(),is(equalTo("Sphinx")));
    }

    @Test
    public void addressTest()
    {
        assertThat(restaurant.getAddress(),is(equalTo("Gdańsk ul. Miastowa 53")));
    }

    @Test
    public void startTimeTest()
    {
        assertThat(restaurant.getStartTime(),is(equalTo(LocalTime.of(10,0))));
    }

    @Test
    public void endTimeTest()
    {
        assertThat(restaurant.getEndTime(),is(equalTo(LocalTime.of(20,0))));
    }

    @Test
    public void tablesTest()
    {
        assertThat(restaurant.getTables(),allOf(isA(List.class),hasSize(0)));
    }

    @Test
    public void usersTest()
    {
        assertThat(restaurant.getUsers(),allOf(isA(List.class),hasSize(0)));
    }
}


class RestaurantIdTest
{
    private static List<IRestaurant> restaurants;
    private static int beforeIdCounter;

    @BeforeAll
    public static void setUp()
    {
        Restaurant.resetIdCounter();
        beforeIdCounter = Restaurant.getIdCounter();
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        restaurants = new ArrayList<>();
        restaurants.add( new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",startTime,endTime));
        restaurants.add( new Restaurant("AiOLi", "Gdańsk Ul. Partyzantów 1",startTime,endTime));
        restaurants.add( new Restaurant("Da Grasso", "Gdynia Al. Zwycięstwa 43",startTime,endTime));
        restaurants.add( new Restaurant("Sphinx", "Sopot Ul. Matejki 99",startTime,endTime));
    }

    @Test
    public void positiveNumberIdTest()
    {
        assertThat(restaurants, hasItems(hasProperty("id", greaterThanOrEqualTo(0))));
    }

    @Test
    public void increasingIdTest()
    {
        assertThat(restaurants, hasItems(hasProperty("id",in(Arrays.asList(0,1,2,3)))));
    }

    @Test
    public void changingIdCounterTest()
    {
        assertThat(Restaurant.getIdCounter(),equalTo(beforeIdCounter+4));
    }
}

class RestaurantNameTest
{
    private Restaurant restaurant;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        restaurant = new Restaurant("Sphinx", "Sopot Ul. Matejki 99",startTime,endTime);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Nazwa z wielkiej litery","nazwa z małej litery","dowolne znaki _&*_%@","2141241"})
    public void correctNameTest(String name)
    {
        restaurant.setName(name);
        assertThat(restaurant,hasProperty("name",equalTo(name)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"","zbyt długi tekst nie powinien być używany w tym miejscu"})
    public void incorrectNameTest(String name)
    {
        assertThrows(IllegalArgumentException.class, ()-> restaurant.setName(name));
    }
}

class RestaurantAddressTest
{
    private Restaurant restaurant;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        restaurant = new Restaurant("Sphinx", "Sopot Ul. Matejki 99",startTime,endTime);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Nazwa z wielkiej litery","nazwa z małej litery","dowolne znaki _&*_%@","2141241"})
    public void correctAddressTest(String address)
    {
        restaurant.setAddress(address);
        assertThat(restaurant,hasProperty("address",equalTo(address)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"","Adres czasem bywa długi, ale długość pola w bazie danych" +
            " powinna być ograniczona, więc zakładam, że nie może być jakoś przesadnie długi."})
    public void incorrectAddressTest(String address)
    {
        assertThrows(IllegalArgumentException.class, ()-> restaurant.setAddress(address));
    }
}

class RestaurantTimeTest
{
    private Restaurant restaurant;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        restaurant = new Restaurant("Sphinx", "Sopot Ul. Matejki 99",startTime,endTime);
    }

    @ParameterizedTest
    @CsvSource({"0,00","10,15","15,00","19,30","19,45"})
    public void correctStartTimeTest(int hour, int minute)
    {
        restaurant.setStartTime(LocalTime.of(hour, minute));
        assertThat(restaurant,hasProperty("startTime",equalTo(LocalTime.of(hour, minute))));
    }

    @ParameterizedTest
    @CsvSource({"10,12","-13,30","14,-15","14,51","15,01","19,4","20,3","21,00","24,5","20,00"})
    public void incorrectStartTimeTest(int hour, int minute)
    {
        assertThrows(DateTimeException.class, ()-> restaurant.setStartTime(LocalTime.of(hour, minute)));

    }

    @ParameterizedTest
    @CsvSource({"10,15","15,00","19,30","19,45","23,45"})
    public void correctEndTimeTest(int hour, int minute)
    {
        restaurant.setEndTime(LocalTime.of(hour, minute));
        assertThat(restaurant,hasProperty("endTime",equalTo(LocalTime.of(hour, minute))));
    }

    @ParameterizedTest
    @CsvSource({"10,12","15,01","20,3","9,4","24,5","9,51","8,00","-13,30","17,-15","10,00"})
    public void incorrectEndTimeTest(int hour, int minute)
    {
        assertThrows( DateTimeException.class, ()-> restaurant.setEndTime(LocalTime.of(hour, minute)));
    }
}

class RestaurantRelationsTest
{
    private Restaurant restaurant;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        restaurant = new Restaurant("Sphinx", "Sopot Ul. Matejki 99",startTime,endTime);
    }

    @Test
    public void AddingUsersToListTest()
    {
        int beforeAdding = restaurant.getUsers().size();
        User user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        int afterAdding = restaurant.getUsers().size();
        assertAll(
                ()-> assertThat(beforeAdding,equalTo(afterAdding-1)),
                ()-> assertThat(user.getRestaurant(),equalTo(restaurant)),
                ()-> assertThat(restaurant.getUsers(),hasItems(hasProperty("restaurant", equalTo(restaurant))))
        );
    }

    @Test
    public void AddingDuplicatedDataUsersToListTest()
    {
        User.resetIdCounter();
        int beforeAdding = restaurant.getUsers().size();
        User user1 = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        User user2 = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        int afterAdding = restaurant.getUsers().size();

        assertAll(
                ()-> assertThat(beforeAdding,equalTo(afterAdding-2)),
                ()-> assertThat(user1.getId(),not(equalTo(user2.getId())))
        );
    }

    @Test
    public void AddingTablesToListTest()
    {
        int beforeAdding = restaurant.getTables().size();
        Table table = new Table("stolik nr1",5,restaurant);
        int afterAdding = restaurant.getTables().size();
        assertAll(
                ()-> assertThat(beforeAdding,equalTo(afterAdding-1)),
                ()-> assertThat(table.getRestaurant(),equalTo(restaurant)),
                ()-> assertThat(restaurant.getTables(),hasItems(hasProperty("restaurant", equalTo(restaurant))))
        );
    }

    @Test
    public void AddingDuplicatedDataTablesToListTest()
    {
        Table.resetIdCounter();
        int beforeAdding = restaurant.getTables().size();
        Table table1 = new Table("stolik nr1",5,restaurant);
        Table table2 = new Table("stolik nr2",7,restaurant);
        int afterAdding = restaurant.getTables().size();

        assertAll(
                ()-> assertThat(beforeAdding,equalTo(afterAdding-2)),
                ()-> assertThat(table1.getId(),not(equalTo(table2.getId())))
        );
    }

}

class csvOperationsAndStatisticsTest
{
    private Restaurant restaurant;
    private String usersFile, tablesFile, reservationsFile, restaurantFile;

    @BeforeEach
    void setUp()
    {
        clear();
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        restaurant = new Restaurant("Sphinx", "Sopot Ul. Matejki 99",startTime,endTime);
        usersFile = "csv/restaurant"+restaurant.getId()+"users.csv";
        tablesFile = "csv/restaurant"+restaurant.getId()+"tables.csv";
        reservationsFile = "csv/restaurant"+restaurant.getId()+"reservations.csv";
        restaurantFile = "csv/restaurant"+restaurant.getId()+".csv";

    }

    private void addUsers(Restaurant restaurant)
    {
        User user1 = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        new User("Jan Nowak" , "jnowak@gmail.com","432623342",restaurant);
        new User("Tomasz Nowak" , "tnowak@gmail.com","+48386746251",restaurant);
        new User("Katarzyna Nowak" , "knowak@gmail.com","+48 386 746 251",restaurant);
    }
    private void addTables(Restaurant restaurant)
    {
        Table table1 = new Table("nr. 1",5,restaurant);
        new Table("nr. 2",3,restaurant);
        new Table("nr. 3",7,restaurant);
        new Table("VIP",20,restaurant);
    }

    private void clear()
    {
        Restaurant.resetIdCounter();
        User.resetIdCounter();
        Table.resetIdCounter();
        Reservation.resetIdCounter();

        File dir = new File("csv");
        File[] listFiles = dir.listFiles();
        if (listFiles !=null)
        {
            for(File file : listFiles)
            {
                file.delete();
            }
        }
    }

    @Test
    void saveToCsvUsersTest() throws IOException
    {
        addUsers(restaurant);
        restaurant.saveDatabaseToCSV();

        assertAll(
                ()-> assertTrue(new File(restaurantFile).exists()),
                ()-> assertTrue(new File(usersFile).exists()),
                ()-> assertFalse(new File(tablesFile).exists()),
                ()-> assertFalse(new File(reservationsFile).exists())
        );    }
    @Test
    void saveToCsvTablesTest() throws IOException
    {
        addTables(restaurant);
        restaurant.saveDatabaseToCSV();

        assertAll(
                ()-> assertTrue(new File(restaurantFile).exists()),
                ()-> assertFalse(new File(usersFile).exists()),
                ()-> assertTrue(new File(tablesFile).exists()),
                ()-> assertFalse(new File(reservationsFile).exists())
        );    }
    @Test
    void saveToCsvUsersAndTablesTest() throws IOException
    {
        addUsers(restaurant);
        addTables(restaurant);
        restaurant.saveDatabaseToCSV();

        assertAll(
                ()-> assertTrue(new File(restaurantFile).exists()),
                ()-> assertTrue(new File(usersFile).exists()),
                ()-> assertTrue(new File(tablesFile).exists()),
                ()-> assertFalse(new File(reservationsFile).exists())
        );
    }
    @Test
    void saveToCsvAllTest() throws IOException
    {
        addUsers(restaurant);
        addTables(restaurant);

        User user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        Table table = new Table("nr. 5",5,restaurant);

        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        Reservation reservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);

        restaurant.saveDatabaseToCSV();

        assertAll(
                ()-> assertTrue(new File(restaurantFile).exists()),
                ()-> assertTrue(new File(usersFile).exists()),
                ()-> assertTrue(new File(tablesFile).exists()),
                ()-> assertTrue(new File(reservationsFile).exists())
        );
    }

    @Test
    void loadAllFromCsvTest() throws IOException
    {
        addUsers(restaurant);
        addTables(restaurant);
        User user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        Table table = new Table("nr. 5",5,restaurant);
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);

        restaurant.saveDatabaseToCSV();
        int savedId = restaurant.getId();
        restaurant = null;
        restaurant = Restaurant.loadDatabaseFromCSV(savedId);

        assertAll(
                ()-> assertThat(restaurant,notNullValue()),
                ()-> assertThat(restaurant.getUsers(),hasSize(5)),
                ()-> assertThat(restaurant.getTables(),hasSize(5)),
                ()-> assertThat(restaurant.getUsers().toArray()[4],hasProperty("reservations",hasSize(1)))
        );
    }

    @Test
    void avgReservationsPerDayTest()
    {
        User user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        Table table = new Table("nr. 5",5,restaurant);
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        new Reservation(3,startTime.plusHours(3),endTime.plusHours(3), LocalDate.now().plusDays(3),user,table);
        new Reservation(3,startTime,endTime, LocalDate.now().plusDays(3),user,table);
        new Reservation(3,startTime,endTime, LocalDate.now().plusDays(1),user,table);

        assertThat(restaurant.avgReservationsPerDay(),equalTo(1.0));
    }

    @Test
    void bestClientTest()
    {
        User user1 = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        User user2 = new User("Pieter" , "pieter@gmail.com","123-333-678",restaurant);
        Table table = new Table("nr. 5",5,restaurant);
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        new Reservation(3,startTime.plusHours(3),endTime.plusHours(3), LocalDate.now().plusDays(3),user2,table);
        new Reservation(3,startTime,endTime, LocalDate.now().plusDays(3),user2,table);
        new Reservation(3,startTime,endTime, LocalDate.now().plusDays(1),user1,table);

        assertThat(restaurant.bestClient(),equalTo(user2));
    }

    @Test
    void uselessClientsTest()
    {
        User user1 = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        User user2 = new User("Pieter" , "pieter@gmail.com","123-333-678",restaurant);
        new User("Tomasz Nowak" , "tnowak@gmail.com","+48386746251",restaurant);
        new User("Katarzyna Nowak" , "knowak@gmail.com","+48 386 746 251",restaurant);
        Table table = new Table("nr. 5",5,restaurant);
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        new Reservation(3,startTime.plusHours(3),endTime.plusHours(3), LocalDate.now().plusDays(3),user1,table);
        new Reservation(3,startTime,endTime, LocalDate.now().plusDays(3),user2,table);
        new Reservation(3,startTime,endTime, LocalDate.now().plusDays(1),user2,table);

        assertThat(restaurant.uselessClients(),equalTo(2));
    }
}

