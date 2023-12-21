package code.grind.giftedschoolonline.artifact.controller;

import code.grind.giftedschoolonline.artifact.service.ArtifactService;
import code.grind.giftedschoolonline.system.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class ArtifactController {

    private final ArtifactService artifactService;

    public ArtifactController(ArtifactService artifactService) {
        this.artifactService = artifactService;
    }

    @GetMapping("/api/v1/artifacts/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId){
            return null;
    }
}
