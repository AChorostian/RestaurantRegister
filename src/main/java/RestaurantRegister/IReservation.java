package RestaurantRegister;

import java.time.*;

public interface IReservation
{
    int getId();
    int getPersonsNumber();
    LocalTime getStartTime();
    LocalTime getEndTime();
    LocalDate getDate();
    IUser getUser();
    ITable getTable();

    void setId(int id);
    void setPersonsNumber(int personsNumber);
    void setStartTime(LocalTime startTime);
    void setEndTime(LocalTime endTime);
    void setDate(LocalDate date);
    void setUser(IUser user);
    void setTable(ITable table);

    void sendEmailConfirmation();
}
