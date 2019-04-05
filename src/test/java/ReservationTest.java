import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import sun.tools.jconsole.Tab;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    public void userTest()
    {
        assertThat(reservation.getUser()).isNotNull();
    }

    @Test
    public void tableTest()
    {
        assertThat(reservation.getTable()).isNotNull();
    }
}

class ReservationIdTest
{
    private static List<IReservation> reservations;
    private static int beforeIdCounter;

    @BeforeAll
    public static void setUp()
    {
        LocalTime rStartTime = LocalTime.of(10,0);
        LocalTime rEndTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",rStartTime,rEndTime);
        User user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        Table table = new Table("nr. 5",5,restaurant);

        LocalTime startTime = LocalTime.of(11,0);
        LocalTime endTime = LocalTime.of(11,30);
        Reservation.resetIdCounter();
        beforeIdCounter = Reservation.getIdCounter();
        reservations = new ArrayList<>();
        reservations.add(new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table));
        reservations.add(new Reservation(3,startTime.plusHours(1),endTime.plusHours(1), LocalDate.now().plusDays(5),user,table));
        reservations.add(new Reservation(3,startTime.plusHours(2),endTime.plusHours(2), LocalDate.now().plusDays(5),user,table));
        reservations.add(new Reservation(3,startTime.plusHours(3),endTime.plusHours(3), LocalDate.now().plusDays(5),user,table));
    }

    @Test
    public void changingIdCounterTest()
    {
        assertThat(Reservation.getIdCounter()).isEqualTo(beforeIdCounter+4);
    }

    @Test
    public void positiveNumberIdTest()
    {
        assertThat(reservations).allMatch(u -> u.getId() >= 0 );
    }

    @Test
    public void increasingIdTest()
    {
        assertThat(reservations).extracting(IReservation::getId).containsOnly(0,1,2,3);
    }

    @Test
    public void incrementOrderIdTest()
    {
        assertThat(reservations).extracting(IReservation::getId).containsExactly(0,1,2,3);
    }
}

class ReservationTimeTest
{
    private Reservation reservation;
    private User user;
    private Table table;

    @BeforeEach
    public void setUp()
    {
        LocalTime rStartTime = LocalTime.of(10,0);
        LocalTime rEndTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",rStartTime,rEndTime);
        user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        table = new Table("nr. 5",5,restaurant);

        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        reservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
    }

    @ParameterizedTest
    @CsvSource({"10,15,19,45","15,00,15,15","19,45,20,00","10,00,20,00"})
    public void correctReservingTimeInOpenHoursTest(int startHour, int startMinute, int endHour, int endMinute)
    {
        reservation.setStartTime(LocalTime.of(startHour,startMinute));
        reservation.setEndTime(LocalTime.of(endHour,endMinute));
        assertAll(
                ()-> assertThat(reservation.getStartTime()).isEqualTo(LocalTime.of(startHour,startMinute)),
                ()-> assertThat(reservation.getEndTime()).isEqualTo(LocalTime.of(endHour,endMinute))
        );
    }

    @ParameterizedTest
    @CsvSource({"13,11,19,45","15,0,15,19"})
    public void incorrectTimeFormatTest(int startHour, int startMinute, int endHour, int endMinute)
    {
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            reservation.setStartTime(LocalTime.of(startHour,startMinute));
            reservation.setEndTime(LocalTime.of(endHour,endMinute));
        } );
    }

    @ParameterizedTest
    @CsvSource({"9,15,19,45","15,00,21,15","19,45,21,00","0,00,23,00"})
    public void incorrectReservingTimeInOpenHoursTest(int startHour, int startMinute, int endHour, int endMinute)
    {
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            reservation.setStartTime(LocalTime.of(startHour,startMinute));
            reservation.setEndTime(LocalTime.of(endHour,endMinute));
        } );
    }

    @Test
    public void duplicateReservationTimeTest()
    {
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);

        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            Reservation duplicateReservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    public void overlapAfterReservationTimeTest()
    {
        LocalTime startTime = LocalTime.of(15,0);
        LocalTime endTime = LocalTime.of(17,0);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            Reservation duplicateReservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    public void overlapBeforeReservationTimeTest()
    {
        LocalTime startTime = LocalTime.of(13,0);
        LocalTime endTime = LocalTime.of(15,0);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            Reservation duplicateReservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    public void containingReservationTimeTest()
    {
        LocalTime startTime = LocalTime.of(12,0);
        LocalTime endTime = LocalTime.of(18,0);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            Reservation duplicateReservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    public void concludedReservationTimeTest()
    {
        LocalTime startTime = LocalTime.of(14,30);
        LocalTime endTime = LocalTime.of(15,30);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            Reservation duplicateReservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    public void stickedAfterReservationTimeTest()
    {
        LocalTime startTime = LocalTime.of(16,0);
        LocalTime endTime = LocalTime.of(17,30);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            Reservation duplicateReservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    public void stickedBeforeReservationTimeTest()
    {
        LocalTime startTime = LocalTime.of(12,0);
        LocalTime endTime = LocalTime.of(14,0);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            Reservation duplicateReservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    public void ovarlapAndIncorrectOpenHoursReservationTimeTest()
    {
        LocalTime startTime = LocalTime.of(8,0);
        LocalTime endTime = LocalTime.of(15,0);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            Reservation duplicateReservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

}