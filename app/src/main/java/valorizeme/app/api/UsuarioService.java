package valorizeme.app.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import valorizeme.app.entidades.Usuario;


public interface UsuarioService {
    @Headers({
            "Accept: application/json",
            "User-Agent: AppValme"
    })
    @GET("usuarios")
    Call<List<Usuario>> getUsuarios();

    @GET("usuarios/{id}")
    Call<Usuario> getUsuario(@Path("id") String codigo);

    @POST("usuarios")
    Call<Usuario> criaUsuario(@Body Usuario usuario);

    @PUT("usuarios/{id}")
    Call<Usuario> atualizaUsuario(@Path("id") String codigo, @Body Usuario usuario);

    @DELETE("usuarios/{id}")
    Call<Boolean> excluiUsuario(@Path("id") String codigo);
}
