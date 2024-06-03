import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * NOTE: Profe, se que esto no se dio en el teorico pero quise
 * simplificar los test usando test parametrizados.
 */
public class CodificadorMensajeTest {

    public static Stream<Arguments> argumentProviderGenerarCodigoEncripcion() {
        return Stream.of(
                Arguments.of(new int[] {4, 2, 0}, "hola", "hola, como, estas?"),
                Arguments.of(new int[] {8, 5, 7}, "Viterbo~", "lista del super?")
        );
    }

    @ParameterizedTest
    @MethodSource("argumentProviderGenerarCodigoEncripcion")
    public void generarCodigoEncripcion_ConClaveyMensajeValido_OK(int[] codigoEncripcionValido, String lineaUno, String lineaDos) {
        Mensaje mensajeValido = new Mensaje();
        mensajeValido.agregarLinea(lineaUno);
        mensajeValido.agregarLinea(lineaDos);

        CodificadorMensajes encoder = new CodificadorMensajes(mensajeValido);


        Assertions.assertDoesNotThrow(
                encoder::codificarMensaje,
                "se espera que no lanze una excepcion durante el test"
        );

        Assertions.assertArrayEquals(
                encoder.obtenerCodigoEncripcion(),
                codigoEncripcionValido,
                "se espera que ambas claves sean iguales"
        );
    }


    @Test
    public void codificarYDecodificarMensaje_ConClaveValidaYMensajeValido_OK() {
        int[] claveValida = new int[] {8, 5, 7};

        Mensaje mensajeValido = new Mensaje();
        mensajeValido.agregarLinea("Viterbo~");
        mensajeValido.agregarLinea("lista del super");

        CodificadorMensajes encode = new CodificadorMensajes(mensajeValido);
        encode.codificarMensaje();

        Mensaje mensajeCodificadoValido = encode.obtenerMensajeCodificado();
        int[] claveValidaEncriptada = encode.obtenerCodigoEncripcion();

        Assertions.assertArrayEquals(claveValida, claveValidaEncriptada, "se esperaba que ambas claves fueran iguales");

        DecodificadorMensajes decode = new DecodificadorMensajes(mensajeCodificadoValido, claveValida);

        decode.decodificarMensaje();
        Mensaje mensajeDecodificado = decode.obtenerMensajeDecodificado();

        Assertions.assertTrue(mensajeDecodificado.equals(mensajeValido), "se esperaba que ambos fuesen iguales");
    }
}
