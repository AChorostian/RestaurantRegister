import java.util.List;
import java.util.ArrayList;

public class Table implements ITable
{
    private int id;
    private int seats;
    private List<IReservation> reservations;

    private static int idCounter=1;

    public Table(int seats)
    {
        if (seats < 1) throw new IllegalArgumentException();
        this.seats = seats;

        this.id = idCounter++;
        this.reservations = new ArrayList<IReservation>() {};
    }

    public int getId()
    { return id; }

    public int getSeats()
    { return seats; }

    public List<IReservation> getReservations()
    { return reservations; }

    public static void resetIdCounter()
    {
        idCounter = 0;
    }

    public void setSeats(int seats)
    {
        if (seats < 1) throw new IllegalArgumentException();
        this.seats = seats;
    }

    public void addReservation(IReservation reservation)
    {
        // todo: exceptions
        reservations.add(reservation);
    }

    public void deleteReservation(IReservation reservation)
    {
        // todo: exceptions
        reservations.remove(reservation);
    }
}
