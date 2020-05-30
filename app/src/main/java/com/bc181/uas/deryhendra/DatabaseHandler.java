package com.bc181.uas.deryhendra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bc181.deryhendra.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "db_film";
    private final static String TABLE_FILM = "t_film";
    private final static String KEY_ID_FILM = "ID_film";
    private final static String KEY_JUDUl = "Judul";
    private final static String KEY_TAHUN = "Tahun";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_GENRE = "Genre";
    private final static String KEY_PEMAIN = "Pemain";
    private final static String KEY_SIPNOSIS= "SIPNOSIS";
    private SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
    private Context context;


    public DatabaseHandler(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_FILM = "CREATE TABLE " +  TABLE_FILM
                + "(" + KEY_ID_FILM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUl + " TEXT, " + KEY_TAHUN + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_GENRE + " TEXT, "
                + KEY_PEMAIN + " TEXT, " + KEY_SIPNOSIS + " TEXT);";
        db.execSQL(CREATE_TABLE_FILM);
        inisialisasiFilmAwal(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_FILM;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }

    public void tambahFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUl, dataFilm.getJudul());
        cv.put(KEY_TAHUN, sdFormat.format(dataFilm.getTahun()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_GENRE, dataFilm.getGenre());
        cv.put(KEY_PEMAIN, dataFilm.getPemain());
        cv.put(KEY_SIPNOSIS, dataFilm.getSipnosis());


        db.insert(TABLE_FILM, null, cv);
        db.close();
    }

    public void tambahFilm(Film dataFilm, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUl, dataFilm.getJudul());
        cv.put(KEY_TAHUN, sdFormat.format(dataFilm.getTahun()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_GENRE, dataFilm.getGenre());
        cv.put(KEY_PEMAIN, dataFilm.getPemain());
        cv.put(KEY_SIPNOSIS, dataFilm.getSipnosis());

        db.insert(TABLE_FILM, null, cv);

    }

    public void editFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUl, dataFilm.getJudul());
        cv.put(KEY_TAHUN, sdFormat.format(dataFilm.getTahun()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_GENRE, dataFilm.getGenre());
        cv.put(KEY_PEMAIN, dataFilm.getPemain());
        cv.put(KEY_SIPNOSIS, dataFilm.getSipnosis());


        db.update(TABLE_FILM, cv, KEY_ID_FILM + "=?", new String[]{String.valueOf(dataFilm.getIdFilm())});
        db.close();
    }

    public void hapusFilm(int idFilm) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FILM, KEY_ID_FILM + "=?", new String[]{String.valueOf(idFilm)});
        db.close();
    }

    public ArrayList<Film> getAllFilm() {
        ArrayList<Film> dataFilm = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if(csr.moveToFirst()){
            do {
                Date tempDate = new Date();
                try {
                    tempDate = sdFormat.parse(csr.getString(2));
                }
                catch (ParseException er){
                    er.printStackTrace();
                }

                Film tempFilm = new Film(
                        csr.getInt(0),
                        csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6)
                );

                dataFilm.add(tempFilm);
            } while (csr.moveToNext());
        }
        return dataFilm;
    }

    private String storeImageFile(int id) {
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = InputActivity.saveImageToInternalStorage(image, context);
        return location;
    }

    private void inisialisasiFilmAwal(SQLiteDatabase db) {
        int idFilm = 0;
        Date tempDate = new Date();

        //menambah data Film 1
        try{
            tempDate = sdFormat.parse("2020");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film1 = new Film(
                idFilm,
                "Milea Suara Dari Dilan",
                tempDate,
                storeImageFile(R.drawable.film1),
                "Romantice",
                "Iqbaal Ramadhan sebagai Dilan,Vanesha Prescilla sebagai Milea,Ira Wibowo sebagai ibu Dilan,Bucek Depp sebagai ayah Dilan,Happy Salma sebagai ibu Milea,Farhan sebagai ayah Milea,Adhisty Zara sebagai Disa,Yoriko Angeline sebagai Wati,Debo Andryos sebagai Nandan,Zulfa Maharani sebagai Rani,Gusti Rayhan sebagai Akew,Omara Esteghlal sebagai Piyan,Giulio Parengkuan sebagai Anhar,Andovi da Lopez sebagai Verdi,Jerome Kurnia sebagai Yugo,Tike Priyatna sebagai Eem,Bima Azriel sebagai Dilan kecil",
                "Dilan (Iqbaal Ramadhan), panglima tempur sebuah geng motor di Bandung awal 90-an,\n" +
                        "menjalin hubungan dengan seorang siswi baru dari Jakarta bernama Milea (Vanesha Prescilla). Dilan selalu bahagia saat bersama Milea, namun teman-teman geng motor merasa Dilan makin menjauh dari kelompoknya karena Milea. \n" +
                        "Terjadi peristiwa yang mengerikan, salah satu anggota mereka, Akew (Gusti Rayhan), meninggal akibat dikeroyok oleh sekelompok orang. Peristiwa itu membuat Milea khawatir akan keselamatan Dilan.\n" +
                        "Milea membuat keputusan untuk berpisah dengan Dilan sebagai peringatan agar Dilan menjauh dari geng motor. Peristiwa Akew menyeret Dilan ke pihak berwajib bersama teman-temannya..\n" +
                        "Perpisahan yang tadinya hanya gertakan Milea menjadi perpisahan yang berlangsung lama sampai mereka lulus kuliah dan dewasa.\n" +
                        "Mereka berdua masih membawa perasaan yang sama saat mereka kembali bertemu di reuni,\n" +
                        "namun masing-masing saat itu sudah memiliki pasangan.\n"
        );

        tambahFilm(film1, db);
        idFilm++;

        // Data Film ke 2
        try{
            tempDate = sdFormat.parse("2020");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film2 = new Film(
                idFilm,
                "Kajeng Kliwon",
                tempDate,
                storeImageFile(R.drawable.film2),
                "Horor",
                "Amanda Manoppo sebagai Agni,Christ Laurent sebaga Nicho,Egy Fedly,Indah Kalalo,\n" +
                        "Atikah Suhaime,Cathrine Wilson",
                "Agni (Amanda Manoppo) merupakan dokter muda asli Bali. Dia akan menikahi kekasihnya, Nicho (Chris Laurent), yang merupakan fotografer dari Jakarta. Walaupun sering terjadi konflik dalam mempersiapkan pernikahan, mereka tetap merasa bahagia. Tibalah malam Kajeng Kliwon, malam makhluk mistik masuk ke dunia manusia. Kedatangan mereka ternyata turut mengusik kehidupan Agni dan Nicho. Salah satu makhluk mistik yang mengganggu mereka yaitu Rangda. Gangguan berawal dari sebuah kejadian pembunuhan. Rangda kemudian mengancam jiwa Agni dan Nicho. Belum usai masalah gangguan mistik, hubungan mereka mendapat tantangan pada kehadiran Wijaya (Vincent Andrianto). Pemuda asli Bali itu berusaha mempengaruhi Agni untuk menikahi pemuda Bali asli. Kajeng Kliwon berada dalam arahan sutradara Bambang Drias dan penulis naskah Baskoro Adi dan Nicholas Raven.\n" +
                        "\n" +
                        "Baca selengkapnya di artikel Sinopsis Kajeng Kliwon: Film Horor yang Angkat Kisah Mistik Bali" );
        tambahFilm(film2, db);
        idFilm++;

        // Tambah Film 3

        try{
            tempDate = sdFormat.parse("2018");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film3 = new Film(
                idFilm,
                "Si Doel The Movie",
                tempDate,
                storeImageFile(R.drawable.film3),
                "Drama",
                "Rano Karno sebagai Doel,\n" +
                        "Cornelia Agatha sebagai Sarah,\n" +
                        "Maudy Koesnaedi sebagai Zaenab,\n" +
                        "Mandra sebagai Mandra,\n" +
                        "Aminah Cendrakasih sebagai Mpok Lela atau Maknyak,\n" +
                        "Suti Karno sebagai Atun,\n" +
                        "Adam Jagwani aka Stardust sebagai Hans,\n" +
                        "Salman Alfarizi sebagai Ahong,\n" +
                        "Rey Bong sebagai Abdullah atau Dul,\n" +
                        "Ahmad Zulhoir Mardia sebagai Kartubi atau Abi,\n" +
                        "Maryati Tohir sebagai Munaroh,\n" +
                        "Nabila Bintang Adelia sebagai Mentari,\n" +
                        "Wizzy sebagai Gadis,\n" +
                        "Opie Kumis sebagai tukang ojek online\n",
                "Wanita mana yang tak hancur hatinya, mendapati suami yang ia tunggu kepulangannya membawa kabar bahwa ia bertemu dengan belahan jiwa beserta anak yang selama ini dirindukannya.\n" +
                        "\n" +
                        "Hal itulah yang dirasakan ZAENAB dan membuat hatinya menggalau dalam kebisuan untuk memilih antara mempertahankan pernikahannya dengan DOEL, atau merelakannya. Terlebih saat DOEL mengatakan bahwa SARAH dan DUL akan menetap di Jakarta.\n" +
                        "\n" +
                        "Dalam kegelisahannya, ZAENAB mencoba mencari jawaban atas takdir yang harus ia pilih, dan harapan itu mulai terbuka lebar saat DOEL mendapat kabar dari ATUN bahwa kemungkinan ZAENAB tengah mengandung.\n" +
                        "\n" +
                        "Dan penegasan itu datang saat SARAH datang berkunjung ke rumah untuk mengungkapkan penyesalan atas apa yang terjadi di masa lalu kepada MAKNYAK dan meminta DUL untuk memanggil ZAENAB sebagai ibu.");
        tambahFilm(film3, db);


    }

}