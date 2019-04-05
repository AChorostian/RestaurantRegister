import java.time.LocalTime;
import java.util.List;

public interface IRestaurant
{
    int getId();
    String getName();
    String getAddress();
    LocalTime getStartTime();
    LocalTime getEndTime();
    List<ITable> getTables();
    List<IUser> getUsers();

    void setName(String name);
    void setAddress(String address);
    void setStartTime(LocalTime startTime);
    void setEndTime(LocalTime endTime);

    void addTable(ITable table);
    void addUser(IUser user);

    void loadDatabaseFromCSV();
    void saveDatabaseToCSV();

    void avgReservationsPerMonth();
    void avgReservationsPerDay();
    void avgReservationsPerUser();
    void bestClient();
    void bestDayOfTheWeek();
}
