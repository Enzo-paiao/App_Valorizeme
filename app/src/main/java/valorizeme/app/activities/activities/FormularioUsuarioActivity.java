package valorizeme.app.activities.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valorizeme.app.R;
import valorizeme.app.api.RestServiceGenerator;
import valorizeme.app.api.UsuarioService;
import valorizeme.app.entidades.Usuario;

public class FormularioUsuarioActivity extends AppCompatActivity {

    private UsuarioService service = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_usuario);
        setTitle("Edição de Usuario");
        service = RestServiceGenerator.createService(UsuarioService.class);
        configuraBotaoSalvar();
        inicializaObjeto();
    }

    private void inicializaObjeto() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra("objeto") != null) {
            Usuario objeto = (Usuario) intent.getSerializableExtra("objeto");
            EditText codigo = findViewById(R.id.editTextCodigo);
            EditText nome = findViewById(R.id.editTextNome);
            EditText descricao = findViewById(R.id.editTextDescricao);
            codigo.setText(objeto.getCodigo());
            nome.setText(objeto.getNome());
            descricao.setText(objeto.getDescricao());
            codigo.setEnabled(false);
            Button botaoSalvar = findViewById(R.id.buttonSalvar);
            botaoSalvar.setText("Atualizar");
        }
    }

    private void configuraBotaoSalvar() {
        Button botaoSalvar = findViewById(R.id.buttonSalvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FormularioUsuario","Clicou em Salvar");
                Usuario usuario = recuperaInformacoesFormulario();
                Intent intent = getIntent();
                if (intent.getSerializableExtra("objeto") != null) {
                    Usuario objeto = (Usuario) intent.getSerializableExtra("objeto");
                    usuario.setCodigo(objeto.getCodigo());
                    usuario.setDataCriacao(objeto.getDataCriacao());
                    if (validaFormulario(usuario)) {
                        atualizaUsuario(usuario);
                    }
                } else {
                    usuario.setDataCriacao(new Date());
                    if (validaFormulario(usuario)) {
                        salvaUsuario(usuario);
                    }
                }
            }
        });
    }

    private boolean validaFormulario(Usuario usuario){
        boolean valido = true;
        EditText codigo = findViewById(R.id.editTextCodigo);
        EditText nome = findViewById(R.id.editTextNome);
        EditText descricao = findViewById(R.id.editTextDescricao);
        if (usuario.getCodigo() == null || usuario.getCodigo().trim().length() == 0){
            codigo.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            valido = false;
        } else {
            codigo.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP);
        }
        if (usuario.getNome() == null || usuario.getNome().trim().length() == 0){
            nome.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            valido = false;
        } else {
            nome.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP);
        }
        if (usuario.getDescricao() == null || usuario.getDescricao().trim().length() == 0){
            descricao.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            valido = false;
        } else {
            descricao.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP);
        }
        if (!valido){
            Log.e("FormularioUsuario", "Favor verificar os campos destacados");
            Toast.makeText(getApplicationContext(), "Favor verificar os campos destacados", Toast.LENGTH_LONG).show();
        }
        return valido;
    }

    private void salvaUsuario(Usuario usuario) {
        Call<Usuario> call = service.criaUsuario(usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Log.i("FormularioUsuario", "Salvou o Usuario "+ usuario.getCodigo());
                    Toast.makeText(getApplicationContext(), "Salvou o Usuario "+ usuario.getCodigo(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.e("FormularioUsuario", "Erro (" + response.code()+"): Verifique novamente os valores");
                    Toast.makeText(getApplicationContext(), "Erro (" + response.code()+"): Verifique novamente os valores", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("FormularioUsuario", "Erro: " + t.getMessage());
            }
        });
    }

    private void atualizaUsuario(Usuario usuario) {
        Log.i("FormularioUsuario","Vai atualizar Usuario "+usuario.getCodigo());
        Call<Usuario> call = service.atualizaUsuario(usuario.getCodigo(), usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Log.i("FormularioUsuario", "Atualizou o Usuario " + usuario.getCodigo());
                    Toast.makeText(getApplicationContext(), "Atualizou o Usuario " + usuario.getCodigo(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.e("FormularioUsuario", "Erro (" + response.code()+"): Verifique novamente os valores");
                    Toast.makeText(getApplicationContext(), "Erro (" + response.code()+"): Verifique novamente os valores", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("FormularioUsuario", "Erro: " + t.getMessage());
            }
        });
    }

    @NotNull
    private Usuario recuperaInformacoesFormulario() {
        EditText codigo = findViewById(R.id.editTextCodigo);
        EditText nome = findViewById(R.id.editTextNome);
        EditText descricao = findViewById(R.id.editTextDescricao);
        Usuario usuario = new Usuario();
        usuario.setCodigo(codigo.getText().toString());
        usuario.setNome(nome.getText().toString());
        usuario.setDescricao(descricao.getText().toString());
        usuario.setDataCriacao(new Date());
        return usuario;
    }

}