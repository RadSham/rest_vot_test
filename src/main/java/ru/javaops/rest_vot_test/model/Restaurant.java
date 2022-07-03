package ru.javaops.rest_vot_test.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.rest_vot_test.util.validation.NoHtml;
import ru.javaops.rest_vot_test.util.validation.PhoneNumber;


import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @Column(name = "phone", unique = true, nullable = false)
    @PhoneNumber
    @NoHtml
    protected String phone;

    public Restaurant(Integer id, String name, String phone) {
        super(id, name);
        this.phone = phone;
    }
}
