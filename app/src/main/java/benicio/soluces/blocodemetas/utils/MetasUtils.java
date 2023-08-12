package benicio.soluces.blocodemetas.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import benicio.soluces.blocodemetas.models.MetaModel;

public class MetasUtils {
    private static final String PREF_NAME = "meta_prefs";
    private static final String KEY_TRANSACOES = "metas";

    // Método para salvar a lista de transações
    public static void saveMetas(Context context, List<MetaModel> transacoes) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(transacoes);
        editor.putString(KEY_TRANSACOES, json);
        editor.apply();
    }

    // Método para carregar a lista de transações
    public static List<MetaModel> loadMetas(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(KEY_TRANSACOES, "");
            Type type = new TypeToken<List<MetaModel>>() {
            }.getType();
            return gson.fromJson(json, type);
        }catch (Exception e){
            return new ArrayList<>();
        }
    }
}
