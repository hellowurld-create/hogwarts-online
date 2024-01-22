package code.grind.giftedschoolonline.wizard.service;

import code.grind.giftedschoolonline.artifact.Artifact;
import code.grind.giftedschoolonline.artifact.repository.ArtifactRepository;
import code.grind.giftedschoolonline.wizard.Wizard;
import code.grind.giftedschoolonline.wizard.repository.WizardRepository;
import code.grind.giftedschoolonline.system.Exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@Transactional
public class WizardService {
    public final WizardRepository wizardRepository;

    private final ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    public List<Wizard> findAll(){
        return this.wizardRepository.findAll();
    }
    public Wizard findById(Integer wizardId){
        return this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
    }

    public Wizard save(Wizard newWizard) {
        return this.wizardRepository.save(newWizard);
    }
    public Wizard update(Integer wizardId, Wizard update) {
        return this.wizardRepository.findById(wizardId)
                .map(oldWizard -> {
                    oldWizard.setName(update.getName());
                    return this.wizardRepository.save(oldWizard);
                })
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
    }

    public void delete(Integer wizardId) {
        Wizard wizardToBeDeleted = this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

        // Before deletion, we will unassign this wizard's owned artifacts.
        wizardToBeDeleted.removeAllArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }

    public void assignArtifact(Integer wizardId, String artifactId){
        //Find the artifact by id in db.
       Artifact artifactToBeAssigned = this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));

        //Find the wizard by id from db.
       Wizard wizard = this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
        //Assign Artifact
        //See if the artifact is already own by a wizard.
        if(artifactToBeAssigned.getOwner() != null){
            artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
        }
        wizard.addArtifact(artifactToBeAssigned);
    }


}
