CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS city (
    id                      UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at              TIMESTAMP DEFAULT now(),
    updated_at              TIMESTAMP,
    name                    VARCHAR NOT NULL,
    image_url               VARCHAR
);
