package Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jack on 5/8/2016.
 */
public class Utils {
    public static void showMessage(Context ctx, String msg) {
        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
    }
}

