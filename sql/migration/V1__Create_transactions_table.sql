CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(50) PRIMARY KEY,
    status VARCHAR(20),
    payer_name VARCHAR(255),
    credit_card VARCHAR(16),
    expiration_date VARCHAR(5),
    cvv VARCHAR(3),
    amount DOUBLE PRECISION,
    created_at VARCHAR(20)
);
