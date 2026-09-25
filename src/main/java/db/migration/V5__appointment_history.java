package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V5__appointment_history extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        boolean mysql = context.getConnection().getMetaData().getDatabaseProductName().equalsIgnoreCase("MySQL");
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("ALTER TABLE appointments DROP " + (mysql ? "CHECK" : "CONSTRAINT") + " ck_appointments_status");
            statement.execute("ALTER TABLE appointments ADD CONSTRAINT ck_appointments_status CHECK (status IN ('REQUESTED','APPROVED','REJECTED','CANCELLED','COMPLETED','NO_SHOW'))");
            statement.execute("ALTER TABLE appointments ADD COLUMN rejection_reason VARCHAR(1000) NULL");
            statement.execute("""
                    CREATE TABLE appointment_history (
                        id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        appointment_id BIGINT NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        actor_id BIGINT NULL,
                        source VARCHAR(20) NOT NULL,
                        changed_at TIMESTAMP NOT NULL,
                        reason VARCHAR(1000) NULL,
                        CONSTRAINT fk_appointment_history_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id),
                        CONSTRAINT fk_appointment_history_actor FOREIGN KEY (actor_id) REFERENCES users(id),
                        CONSTRAINT ck_appointment_history_source CHECK (source IN ('SYSTEM','USER','ADMIN'))
                    )
                    """);
            statement.execute("CREATE INDEX idx_appointment_history_appointment ON appointment_history(appointment_id, changed_at, id)");
            statement.execute("INSERT INTO appointment_history (appointment_id,status,actor_id,source,changed_at,reason) SELECT id,status,NULL,'SYSTEM',created_at,NULL FROM appointments");
        }
    }
}
