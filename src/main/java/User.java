import java.util.ArrayList;
import java.util.List;

final public class User implements IUser
{
    private int id;
    private String fullName;
    private String eMail;
    private String phone;

    private IRestaurant restaurant;
    private List<IReservation> reservations;

    private static int idCounter=0;

    public User(String fullName, String eMail, String phone, Restaurant restaurant)
    {
        setFullName(fullName);
        setEMail(eMail);
        setPhone(phone);

        this.id = idCounter++;
        this.restaurant = restaurant;
        this.reservations = new ArrayList<IReservation>() {};

        restaurant.addUser(this);
    }

    public int getId()
    { return id; }

    public String getFullName()
    { return fullName; }

    public String getEMail()
    { return eMail; }

    public String getPhone()
    { return phone; }

    public IRestaurant getRestaurant()
    { return restaurant; }

    public List<IReservation> getReservations()
    { return reservations; }

    public static int getIdCounter()
    { return idCounter; }

    public static void resetIdCounter()
    { idCounter = 0; }

    public void setFullName(String fullName)
    {
        if (fullName.matches("([a-z]|[A-Z]|[ĄąĆćĘęŁłŃńÓóŚśŹźŻż]| |-)+"))
            this.fullName = fullName;
        else
            throw new IllegalArgumentException();
    }

    public void setEMail(String eMail)
    {
        if (eMail.matches("^[a-zA-Z0-9_]+[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]*@[a-zA-Z0-9.-]+[.][a-zA-Z0-9]+$"))
            this.eMail = eMail;
        else
            throw new IllegalArgumentException();
    }

    public void setPhone(String phone)
    {
        if (phone.matches("^(([\\s-+()]*\\d[\\s-+()]*){9})|(([\\s-+()]*\\d[\\s-+()]*){11})$"))
            this.phone = phone;
        else
            throw new IllegalArgumentException();
    }

    public void addReservation(IReservation reservation)
    {
        // todo: exceptions
        reservations.add(reservation);
    }
}
