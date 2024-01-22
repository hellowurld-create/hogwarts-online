package code.grind.giftedschoolonline.artifact.service;

import code.grind.giftedschoolonline.artifact.Artifact;
import code.grind.giftedschoolonline.artifact.repository.ArtifactRepository;
import code.grind.giftedschoolonline.system.Exception.ObjectNotFoundException;
import code.grind.giftedschoolonline.utils.IdWorker;
import code.grind.giftedschoolonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifactList;


    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
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

        this.artifactList = new ArrayList<>();
        this.artifactList.add(a1);
        this.artifactList.add(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //Given. Arrange Inputs and targets. Define the behaviour of the Mock object artifact repository.
        /*
         "id": "1250808601744904191",
         "name": "Deluminator",
         "description": "A Deliminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.",
         "imageUrl": "ImageUrl",
        */
        Artifact a = new Artifact();
        a.setId("1250808601744904191");
        a.setName("Deluminator");
        a.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Albus Dumbledore");

        a.setOwner(w);

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(a));//Defines the behaviour of mock object

        //When. Act on the target behaviour(artifact service). When steps cover the method to be tested

        Artifact returnedArtifact = artifactService.findById("1250808601744904191");

        //Then. Assert expected outcomes.
        assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
        verify(artifactRepository, times(1)).findById("1250808601744904191");
    }

    @Test
    void testFindbyIdNotFound(){
        //Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()->{
            Artifact returnedArtifact = artifactService.findById("1250808601744904191");
        });
        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with id: 1250808601744904191😢");
        verify(artifactRepository, times(1)).findById("1250808601744904191");
    }

    @Test
    void testFindAllSuccess(){
        //Given
        given(artifactRepository.findAll()).willReturn(this.artifactList);
        //When
        List<Artifact> actualArtifacts = artifactService.findAll();
        //Then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifactList.size());
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        //Given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription("Description...");
        newArtifact.setImageUrl("imageUrl...");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);

        //When
        Artifact savedArtifact = artifactService.save(newArtifact);

        //Then
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository,times(1)).save(newArtifact);
    }

    @Test
    void  testUpdateSuccess(){
        //given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904191");
        oldArtifact.setName("Deluminator");
        oldArtifact.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact update = new Artifact();
        update.setId("1250808601744904191");
        update.setName("Deluminator");
        update.setDescription("A Deluminator is a machine invented by Junior Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        //When
      Artifact updatedArtifact = artifactService.update("1250808601744904191",update);

      //Then
        assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
        verify(artifactRepository,times(1)).findById("1250808601744904191");
        verify(artifactRepository,times(1)).save(oldArtifact);

    }

    @Test
    void testUpdateNotFound(){
        //Given
        Artifact update = new Artifact();
        update.setName("Deluminator");
        update.setDescription("A Deluminator is a machine invented by Junior Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class, ()->{
            artifactService.update("1250808601744904191", update);
        });

        //Then
        verify(artifactRepository, times(1)).findById("1250808601744904191");
    }

    @Test
    void testDeleteSuccess(){
        //Given
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904191");
        artifact.setName("Deluminator");
        artifact.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        artifact.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById("1250808601744904191");


        //When
        artifactService.delete("1250808601744904191");

        //Then
        verify(artifactRepository, times(1)).deleteById("1250808601744904191");
    }

    void testDeleteNotFound(){
        //Given
        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class, () ->{
            artifactService.delete("1250808601744904191");
        });

        //Then
        verify(artifactRepository, times(1)).findById("1250808601744904191");
    }
}