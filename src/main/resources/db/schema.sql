UPDATE affiliation_certification
SET authentication_type = 'NEW_STUDENT'
WHERE authentication_type = '0';

ALTER TABLE affiliation_file
    MODIFY authentication_type ENUM('NEW_STUDENT', 'CURRENT_STUDENT') NOT NULL;


UPDATE affiliation_file
SET authentication_type = 'NEW_STUDENT'
WHERE authentication_type = '0';

UPDATE affiliation_file
SET authentication_type = 'TRANSFER'
WHERE authentication_type = '1';


SELECT * FROM affiliation_file
WHERE pk_upload_userid = 1 AND authentication_type = 'NEW_STUDENT';