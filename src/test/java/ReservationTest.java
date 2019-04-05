import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


class ReservationTest
{
    @Test
    public void initTest()
    {
        assertTrue(true);
    }
}

class NewReservationTest
{
    private Reservation reservation;

    @BeforeEach
    public void setUp()
    {
        LocalTime rStartTime = LocalTime.of(10,0);
        LocalTime rEndTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",rStartTime,rEndTime);
        User user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        Table table = new Table("nr. 5",5,restaurant);

        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        reservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
    }

    @Test
    public void classTest()
    {
        assertThat(reservation).isInstanceOf(Reservation.class);
    }

    @Test
    public void seatsTest()
    {
        assertThat(reservation.getSeats()).isEqualTo(3);
    }

    @Test
    public void dateTest()
    {
        assertThat(reservation.getDate()).isEqualTo(LocalDate.now().plusDays(5));
    }

    @Test
    public void startTimeTest()
    {
        assertThat(reservation.getStartTime()).isEqualTo(LocalTime.of(14,0));
    }

    @Test
    public void endTimeTest()
    {
        assertThat(reservation.getEndTime()).isEqualTo(LocalTime.of(16,0));
    }

    @Test
    public void phoneTest()
    {
        assertThat(reservation.getUser()).isNotNull();
    }

    @Test
    public void reservationsTest()
    {
        assertThat(reservation.getTable()).isNotNull();
    }
}