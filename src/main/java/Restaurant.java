import java.time.LocalTime;
import java.util.List;

public class Restaurant //implements IRestaurant
{
    private int id;
    private String name;
    private String address;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<ITable> tables;
    private List<IUser> users;


}
