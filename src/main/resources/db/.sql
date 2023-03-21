DROP TABLE IF EXISTS person, card, account, passport, card_transaction, account_transaction, person;
DROP TYPE IF EXISTS currency_type;

CREATE TABLE IF NOT EXISTS passport
(
    passport_id   BIGSERIAL PRIMARY KEY,
    dob           TIMESTAMP    NOT NULL,
    identity_code VARCHAR(14)  NOT NULL,
    firstname     VARCHAR(255) NOT NULL,
    surname       VARCHAR(255) NOT NULL,
    succession    VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS person
(
    person_id     BIGSERIAL PRIMARY KEY,
    email         VARCHAR(50) CHECK (person.email ~* '^\S+@\S+\.\S+$')
                              NOT NULL UNIQUE,
    password_hash VARCHAR(72) NOT NULL,
    passport_id   BIGINT REFERENCES passport (passport_id)
);

CREATE TABLE IF NOT EXISTS account
(
    account_id    BIGSERIAL PRIMARY KEY,
    money         DECIMAL                              NOT NULL,
    iban          VARCHAR(28)                          NOT NULL,
    num           VARCHAR(16)                          NOT NULL,
    currency_type VARCHAR(10)                          NOT NULL,
    person_id     BIGINT REFERENCES person (person_id) NOT NULL
);

CREATE TABLE IF NOT EXISTS card
(
    card_id         BIGSERIAL PRIMARY KEY,
    pin             VARCHAR(4)                             NOT NULL,
    num             VARCHAR(16)                            NOT NULL,
    cvv             VARCHAR(3)                             NOT NULL,
    expiration_time TIMESTAMP CHECK (EXTRACT(YEAR FROM expiration_time)::int >
                                     EXTRACT(YEAR FROM current_timestamp)::int),
    person_id       BIGINT REFERENCES person (person_id)   NOT NULL,
    account_id      BIGINT REFERENCES account (account_id) NOT NULL
);

CREATE TYPE currency_type AS ENUM (
    'EUR',
    'USD',
    'RUB',
    'BYN'
    );

CREATE TABLE IF NOT EXISTS account_transaction
(
    account_transaction_id BIGSERIAL PRIMARY KEY,
    from_account_id        BIGINT         NOT NULL,
    to_account_id          BIGINT         NOT NULL,
    amount                 NUMERIC(10, 3) NOT NULL,
    currency               VARCHAR(10)  NOT NULL,
    being_at               TIMESTAMP DEFAULT now(),
    FOREIGN KEY (from_account_id) REFERENCES account (account_id),
    FOREIGN KEY (to_account_id) REFERENCES account (account_id)
);

CREATE TABLE IF NOT EXISTS card_transaction
(
    card_transaction_id BIGSERIAL PRIMARY KEY,
    from_card_id        BIGINT         NOT NULL,
    to_card_id          BIGINT         NOT NULL,
    amount              NUMERIC(10, 3) NOT NULL,
    currency            VARCHAR(10)  NOT NULL,
    being_at            TIMESTAMP DEFAULT now(),
    FOREIGN KEY (from_card_id) REFERENCES card (card_id),
    FOREIGN KEY (to_card_id) REFERENCES card (card_id)
);

DROP FUNCTION IF EXISTS made_account_transaction(to_account_id bigint, from_account_id bigint,
                                                 amount numeric(10, 3), currencies varchar);

CREATE FUNCTION
    made_account_transaction(to_account_id bigint, from_account_id bigint, amount numeric(10, 3),
                             currencies varchar)
    RETURNS boolean AS
$$
DECLARE
    from_account_currency_type currency_type;
    to_account_currency_type   currency_type;
    from_account_currency      numeric(10, 3);
    to_account_currency        numeric(10, 3);
    money_var                  numeric(10, 3);
    temp_amount                numeric(10, 3);
    USD constant               text := 'USD';
BEGIN
    SELECT money INTO money_var FROM account WHERE account_id = from_account_id;

    IF money_var < amount OR amount < 0 THEN
        RAISE EXCEPTION 'Not enough money to make a transaction in card' USING ERRCODE = 'P0001';
    ELSEIF to_account_id = from_account_id THEN
        RAISE EXCEPTION 'Reflection transaction is not allowed' USING ERRCODE = 'P0001';
    END IF;

    SELECT currency_type
    INTO from_account_currency_type
    FROM account
    WHERE account_id = from_account_id;
    SELECT currency_type
    INTO to_account_currency_type
    FROM account
    WHERE account_id = to_account_id;

    IF from_account_currency_type = to_account_currency_type THEN
        UPDATE account SET money = @money + amount WHERE account_id = to_account_id;
    ELSE
        IF from_account_currency_type::text NOT LIKE USD THEN

            SELECT value
            INTO from_account_currency
            FROM (SELECT * FROM json_each(currencies::json)) AS "*2"
            WHERE key LIKE concat('%', from_account_currency_type::text);

            temp_amount := amount / from_account_currency;
        ELSE
            temp_amount := amount;
        END IF;
        IF to_account_currency_type::text != USD THEN
            SELECT value
            INTO to_account_currency
            FROM (SELECT * FROM json_each(currencies::json)) AS "*2"
            WHERE key LIKE concat('%', to_account_currency_type::text);
            raise notice '%', to_account_currency;

            temp_amount := temp_amount * to_account_currency;
            raise notice '%', temp_amount;
        END IF;
        UPDATE account SET money = @money + temp_amount WHERE account_id = to_account_id;
    END IF;

    UPDATE account SET money = @money - amount WHERE account_id = from_account_id;

    RETURN true;
END;
$$ LANGUAGE plpgsql
    SECURITY DEFINER;

DROP FUNCTION IF EXISTS log_card_transaction(to_card BIGINT, from_card BIGINT,
                                             money_amount NUMERIC(10, 3));

CREATE FUNCTION
    log_card_transaction(from_card BIGINT, to_card BIGINT, money_amount NUMERIC(10, 3))
    RETURNS boolean AS
$$
DECLARE
    curr_type currency_type;
BEGIN

    SELECT currency_type
    INTO curr_type
    FROM card
             JOIN account ca on ca.account_id = card.account_id
    WHERE card.card_id = from_card;

    INSERT INTO card_transaction(to_card_id, from_card_id, amount, currency)
    VALUES (to_card, from_card, money_amount, curr_type);

    RETURN true;
END;
$$ LANGUAGE plpgsql
    SECURITY DEFINER;

DROP FUNCTION IF EXISTS log_account_transaction(from_account BIGINT, to_account BIGINT,
                                                money_amount NUMERIC(10, 3));

CREATE FUNCTION
    log_account_transaction(from_account BIGINT, to_account BIGINT, money_amount NUMERIC(10, 3))
    RETURNS boolean AS
$$
DECLARE
    curr_type currency_type;
BEGIN

    SELECT currency_type
    INTO curr_type
    FROM account
    WHERE account.account_id = from_account;

    INSERT INTO account_transaction(to_account_id, from_account_id, amount, currency)
    VALUES (to_account, from_account, money_amount, curr_type);

    RETURN true;
END;
$$ LANGUAGE plpgsql
    SECURITY DEFINER;