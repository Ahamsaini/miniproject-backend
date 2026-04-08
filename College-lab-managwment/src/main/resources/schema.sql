-- College Lab Management System Schema SQL
-- Generated based on JPA Entity Model

-- Users Table (Parent Table for Students, Teachers, Admins)
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15),
    profile_picture_url VARCHAR(255),
    role VARCHAR(50) NOT NULL,
    last_login TIMESTAMP NULL,
    email_verified BOOLEAN DEFAULT FALSE,
    account_locked BOOLEAN DEFAULT FALSE,
    is_approved BOOLEAN DEFAULT FALSE,
    failed_login_attempts INTEGER DEFAULT 0,
    user_type VARCHAR(255)
);

-- Students Table (Inherits from users)
CREATE TABLE IF NOT EXISTS students (
    user_id VARCHAR(36) PRIMARY KEY,
    roll_number VARCHAR(20) UNIQUE,
    registration_number VARCHAR(20) UNIQUE,
    date_of_birth DATE,
    gender VARCHAR(20),
    address VARCHAR(500),
    emergency_contact VARCHAR(15),
    guardian_name VARCHAR(100),
    guardian_contact VARCHAR(15),
    academic_year VARCHAR(9),
    current_semester INTEGER,
    section VARCHAR(5),
    batch VARCHAR(10),
    blood_group VARCHAR(5),
    aadhar_number VARCHAR(12) UNIQUE,
    pan_number VARCHAR(10) UNIQUE,
    course_id VARCHAR(36),
    CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Teachers Table (Inherits from users)
CREATE TABLE IF NOT EXISTS teachers (
    user_id VARCHAR(36) PRIMARY KEY,
    employee_id VARCHAR(20) UNIQUE,
    qualification VARCHAR(100),
    department VARCHAR(100),
    designation VARCHAR(50),
    years_of_experience INTEGER,
    office_room VARCHAR(20),
    office_hours VARCHAR(100),
    specialization VARCHAR(200),
    CONSTRAINT fk_teacher_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Admins Table (Inherits from users)
CREATE TABLE IF NOT EXISTS admins (
    user_id VARCHAR(36) PRIMARY KEY,
    employee_id VARCHAR(20) UNIQUE,
    department VARCHAR(100),
    designation VARCHAR(50),
    admin_level INTEGER,
    permission_level VARCHAR(20),
    CONSTRAINT fk_admin_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Courses Table
CREATE TABLE IF NOT EXISTS courses (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(200) NOT NULL,
    description TEXT,
    duration_years INTEGER,
    total_semesters INTEGER,
    department VARCHAR(100),
    total_credits INTEGER,
    eligibility_criteria TEXT,
    fee_structure JSON,
    status VARCHAR(50) NOT NULL
);

-- Subjects Table
CREATE TABLE IF NOT EXISTS subjects (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    subject_code VARCHAR(20) NOT NULL UNIQUE,
    subject_name VARCHAR(200) NOT NULL,
    description TEXT,
    semester_number INTEGER,
    theory_hours INTEGER,
    lab_hours INTEGER,
    total_credits INTEGER,
    prerequisites JSON,
    syllabus TEXT,
    reference_books JSON,
    course_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_subject_course FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Labs Table
CREATE TABLE IF NOT EXISTS labs (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    lab_code VARCHAR(20) NOT NULL UNIQUE,
    lab_name VARCHAR(100) NOT NULL,
    room_number VARCHAR(20),
    floor INTEGER,
    building VARCHAR(50),
    capacity INTEGER,
    available_pcs INTEGER,
    equipment_details JSON,
    software_installed JSON,
    is_air_conditioned BOOLEAN DEFAULT FALSE,
    has_projector BOOLEAN DEFAULT FALSE,
    internet_speed VARCHAR(255),
    maintenance_schedule JSON,
    lab_type VARCHAR(50)
);

-- Lab Sessions Table
CREATE TABLE IF NOT EXISTS lab_sessions (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    lab_id VARCHAR(36) NOT NULL,
    subject_id VARCHAR(36) NOT NULL,
    teacher_id VARCHAR(36) NOT NULL,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    duration_minutes INTEGER,
    session_topic VARCHAR(200),
    experiment_name VARCHAR(200),
    objectives TEXT,
    materials_required JSON,
    status VARCHAR(50),
    section VARCHAR(5),
    is_code_generated BOOLEAN DEFAULT FALSE,
    code_generated_at TIMESTAMP NULL,
    attendance_marked BOOLEAN DEFAULT FALSE,
    attendance_marked_at TIMESTAMP NULL,
    CONSTRAINT fk_labsession_lab FOREIGN KEY (lab_id) REFERENCES labs(id),
    CONSTRAINT fk_labsession_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT fk_labsession_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(user_id)
);

-- Attendance Table
CREATE TABLE IF NOT EXISTS attendances (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    student_id VARCHAR(36) NOT NULL,
    lab_session_id VARCHAR(36) NOT NULL,
    entry_time TIMESTAMP NULL,
    exit_time TIMESTAMP NULL,
    duration_minutes INTEGER,
    pc_number VARCHAR(255),
    status VARCHAR(50),
    is_late BOOLEAN DEFAULT FALSE,
    late_minutes INTEGER,
    is_early_exit BOOLEAN DEFAULT FALSE,
    early_exit_minutes INTEGER,
    remarks VARCHAR(500),
    verified_by VARCHAR(255),
    verified_at TIMESTAMP NULL,
    CONSTRAINT uk_student_session UNIQUE (student_id, lab_session_id),
    CONSTRAINT fk_attendance_student FOREIGN KEY (student_id) REFERENCES students(user_id),
    CONSTRAINT fk_attendance_session FOREIGN KEY (lab_session_id) REFERENCES lab_sessions(id)
);

-- Lab Allocations Table
CREATE TABLE IF NOT EXISTS lab_allocations (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    student_id VARCHAR(36) NOT NULL,
    lab_session_id VARCHAR(36) NOT NULL,
    pc_number VARCHAR(10),
    status VARCHAR(50),
    allocation_date DATE,
    is_manual_allocation BOOLEAN DEFAULT FALSE,
    allocation_notes VARCHAR(500),
    CONSTRAINT uk_allocation_student_session UNIQUE (student_id, lab_session_id),
    CONSTRAINT fk_allocation_student FOREIGN KEY (student_id) REFERENCES students(user_id),
    CONSTRAINT fk_allocation_session FOREIGN KEY (lab_session_id) REFERENCES lab_sessions(id)
);

-- Audit Logs Table
CREATE TABLE IF NOT EXISTS audit_logs (
    id VARCHAR(36) PRIMARY KEY,
    action VARCHAR(50) NOT NULL,
    entity_name VARCHAR(100),
    entity_id VARCHAR(100),
    old_values JSON,
    new_values JSON,
    performed_by VARCHAR(255),
    performed_by_username VARCHAR(255),
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    timestamp TIMESTAMP NOT NULL,
    status VARCHAR(20),
    error_message TEXT,
    request_url VARCHAR(500),
    request_method VARCHAR(10),
    execution_time_ms BIGINT
);

-- Lab Timetable Slots Table
CREATE TABLE IF NOT EXISTS lab_timetable_slots (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    course_id VARCHAR(36) NOT NULL,
    subject_id VARCHAR(36) NOT NULL,
    lab_id VARCHAR(36) NOT NULL,
    teacher_id VARCHAR(36) NOT NULL,
    semester INTEGER NOT NULL,
    academic_year VARCHAR(9),
    section VARCHAR(10),
    day_of_week VARCHAR(255) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_recurring BOOLEAN DEFAULT TRUE,
    recurrence_pattern VARCHAR(50),
    notes TEXT,
    CONSTRAINT fk_slot_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT fk_slot_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT fk_slot_lab FOREIGN KEY (lab_id) REFERENCES labs(id),
    CONSTRAINT fk_slot_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(user_id)
);

-- Lecture Schedules Table
CREATE TABLE IF NOT EXISTS lecture_schedules (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    subject_id VARCHAR(36) NOT NULL,
    teacher_id VARCHAR(36) NOT NULL,
    day_of_week VARCHAR(255) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    classroom VARCHAR(20),
    section VARCHAR(5),
    academic_year VARCHAR(9),
    semester INTEGER,
    is_recurring BOOLEAN DEFAULT TRUE,
    recurrence_pattern VARCHAR(50),
    valid_from DATE,
    valid_to DATE,
    CONSTRAINT fk_lecture_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT fk_lecture_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(user_id)
);

-- Session Codes Table
CREATE TABLE IF NOT EXISTS session_codes (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    lab_session_id VARCHAR(36) NOT NULL,
    code_type VARCHAR(255) NOT NULL,
    code VARCHAR(10) NOT NULL UNIQUE,
    generated_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    used_at TIMESTAMP NULL,
    total_uses INTEGER DEFAULT 0,
    max_uses INTEGER DEFAULT 50,
    is_valid BOOLEAN DEFAULT TRUE,
    CONSTRAINT uk_session_code_type UNIQUE (lab_session_id, code_type),
    CONSTRAINT fk_sessioncode_session FOREIGN KEY (lab_session_id) REFERENCES lab_sessions(id)
);

-- Student Subject Enrollments Table
CREATE TABLE IF NOT EXISTS student_subject_enrollments (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    student_id VARCHAR(36) NOT NULL,
    subject_id VARCHAR(36) NOT NULL,
    semester INTEGER NOT NULL,
    academic_year VARCHAR(9),
    enrollment_date DATE,
    status VARCHAR(50),
    grade VARCHAR(255),
    attendance_percentage DOUBLE PRECISION,
    CONSTRAINT uk_student_subject_enroll UNIQUE (student_id, subject_id, semester, academic_year),
    CONSTRAINT fk_enroll_student FOREIGN KEY (student_id) REFERENCES students(user_id),
    CONSTRAINT fk_enroll_subject FOREIGN KEY (subject_id) REFERENCES subjects(id)
);

-- Teacher Subject Expertise Table
CREATE TABLE IF NOT EXISTS teacher_subject_expertise (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,
    teacher_id VARCHAR(36) NOT NULL,
    subject_id VARCHAR(36) NOT NULL,
    experience_years INTEGER,
    proficiency_level VARCHAR(50),
    is_primary_expert BOOLEAN DEFAULT FALSE,
    assigned_date DATE,
    notes TEXT,
    CONSTRAINT uk_teacher_subject UNIQUE (teacher_id, subject_id),
    CONSTRAINT fk_expertise_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(user_id),
    CONSTRAINT fk_expertise_subject FOREIGN KEY (subject_id) REFERENCES subjects(id)
);
