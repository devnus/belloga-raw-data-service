CREATE TABLE raw_data (
    raw_data_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    enterprise_id VARCHAR(255) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_agreed BOOLEAN NOT NULL,
    data_type VARCHAR(255) NOT NULL
);
