package RestaurantRegister;

import java.util.List;

public interface ITable
{
    int getId();
    int getPersonsNumber();
    List<IReservation> getReservationsList();

    void setId(int id);
    void setPersonsNumber(int personsNumber);
    void addReservation(IReservation reservation);
    void deleteReservation(IReservation reservation);
}

