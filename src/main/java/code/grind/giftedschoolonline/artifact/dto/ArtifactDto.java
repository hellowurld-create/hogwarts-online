package code.grind.giftedschoolonline.artifact.dto;

import code.grind.giftedschoolonline.wizard.dto.WizardDto;
import jakarta.validation.constraints.NotEmpty;

public record ArtifactDto(String id,
                          @NotEmpty(message = "name is required.")
                          String name,
                          @NotEmpty(message = "description is required.")
                          String description,
                          @NotEmpty(message = "image is required.")
                          String imageUrl,
                          WizardDto owner) {
}
