package code.grind.giftedschoolonline.artifact.service;

import code.grind.giftedschoolonline.artifact.Artifact;

public class ArtifactNotFoundException extends RuntimeException {
    public ArtifactNotFoundException(String id){
        super("Could not find artifact with id: " + id + "😢");
    }
}
