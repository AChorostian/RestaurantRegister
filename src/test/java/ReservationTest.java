import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.mail.MessagingException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

class NewReservationTest
{
    private Reservation reservation;

    @BeforeEach
    void setUp()
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
    void classTest()
    {
        assertThat(reservation).isInstanceOf(Reservation.class);
    }

    @Test
    void seatsTest()
    {
        assertThat(reservation.getSeats()).isEqualTo(3);
    }

    @Test
    void dateTest()
    {
        assertThat(reservation.getDate()).isEqualTo(LocalDate.now().plusDays(5));
    }

    @Test
    void startTimeTest()
    {
        assertThat(reservation.getStartTime()).isEqualTo(LocalTime.of(14,0));
    }

    @Test
    void endTimeTest()
    {
        assertThat(reservation.getEndTime()).isEqualTo(LocalTime.of(16,0));
    }

    @Test
    void userTest()
    {
        assertThat(reservation.getUser()).isNotNull();
    }

    @Test
    void tableTest()
    {
        assertThat(reservation.getTable()).isNotNull();
    }
}

class ReservationIdTest
{
    private static List<IReservation> reservations;
    private static int beforeIdCounter;

    @BeforeAll
    static void setUp()
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
    void changingIdCounterTest()
    {
        assertThat(Reservation.getIdCounter()).isEqualTo(beforeIdCounter+4);
    }

    @Test
    void positiveNumberIdTest()
    {
        assertThat(reservations).allMatch(u -> u.getId() >= 0 );
    }

    @Test
    void increasingIdTest()
    {
        assertThat(reservations).extracting(IReservation::getId).containsOnly(0,1,2,3);
    }

    @Test
    void incrementOrderIdTest()
    {
        assertThat(reservations).extracting(IReservation::getId).containsExactly(0,1,2,3);
    }
}

class ReservationTimeTest
{
    private User user;
    private Table table;

    @BeforeEach
    void setUp()
    {
        LocalTime rStartTime = LocalTime.of(10,0);
        LocalTime rEndTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",rStartTime,rEndTime);
        user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        table = new Table("nr. 5",5,restaurant);
    }

    @ParameterizedTest
    @CsvSource({"10,15,19,45","15,00,15,15","19,45,20,00","10,00,20,00"})
    void correctReservingTimeInOpenHoursTest(int startHour, int startMinute, int endHour, int endMinute)
    {
        LocalTime startTime = LocalTime.of(startHour,startMinute);
        LocalTime endTime = LocalTime.of(endHour,endMinute);
        Reservation reservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);

