package code.grind.giftedschoolonline.artifact.repository;

import code.grind.giftedschoolonline.artifact.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, String> {

}
