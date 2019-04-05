import java.time.DateTimeException;
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
        this.table = table;
        this.user = user;

        setSeats(seats);
        setStartTime(startTime);
        setEndTime(endTime);
        setDate(date);

        this.id = idCounter++;
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
        if (seats<1 || seats>table.getSeats())
            throw new IllegalArgumentException();
        this.seats = seats;
    }

    public void setStartTime(LocalTime startTime)
    {
        if (startTime.getMinute()%15 != 0)
            throw new DateTimeException("Number of minutes should be a multiple of 15");
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime)
    {
        if (endTime.getMinute()%15 != 0)
            throw new DateTimeException("Number of minutes should be a multiple of 15");
        this.endTime = endTime;
    }

    public void setDate(LocalDate date)
    {
        //todo: exceptions
        this.date = date;
    }

    public void sendEmailConfirmation()
    {
        //todo: exceptions
        //todo: email sending functionality
    }
}
