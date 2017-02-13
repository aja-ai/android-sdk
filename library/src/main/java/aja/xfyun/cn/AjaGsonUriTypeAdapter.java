package aja.xfyun.cn;

import android.net.Uri;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

class AjaGsonUriTypeAdapter extends TypeAdapter<Uri> {

    @Override
    public Uri read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        return Uri.parse(reader.nextString());
    }

    @Override
    public void write(JsonWriter writer, Uri uri) throws IOException {
        if (uri == null) {
            writer.nullValue();
            return;
        }

        writer.value(uri.toString());
    }

}
