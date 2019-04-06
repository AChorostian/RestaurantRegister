import java.time.*;

public interface IReservation
{
    int getId();
    int getSeats();
    LocalTime getStartTime();
    LocalTime getEndTime();
    LocalDate getDate();
    IUser getUser();
    ITable getTable();

    void setSeats(int seats);
    void setStartTime(LocalTime startTime);
    void setEndTime(LocalTime endTime);
    void setDate(LocalDate date);
}
