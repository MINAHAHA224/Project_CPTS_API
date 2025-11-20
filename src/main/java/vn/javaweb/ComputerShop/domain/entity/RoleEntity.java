package vn.javaweb.ComputerShop.domain.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import vn.javaweb.ComputerShop.domain.entity.abstracts.BaseIdEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseIdEntity<Long>{

    private String name;
    private String description;

    // role - one => many - users . ctrl + k . press 's'
    @OneToMany(mappedBy = "role")
    private List<UserEntity> users;


}
