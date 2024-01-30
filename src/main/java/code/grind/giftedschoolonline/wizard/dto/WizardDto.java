package code.grind.giftedschoolonline.wizard.dto;

import jakarta.validation.constraints.NotEmpty;

public record WizardDto(Integer id,
                        @NotEmpty(message = "name is required.")
                        String name,
                        Integer numberOfArtifacts) {

}
