import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

class NewUserTest
{
    private User user;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",startTime,endTime);
        user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant);
    }

    @Test
    public void classTest()
    {
        assertThat(user).isInstanceOf(User.class);
    }

    @Test
    public void fullNameTest()
    {
        assertThat(user.getFullName()).isEqualToIgnoringCase("jan kowalski");
    }

    @Test
    public void eMailTest()
    {
        assertThat(user.getEMail()).isNotEmpty();
    }

    @Test
    public void phoneTest()
    {
        assertThat(user).hasFieldOrPropertyWithValue("phone","123-543-678");
    }

    @Test
    public void reservationsTest()
    {
        assertThat(user.getReservations()).isInstanceOf(List.class).hasSize(0);
    }
}

class UserIdTest
{
    private static List<IUser> users;
    private static int beforeIdCounter;

    @BeforeAll
    public static void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",startTime,endTime);
        User.resetIdCounter();
        users = new ArrayList<>();
        beforeIdCounter = User.getIdCounter();
        users.add( new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678",restaurant));
        users.add( new User("Jan Nowak" , "jnowak@gmail.com","432623342",restaurant));
        users.add( new User("Tomasz Nowak" , "tnowak@gmail.com","+48386746251",restaurant));
        users.add( new User("Katarzyna Nowak" , "knowak@gmail.com","+48 386 746 251",restaurant));
    }

    @Test
    public void changingIdCounterTest()
    {
        assertThat(User.getIdCounter()).isEqualTo(beforeIdCounter+4);
    }

    @Test
    public void positiveNumberIdTest()
    {
        assertThat(users).allMatch(u -> u.getId() >= 0 );
    }

    @Test
    public void increasingIdTest()
    {
        assertThat(users).extracting(IUser::getId).containsOnly(0,1,2,3);
    }

    @Test
    public void incrementOrderIdTest()
    {
        assertThat(users).extracting(IUser::getId).containsExactly(0,1,2,3);
    }
}

class UserFullNameTest
{
    private User user;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",startTime,endTime);
        user = new User("Jan Kowalski" , "jkowalski@gmail.com","123456789",restaurant);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jan Kowalski", "bezimienny", "Imiona-są-różne", "Nazwiska mogą mieć kilka członów"})
    public void correctFullNameTest(String newName)
    {
        user.setFullName(newName);
        assertThat(user.getFullName()).isEqualTo(newName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "214", "Tomasz2", "Znaki+", "*&Specjalne","'"})
    public void incorrectFullNameTest(String newName)
    {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( ()-> user.setFullName(newName) );
    }

}

class UserEMailTest
{
    private User user;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",startTime,endTime);
        user = new User("Jan Kowalski" , "jkowalski@gmail.com","123456789",restaurant);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "email@domain.com",
            "firstname.lastname@domain.com",
            "email@subdomain.domain.com",
            "firstname+lastname@domain.com",
            "email@123.123.123.123",
            "1234567890@domain.com",
            "email@domain-one.com",
            "_______@domain.com",
            "email@domain.name",
            "email@domain.co.jp",
            "firstname-lastname@domain.com"
    })
    public void correctEMailTest(String eMail)
    {
        user.setEMail(eMail);
        assertThat(user.getEMail()).isEqualTo(eMail);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "#@%^%#$@#$@#.com",
            "@domain.com",
            "Joe Smith <email@domain.com>",
            "email.domain.com",
            "email@domain@domain.com",
            ".email@domain.com",
            "あいうえお@domain.com",
            "email@domain.com (Joe Smith)",
            "email@domain",
    })
    public void incorrectEMailTest(String eMail)
    {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( ()-> user.setEMail(eMail) );
    }
}

class UserPhoneTest
{
    private User user;

    @BeforeEach
    public void setUp()
    {
        LocalTime startTime = LocalTime.of(10,0);
        LocalTime endTime = LocalTime.of(20,0);
        Restaurant restaurant = new Restaurant("Stacja","Gdańsk Al. Grunwaldzka 111",startTime,endTime);
        user = new User("Jan Kowalski" , "jkowalski@gmail.com","123456789",restaurant);
    }

    @ParameterizedTest
    @ValueSource(strings = { // 9 lub 11 cyfr / dozwolone spacje, plusy, myslniki, nawiasy
            "(58) 234 43 23",
            "123-456-789",
            "+48543857694",
            "(+48) 343-324-345",
            "123445656",
            "586769(4)85",
            "48373+234+234",
            "1-2-3-4-5-6-7-8-9"
    })
    public void correctPhoneTest(String phone)
    {
        user.setPhone(phone);
        assertThat(user.getPhone()).isEqualTo(phone);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123.231.353",
            "12",
            "",
            "1241244213",
            "312*23452*6",
            "214512512163263632",
            "fsfsd325325234",
            "42124-=124"
    })
    public void incorrectPhoneTest(String phone)
    {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( ()-> user.setPhone(phone) );
    }
}

class UserRelationsTest
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
        int beforeAdding = user.getReservations().size();
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime endTime = LocalTime.of(16,0);
        Reservation reservation = new Reservation(3,startTime,endTime, LocalDate.now().plusDays(5),user,table);
        int afterAdding = user.getReservations().size();
        assertAll(
                ()-> assertThat(beforeAdding).isEqualTo(afterAdding-1),
                ()-> assertThat(reservation.getTable()).isEqualTo(table)
        );
    }
}




















