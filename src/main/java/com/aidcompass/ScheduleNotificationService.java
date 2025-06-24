package com.aidcompass;

//import com.aidcompass.repositories.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleNotificationService {

    //private final AppointmentRepository appointmentRepository;


    // повідомлення корустувача про запис за день до нього
    public void notifyBatchBeforeAppointment() {

    }
}
