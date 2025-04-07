package PibuStory.mail;

import PibuStory.mail.Email;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface EmailRepository extends MongoRepository<Email, String> {
}