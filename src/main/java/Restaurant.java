import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.*;
import java.nio.file.*;
import java.util.Arrays;

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
        setName(name);
        setAddress(address);
        setStartTime(startTime);
        setEndTime(endTime);

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
        if (endTime!=null)
            if (startTime.isAfter(endTime) || startTime.equals(endTime))
                throw new DateTimeException("Start time should be earlier than end time");
        if (startTime.getMinute()%15 != 0)
            throw new DateTimeException("Number of minutes should be a multiple of 15");
        else
            this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) throws DateTimeException
    {
        if (startTime!=null)
            if (endTime.isBefore(startTime) || endTime.equals(startTime))
                throw new DateTimeException("End time should be later than start time");
        if (endTime.getMinute()%15 != 0)
            throw new DateTimeException("Number of minutes should be a multiple of 15");
        else
            this.endTime = endTime;
    }

    public void addTable(ITable table)
    {
        tables.add(table);
    }

    public void addUser(IUser user)
    {
        users.add(user);
    }

    public void loadDatabaseFromCSV()
    {
        // todo
        // baza jest wczytywana z trzech plików csv rest(id)users , rest(id)tables , rest(id)reservations
        // może być tylko część tych plików, ale jeśli są rezerwacje, to muszą istnieć users i tables
        // baza musi być pusta.
    }

    public void saveDatabaseToCSV() throws IOException
    {
        if (Reservation.getIdCounter() != 0)
        {
            Writer writer = Files.newBufferedWriter(Paths.get("csv/restaurant"+id+"reservations.csv"));
            CSVWriter csvWriter = new CSVWriter(writer, ';', CSVWriter.NO_QUOTE_CHARACTER);
            String[] line = new String[7];

            for (IUser user : this.getUsers())
            {
                for (IReservation reservation : user.getReservations())
                {
                    line[0] = String.valueOf(reservation.getId());
                    line[1] = String.valueOf(reservation.getSeats());
                    line[2] = String.valueOf(reservation.getUser().getId());
                    line[3] = String.valueOf(reservation.getTable().getId());
                    line[4] = String.valueOf(reservation.getStartTime());
                    line[5] = String.valueOf(reservation.getEndTime());
                    line[6] = String.valueOf(reservation.getDate());
                    csvWriter.writeNext(line);
                }
            }
            csvWriter.close();
        }
        if (User.getIdCounter() != 0)
        {
            Writer writer = Files.newBufferedWriter(Paths.get("csv/restaurant"+id+"users.csv"));
            CSVWriter csvWriter = new CSVWriter(writer, ';', CSVWriter.NO_QUOTE_CHARACTER);
            String[] line = new String[4];

            for (IUser user : this.getUsers())
            {
                line[0] = String.valueOf(user.getId());
                line[1] = String.valueOf(user.getEMail());
                line[2] = String.valueOf(user.getFullName());
                line[3] = String.valueOf(user.getPhone());
                csvWriter.writeNext(line);
            }
            csvWriter.close();
        }
        if (Table.getIdCounter() != 0)
        {
            Writer writer = Files.newBufferedWriter(Paths.get("csv/restaurant"+id+"tables.csv"));
            CSVWriter csvWriter = new CSVWriter(writer, ';', CSVWriter.NO_QUOTE_CHARACTER);
            String[] line = new String[3];

            for (ITable table : this.getTables())
            {
                line[0] = String.valueOf(table.getId());
                line[1] = String.valueOf(table.getSeats());
                line[2] = String.valueOf(table.getName());
                csvWriter.writeNext(line);
            }
            csvWriter.close();
        }
        Writer writer = Files.newBufferedWriter(Paths.get("csv/restaurant"+id+".csv"));
        CSVWriter csvWriter = new CSVWriter(writer, ';', CSVWriter.NO_QUOTE_CHARACTER);
        String[] line = new String[5];

        line[0] = String.valueOf(this.id);
        line[1] = String.valueOf(this.name);
        line[2] = String.valueOf(this.address);
        line[3] = String.valueOf(this.startTime);
        line[4] = String.valueOf(this.endTime);
        csvWriter.writeNext(line);
        csvWriter.close();

        // todo
        //        // baza jest zapisywana do trzech plików csv
        //        // rest(id)users ,
        //        // rest(id)tables ,
        //        // rest(id)reservations
        //        // pod warunkiem, że istnieją dane (w każdym z osobna)
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
