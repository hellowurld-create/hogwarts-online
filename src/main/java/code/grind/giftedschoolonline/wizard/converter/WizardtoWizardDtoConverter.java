package code.grind.giftedschoolonline.wizard.converter;

import code.grind.giftedschoolonline.wizard.Wizard;
import code.grind.giftedschoolonline.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardtoWizardDtoConverter implements Converter<Wizard, WizardDto> {

    @Override
    public WizardDto convert(Wizard source) {
        WizardDto wizardDto = new WizardDto(source.getId(),
                source.getName(),
                source.getNumberOfArtifacts());
        return wizardDto;
    }
}
