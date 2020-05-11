package com.mattlalonde.website.admin.tags.domain;

import com.mattlalonde.website.admin.tags.domain.commands.CreateTagCommand;
import com.mattlalonde.website.admin.tags.domain.commands.UpdateTagCommand;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "tags")
public class Tag implements Serializable {

    @Id
    private UUID id;

    @NotEmpty(message = "Tags must have a name")
    private String name;

    private String description;

    public static Tag create(CreateTagCommand command) {
        Assert.notNull(command.getId(), "Tag id must be provided");
        Assert.hasText(command.getName(), "Tags must have a name");

        Tag result = new Tag();
        result.setId(command.getId());
        result.setName(command.getName());
        result.setDescription(command.getDescription());

        return result;
    }

    public void apply(UpdateTagCommand command) {
        Assert.isTrue(getId() == command.getId(), "Error updating article, id's must match.");
        Assert.notNull(command.getId(), "Tag id must be provided");
        Assert.hasText(command.getName(), "Tags must have a name");

        setName(command.getName());
        setDescription(command.getDescription());
    }
}
