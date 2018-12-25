import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class SmsEntity {

    private String texto;
    private String fecha;
    private List<String> telefonos;
    private String remitente;

    @Override
    public String toString() {
        return "{" +
                "texto='" + texto + '\'' +
                ", fecha='" + fecha + '\'' +
                ", telefonos=" + telefonos +
                ", remitente='" + remitente + '\'' +
                '}';
    }


}
