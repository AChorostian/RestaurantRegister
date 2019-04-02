package RestaurantRegisterTest;
import RestaurantRegister.*;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void ClassTest()
    {
        assertThat(user).isInstanceOf(User.class);
    }

    @Test
    public void FullNameTest()
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
