import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation implements IReservation
{
    private int id;
    private int seats;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;

    private IUser user;
    private ITable table;

    private static int idCounter=0;

    public Reservation(int seats,LocalTime startTime, LocalTime endTime, LocalDate date, IUser user, ITable table)
    {
        //todo: exceptions

        this.seats = seats;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;

        this.id = idCounter++;
        this.user = user;
        this.table = table;

        //todo: auto add to lists in User and Table
    }

    public int getId()
    { return id; }

    public int getSeats()
    { return seats; }

    public LocalTime getStartTime()
    { return startTime; }

    public LocalTime getEndTime()
    { return endTime; }

    public LocalDate getDate()
    { return date; }

    public IUser getUser()
    { return user; }

    public ITable getTable()
    { return table; }

    public static int getIdCounter()
    { return idCounter; }

    public static void resetIdCounter()
    { idCounter = 0; }

    public void setSeats(int seats)
    {
        //todo: exceptions
    }

    public void setStartTime(LocalTime startTime)
    {
        //todo: exceptions
    }

    public void setEndTime(LocalTime endTime)
    {
        //todo: exceptions
    }

    public void setDate(LocalDate date)
    {
        //todo: exceptions
    }

    public void setUser(IUser user)
    {
        //todo: exceptions
    }

    public void setTable(ITable table)
    {
        //todo: exceptions
    }

    public void sendEmailConfirmation()
    {
        //todo: exceptions
        //todo: email sending functionality
    }
}
