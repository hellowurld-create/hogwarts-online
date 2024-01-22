package code.grind.giftedschoolonline.artifact.service;

import code.grind.giftedschoolonline.artifact.Artifact;
import code.grind.giftedschoolonline.artifact.repository.ArtifactRepository;
import code.grind.giftedschoolonline.utils.IdWorker;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;
    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
    }

    public Artifact findById(String artifactId){
        return this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
    }

    public List<Artifact> findAll(){
        return this.artifactRepository.findAll();
    }

    public  Artifact save(Artifact newArtifact){
        newArtifact.setId(String.valueOf(idWorker.nextId()));
        return this.artifactRepository.save(newArtifact);
    }

    public Artifact update(String artifactId, Artifact update){
      return this.artifactRepository.findById(artifactId)
               .map(oldArtifact ->{
                   oldArtifact.setName(update.getName());
                   oldArtifact.setDescription(update.getDescription());
                   oldArtifact.setImageUrl(update.getImageUrl());
                   return this.artifactRepository.save(oldArtifact);
               })
        .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
    }

    public void delete(String artifactId){
        this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
        this.artifactRepository.deleteById(artifactId);
    }
}
