package code.grind.giftedschoolonline.wizard.service;

import code.grind.giftedschoolonline.artifact.Artifact;
import code.grind.giftedschoolonline.artifact.repository.ArtifactRepository;
import code.grind.giftedschoolonline.wizard.Wizard;
import code.grind.giftedschoolonline.wizard.repository.WizardRepository;
import code.grind.giftedschoolonline.system.Exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WizardServiceTest {
    @Mock
    WizardRepository wizardRepository;


    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizardList;

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        this.wizardList = new ArrayList<>();
        this.wizardList.add(w1);
        this.wizardList.add(w2);
        this.wizardList.add(w3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardRepository.
        given(this.wizardRepository.findAll()).willReturn(this.wizardList);

        // When. Act on the target behavior. Act steps should cover the method to be tested.
        List<Wizard> actualWizards = this.wizardService.findAll();

        // Then. Assert expected outcomes.
        assertThat(actualWizards.size()).isEqualTo(this.wizardList.size());

        // Verify wizardRepository.findAll() is called exactly once.
        verify(this.wizardRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess(){
        //Given
    Wizard wiz = new Wizard();
    wiz.setId(1);
    wiz.setName("Albus Dumbledore");

    given(wizardRepository.findById(1)).willReturn(Optional.of(wiz));//Defines the behaviour of mock object
        //When
        Wizard returnedWizard = wizardService.findById(1);

        //Then
        assertThat(returnedWizard.getId()).isEqualTo(wiz.getId());
        assertThat(returnedWizard.getName()).isEqualTo(wiz.getName());
        verify(wizardRepository, times(1)).findById(1);

    }

    @Test
    void testFindbyIdNotFound(){
        //Given
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()->{
            Wizard returnedArtifact = this.wizardService.findById(1);
        });
        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with id: 1😢");
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void TestSaveSuccess(){
        //Given
        Wizard newWizard = new Wizard();
        newWizard.setName("Hermione Granger");

        given(wizardRepository.save(newWizard)).willReturn(newWizard);

        //When
        Wizard savedWizard = this.wizardService.save(newWizard);

        //Then
        assertThat(savedWizard.getName()).isEqualTo(newWizard.getName());
        verify(wizardRepository,times(1)).save(newWizard);

    }

    @Test
    void TestUpdateSuccess(){
        //given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(4);
        oldWizard.setName("Neville Longbottom");

        Wizard update = new Wizard();
        update.setId(4);
        update.setName("Neville ShortBottom");


        given(wizardRepository.findById(4)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(oldWizard)).willReturn(oldWizard);

        //When
        Wizard updatedWizard = this.wizardService.update(4, update);

        //Then
        assertThat(updatedWizard.getId()).isEqualTo(update.getId());
        verify(wizardRepository,times(1)).findById(4);
        verify(wizardRepository,times(1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        Wizard update = new Wizard();
        update.setName("Albus Dumbledore - update");

        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.update(1, update);
        });

        // Then
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        doNothing().when(this.wizardRepository).deleteById(1);

        // When
        this.wizardService.delete(1);

        // Then
        verify(this.wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.delete(1);
        });

        // Then
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testAssignArtifactSuccess() {
        // Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.of(w3));

        // When
        this.wizardService.assignArtifact(3, "1250808601744904192");

        // Then
        assertThat(a.getOwner().getId()).isEqualTo(3);
        assertThat(w3.getArtifactList()).contains(a);
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() {
        // Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a);

        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(3, "1250808601744904192");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with id: 3😢");
        assertThat(a.getOwner().getId()).isEqualTo(2);
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() {
        // Given
        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(3, "1250808601744904192");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with id: 1250808601744904192😢");
    }

}
