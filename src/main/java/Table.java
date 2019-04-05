import java.util.List;
import java.util.ArrayList;

public class Table implements ITable
{
    private int id;
    private String name;
    private int seats;

    private IRestaurant restaurant;
    private List<IReservation> reservations;

    private static int idCounter=1;

    public Table(String name, int seats, Restaurant restaurant)
    {
        if (seats < 1) throw new IllegalArgumentException();
        this.name = name;
        this.seats = seats;

        this.id = idCounter++;
        this.restaurant = restaurant;
        this.reservations = new ArrayList<IReservation>() {};

        restaurant.addTable(this);
    }

    public int getId()
    { return id; }

    public String getName()
    { return name; }

    public int getSeats()
    { return seats; }

    public IRestaurant getRestaurant()
    { return restaurant; }

    public List<IReservation> getReservations()
    { return reservations; }

    public static int getIdCounter()
    { return idCounter; }

    public static void resetIdCounter()
    { idCounter = 0; }

    public void setName(String name)
    {
        // todo: exceptions
        this.name = name;
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
}
