import java.util.List;
import java.util.ArrayList;

public class Table implements ITable
{
    private int id;
    private int seats;
    private List<IReservation> reservationsList;

    private static int idCounter=1;

    public Table(int seats)
    {
        // todo: exceptions
        this.seats = seats;

        this.id = idCounter++;
        this.reservationsList = new ArrayList<IReservation>() {};
    }

    public int getId()
    { return id; }

    public int getSeats()
    { return seats; }

    public List<IReservation> getReservationsList()
    { return reservationsList; }

    public static void resetIdCounter()
    {
        idCounter = 0;
    }

    public void setSeats(int seats)
    {
        // todo: exceptions
        this.seats = seats;
    }

    public void addReservation(IReservation reservation)
    {
        // todo: exceptions
        reservationsList.add(reservation);
    }

    public void deleteReservation(IReservation reservation)
    {
        // todo: exceptions
        reservationsList.remove(reservation);
    }
}
