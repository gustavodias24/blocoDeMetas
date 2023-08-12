package benicio.soluces.blocodemetas.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import benicio.soluces.blocodemetas.models.MetaModel;
import benicio.soluces.blocodemetas.models.SubMetaModel;

public class SubMetasUtils {
    private static final String PREF_NAME = "submeta_prefs";
    private static final String KEY_TRANSACOES = "submetas";

    // Método para salvar a lista de transações
    public static void saveSubMetas(Context context, List<SubMetaModel> transacoes) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(transacoes);
        editor.putString(KEY_TRANSACOES, json);
        editor.apply();
    }

    // Método para carregar a lista de transações
    public static List<SubMetaModel> loadSubMetas(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(KEY_TRANSACOES, "");
            Type type = new TypeToken<List<SubMetaModel>>() {
            }.getType();
            return gson.fromJson(json, type);
        }catch (Exception e){
            return new ArrayList<>();
        }
    }
}