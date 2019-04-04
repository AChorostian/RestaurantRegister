import java.util.List;

public interface ITable
{
    int getId();
    String getName();
    int getSeats();
    IRestaurant getRestaurant();
    List<IReservation> getReservations();

    void setName(String name);
    void setSeats(int seats);
    void addReservation(IReservation reservation);
    void deleteReservation(IReservation reservation);
}

