CREATE TABLE IF NOT EXISTS employee
(
    id BIGSERIAL PRIMARY KEY,
    dob date NOT NULL,
    name character varying(50) NOT NULL,
    CONSTRAINT unqemployeename UNIQUE (name)
);