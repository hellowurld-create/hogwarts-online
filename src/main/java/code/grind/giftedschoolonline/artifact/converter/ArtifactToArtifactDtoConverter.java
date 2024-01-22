package code.grind.giftedschoolonline.artifact.converter;

import code.grind.giftedschoolonline.artifact.Artifact;
import code.grind.giftedschoolonline.artifact.dto.ArtifactDto;
import code.grind.giftedschoolonline.wizard.converter.WizardtoWizardDtoConverter;
import code.grind.giftedschoolonline.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {

    private final WizardtoWizardDtoConverter wizardtoWizardDtoConverter;

    public ArtifactToArtifactDtoConverter(WizardtoWizardDtoConverter wizardtoWizardDtoConverter) {
        this.wizardtoWizardDtoConverter = wizardtoWizardDtoConverter;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        ArtifactDto artifactDto = new ArtifactDto(source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImageUrl(),
                source.getOwner() != null ?
                        this.wizardtoWizardDtoConverter.convert(source.getOwner())
                        : null);
        return artifactDto;
    }
}
