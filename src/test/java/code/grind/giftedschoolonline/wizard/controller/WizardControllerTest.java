package code.grind.giftedschoolonline.wizard.controller;

import code.grind.giftedschoolonline.artifact.Artifact;
import code.grind.giftedschoolonline.system.StatusCode;
import code.grind.giftedschoolonline.wizard.Wizard;
import code.grind.giftedschoolonline.wizard.dto.WizardDto;
import code.grind.giftedschoolonline.wizard.service.WizardService;
import code.grind.giftedschoolonline.system.Exception.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)//Turns off spring security
public class WizardControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    WizardService wizardService;

    @Autowired
    ObjectMapper objectMapper;
    List<Wizard> wizardList;

    @Value("${api.endpoint.base-url}")
    String baseUrl;


    @BeforeEach
    void setUp() throws Exception {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");

        this.wizardList = new ArrayList<>();

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);
        w1.addArtifact(a3);
        this.wizardList.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a2);
        w2.addArtifact(a4);
        this.wizardList.add(w2);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");
        w3.addArtifact(a5);
        this.wizardList.add(w3);
    }

@Test
    void testFindAllWizardsSuccess() throws Exception {
        //Given
    given(this.wizardService.findAll()).willReturn(this.wizardList);

    // When and then
    this.mockMvc.perform(get(this.baseUrl+"/wizards").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find All Success"))
            .andExpect(jsonPath("$.data", Matchers.hasSize(this.wizardList.size())))
            .andExpect(jsonPath("$.data[0].id").value(1))
            .andExpect(jsonPath("$.data[0].name").value("Albus Dumbledore"))
            .andExpect(jsonPath("$.data[1].id").value(2))
            .andExpect(jsonPath("$.data[1].name").value("Harry Potter"));
}

@Test
    void TestFindWizardByIdSuccess() throws Exception {
        //Given
    given(this.wizardService
            .findById(1))
            .willReturn(this.wizardList.get(0));
    //When and Then
    this.mockMvc.perform(get(this.baseUrl+"/wizards/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find One Success"))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.name").value("Albus Dumbledore"));

}

@Test
    void TestFindWizardByIdNotFound () throws Exception{
    //Given
    given(this.wizardService
            .findById(5))
            .willThrow(new ObjectNotFoundException("wizard", 5));
    //When and then together
    this.mockMvc.perform(get(this.baseUrl+"/artifacts/5")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find artifact with id: 5😢"))
            .andExpect(jsonPath("$.data").isEmpty());
}
    @Test
    void TestAddWizardSuccess() throws Exception{
        WizardDto wizardDto = new WizardDto(null, "Hermione Granger", 0);

        String json = this.objectMapper.writeValueAsString(wizardDto);

        Wizard savedWizard = new Wizard();
        savedWizard.setId(4);
        savedWizard.setName("Hermione Granger");

        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        given(this.wizardService.save(Mockito.any(Wizard.class))).willReturn(savedWizard);

        // When and then
        this.mockMvc.perform(post(this.baseUrl+"/wizards").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Hermione Granger"));
    }

    @Test
    void TestUpdateSuccess() throws Exception{
        WizardDto wizardDto = new WizardDto(null, "Updated wizard name", 0);

        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(1);
        updatedWizard.setName("Updated wizard name");

        String json = this.objectMapper.writeValueAsString(updatedWizard);

        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        given(this.wizardService.update(eq(1), Mockito.any(Wizard.class))).willReturn(updatedWizard);

        // When and then
        this.mockMvc.perform(put(this.baseUrl+"/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Updated wizard name"));
    }



    @Test
    void testDeleteWizardSuccess() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        doNothing().when(this.wizardService).delete(3);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl+"/wizards/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWizardErrorWithNonExistentId() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        doThrow(new ObjectNotFoundException("wizard", 5)).when(this.wizardService).delete(5);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl+"/wizards/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id: 5😢"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactSuccess() throws Exception {
        // Given
        doNothing().when(this.wizardService).assignArtifact(2, "1250808601744904191");

        // When and then
        this.mockMvc.perform(put(this.baseUrl+"/wizards/2/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact Assignment Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("wizard", 5)).when(this.wizardService).assignArtifact(5, "1250808601744904191");

        // When and then
        this.mockMvc.perform(put(this.baseUrl+"/wizards/5/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id: 5😢"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("artifact", "1250808601744904199")).when(this.wizardService).assignArtifact(2, "1250808601744904199");

        // When and then
        this.mockMvc.perform(put(this.baseUrl+"/wizards/2/artifacts/1250808601744904199").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id: 1250808601744904199😢"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}

