DESCRIBE customers;
SELECT * FROM customers;

DESCRIBE accounts;
SELECT * FROM accounts;

DESCRIBE transactions;
SELECT * FROM transactions;

DESCRIBE otps;
SELECT* FROM otps;

DESCRIBE notifications;
SELECT * FROM notifications;

DESCRIBE vaults;
SELECT * FROM vaults;

DESCRIBE exchange_rates;
SELECT * FROM exchange_rates;
INSERT INTO exchange_rates VALUES(1,"NGN", 0.00074, "USD", sysdate());
INSERT INTO exchange_rates VALUES(2,"USD", 1343.84, "NGN", sysdate());

DESCRIBE external_banks;
SELECT * FROM external_banks;
INSERT INTO external_banks VALUES (1, "12345", "ABSA bank");
INSERT INTO external_banks VALUES (2, "67890", "Zenith Bank");

DESCRIBE external_bank_accounts;
SELECT * FROM external_bank_accounts;
INSERT INTO external_bank_accounts VALUES (1, "Chauke Vushaka",  "1234567890", 1100, "USD", 1);
INSERT INTO external_bank_accounts VALUES (2, "Emeka Nwokocha",  "0123456789", 8000, "NGN", 2);

DESCRIBE bank_earnings;
SELECT * FROM bank_earnings;

DESCRIBE admins;
SELECT * FROM admins;

DESCRIBE admin_audit_logs;
SELECT * FROM admin_audit_logs;
ALTER TABLE admin_audit_logs MODIFY COLUMN target_account_number VARCHAR(20);

