package chat.rocket.reactnative;

import android.util.Log;
import android.widget.Toast;

import chat.rocket.reactnative.cschat.CSSSLSocket;
import chat.rocket.reactnative.cschat.HttpClientBuilderFactory;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;


// import com.csg.cs.csrocketchat.apac.android.pta.CustomToastPackage;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.good.gd.GDAndroid;
import com.good.gd.GDStateListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class WebSocketModule extends ReactContextBaseJavaModule {
  private static final String TAG = WebSocketModule.class.getSimpleName();
  private static ReactApplicationContext reactContext;
  private OkHttpClient webSocketClient;
  private WebSocket webSocket;

  @Override
  public String getName() {
    return "WebSocketMod";
  }

  WebSocketModule(ReactApplicationContext context) {
    super(context);
    reactContext = context;
  }

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
    constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
    return constants;
  }

  private OkHttpClient provideOkHttpClient() {
    return new HttpClientBuilderFactory()
            .create()
            .build();
  }

  private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
    reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
  }

  // Native module's method that are exposed to React-Native
  @ReactMethod
  public void connect(String url) {

    webSocketClient = provideOkHttpClient();
    Request request = new Request.Builder()
            .url(url)
            .build();
    Log.d(TAG, "Request built for " + url);

    WebSocketListener listener = new WebSocketListener() {
      @Override
      public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        String responseString;
        WritableMap params = Arguments.createMap();

        // Despite being a nullable variable, the variable is still sometimes the null value
        responseString = response == null ? "null" : response.toString();

        Log.d(TAG + " onFailure", responseString);
        Log.d(TAG + " onFailure thrown",  t.toString());

        params.putString("response", responseString);
        params.putString("t", t.toString());
        sendEvent(reactContext, "EventOnFailure", params);
      }

      @Override
      public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        Log.d(TAG + " onOpen", response.toString());
        WritableMap params = Arguments.createMap();
        params.putString("response", response.toString());
        sendEvent(reactContext, "EventOnOpen", params);

      }

      @Override
      public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        Log.d(TAG + " onClosed", reason);
        Log.d(TAG + " onClosed[code]", String.valueOf(code));

        WritableMap params = Arguments.createMap();
        params.putInt("code", code);
        params.putString("reason", reason);
        sendEvent(reactContext, "EventOnClosed", params);
      }

      @Override
      public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
        Log.d(TAG + " onClosing", reason);
        Log.d(TAG + " onClosing[code]", String.valueOf(code));

        WritableMap params = Arguments.createMap();
        params.putInt("code", code);
        params.putString("reason", reason);
        sendEvent(reactContext, "EventOnClosing", params);
      }

      @Override
      public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        Log.d(TAG + " onMessageString", text);

        WritableMap params = Arguments.createMap();
        params.putString("text", text);
        sendEvent(reactContext, "EventOnMessage", params);
      }

      @Override
      public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
        String text = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
          text = bytes.string(StandardCharsets.UTF_8);
        }
        Log.d(TAG + " onMessageByte", text);

        WritableMap params = Arguments.createMap();
        params.putString("text", text);
        sendEvent(reactContext, "EventOnMessage", params);
      }
    };

    webSocket = webSocketClient.newWebSocket(request, listener);
  }

  @ReactMethod
  public boolean send(String stringData) {
    return webSocket.send(stringData);
  }

  @ReactMethod
  public boolean close(int code, String reason ) {
    return webSocket.close(code, reason);
  }

  @ReactMethod
  public void show(String message, int duration) {
    Toast.makeText(getReactApplicationContext(), message, duration).show();
  }
}
