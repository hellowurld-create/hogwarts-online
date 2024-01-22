package code.grind.giftedschoolonline.wizard.controller;


import code.grind.giftedschoolonline.system.Result;
import code.grind.giftedschoolonline.system.StatusCode;
import code.grind.giftedschoolonline.wizard.Wizard;
import code.grind.giftedschoolonline.wizard.converter.WizardDtoToWizardConverter;
import code.grind.giftedschoolonline.wizard.converter.WizardtoWizardDtoConverter;
import code.grind.giftedschoolonline.wizard.dto.WizardDto;
import code.grind.giftedschoolonline.wizard.service.WizardService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {
private final WizardService wizardService;

private final WizardtoWizardDtoConverter wizardtoWizardDtoConverter;

private final WizardDtoToWizardConverter wizardDtoToWizardConverter;

    public WizardController(WizardService wizardService, WizardtoWizardDtoConverter wizardtoWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizardtoWizardDtoConverter = wizardtoWizardDtoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId){
        Wizard foundWizard = this.wizardService.findById(wizardId);
        WizardDto wizardDto = this.wizardtoWizardDtoConverter.convert(foundWizard);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardDto);
    }

    @GetMapping()
    public Result findAllWizards(){
        List<Wizard> foundWizards = this.wizardService.findAll();
        // Convert foundWizards to a list of WizardDtos.
        List<WizardDto> wizardDtos = foundWizards.stream()
                .map(this.wizardtoWizardDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", wizardDtos);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto) {
        Wizard newWizard = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard savedWizard = this.wizardService.save(newWizard);
        WizardDto savedWizardDto = this.wizardtoWizardDtoConverter.convert(savedWizard);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedWizardDto);
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Integer wizardId, @Valid @RequestBody WizardDto wizardDto) {
        Wizard update = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard updatedWizard = this.wizardService.update(wizardId, update);
        WizardDto updatedWizardDto = this.wizardtoWizardDtoConverter.convert(updatedWizard);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedWizardDto);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId) {
        this.wizardService.delete(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact(@PathVariable Integer wizardId, @PathVariable String artifactId) {
        this.wizardService.assignArtifact(wizardId, artifactId);
        return new Result(true, StatusCode.SUCCESS, "Artifact Assignment Success");
    }

}
