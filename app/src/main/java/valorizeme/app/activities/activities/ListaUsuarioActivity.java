package valorizeme.app.activities.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import valorizeme.app.R;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valorizeme.app.api.RestServiceGenerator;
import valorizeme.app.api.UsuarioService;
import valorizeme.app.entidades.Usuario;


public class ListaUsuarioActivity extends AppCompatActivity {

    private UsuarioService service = null;
    final private ListaUsuarioActivity mainActivity = this;
    private final Context context;

    public ListaUsuarioActivity() {
        context = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Lista de Usuarios");
        setContentView(R.layout.activity_list_usuario);
        service = RestServiceGenerator.createService(UsuarioService.class);
        buscaUsuarios();
        criaAcaoBotaoFlutuante();
        criaAcaoCliqueLongo();
    }

    private void criaAcaoCliqueLongo() {
        ListView listView = findViewById(R.id.listViewListaUsuarios);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ListaUsuarioActivity","Clicou em clique longo na posicao "+position);
                final Usuario objetoSelecionado = (Usuario) parent.getAdapter().getItem(position);
                Log.i("ListaUsuarioActivity", "Selecionou o Usuario "+objetoSelecionado.getCodigo());
                new AlertDialog.Builder(parent.getContext()).setTitle("Removendo Usuario")
                        .setMessage("Tem certeza que quer remover o Usuario "+objetoSelecionado.getCodigo()+"?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeUsuario(objetoSelecionado);
                            }
                        }).setNegativeButton("Não", null).show();
                return true;
            }
        });
    }

    private void removeUsuario(Usuario usuario) {
        Log.i("ListaCriptoActivity","Vai remover usuario "+usuario.getCodigo());
        Call<Boolean> call = this.service.excluiUsuario(usuario.getCodigo());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Log.i("ListaUsuarioActivity", "Removeu a Usuario " + usuario.getCodigo());
                    Toast.makeText(getApplicationContext(), "Removeu a Usuario " + usuario.getCodigo(), Toast.LENGTH_LONG).show();
                    onResume();
                } else {
                    Log.e("ListaUsuarioActivity", "Erro (" + response.code()+"): Verifique novamente os valores");
                    Toast.makeText(getApplicationContext(), "Erro (" + response.code()+"): Verifique novamente os valores", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("ListaUsuarioActivity", "Erro: " + t.getMessage());
            }
        });
    }

    private void criaAcaoBotaoFlutuante() {
        FloatingActionButton botaoNovo = findViewById(R.id.floatingActionButton);
        botaoNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity","Clicou no botão para adicionar Usuario");
                startActivity(new Intent(ListaUsuarioActivity.this,
                        FormularioUsuarioActivity.class));
            }
        });
    }

    public void buscaUsuarios(){
        UsuarioService service = RestServiceGenerator.createService(UsuarioService.class);
        Call<List<Usuario>> call = service.getUsuarios();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    Log.i("ListaUsuarioActivity", "Retornou " + response.body().size() + " Usuario!");
                    ListView listView = findViewById(R.id.listViewListaUsuarios);
                    listView.setAdapter(new ListaUsuarioAdapter(context,response.body()));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.i("ListaUsuarioActivity", "Selecionou o objeto de posicao "+position);
                            Usuario objetoSelecionado = (Usuario) parent.getAdapter().getItem(position);
                            Log.i("ListaUsuarioActivity", "Selecionou o Usuario "+objetoSelecionado.getCodigo());
                            Intent intent = new Intent(ListaUsuarioActivity.this, FormularioUsuarioActivity.class);
                            intent.putExtra("objeto", objetoSelecionado);
                            startActivity(intent);
                        }
                    });
                } else {
                    Log.e("UsuarioDAO", "" + response.message());
                    Toast.makeText(getApplicationContext(), "Erro: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Log.e("Error", "" + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        buscaUsuarios();
    }
}