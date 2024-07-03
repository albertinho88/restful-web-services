package ec.clicka.rest.webservices.restfulwebservices.versioning;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class PersonV2 {

    private String firstName;
    private String secondName;

    public PersonV2(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }
}
