package com.purpleWeb.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.purpleWeb.model.Newsletter;

public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
    boolean existsByEmail(String email); // Optional: To check if the email is already subscribed
}

