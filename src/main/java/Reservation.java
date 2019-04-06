import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class Reservation implements IReservation
{
    private int id;
    private int seats;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private int confirmationCode;

    private IUser user;
    private ITable table;

    private static int idCounter=0;

    public Reservation(int seats,LocalTime startTime, LocalTime endTime, LocalDate date, IUser user, ITable table)
    {
        this.table = table;
        this.user = user;
        setDate(date);

        setSeats(seats);
        setStartTime(startTime);
        setEndTime(endTime);

        this.id = idCounter++;
        table.addReservation(this);
        user.addReservation(this);

        confirmationCode = this.hashCode();
    }

    public int getId()
    { return id; }

    public int getSeats()
    { return seats; }

    public LocalTime getStartTime()
    { return startTime; }

    public LocalTime getEndTime()
    { return endTime; }

    public LocalDate getDate()
    { return date; }

    public IUser getUser()
    { return user; }

    public ITable getTable()
    { return table; }

    public static int getIdCounter()
    { return idCounter; }

    public static void resetIdCounter()
    { idCounter = 0; }

    public void setSeats(int seats)
    {
        if (seats<1 || seats>table.getSeats())
            throw new IllegalArgumentException();
        this.seats = seats;
    }

    public void setStartTime(LocalTime startTime)
    {
        if (endTime!=null)
        {
            if (startTime.isAfter(endTime))
                throw new DateTimeException("Start time should be earlier than end time");
            if (startTime.equals(endTime))
                throw new DateTimeException("Start time should be earlier than end time");
        }
        if (startTime.getMinute()%15 != 0)
            throw new DateTimeException("Number of minutes should be a multiple of 15");
        if (startTime.isBefore(this.table.getRestaurant().getStartTime()))
            throw new DateTimeException("Reservation can not start before restaurant opening");
        for (IReservation reservation : table.getReservations())
        {
            if (reservation.getDate().equals(this.getDate()) ) {
                if (startTime.isAfter(reservation.getStartTime()) && startTime.isBefore(reservation.getEndTime()))
                    throw new DateTimeException("Reservation can not start during other reservation");
                if (startTime.equals(reservation.getStartTime()) || startTime.equals(reservation.getEndTime()))
                    throw new DateTimeException("Reservation can not start during other reservation");
                if (endTime != null) {
                    if (reservation.getStartTime().isAfter(startTime) && reservation.getEndTime().isBefore(endTime))
                        throw new DateTimeException("new reservation can not contain other reservation");
                }
            }
        }
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime)
    {
        if (startTime!=null)
            if (endTime.isBefore(startTime) || endTime.equals(startTime))
                throw new DateTimeException("End time should be later than start time");
        if (endTime.getMinute()%15 != 0)
            throw new DateTimeException("Number of minutes should be a multiple of 15");
        if (endTime.isAfter(this.table.getRestaurant().getEndTime()))
            throw new DateTimeException("Reservation can not end after restaurant closing");
        for (IReservation reservation : table.getReservations())
        {
            if (reservation.getDate().equals(this.getDate()))
            {
                if (endTime.isAfter(reservation.getStartTime()) && endTime.isBefore(reservation.getEndTime()))
                    throw new DateTimeException("Reservation can not end during other reservation");
                if (endTime.equals(reservation.getStartTime()) || endTime.equals(reservation.getEndTime()))
                    throw new DateTimeException("Reservation can not end during other reservation");
                if (startTime!=null)
                {
                    if (reservation.getStartTime().isAfter(startTime) && reservation.getEndTime().isBefore(endTime))
                        throw new DateTimeException("new reservation can not contain other reservation");
                }
            }
        }
        this.endTime = endTime;
    }

    public void setDate(LocalDate date)
    {
        if(date.isBefore(LocalDate.now()))
            throw new DateTimeException("Reservation can not be set as passed");
        for (IReservation reservation : table.getReservations())
        {
            if (reservation.getDate().equals(date))
            {
                if (startTime!=null)
                {
                    if (startTime.isAfter(reservation.getStartTime()) && startTime.isBefore(reservation.getEndTime()))
                        throw new DateTimeException("Reservation can not start during other reservation");
                    if (startTime.equals(reservation.getStartTime()) || startTime.equals(reservation.getEndTime()))
                        throw new DateTimeException("Reservation can not start during other reservation");
                }
                if (endTime!=null)
                {
                    if (endTime.isAfter(reservation.getStartTime()) && endTime.isBefore(reservation.getEndTime()))
                        throw new DateTimeException("Reservation can not end during other reservation");
                    if (endTime.equals(reservation.getStartTime()) || endTime.equals(reservation.getEndTime()))
                        throw new DateTimeException("Reservation can not end during other reservation");
                }
                if (startTime != null && endTime != null)
                {
                    if (reservation.getStartTime().isAfter(startTime) && reservation.getEndTime().isBefore(endTime))
                        throw new DateTimeException("new reservation can not contain other reservation");
                }
            }
        }
        this.date = date;
    }

    public int getConfirmationCode()
    {
        return confirmationCode;
    }

    public void sendEmailConfirmation() throws MessagingException
    {
        String to = "confirmations@"+user.getRestaurant().getName()+".com";
        String from = user.getEMail();
        String host = "localhost";

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
        message.setSubject("Confirmation");
        message.setText("Hello "+user.getFullName()+", your confirmation code is: "+getConfirmationCode());
        Transport.send(message);
    }
}
