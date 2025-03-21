package org.example.repository;

import org.example.entity.log.LogSendEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogSendEmailRepository extends JpaRepository<LogSendEmail,Long> {

}
