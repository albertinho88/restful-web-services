package ec.clicka.rest.webservices.restfulwebservices.versioning;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class PersonV1 {

    private String name;

    public PersonV1(String name) {
        super();
        this.name = name;
    }
}
