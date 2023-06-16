package valorizeme.app.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Usuario implements Serializable{
    private String codigo;
    private String nome;
    private String descricao;
    private Date dataCriacao;

    public Usuario() {
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataCriacao=" + dataCriacao +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario that = (Usuario) o;
        return Objects.equals(getCodigo(), that.getCodigo()) &&
                Objects.equals(getNome(), that.getNome()) &&
                Objects.equals(getDescricao(), that.getDescricao()) &&
                Objects.equals(getDataCriacao(), that.getDataCriacao());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodigo(), getNome(), getDescricao(), getDataCriacao());
    }

}
