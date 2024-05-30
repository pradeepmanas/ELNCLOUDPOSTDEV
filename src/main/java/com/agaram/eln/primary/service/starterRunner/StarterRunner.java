package com.agaram.eln.primary.service.starterRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.fetchtenantsource.Datasourcemaster;
import com.agaram.eln.primary.model.sheetManipulation.Notification;
import com.agaram.eln.primary.repository.multitenant.DataSourceConfigRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Service
public class StarterRunner {
	
	@Autowired
    private DataSourceConfigRepository configRepo;

    private Map<Integer, TimerTask> scheduledTasks = new ConcurrentHashMap<>();
    
    private static final int MAX_POOL_SIZE = 10;
    private static final int MIN_IDLE = 5;
    private static final int CONNECTION_TIMEOUT = 120000;
    private static final int CONNECTION_THRESHOLD = 120000;

    public void executeOnStartup() throws SQLException {
        System.out.println("Task executed on startup");
        checkAndScheduleReminders();
    }

    public Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationid(rs.getLong("notificationid"));
        notification.setAddedon(rs.getTimestamp("addedon"));
        notification.setOrderid(rs.getLong("orderid"));
        notification.setAddedby(rs.getString("addedby"));
        notification.setDuedate(rs.getTimestamp("duedate"));
        notification.setIntervals(rs.getString("intervals"));
        notification.setScreen(rs.getString("screen"));
        notification.setCautiondate(rs.getTimestamp("cautiondate"));
        notification.setDescription(rs.getString("description"));
        notification.setUsercode(rs.getInt("usercode"));
        notification.setStatus(rs.getInt("status"));
        notification.setCurrentdate(rs.getTimestamp("currentdate"));
        notification.setBatchid(rs.getString("batchid"));

        return notification;
    }

    public void checkAndScheduleReminders() throws SQLException {
        List<Datasourcemaster> configList = configRepo.findByinitialize(true);

        // Get current date and time
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        // Convert to java.sql.Date
        Date toDate = Date.from(instant);

        // Get the current date and subtract one day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);  // Subtract one day
        Date fromDate = new Date(calendar.getTimeInMillis());
        
        for (Datasourcemaster objData : configList) {
            HikariConfig configuration = createHikariConfig(objData);
            try (HikariDataSource dataSource = new HikariDataSource(configuration);
                 Connection con = dataSource.getConnection()) {

                String query = "SELECT * FROM Notification WHERE status = 1 and cautiondate BETWEEN ? AND ?";
                try (PreparedStatement pst = con.prepareStatement(query)) {
                   
                	pst.setTimestamp(1, new Timestamp(fromDate.getTime()));
                    pst.setTimestamp(2, new Timestamp(toDate.getTime()));

                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                            Notification objNotification = mapResultSetToNotification(rs);
                            scheduleNotificationIfDue(objNotification, configuration);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Consider logging this properly
            }
        }
    }

    private HikariConfig createHikariConfig(Datasourcemaster objData) {
        HikariConfig configuration = new HikariConfig();
        configuration.setDriverClassName("org.postgresql.Driver");
        configuration.setJdbcUrl(objData.getUrl());
        configuration.setUsername(objData.getUsername());
        configuration.setPassword(objData.getPassword());
        configuration.setMaximumPoolSize(MAX_POOL_SIZE);
        configuration.setPoolName(objData.getUrl());
        configuration.setMinimumIdle(MIN_IDLE);
        configuration.setConnectionTestQuery("SELECT 1");
        configuration.setConnectionTimeout(CONNECTION_TIMEOUT);
        configuration.setLeakDetectionThreshold(CONNECTION_THRESHOLD);
        return configuration;
    }

    private void scheduleNotificationIfDue(Notification objNotification, HikariConfig configuration) throws SQLException {
        Date cautionDate = objNotification.getCautiondate();
        Instant caution = cautionDate.toInstant();
        LocalDateTime cautionTime = LocalDateTime.ofInstant(caution, ZoneId.systemDefault());
        LocalDateTime currentTime = LocalDateTime.now();

        if (cautionTime.isAfter(currentTime)) {
            Duration duration = Duration.between(currentTime, cautionTime);
            long delay = duration.toMillis();
            scheduleNotification(objNotification, delay, configuration);
        }else {
        	executeNotificationPop(objNotification, configuration);
        }
    }

    private void scheduleNotification(Notification objNotification, long delay, HikariConfig configuration) {
        TimerTask task = new TimerTask() {
            @SuppressWarnings("unlikely-arg-type")
			@Override
            public void run() {
                try {
                    executeNotificationPop(objNotification, configuration);
                } catch (SQLException e) {
                    e.printStackTrace(); // Consider logging this properly
                }
                scheduledTasks.remove(objNotification.getNotificationid());
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, delay);
        scheduledTasks.put(objNotification.getNotificationid().intValue(), task);
    }

    public void executeNotificationPop(Notification notification, HikariConfig configuration) throws SQLException {
        try (HikariDataSource dataSource = new HikariDataSource(configuration);
             Connection con = dataSource.getConnection()) {

            LocalDateTime localDateTime = LocalDateTime.now();
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            Date cDate = Date.from(instant);

            String details = "{\"ordercode\" :\"" + notification.getOrderid() + "\",\"order\" :\""
                    + notification.getBatchid() + "\",\"description\":\"" + notification.getDescription()
                    + "\",\"screen\":\"" + notification.getScreen() + "\"}";
            String path = notification.getScreen().equals("Sheet Order") ? "/registertask" : "/Protocolorder";

            String updateString = "INSERT INTO public.lsnotification(isnewnotification, notification, " +
                    "createdtimestamp, notificationdetils, notificationpath, notifationfrom_usercode, " +
                    "notifationto_usercode, repositorycode, repositorydatacode, notificationfor) VALUES ( 1, 'CAUTIONALERT', ?, ?, ?, ?, ?, 0, 0, 1); " +
                    "UPDATE Notification SET status = 0 WHERE notificationid = ?";

            try (PreparedStatement pst = con.prepareStatement(updateString)) {
                pst.setTimestamp(1, new Timestamp(cDate.getTime()));
                pst.setString(2, details);
                pst.setString(3, path);
                pst.setInt(4, notification.getUsercode());
                pst.setInt(5, notification.getUsercode());
                pst.setLong(6, notification.getNotificationid());

                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this properly
        }
    }
}