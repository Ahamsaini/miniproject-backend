package com.mainApp.service.serviceImp;

import com.mainApp.exception.ResourceNotFoundException;
import com.mainApp.mapper.LabTimetableSlotMapper;
import com.mainApp.model.*;
import com.mainApp.repository.*;
import com.mainApp.requestdto.LabTimetableSlotRequest;
import com.mainApp.responcedto.LabTimetableSlotResponse;
import com.mainApp.responcedto.TimetableConflictResponse;
import com.mainApp.roles.DayOfWeek;
import com.mainApp.roles.LabSessionStatus;
import com.mainApp.service.serviceInterface.LabTimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LabTimetableServiceImpl implements LabTimetableService {

    private final LabTimetableSlotRepository timetableSlotRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final LabRepository labRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final LabSessionRepository labSessionRepository;
    private final LabTimetableSlotMapper labTimetableSlotMapper;

    @Override
    @Transactional
    public LabTimetableSlotResponse createTimetableSlot(LabTimetableSlotRequest request) {
        // Validate for conflicts
        TimetableConflictResponse conflicts = checkConflicts(request, "");
        if (conflicts.isHasConflicts()) {
            throw new IllegalStateException("Cannot create slot: " + String.join(", ", conflicts.getConflicts()));
        }

        // Fetch related entities
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found"));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        // Create and save slot
        LabTimetableSlot slot = labTimetableSlotMapper.toEntity(request, course, subject, lab, teacher);
        slot = timetableSlotRepository.save(slot);

        // Automatically generate sessions for the current week if applicable
        try {
            LocalDate today = LocalDate.now();
            generateLabSessionsFromTimetable(slot.getCourse().getId(), slot.getSemester(),
                    today, today.plusDays(7));
        } catch (Exception e) {
            // Silently fail if no slots found or other issues (session generation is
            // secondary)
        }

        return labTimetableSlotMapper.toResponse(slot);
    }

    @Override
    @Transactional
    public LabTimetableSlotResponse updateTimetableSlot(String slotId, LabTimetableSlotRequest request) {
        LabTimetableSlot slot = timetableSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Timetable slot not found"));

        // Validate for conflicts (exclude current slot)
        TimetableConflictResponse conflicts = checkConflicts(request, slotId);
        if (conflicts.isHasConflicts()) {
            throw new IllegalStateException("Cannot update slot: " + String.join(", ", conflicts.getConflicts()));
        }

        // Fetch related entities
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found"));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        // Update slot
        labTimetableSlotMapper.updateEntity(request, slot, course, subject, lab, teacher);
        slot = timetableSlotRepository.save(slot);

        // Automatically update sessions for the current week if applicable
        try {
            LocalDate today = LocalDate.now();
            generateLabSessionsFromTimetable(slot.getCourse().getId(), slot.getSemester(),
                    today, today.plusDays(7));
        } catch (Exception e) {
            // Silently fail
        }

        return labTimetableSlotMapper.toResponse(slot);
    }

    @Override
    @Transactional
    public void deleteTimetableSlot(String slotId) {
        if (!timetableSlotRepository.existsById(slotId)) {
            throw new ResourceNotFoundException("Timetable slot not found");
        }
        timetableSlotRepository.deleteById(slotId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTimetableSlotResponse> getCourseTimetable(String courseId, Integer semester) {
        List<LabTimetableSlot> allSlots = timetableSlotRepository.findAll();
        List<LabTimetableSlotResponse> responses = new ArrayList<>();

        for (LabTimetableSlot slot : allSlots) {
            LabTimetableSlotResponse res = labTimetableSlotMapper.toResponse(slot);
            boolean isCurrent = slot.getCourse().getId().equals(courseId) && slot.getSemester().equals(semester);
            res.setIsTarget(isCurrent);
            responses.add(res);
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTimetableSlotResponse> getStudentTimetable(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (student.getCourse() == null) {
            return new ArrayList<>();
        }

        List<LabTimetableSlot> slots = timetableSlotRepository.findStudentTimetable(
                student.getCourse().getId(),
                student.getCurrentSemester());
        return labTimetableSlotMapper.toResponseList(slots);
    }

    @Override
    public TimetableConflictResponse checkConflicts(LabTimetableSlotRequest request, String excludeSlotId) {
        TimetableConflictResponse response = new TimetableConflictResponse();

        String exclude = excludeSlotId == null ? "" : excludeSlotId;

        // Check lab availability
        boolean labConflict = timetableSlotRepository.existsLabConflict(
                request.getLabId(),
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime(),
                exclude);

        if (labConflict) {
            response.addConflict("Lab is already booked during this time slot");
        }

        // Check teacher availability
        boolean teacherConflict = timetableSlotRepository.existsTeacherConflict(
                request.getTeacherId(),
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime(),
                exclude);

        if (teacherConflict) {
            response.addConflict("Teacher is already assigned to another session during this time");
        }

        return response;
    }

    @Override
    @Transactional
    public void generateLabSessionsFromTimetable(String courseId, Integer semester,
            LocalDate startDate, LocalDate endDate) {
        // Get all timetable slots for the course-semester
        List<LabTimetableSlot> slots = timetableSlotRepository
                .findByCourseIdAndSemesterOrderByDayOfWeekAscStartTimeAsc(courseId, semester);

        if (slots.isEmpty()) {
            throw new IllegalStateException("No timetable slots found for this course and semester");
        }

        // Iterate through each date in the range
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = convertToDayOfWeek(currentDate.getDayOfWeek());

            // Find slots for this day
            for (LabTimetableSlot slot : slots) {
                if (slot.getDayOfWeek() == dayOfWeek) {
                    // Check if session already exists
                    boolean exists = labSessionRepository.existsByLabAndSessionDateAndStartTime(
                            slot.getLab(), currentDate, slot.getStartTime());

                    if (!exists) {
                        // Create lab session from template
                        LabSession session = new LabSession();
                        session.setLab(slot.getLab());
                        session.setSubject(slot.getSubject());
                        session.setTeacher(slot.getTeacher());
                        session.setSessionDate(currentDate);
                        session.setStartTime(slot.getStartTime());
                        session.setEndTime(slot.getEndTime());

                        // Calculate duration
                        int durationMinutes = (int) java.time.Duration.between(
                                slot.getStartTime(), slot.getEndTime()).toMinutes();
                        session.setDurationMinutes(durationMinutes);

                        session.setStatus(LabSessionStatus.SCHEDULED);
                        session.setIsCodeGenerated(false);
                        session.setAttendanceMarked(false);

                        labSessionRepository.save(session);
                    }
                }
            }

            currentDate = currentDate.plusDays(1);
        }
    }

    private DayOfWeek convertToDayOfWeek(java.time.DayOfWeek javaDayOfWeek) {
        return DayOfWeek.valueOf(javaDayOfWeek.toString());
    }
}
