import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

class NewRestaurantTest
{
    private Restaurant restaurant;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        restaurant = new Restaurant("Ekskluzywa", "Gdańsk ul. Miastowa 53", startTime, endTime);
    }

    @Test
    public void classTest()
    {
        assertThat(restaurant,isA(Restaurant.class));
    }

    @Test
    public void nameTest()
    {
        assertThat(restaurant.getName(),is(equalTo("Ekskluzywa")));
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