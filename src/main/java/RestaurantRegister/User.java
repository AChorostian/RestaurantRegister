package RestaurantRegister;

import java.util.ArrayList;
import java.util.List;

public class User implements IUser
{
    private int id;
    private String fullName;
    private String eMail;
    private String phone;
    private List<IReservation> reservationsList;

    private static int idCounter=0;

    public User(String fullName, String eMail, String phone)
    {
        // todo: exceptions
        this.fullName = fullName;
        this.eMail = eMail;
        this.phone = phone;

        this.id = idCounter++;
        this.reservationsList = new ArrayList<IReservation>() {};
    }

    public int getId()
    { return id; }

    public String getFullName()
    { return fullName; }

    public String getEMail()
    { return eMail; }

    public String getPhone()
    { return phone; }

    public List<IReservation> getReservationsList()
    { return reservationsList; }

    public static void resetIdCounter()
    {
        idCounter = 0;
    }

    public void setFullName(String fullName)
    {
        // todo: exceptions
        this.fullName = fullName;
    }

    public void setEMail(String eMail)
    {
        // todo: exceptions
        this.eMail = eMail;
    }

    public void setPhone(String phone)
    {
        // todo: exceptions
        this.phone = phone;
    }

    public void addReservation(IReservation reservation)
    {
        // todo: exceptions
        reservationsList.add(reservation);
    }
    public void deleteReservation(IReservation reservation)
    {
        // todo: exceptions
        reservationsList.remove(reservation);
    }
}
