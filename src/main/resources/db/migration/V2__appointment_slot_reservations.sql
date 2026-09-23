CREATE TABLE professionals (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_professionals_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT uk_professionals_user UNIQUE (user_id)
);

CREATE TABLE specialties (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    duration_minutes INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uk_specialties_name UNIQUE (name),
    CONSTRAINT ck_specialties_duration CHECK (duration_minutes IN (30, 60))
);

CREATE TABLE professional_specialties (
    professional_id BIGINT NOT NULL,
    specialty_id BIGINT NOT NULL,
    PRIMARY KEY (professional_id, specialty_id),
    CONSTRAINT fk_professional_specialties_professional FOREIGN KEY (professional_id) REFERENCES professionals (id),
    CONSTRAINT fk_professional_specialties_specialty FOREIGN KEY (specialty_id) REFERENCES specialties (id)
);

CREATE TABLE availability_slots (
    id BIGINT NOT NULL AUTO_INCREMENT,
    professional_id BIGINT NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_availability_slots_professional FOREIGN KEY (professional_id) REFERENCES professionals (id),
    CONSTRAINT uk_availability_slots_professional_start UNIQUE (professional_id, starts_at)
);

CREATE TABLE appointments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    professional_id BIGINT NOT NULL,
    specialty_id BIGINT NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    appointment_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_appointments_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_appointments_professional FOREIGN KEY (professional_id) REFERENCES professionals (id),
    CONSTRAINT fk_appointments_specialty FOREIGN KEY (specialty_id) REFERENCES specialties (id),
    CONSTRAINT ck_appointments_type CHECK (appointment_type IN ('GENERAL', 'SPECIALIZED')),
    CONSTRAINT ck_appointments_status CHECK (status IN ('APPROVED', 'REQUESTED'))
);

CREATE TABLE appointment_slots (
    id BIGINT NOT NULL AUTO_INCREMENT,
    appointment_id BIGINT NOT NULL,
    slot_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_appointment_slots_appointment FOREIGN KEY (appointment_id) REFERENCES appointments (id),
    CONSTRAINT fk_appointment_slots_slot FOREIGN KEY (slot_id) REFERENCES availability_slots (id),
    CONSTRAINT uk_appointment_slots_slot UNIQUE (slot_id),
    CONSTRAINT uk_appointment_slots_appointment_slot UNIQUE (appointment_id, slot_id)
);
