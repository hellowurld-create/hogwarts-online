package code.grind.giftedschoolonline.artifact.service;

import code.grind.giftedschoolonline.artifact.Artifact;
import code.grind.giftedschoolonline.artifact.repository.ArtifactRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;


    public ArtifactService(ArtifactRepository artifactRepository) {
        this.artifactRepository = artifactRepository;
    }

    public Artifact findById(String artifactId){
        return null;
    }
}
