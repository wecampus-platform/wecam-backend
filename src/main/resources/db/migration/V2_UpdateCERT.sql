ALTER TABLE affiliation_certification ADD COLUMN user_name VARCHAR(255);
ALTER TABLE user MODIFY expires_at DATETIME NULL;
ALTER TABLE user
    ADD COLUMN organization BIGINT,
    ADD CONSTRAINT fk_user_organizationschool_pk_id
        FOREIGN KEY (organization)
            REFERENCES organization(organization_id);
ALTER TABLE user
    ADD COLUMN school_id BIGINT,
    ADD CONSTRAINT fk_user_school
        FOREIGN KEY (school_id)
            REFERENCES university(school_id);