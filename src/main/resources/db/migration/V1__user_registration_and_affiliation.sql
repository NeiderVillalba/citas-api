CREATE TABLE roles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(30) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_roles_code UNIQUE (code)
);

INSERT INTO roles (code) VALUES ('USER'), ('PROFESSIONAL'), ('ADMIN');

CREATE TABLE eps (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uk_eps_name UNIQUE (name)
);

CREATE TABLE eps_plans (
    id BIGINT NOT NULL AUTO_INCREMENT,
    eps_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_eps_plans_eps FOREIGN KEY (eps_id) REFERENCES eps (id),
    CONSTRAINT uk_eps_plan_name UNIQUE (eps_id, name),
    INDEX idx_eps_plans_active (active, eps_id)
);

CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    document_type VARCHAR(30) NOT NULL,
    document_number VARCHAR(50) NOT NULL,
    email VARCHAR(254) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_document UNIQUE (document_number),
    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE TABLE user_roles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT uk_user_roles_user_role UNIQUE (user_id, role_id)
);

CREATE TABLE user_affiliations (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_affiliations_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_affiliations_plan FOREIGN KEY (plan_id) REFERENCES eps_plans (id),
    CONSTRAINT uk_user_affiliations_user UNIQUE (user_id),
    INDEX idx_user_affiliations_plan (plan_id)
);
