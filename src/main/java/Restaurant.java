import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements IRestaurant
{
    private int id;
    private String name;
    private String address;
    private LocalTime startTime;
    private LocalTime endTime;

    private List<ITable> tables;
    private List<IUser> users;

    private static int idCounter=1;

    public Restaurant(String name, String address, LocalTime startTime, LocalTime endTime)
    {
        // todo: exceptions
        this.name = name;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;

        this.id = idCounter++;
        this.tables = new ArrayList<ITable>() {};
        this.users = new ArrayList<IUser>() {};
    }

    public int getId()
    { return id; }

    public String getName()
    { return name; }

    public String getAddress()
    { return address; }

    public LocalTime getStartTime()
    { return startTime; }

    public LocalTime getEndTime()
    { return endTime; }

    public List<ITable> getTables()
    { return tables; }

    public List<IUser> getUsers()
    { return users; }

    public static int getIdCounter()
    { return idCounter; }

    public static void resetIdCounter()
    { idCounter = 0; }

    public void setName(String name)
    {
        if (name.length()==0 || name.length()>50)
            throw new IllegalArgumentException();
        else
            this.name = name;
    }

    public void setAddress(String address)
    {
        if (address.length()==0 || address.length()>100)
            throw new IllegalArgumentException();
        else
            this.address = address;
    }

    public void setStartTime(LocalTime startTime) throws DateTimeException
    {
        if (startTime.isAfter(endTime) || startTime.equals(endTime))
            throw new DateTimeException("Start time should be earlier than end time");
        if (startTime.getMinute()%15 != 0)
            throw new DateTimeException("Number of minutes should be a multiple of 15");
        else
            this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) throws DateTimeException
    {
        if (endTime.isBefore(startTime) || endTime.equals(startTime))
            throw new DateTimeException("End time should be later than start time");
        if (endTime.getMinute()%15 != 0)
            throw new DateTimeException("Number of minutes should be a multiple of 15");
        else
            this.endTime = endTime;
    }

    public void addTable(ITable table)
    {
        // todo: exceptions
        tables.add(table);
    }

    public void addUser(IUser user)
    {
        // todo: exceptions
        users.add(user);
    }

    public void loadDatabaseFromCSV()
    {
        // todo
        // baza jest wczytywana z trzech plików csv rest(id)users , rest(id)tables , rest(id)reservations
        // może być tylko część tych plików, ale jeśli są rezerwacje, to muszą istnieć users i tables
        // baza musi być pusta.
    }

    public void saveDatabaseToCSV()
    {
        // todo
        // baza jest zapisywana do trzech plików csv
        // rest(id)users ,
        // rest(id)tables ,
        // rest(id)reservations
        // pod warunkiem, że istnieją dane (w każdym z osobna)
    }

    public void avgReservationsPerMonth()
    {
        // todo
    }

    public void avgReservationsPerDay()
    {
        // todo
    }

    public void avgReservationsPerUser()
    {
        // todo
    }

    public void bestClient()
    {
        // todo
    }

    public void bestDayOfTheWeek()
    {
        // todo
    }
}
