package code.grind.giftedschoolonline.wizard;

import code.grind.giftedschoolonline.artifact.Artifact;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wizard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE}, mappedBy = "owner")
    private List<Artifact> artifactList = new ArrayList<>();

    public Wizard() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artifact> getArtifactList() {
        return artifactList;
    }

    public void setArtifactList(List<Artifact> artifactList) {
        this.artifactList = artifactList;
    }

    public void addArtifact(Artifact artifact) {
        artifact.setOwner(this);
        this.artifactList.add(artifact);
    }

    public Integer getNumberOfArtifacts() {
        return this.artifactList.size();
    }


    public void removeAllArtifacts() {
        this.artifactList.stream().forEach(artifact -> artifact.setOwner(null));
        this.artifactList = new ArrayList<>();
    }

    public void removeArtifact(Artifact artifactToBeAssigned) {
        // Remove artifact owner.
        artifactToBeAssigned.setOwner(null);
        this.artifactList.remove(artifactToBeAssigned);
    }
}