        assertAll(
                ()-> assertThat(reservation.getStartTime()).isEqualTo(LocalTime.of(startHour,startMinute)),
                ()-> assertThat(reservation.getEndTime()).isEqualTo(LocalTime.of(endHour,endMinute))
        );
    }

    @ParameterizedTest
    @CsvSource({"13,11,19,45","15,0,15,19","12,30,10,00"})
    void incorrectTimeFormatTest(int startHour, int startMinute, int endHour, int endMinute)
    {
        LocalTime startTime = LocalTime.of(startHour,startMinute);
        LocalTime endTime = LocalTime.of(endHour,endMinute);

        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @ParameterizedTest
    @CsvSource({"9,15,19,45","15,00,21,15","19,45,21,00","0,00,23,00"})
    void incorrectReservingTimeInOpenHoursTest(int startHour, int startMinute, int endHour, int endMinute)
    {
        LocalTime startTime = LocalTime.of(startHour,startMinute);
        LocalTime endTime = LocalTime.of(endHour,endMinute);

        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    void duplicateReservationTimeTest()
    {
        LocalTime rStartTime = LocalTime.of(14,0);
        LocalTime rEndTime = LocalTime.of(16,0);
        new Reservation(3,rStartTime,rEndTime, LocalDate.now().plusDays(5),user,table);

        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);

        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    void overlapAfterReservationTimeTest()
    {
        LocalTime rStartTime = LocalTime.of(14,0);
        LocalTime rEndTime = LocalTime.of(16,0);
        new Reservation(3,rStartTime,rEndTime, LocalDate.now().plusDays(5),user,table);

        LocalTime startTime = LocalTime.of(15,0);
        LocalTime endTime = LocalTime.of(17,0);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    void overlapBeforeReservationTimeTest()
    {
        LocalTime rStartTime = LocalTime.of(14,0);
        LocalTime rEndTime = LocalTime.of(16,0);
        new Reservation(3,rStartTime,rEndTime, LocalDate.now().plusDays(5),user,table);

        LocalTime startTime = LocalTime.of(13,0);
        LocalTime endTime = LocalTime.of(15,0);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    void containingReservationTimeTest()
    {
        LocalTime rStartTime = LocalTime.of(14,0);
        LocalTime rEndTime = LocalTime.of(16,0);
        new Reservation(3,rStartTime,rEndTime, LocalDate.now().plusDays(5),user,table);

        LocalTime startTime = LocalTime.of(12,0);
        LocalTime endTime = LocalTime.of(18,0);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    void concludedReservationTimeTest()
    {
        LocalTime rStartTime = LocalTime.of(14,0);
        LocalTime rEndTime = LocalTime.of(16,0);
        new Reservation(3,rStartTime,rEndTime, LocalDate.now().plusDays(5),user,table);

        LocalTime startTime = LocalTime.of(14,30);
        LocalTime endTime = LocalTime.of(15,30);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    void justAfterReservationTimeTest()
    {
        LocalTime rStartTime = LocalTime.of(14,0);
        LocalTime rEndTime = LocalTime.of(16,0);
        new Reservation(3,rStartTime,rEndTime, LocalDate.now().plusDays(5),user,table);

        LocalTime startTime = LocalTime.of(16,0);
        LocalTime endTime = LocalTime.of(17,30);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    void justBeforeReservationTimeTest()
    {
        LocalTime rStartTime = LocalTime.of(14,0);
        LocalTime rEndTime = LocalTime.of(16,0);
        new Reservation(3,rStartTime,rEndTime, LocalDate.now().plusDays(5),user,table);

        LocalTime startTime = LocalTime.of(12,0);
        LocalTime endTime = LocalTime.of(14,0);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }

    @Test
    void overlapAndIncorrectOpenHoursReservationTimeTest()
    {
        LocalTime rStartTime = LocalTime.of(14,0);
        LocalTime rEndTime = LocalTime.of(16,0);
        new Reservation(3,rStartTime,rEndTime, LocalDate.now().plusDays(5),user,table);

        LocalTime startTime = LocalTime.of(8,0);
        LocalTime endTime = LocalTime.of(15,0);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        } );
    }
}

class ReservationSeatsTest
{
    private Restaurant restaurant;
    private User user;

    @BeforeEach
    void setUp()
    {
        LocalTime rStartTime = LocalTime.of(10,0);
        LocalTime rEndTime = LocalTime.of(20,0);
        restaurant = new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",rStartTime,rEndTime);
        user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
    }

    @ParameterizedTest
    @CsvSource({"1,1","10,15","3,3","1,2"})
    void correctSeatsNumberTest(int wantedSeats, int tableSeats)
    {
        Table table = new Table("stolik",tableSeats,restaurant);
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        Reservation reservation = new Reservation(wantedSeats,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        assertThat(reservation).isNotNull().hasFieldOrPropertyWithValue("seats",wantedSeats);
    }

    @ParameterizedTest
    @CsvSource({"0,3","16,15","3325,5","-5,8"})
    void incorrectSeatsNumberTest(int wantedSeats, int tableSeats)
    {
        Table table = new Table("stolik",tableSeats,restaurant);
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()-> {
            new Reservation(wantedSeats,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        });
    }
}

class ReservationDateTest
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
    void sameTimeOtherDayTest()
    {
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);

        Reservation reservation1 = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        Reservation reservation2 = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(3),user,table);

        assertAll(
                ()-> assertThat(reservation1).isNotNull(),
                ()-> assertThat(reservation2).isNotNull()
        );
    }

    @Test
    void sameTimeSameDayTest()
    {
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        });
    }

    @Test
    void passedDayTest()
    {
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        assertThatExceptionOfType(DateTimeException.class).isThrownBy(()-> {
            new Reservation(3,startTime,endTime, LocalDate.now().minusDays(5),user,table);
        });
    }
}

class GeneratingConfirmationTest
{

    @Test
    void generatingConfirmationCodeTest()
    {
        LocalTime rStartTime = LocalTime.of(10,0);
        LocalTime rEndTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",rStartTime,rEndTime);
        User user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
        Table table = new Table("nr. 5",5,restaurant);
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        Reservation reservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        assertThat(reservation.getConfirmationCode()).isEqualTo(reservation.hashCode());
    }

}
