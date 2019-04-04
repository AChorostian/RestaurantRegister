import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
        assertThat(restaurant.getTables(),allOf(isA(List.class),hasSize(0)));
    }
}


class RestaurantIdTest
{
    private static List<IRestaurant> restaurants;

    @BeforeAll
    public static void setUp()
    {
        Restaurant.resetIdCounter();
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