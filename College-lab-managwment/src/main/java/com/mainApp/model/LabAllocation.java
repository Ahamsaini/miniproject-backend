package com.mainApp.model;

import java.time.LocalDate;

import com.mainApp.roles.AllocationStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "lab_allocations", uniqueConstraints = @UniqueConstraint(columnNames = { "student_id",
        "lab_session_id" }))
public class LabAllocation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "labAllocations", "subjectEnrollments", "attendances" })
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_session_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "labAllocations", "attendances", "sessionCodes" })
    private LabSession labSession;

    @Column(name = "pc_number", length = 10)
    private String pcNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AllocationStatus status;

    @Column(name = "allocation_date")
    private LocalDate allocationDate;

    @Column(name = "is_manual_allocation")
    private Boolean isManualAllocation = false;

    @Column(name = "allocation_notes", length = 500)
    private String allocationNotes;
}
