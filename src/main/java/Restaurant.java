import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javafx.util.converter.LocalTimeStringConverter;

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

    public static Restaurant loadDatabaseFromCSV(int id) throws IOException
    {
        Reader reader1 = Files.newBufferedReader(Paths.get("csv/restaurant"+id+".csv"));
        CSVReader csvReader1 = new CSVReader(reader1,';');
        String[] line1;
        line1 = csvReader1.readNext();
        Restaurant restaurant = new Restaurant(line1[1],line1[2],LocalTime.parse(line1[3]),LocalTime.parse(line1[4]));
        csvReader1.close();

        try ( Reader reader2 = Files.newBufferedReader(Paths.get("csv/restaurant"+id+"users.csv")) )
        {
            CSVReader csvReader2 = new CSVReader(reader2,';');
            String[] line2;

            while ((line2 = csvReader2.readNext()) != null)
            {
                new User(line2[2],line2[1],line2[3],restaurant);
            }
            csvReader2.close();
        }
        catch(IOException e){}

        try {
            Reader reader2 = Files.newBufferedReader(Paths.get("csv/restaurant"+id+"tables.csv"));
            CSVReader csvReader2 = new CSVReader(reader2,';');
            String[] line2;

            while ((line2 = csvReader2.readNext()) != null)
            {
                new Table(line2[2],Integer.valueOf(line2[1]),restaurant);
            }
            csvReader2.close();
        }
        catch(IOException e){}

        try {
            Reader reader2 = Files.newBufferedReader(Paths.get("csv/restaurant"+id+"reservations.csv"));
            CSVReader csvReader2 = new CSVReader(reader2,';');
            String[] line2;

            while ((line2 = csvReader2.readNext()) != null)
            {
                new Reservation(Integer.valueOf(line2[1]),
                                LocalTime.parse(line2[4]),
                                LocalTime.parse(line2[5]),
                                LocalDate.parse(line2[6]),
                                restaurant.getUsers().get(Integer.valueOf(line2[2])),
                                restaurant.getTables().get(Integer.valueOf(line2[3])));
            }
            csvReader2.close();
        }
        catch(IOException e){}


        return restaurant;
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
    }

    public double avgReservationsPerDay()
    {
        LocalDate lastDay = LocalDate.now();
        int reservationsNumber = 0;
        for (IUser user : this.getUsers())
        {
            for (IReservation reservation : user.getReservations())
            {
                if (reservation.getDate().isAfter(lastDay))
                    lastDay = reservation.getDate();
                reservationsNumber++;
            }
        }
        int days = (int)ChronoUnit.DAYS.between(LocalDate.now(), lastDay);

        return (double)reservationsNumber/(double)days;
    }

    public IUser bestClient()
    {
        if (users.size()!=0)
        {
            int bestSize = 0;
            int bestId = 0;
            for (IUser user : this.getUsers())
            {
                if (user.getReservations().size()>bestSize)
                {
                    bestId = user.getId();
                    bestSize = user.getReservations().size();
                }
            }
            return users.get(bestId);
        }
        else
        return null;
    }

    public int uselessClients()
    {
        if (users.size()!=0)
        {
            int useless=0;
            for (IUser user : this.getUsers())
            {
                if (user.getReservations().size() == 0)
                    useless++;
            }
            return useless;
        }
        else
            return 0;
    }

}
