package app.utils.decipher_state;

import http.HttpClientUtil;
import app.utils.trie.Trie;
import com.google.gson.Gson;
import engine.machine.components.enigma_factory.enigma.generated.CTEDecipher;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static app.utils.AppConstants.BASE_URL;

public class DecipherState {
    SimpleStringProperty excludeChars;
    SimpleStringProperty words;
    SimpleListProperty<String> wordsList;
    SimpleObjectProperty<Trie> dictionary;

    public DecipherState(){
        this.excludeChars = new SimpleStringProperty("");
        this.words = new SimpleStringProperty("");
        this.wordsList = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        this.dictionary = new SimpleObjectProperty<>(new Trie());

        InvalidationListener refreshWordList = ((observable) -> {
            List<String> lst = Arrays.stream(words.get().toUpperCase().split(" "))
                    .collect(Collectors.toList());

            dictionary.set(new Trie());
            lst.forEach(word -> dictionary.get().insert(word));

            wordsList.get().clear();
            wordsList.get().addAll(lst);
        });

        words.addListener(refreshWordList);
        excludeChars.addListener(refreshWordList);
    }

    public void loadData() {
        HttpClientUtil.runAsync(BASE_URL + "/decipher_state", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                CTEDecipher decipher = new Gson().fromJson(response.body().string(), CTEDecipher.class);
                System.out.println(decipher.getCTEDictionary().getWords());
                excludeChars.set(decipher.getCTEDictionary().getExcludeChars());
                words.set(decipher.getCTEDictionary().getWords().trim().toUpperCase()
                        .replaceAll(String.format("[%s]", excludeChars.getValue()), "")
                );
            }
        });
        //TODO: remove chars not in ABC
    }

    public SimpleStringProperty getExcludeCharsProperty() {
        return excludeChars;
    }

    public SimpleStringProperty getWordsPropertyProperty() {
        return words;
    }

    public SimpleListProperty<String> getWordsListProperty() {
        return wordsList;
    }

    public SimpleObjectProperty<Trie> getDictionaryProperty() {
        return dictionary;
    }
}
