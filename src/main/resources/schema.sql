CREATE TABLE ACCOUNT
(
    id                 IDENTITY      NOT NULL AUTO_INCREMENT,
    account_balancepln DECIMAL   NOT NULL,
    account_balanceusd DECIMAL,
    name               VARCHAR2 NOT NULL,
    surname            VARCHAR2 NOT NULL,
    PRIMARY KEY (id)
);