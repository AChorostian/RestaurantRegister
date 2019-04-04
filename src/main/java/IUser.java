import java.util.List;

public interface IUser
{
    int getId();
    String getFullName();
    String getEMail();
    String getPhone();
    IRestaurant getRestaurant();
    List<IReservation> getReservations();

    void setFullName(String fullName);
    void setEMail(String eMail);
    void setPhone(String phone);

    void addReservation(IReservation reservation);
    void deleteReservation(IReservation reservation);
}

