package RestaurantRegisterTest;
import RestaurantRegister.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

class NewUserTest
{
    private User user;

    @BeforeEach
    public void setUp()
    {
        user = new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678");
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
    public void reservationListTest()
    {
        assertThat(user.getReservationsList()).isInstanceOf(List.class).hasSize(0);
    }
}

class UserIdTest
{
    private static List<IUser> users;

    @BeforeAll
    public static void setUp()
    {
        User.resetIdCounter();
        users = new ArrayList<>();
        users.add( new User("Jan Kowalski" , "jkowalski@gmail.com","123-543-678"));
        users.add( new User("Jan Nowak" , "jnowak@gmail.com","432623342"));
        users.add( new User("Tomasz Nowak" , "tnowak@gmail.com","+48386746251"));
        users.add( new User("Katarzyna Nowak" , "knowak@gmail.com","+48 386 746 251"));
    }

    @Test
    public void positiveNumberIdTest()
    {
        assertThat(users).allMatch(u -> u.getId() >= 0 );
    }

    @Test
    public void growingIdTest()
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
        user = new User("Jan Kowalski" , "jkowalski@gmail.com","123456789");
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
        user = new User("Jan Kowalski" , "jkowalski@gmail.com","123456789");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "email@domain.com",
            "firstname.lastname@domain.com",
            "email@subdomain.domain.com",
            "firstname+lastname@domain.com",
            "email@123.123.123.123",
            "email@[123.123.123.123]",
            "\"email\"@domain.com",
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
            "email.@domain.com",
            "email..email@domain.com",
            "あいうえお@domain.com",
            "email@domain.com (Joe Smith)",
            "email@domain",
            "email@-domain.com",
            "email@domain.web",
            "email@111.222.333.44444",
            "email@domain..com"
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
        user = new User("Jan Kowalski" , "jkowalski@gmail.com","123456789");
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






















