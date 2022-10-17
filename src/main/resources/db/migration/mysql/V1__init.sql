CREATE TABLE raw_data (
    raw_data_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    enterprise_id VARCHAR(255) NOT NULL,
    file_url VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    project_id BIGINT NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    data_type VARCHAR(255) NOT NULL
);

CREATE TABLE project (
    project_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_agreed BOOLEAN NOT NULL,
    enterprise_id VARCHAR(255) NOT NULL,
    zip_uuid VARCHAR(255) NOT NULL,
    zip_url VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    data_type VARCHAR(255) NOT NULL
);