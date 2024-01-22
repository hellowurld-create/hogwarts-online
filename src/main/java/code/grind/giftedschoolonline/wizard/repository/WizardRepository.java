package code.grind.giftedschoolonline.wizard.repository;

import code.grind.giftedschoolonline.wizard.Wizard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WizardRepository extends JpaRepository<Wizard, Integer> {
}
