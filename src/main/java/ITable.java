import java.util.List;

public interface ITable
{
    int getId();
    int getSeats();
    List<IReservation> getReservations();

    void setSeats(int seats);
    void addReservation(IReservation reservation);
    void deleteReservation(IReservation reservation);
}

