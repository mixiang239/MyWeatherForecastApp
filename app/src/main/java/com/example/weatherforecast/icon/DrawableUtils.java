package com.example.weatherforecast.icon;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.weatherforecast.R;

public class DrawableUtils {

    /**
     * 根据API返回的数字设置ImageView的资源
     *
     * @param context     上下文对象
     * @param apiDrawableId 从API获取的数字ID
     */
    private static final String TAG = "DrawableUtils";
    public static int getDrawableResource(Context context, int apiDrawableId) {
        // 1. 构建资源名称：添加下划线前缀
        String resourceName = "_" + apiDrawableId;

        // 2. 获取Resources对象
        Resources resources = context.getResources();

        // 3. 获取包名
        String packageName = context.getPackageName();

        // 4. 动态获取资源ID
        int resourceId = resources.getIdentifier(resourceName, "drawable", packageName);

        // 5. 检查资源是否存在并设置
        if (resourceId != 0) {
            Log.d(TAG, "setDrawableResource: 成功找到资源");
            return resourceId;
        } else {
            // 资源未找到时的处理
            Log.d(TAG, "未找到资源: _" + apiDrawableId);
            return R.drawable._default;
        }
    }

}
