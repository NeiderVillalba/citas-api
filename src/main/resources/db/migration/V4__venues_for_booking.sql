CREATE TABLE venues (
    id BIGINT NOT NULL,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(180) NOT NULL,
    address VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_venues_code UNIQUE (code)
);

INSERT INTO venues (id, code, name, address) VALUES
    (1, 'HIC', 'Hospital Internacional de Colombia', 'Km 7 Autopista Bucaramanga-Piedecuesta, Valle de Menzulí, Santander'),
    (2, 'ICV', 'Fundación Cardiovascular de Colombia / Instituto Cardiovascular', 'Calle 155A No. 23-58, Urbanización El Bosque, Floridablanca, Santander');

CREATE TABLE professional_venues (
    professional_id BIGINT NOT NULL,
    venue_id BIGINT NOT NULL,
    PRIMARY KEY (professional_id, venue_id),
    CONSTRAINT fk_professional_venues_professional FOREIGN KEY (professional_id) REFERENCES professionals (id),
    CONSTRAINT fk_professional_venues_venue FOREIGN KEY (venue_id) REFERENCES venues (id)
);

ALTER TABLE availability_slots ADD COLUMN venue_id BIGINT NULL;
ALTER TABLE availability_slots ADD CONSTRAINT fk_availability_slots_venue FOREIGN KEY (venue_id) REFERENCES venues (id);
CREATE INDEX idx_availability_slots_venue_start ON availability_slots (venue_id, starts_at);

ALTER TABLE appointments ADD COLUMN venue_id BIGINT NULL;
ALTER TABLE appointments ADD CONSTRAINT fk_appointments_venue FOREIGN KEY (venue_id) REFERENCES venues (id);
