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
    @ValueSource(strings = {"Jan Kowalski", "Jan Nowak", "Tomasz", "Imionasą-różne", "Nazwiska mogą mieć kilka członów"})
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


























