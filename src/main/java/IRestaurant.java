import java.time.LocalTime;
import java.util.List;

public interface IRestaurant
{
    int getId();
    String getName();
    String getAddress();
    LocalTime getStartTime();
    LocalTime getEndTime();
    List<ITable> getTablesList();
    List<IUser> getUsersList();

    void setId(int id);
    void setName(String name);
    void setAddress(String address);
    void setStartTime();
    void setEndTime();

    void addTable(ITable table);
    void deleteTable(ITable table);
    void addUser(IUser user);
    void deleteUser(IUser user);

    void loadDatabaseFromCSV();
    void saveDatabaseToCSV();

    void avgReservationsPerMonth();
    void avgReservationsPerDay();
    void avgReservationsPerUser();
    void bestClient();
    void bestDayOfTheWeek();
}
