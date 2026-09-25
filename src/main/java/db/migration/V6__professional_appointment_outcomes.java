package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V6__professional_appointment_outcomes extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        boolean mysql = context.getConnection().getMetaData().getDatabaseProductName().equalsIgnoreCase("MySQL");
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("ALTER TABLE appointment_history DROP "
                    + (mysql ? "CHECK" : "CONSTRAINT") + " ck_appointment_history_source");
            statement.execute("ALTER TABLE appointment_history ADD CONSTRAINT ck_appointment_history_source "
                    + "CHECK (source IN ('SYSTEM','USER','ADMIN','PROFESSIONAL'))");
        }
    }
}
