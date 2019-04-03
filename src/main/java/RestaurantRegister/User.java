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
        if (!fullName.matches("([a-z]|[A-Z]|[ĄąĆćĘęŁłŃńÓóŚśŹźŻż]| |-)+") ||
            !eMail.matches("^[a-zA-Z0-9_]+[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]*@[a-zA-Z0-9.-]+[.][a-zA-Z0-9]+$") ||
            !phone.matches("^(([\\s-+()]*\\d[\\s-+()]*){9})|(([\\s-+()]*\\d[\\s-+()]*){11})$"))
                throw new IllegalArgumentException();

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
        reservationsList.add(reservation);
    }
    public void deleteReservation(IReservation reservation)
    {
        // todo: exceptions
        reservationsList.remove(reservation);
    }
}
