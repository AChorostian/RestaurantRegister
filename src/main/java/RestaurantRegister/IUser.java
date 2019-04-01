package RestaurantRegister;

import java.util.List;

public interface IUser
{
    int getId();
    String getFullName();
    String getEMail();
    String getPhone();
    List<IReservation> getReservationsList();

    void setId(int id);
    void setFullName(String fullName);
    void setEMail(String eMail);
    void setPhone(String phone);

    void addReservation(IReservation reservation);
    void deleteReservation(IReservation reservation);
}

