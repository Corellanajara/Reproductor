package pro.mike.com.reproductor;

/**
 * Created by GERALVARADO on 03/09/2017.
 */

public class Song {
    int idSong;
    int idPhoto;
    String nombreCancion;
    public Song(String nombreCancion,int idSong, int idPhoto ){
        this.nombreCancion = nombreCancion;
        this.idSong = idSong;
        this.idPhoto = idPhoto;
    }

    public int getIdSong() {
        return idSong;
    }

    public void setIdSong(int idSong) {
        this.idSong = idSong;
    }

    public int getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(int idPhoto) {
        this.idPhoto = idPhoto;
    }

    public String getNombreCancion() {
        return nombreCancion;
    }

    public void setNombreCancion(String nombreCancion) {
        this.nombreCancion = nombreCancion;
    }

    @Override
    public String toString() {
        return nombreCancion+"" ;
    }
}
