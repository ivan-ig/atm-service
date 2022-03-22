DROP TABLE IF EXISTS Account;
DROP TABLE IF EXISTS Client;

CREATE TABLE CLIENT (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 0) NOT NULL PRIMARY KEY,
    FIRST_NAME VARCHAR NOT NULL,
    PATRONYMIC VARCHAR NOT NULL,
    LAST_NAME VARCHAR NOT NULL
);

CREATE TABLE ACCOUNT (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 0) NOT NULL PRIMARY KEY,
    OWNER_ID BIGINT NOT NULL,
    ACCOUNT_NUMBER VARCHAR NOT NULL UNIQUE,
    CARD_NUMBER BIGINT CHECK (CARD_NUMBER  > 0),
    PIN_CODE INT,
    AMOUNT NUMERIC(20, 2) NOT NULL DEFAULT 0.00,
    CURRENCY VARCHAR(3) NOT NULL,
    FOREIGN KEY (OWNER_ID ) REFERENCES CLIENT(ID)
);

INSERT INTO CLIENT (FIRST_NAME, PATRONYMIC, LAST_NAME) values ('ANDREY', 'IVANOVICH', 'VASILIEV');
INSERT INTO ACCOUNT (OWNER_ID, ACCOUNT_NUMBER, CARD_NUMBER, PIN_CODE, AMOUNT, CURRENCY) VALUES
    (0, '77777777777777711111', 1616161616161111, 1234, '0.00', 'RUB'),
    (0, '77777777777777722222', 1616161616161111, 1234, '0.00', 'USD'),
    (0, '77777777777777755555', null, null, '0.00', 'USD'),
    (0, '77777777777777733333', 1616161616161111, 1234, '0.00', 'EUR');

INSERT INTO CLIENT (FIRST_NAME, PATRONYMIC, LAST_NAME) values ('ANDREY', 'SERGEEVICH', 'VASILIEV');
INSERT INTO ACCOUNT (OWNER_ID, ACCOUNT_NUMBER, CARD_NUMBER, PIN_CODE, AMOUNT, CURRENCY) VALUES
    (1, '77777777777777766666', 1616161616167777, 1234, '0.00', 'RUB'),
    (1, '77777777777777788888', 1616161616168888, 1234, '0.00', 'EUR');

INSERT INTO CLIENT (FIRST_NAME, PATRONYMIC, LAST_NAME) values ('AFANASII', 'AFANASIEVICH', 'FET');
INSERT INTO ACCOUNT (OWNER_ID, ACCOUNT_NUMBER, CARD_NUMBER, PIN_CODE, AMOUNT, CURRENCY) VALUES
    (2, '88888888888888811111', 1616161616162222, 1234, '0.00', 'RUB');
