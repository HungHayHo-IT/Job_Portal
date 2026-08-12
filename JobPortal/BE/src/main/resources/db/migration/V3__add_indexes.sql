CREATE INDEX idx_jobs_status_posted_date
    ON jobs (status, posted_date);

CREATE INDEX idx_jobs_category
    ON jobs (category);

CREATE INDEX idx_jobs_location
    ON jobs (location);

CREATE INDEX idx_job_applications_status_applied_at
    ON job_applications (status, applied_at);

CREATE INDEX idx_contacts_status_created_at
    ON contacts (status, created_at);