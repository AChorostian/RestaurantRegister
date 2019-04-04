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

    public static void resetIdCounter()
    { idCounter = 0; }

    public void setName(String name)
    {
        // todo: exceptions
        this.name = name;
    }

    public void setAddress(String address)
    {
        // todo: exceptions
        this.address = address;
    }

    public void setStartTime(LocalTime startTime)
    {
        // todo: exceptions
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime)
    {
        // todo: exceptions
        this.endTime = endTime;
    }

    public void addTable(ITable table)
    {
        // todo: exceptions
        tables.add(table);
    }

    public void deleteTable(ITable table)
    {
        // todo: exceptions
        tables.remove(table);
    }

    public void addUser(IUser user)
    {
        // todo: exceptions
        users.add(user);
    }

    public void deleteUser(IUser user)
    {
        // todo: exceptions
        users.remove(user);
    }

    public void loadDatabaseFromCSV()
    {
        // todo
    }

    public void saveDatabaseToCSV()
    {
        // todo
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
